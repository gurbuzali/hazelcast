package com.hazelcast.jet;

import com.hazelcast.jet.config.JobConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class NoOpJetInstance implements JetInstance {

    @NotNull
    @Override
    public Job newJob(@NotNull ThePipeline pipeline, @NotNull JobConfig config) {
        return null;
    }

    @NotNull
    @Override
    public Job newJobIfAbsent(@NotNull ThePipeline pipeline, @NotNull JobConfig config) {
        return null;
    }

    @NotNull
    @Override
    public List<Job> getJobs() {
        return null;
    }

    @Nullable
    @Override
    public Job getJob(long jobId) {
        return null;
    }

    @NotNull
    @Override
    public List<Job> getJobs(@NotNull String name) {
        return null;
    }

    @Override
    public JobStateSnapshot getJobStateSnapshot(@NotNull String name) {
        return null;
    }

    @Override
    public Collection<JobStateSnapshot> getJobStateSnapshots() {
        return null;
    }
}
