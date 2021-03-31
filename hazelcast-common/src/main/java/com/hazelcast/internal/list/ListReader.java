package com.hazelcast.internal.list;

import com.hazelcast.internal.serialization.Data;

import java.util.List;

public interface ListReader {

    List<Data> dataSubList(int fromIndex, int toIndex);
}
