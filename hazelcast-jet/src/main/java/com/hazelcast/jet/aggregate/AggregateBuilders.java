package com.hazelcast.jet.aggregate;

import com.hazelcast.jet.pipeline.BatchStage;
import com.hazelcast.jet.pipeline.BatchStageWithKey;
import com.hazelcast.jet.pipeline.StageWithKeyAndWindow;
import com.hazelcast.jet.pipeline.StageWithWindow;

import javax.annotation.Nonnull;

public final class AggregateBuilders {

    private AggregateBuilders() {
    }

    public static <T, R> AggregateBuilder<R> aggregateBuilder(
            @Nonnull BatchStage<T> batchStage,
            @Nonnull AggregateOperation1<? super T, ?, ? extends R> aggrOp
    ) {
        return new AggregateBuilder<>(batchStage, aggrOp);
    }

    public static <T> AggregateBuilder1<T> aggregateBuilder1(
            @Nonnull BatchStage<T> batchStage
    ) {
        return new AggregateBuilder1<>(batchStage);
    }

    public static <T, K, R> GroupAggregateBuilder<K, R> aggregateBuilder(
            @Nonnull BatchStageWithKey<T, K> batchStageWithKey,
            @Nonnull AggregateOperation1<? super T, ?, ? extends R> aggrOp
    ) {
        return new GroupAggregateBuilder<>(batchStageWithKey, aggrOp);
    }

    public static <T, K> GroupAggregateBuilder1<T, K> aggregateBuilder1(
            @Nonnull BatchStageWithKey<T, K> batchStageWithKey
    ) {
        return new GroupAggregateBuilder1<>(batchStageWithKey);
    }

    public static <T, R> WindowAggregateBuilder<R> aggregateBuilder(
            @Nonnull StageWithWindow<T> stageWithWindow,
            @Nonnull AggregateOperation1<? super T, ?, ? extends R> aggrOp
    ) {
        return new WindowAggregateBuilder<>(stageWithWindow.streamStage(), aggrOp, stageWithWindow.windowDefinition());
    }

    public static <T> WindowAggregateBuilder1<T> aggregateBuilder1(
            @Nonnull StageWithWindow<T> stageWithWindow
    ) {
        return new WindowAggregateBuilder1<>(stageWithWindow.streamStage(), stageWithWindow.windowDefinition());
    }

    public static <T, K, R> WindowGroupAggregateBuilder<K, R> aggregateBuilder(
            @Nonnull StageWithKeyAndWindow<T, K> stageWithKeyAndWindow,
            @Nonnull AggregateOperation1<? super T, ?, ? extends R> aggrOp
    ) {
        return new WindowGroupAggregateBuilder<>(stageWithKeyAndWindow, aggrOp);
    }

    public static <T, K> WindowGroupAggregateBuilder1<T, K> aggregateBuilder1(
            @Nonnull StageWithKeyAndWindow<T, K> stageWithKeyAndWindow
    ) {
        return new WindowGroupAggregateBuilder1<>(stageWithKeyAndWindow);
    }

}
