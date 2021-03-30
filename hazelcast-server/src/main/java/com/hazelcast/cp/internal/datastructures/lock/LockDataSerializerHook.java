/*
 * Copyright (c) 2008-2021, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.cp.internal.datastructures.lock;

import com.hazelcast.cp.internal.datastructures.lock.operation.GetLockOwnershipStateOp;
import com.hazelcast.cp.internal.datastructures.lock.operation.LockOp;
import com.hazelcast.cp.internal.datastructures.lock.operation.TryLockOp;
import com.hazelcast.cp.internal.datastructures.lock.operation.UnlockOp;
import com.hazelcast.internal.serialization.DataSerializerHook;
import com.hazelcast.internal.serialization.impl.FactoryIdHelper;
import com.hazelcast.nio.serialization.DataSerializableFactory;

@SuppressWarnings("checkstyle:declarationorder")
public class LockDataSerializerHook extends LockDataSerializerHookBase implements DataSerializerHook {

    @Override
    public int getFactoryId() {
        return F_ID;
    }

    @Override
    public DataSerializableFactory createFactory() {
        return typeId -> {
            switch (typeId) {
                case RAFT_LOCK_REGISTRY:
                    return new LockRegistry();
                case RAFT_LOCK:
                    return new Lock();
                case LOCK_ENDPOINT:
                    return new LockEndpoint();
                case LOCK_INVOCATION_KEY:
                    return new LockInvocationKey();
                case RAFT_LOCK_OWNERSHIP_STATE:
                    return new LockOwnershipState();
                case LOCK_OP:
                    return new LockOp();
                case TRY_LOCK_OP:
                    return new TryLockOp();
                case UNLOCK_OP:
                    return new UnlockOp();
                case GET_RAFT_LOCK_OWNERSHIP_STATE_OP:
                    return new GetLockOwnershipStateOp();
                default:
                    throw new IllegalArgumentException("Undefined type: " + typeId);
            }
        };
    }
}
