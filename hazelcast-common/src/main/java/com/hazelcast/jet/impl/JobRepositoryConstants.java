package com.hazelcast.jet.impl;

import com.hazelcast.jet.Job;
import com.hazelcast.jet.core.metrics.JobMetrics;

import static com.hazelcast.jet.Util.idToString;

public class JobRepositoryConstants {

    /**
     * Prefix of all Hazelcast internal objects used by Jet (such as job
     * metadata, snapshots etc.)
     */
    public static final String INTERNAL_JET_OBJECTS_PREFIX = "__jet.";

    /**
     * State snapshot exported using {@link Job#exportSnapshot(String)} is
     * currently stored in IMaps named with this prefix.
     */
    public static final String EXPORTED_SNAPSHOTS_PREFIX = INTERNAL_JET_OBJECTS_PREFIX + "exportedSnapshot.";

    /**
     * A cache to speed up access to details about exported snapshots.
     */
    public static final String EXPORTED_SNAPSHOTS_DETAIL_CACHE = INTERNAL_JET_OBJECTS_PREFIX + "exportedSnapshotsCache";

    /**
     * Name of internal IMap which stores job resources and attached files.
     */
    public static final String RESOURCES_MAP_NAME_PREFIX = INTERNAL_JET_OBJECTS_PREFIX + "resources.";

    /**
     * Key prefix for attached job files in the IMap named {@link JobRepository#RESOURCES_MAP_NAME_PREFIX}.
     */
    public static final String FILE_STORAGE_KEY_NAME_PREFIX = "f.";

    /**
     * Key prefix for added class resources in the IMap named {@link JobRepository#RESOURCES_MAP_NAME_PREFIX}.
     */
    public static final String CLASS_STORAGE_KEY_NAME_PREFIX = "c.";

    /**
     * Name of internal flake ID generator which is used for unique id generation.
     */
    public static final String RANDOM_ID_GENERATOR_NAME = INTERNAL_JET_OBJECTS_PREFIX + "ids";

    /**
     * Name of internal IMap which stores {@link JobRecord}s.
     */
    public static final String JOB_RECORDS_MAP_NAME = INTERNAL_JET_OBJECTS_PREFIX + "records";

    /**
     * Name of internal IMap which stores {@link JobExecutionRecord}s.
     */
    public static final String JOB_EXECUTION_RECORDS_MAP_NAME = INTERNAL_JET_OBJECTS_PREFIX + "executionRecords";

    /**
     * Name of internal IMap which stores job results
     */
    public static final String JOB_RESULTS_MAP_NAME = INTERNAL_JET_OBJECTS_PREFIX + "results";

    /**
     * Name of internal IMap which stores {@link JobMetrics}s.
     */
    public static final String JOB_METRICS_MAP_NAME = INTERNAL_JET_OBJECTS_PREFIX + "results.metrics";

    /**
     * Prefix for internal IMaps which store snapshot data. Snapshot data for
     * one snapshot is stored in either of the following two maps:
     * <ul>
     *      <li>{@code _jet.snapshot.<jobId>.0}
     *      <li>{@code _jet.snapshot.<jobId>.1}
     * </ul>
     * Which one of these is determined in {@link JobExecutionRecord}.
     */
    public static final String SNAPSHOT_DATA_MAP_PREFIX = INTERNAL_JET_OBJECTS_PREFIX + "snapshot.";

    /**
     * Returns the map name in the form {@code __jet.resources.<jobId>}
     */
    public static String jobResourcesMapName(long jobId) {
        return RESOURCES_MAP_NAME_PREFIX + idToString(jobId);
    }

    /**
     * Returns the key name in the form {@code file.<id>}
     */
    public static String fileKeyName(String id) {
        return FILE_STORAGE_KEY_NAME_PREFIX + id;
    }
}
