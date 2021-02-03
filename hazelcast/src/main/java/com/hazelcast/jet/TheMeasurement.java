package com.hazelcast.jet;

import javax.annotation.Nonnull;

public interface TheMeasurement {

    String tag(String name);

    /**
     * Returns the value associated with this {@link TheMeasurement}.
     */
    public long value();

    /**
     * Returns the timestamps associated with this {@link TheMeasurement}, the
     * moment when the value was gathered.
     */
    public long timestamp();

    /**
     * Returns the name of the metric. For a list of different metrics
     * see {@link MetricNames}.
     */
    @Nonnull
    public String metric();

}
