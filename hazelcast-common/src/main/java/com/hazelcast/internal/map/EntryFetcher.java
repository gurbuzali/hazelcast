package com.hazelcast.internal.map;

import com.hazelcast.internal.iteration.IterationPointer;
import com.hazelcast.map.impl.iterator.MapEntriesWithCursor;
import com.hazelcast.map.impl.query.Query;
import com.hazelcast.map.impl.query.ResultSegment;
import com.hazelcast.spi.impl.InternalCompletableFuture;

public interface EntryFetcher {

    InternalCompletableFuture<MapEntriesWithCursor> fetchEntries(int partitionId, IterationPointer[] pointers, int fetchSize);

    InternalCompletableFuture<ResultSegment> fetchWithQuery(int partitionId, IterationPointer[] pointers, int fetchSize, Query query);
}
