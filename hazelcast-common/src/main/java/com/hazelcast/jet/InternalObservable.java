package com.hazelcast.jet;

import com.hazelcast.core.IFunction;
import com.hazelcast.jet.core.ProcessorMetaSupplier;

import javax.annotation.Nonnull;

import static com.hazelcast.jet.impl.JobRepositoryConstants.INTERNAL_JET_OBJECTS_PREFIX;

public interface InternalObservable<T> extends Observable<T> {

    /**
     * Prefix of all topic names used to back {@link Observable} implementations,
     * necessary so that such topics can't clash with regular topics used
     * for other purposes.
     */
    String JET_OBSERVABLE_NAME_PREFIX = INTERNAL_JET_OBJECTS_PREFIX + "observables.";

    /**
     * Constant ID to be used as a {@link ProcessorMetaSupplier#getTags()
     * PMS tag key} for specifying when a PMS owns an {@link Observable} (ie.
     * is the entity populating the {@link Observable} with data).
     */
    String OWNED_OBSERVABLE = "com.hazelcast.jet.impl.observer.ObservableImpl.ownedObservable";

    /**
     * The maximum number of items that can be retrieved in 1 go using the
     * {@link #readManyAsync(long, int, int, IFunction)} method.
     */
    int RINGBUFFER_MAX_BATCH_SIZE = 1000;

    @Nonnull
    static String ringbufferName(String observableName) {
        return JET_OBSERVABLE_NAME_PREFIX + observableName;
    }

}
