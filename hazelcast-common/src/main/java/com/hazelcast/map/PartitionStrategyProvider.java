package com.hazelcast.map;

import com.hazelcast.partition.PartitioningStrategy;

public interface PartitionStrategyProvider {

    PartitioningStrategy<?> getPartitionStrategy();
}
