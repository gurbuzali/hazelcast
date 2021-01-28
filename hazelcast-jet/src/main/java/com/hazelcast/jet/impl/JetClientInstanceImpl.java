package com.hazelcast.jet.impl;

import com.hazelcast.client.impl.clientside.HazelcastClientInstanceImpl;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.JobStateSnapshot;
import com.hazelcast.jet.ThePipeline;
import com.hazelcast.jet.config.JobConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public class JetClientInstanceImpl implements JetInstance {
    @Nonnull
    @Override
    public Job newJob(@Nonnull ThePipeline pipeline, @Nonnull JobConfig config) {
        return null;
    }

    @Nonnull
    @Override
    public Job newJobIfAbsent(@Nonnull ThePipeline pipeline, @Nonnull JobConfig config) {
        return null;
    }

    @Nonnull
    @Override
    public List<Job> getJobs() {
        return null;
    }

    @Nullable
    @Override
    public Job getJob(long jobId) {
        return null;
    }

    @Nonnull
    @Override
    public List<Job> getJobs(@Nonnull String name) {
        return null;
    }

    @Override
    public JobStateSnapshot getJobStateSnapshot(@Nonnull String name) {
        return null;
    }

    @Override
    public Collection<JobStateSnapshot> getJobStateSnapshots() {
        return null;
    }

    HazelcastClientInstanceImpl client() {
        return null;
    }
}
