package com.hazelcast.jet.pipeline;

public final class JoinBuilders {

    private JoinBuilders() {
    }

    public static <T> HashJoinBuilder<T> hashJoinBuilder(BatchStage<T> stage) {
        return new HashJoinBuilder<>(stage);
    }

    public static <T> StreamHashJoinBuilder<T> streamHashJoinBuilder(StreamStage<T> stage) {
        return new StreamHashJoinBuilder<>(stage);
    }
}
