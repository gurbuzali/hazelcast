package com.hazelcast.jet.impl;

import com.hazelcast.cluster.Address;
import com.hazelcast.instance.impl.HazelcastInstanceImpl;
import com.hazelcast.instance.impl.Node;
import com.hazelcast.internal.util.Preconditions;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.JobAlreadyExistsException;
import com.hazelcast.jet.JobStateSnapshot;
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

public class JetInstanceImpl extends AbstractJetInstance {

    private final NodeEngineImpl nodeEngine;

    public JetInstanceImpl(Node node) {
        super(node.hazelcastInstance);
        this.nodeEngine = node.nodeEngine;
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

    public Job newJobProxy(long jobId) {
        return new JobProxy(this, jobId);
    }

    public Job newJobProxy(long jobId, Object jobDefinition, JobConfig config) {
        return new JobProxy(this, jobId, jobDefinition, config);
    }

    public ILogger getLogger() {
        return nodeEngine.getLogger(getClass());
    }

    public List<Long> getJobIdsByName(String name) {
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

    /**
     * Tells whether this member knows of the given object name.
     * <p>
     * Notes:
     * <ul><li>
     *     this member might not know it exists if the proxy creation operation went wrong
     * </li><li>
     *     this member might not know it was destroyed if the destroy operation went wrong
     * </li><li>
     *     it might be racy with respect to other create/destroy operations
     * </li></ul>
     *
     * @param serviceName for example, {@link MapService#SERVICE_NAME}
     * @param objectName  object name
     * @return true, if this member knows of the object
     */
    @Override
    public boolean existsDistributedObject(@Nonnull String serviceName, @Nonnull String objectName) {
        return ImdgUtil.existsDistributedObject(nodeEngine, serviceName, objectName);
    }

    public NodeEngineImpl nodeEngine() {
        return nodeEngine;
    }

    private Address getMasterAddress() {
        return Preconditions.checkNotNull(nodeEngine.getMasterAddress(), "Cluster has not elected a master");
    }
}
