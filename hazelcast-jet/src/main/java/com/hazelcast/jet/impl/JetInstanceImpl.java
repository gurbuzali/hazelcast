package com.hazelcast.jet.impl;

import com.hazelcast.cluster.Address;
import com.hazelcast.instance.impl.HazelcastInstanceImpl;
import com.hazelcast.instance.impl.Node;
import com.hazelcast.internal.util.Preconditions;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.JobAlreadyExistsException;
import com.hazelcast.jet.JobStateSnapshot;
import com.hazelcast.jet.JobStateSnapshotImpl;
import com.hazelcast.jet.ThePipeline;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.core.JobNotFoundException;
import com.hazelcast.jet.core.JobStatus;
import com.hazelcast.jet.impl.operation.GetJobIdsByNameOperation;
import com.hazelcast.jet.impl.operation.GetJobIdsOperation;
import com.hazelcast.jet.impl.pipeline.PipelineImpl;
import com.hazelcast.jet.impl.util.ImdgUtil;
import com.hazelcast.jet.impl.util.Util;
import com.hazelcast.logging.ILogger;
import com.hazelcast.map.IMap;
import com.hazelcast.map.impl.MapService;
import com.hazelcast.spi.impl.NodeEngineImpl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import static com.hazelcast.jet.impl.JobRepository.exportedSnapshotMapName;
import static com.hazelcast.jet.impl.util.ExceptionUtil.peel;
import static com.hazelcast.jet.impl.util.ExceptionUtil.rethrow;
import static com.hazelcast.jet.impl.util.LoggingUtil.logFine;
import static java.util.stream.Collectors.toList;

public class JetInstanceImpl implements JetInstance {

    private final HazelcastInstanceImpl hazelcastInstance;
    private final NodeEngineImpl nodeEngine;
    private final Supplier<JobRepository> jobRepository;

    public JetInstanceImpl(Node node) {
        this.hazelcastInstance = node.hazelcastInstance;
        this.nodeEngine = node.nodeEngine;
        this.jobRepository = Util.memoizeConcurrent(() -> new JobRepository(hazelcastInstance));
    }

    @Nonnull
    @Override
    public Job newJob(@Nonnull ThePipeline pipeline, @Nonnull JobConfig config) {
        config = config.attachAll(((PipelineImpl) pipeline).attachedFiles());
        long jobId = uploadResourcesAndAssignId(config);
        return newJobProxy(jobId, pipeline, config);
    }

    @Nonnull
    @Override
    public Job newJobIfAbsent(@Nonnull ThePipeline pipeline, @Nonnull JobConfig config) {
        if (config.getName() == null) {
            return newJob(pipeline, config);
        } else {
            while (true) {
                Job job = getJob(config.getName());
                if (job != null) {
                    JobStatus status = job.getStatus();
                    if (status != JobStatus.FAILED && status != JobStatus.COMPLETED) {
                        return job;
                    }
                }
                try {
                    return newJob(pipeline, config);
                } catch (JobAlreadyExistsException e) {
                    logFine(getLogger(), "Could not submit job with duplicate name: %s, ignoring", config.getName());
                }
            }
        }
    }

    @Nonnull
    @Override
    public List<Job> getJobs() {
        Address masterAddress = getMasterAddress();
        Future<List<Long>> future = nodeEngine
                .getOperationService()
                .createInvocationBuilder(JetService.SERVICE_NAME, new GetJobIdsOperation(), masterAddress)
                .invoke();

        try {
            return future.get()
                    .stream()
                    .map(jobId -> new JobProxy(this, jobId))
                    .collect(toList());
        } catch (Throwable t) {
            throw rethrow(t);
        }
    }

    @Nullable
    @Override
    public Job getJob(long jobId) {
        try {
            Job job = newJobProxy(jobId);
            // get the status for the side-effect of throwing an exception if the jobId is invalid
            job.getStatus();
            return job;
        } catch (Throwable t) {
            if (peel(t) instanceof JobNotFoundException) {
                return null;
            }
            throw rethrow(t);
        }
    }

    @Nonnull
    @Override
    public List<Job> getJobs(@Nonnull String name) {
        return Util.toList(getJobIdsByName(name), this::newJobProxy);
    }

    @Override
    public JobStateSnapshot getJobStateSnapshot(@Nonnull String name) {
        String mapName = exportedSnapshotMapName(name);

        if (!ImdgUtil.existsDistributedObject(nodeEngine, MapService.SERVICE_NAME, mapName)) {
            return null;
        }
        IMap<Object, Object> map = hazelcastInstance.getMap(mapName);
        Object validationRecord = map.get(SnapshotValidationRecord.KEY);
        if (validationRecord instanceof SnapshotValidationRecord) {
            // update the cache - for robustness. For example after the map was copied
            hazelcastInstance.getMap(JobRepository.EXPORTED_SNAPSHOTS_DETAIL_CACHE).set(name, validationRecord);
            return new JobStateSnapshotImpl(hazelcastInstance, name, (SnapshotValidationRecord) validationRecord);
        } else {
            return null;
        }
    }

    @Override
    public Collection<JobStateSnapshot> getJobStateSnapshots() {
        return hazelcastInstance.getMap(JobRepository.EXPORTED_SNAPSHOTS_DETAIL_CACHE)
                .entrySet().stream()
                .map(entry -> new JobStateSnapshotImpl(hazelcastInstance, (String) entry.getKey(),
                        (SnapshotValidationRecord) entry.getValue()))
                .collect(toList());
    }

    private long uploadResourcesAndAssignId(JobConfig config) {
        return jobRepository.get().uploadJobResources(config);
    }

    private Job newJobProxy(long jobId) {
        return new JobProxy(this, jobId);
    }

    private Job newJobProxy(long jobId, Object jobDefinition, JobConfig config) {
        return new JobProxy(this, jobId, jobDefinition, config);
    }

    private ILogger getLogger() {
        return nodeEngine.getLogger(getClass());
    }

    private Address getMasterAddress() {
        return Preconditions.checkNotNull(hazelcastInstance.node.getMasterAddress(), "Cluster has not elected a master");
    }

    private List<Long> getJobIdsByName(String name) {
        Address masterAddress = getMasterAddress();
        Future<List<Long>> future = nodeEngine
                .getOperationService()
                .createInvocationBuilder(JetService.SERVICE_NAME, new GetJobIdsByNameOperation(name), masterAddress)
                .invoke();

        try {
            return future.get();
        } catch (Throwable t) {
            throw rethrow(t);
        }
    }

    public NodeEngineImpl nodeEngine() {
        return nodeEngine;
    }
}
