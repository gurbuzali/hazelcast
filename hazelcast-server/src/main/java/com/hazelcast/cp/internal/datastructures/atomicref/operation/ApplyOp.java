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

package com.hazelcast.cp.internal.datastructures.atomicref.operation;

import com.hazelcast.cp.IAtomicReference;
import com.hazelcast.core.IFunction;
import com.hazelcast.cp.internal.datastructures.atomicref.ReturnValueType;
import com.hazelcast.cp.internal.datastructures.atomicref.AtomicRef;
import com.hazelcast.cp.internal.datastructures.atomicref.AtomicRefDataSerializerHook;
import com.hazelcast.internal.nio.IOUtil;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.internal.serialization.Data;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import com.hazelcast.cp.CPGroupId;
import com.hazelcast.spi.impl.NodeEngine;

import java.io.IOException;

import static com.hazelcast.internal.util.Preconditions.checkNotNull;

/**
 * Operation for {@link IAtomicReference#apply(IFunction)},
 * {@link IAtomicReference#alter(IFunction)},
 * {@link IAtomicReference#alterAndGet(IFunction)},
 * and {@link IAtomicReference#getAndAlter(IFunction)}
 */
public class ApplyOp extends AbstractAtomicRefOp implements IdentifiedDataSerializable {

    private Data function;
    private ReturnValueType returnValueType;
    private boolean alter;

    public ApplyOp() {
    }

    public ApplyOp(String name, Data function, ReturnValueType returnValueType, boolean alter) {
        super(name);
        checkNotNull(function);
        checkNotNull(returnValueType);
        this.function = function;
        this.returnValueType = returnValueType;
        this.alter = alter;
    }

    @Override
    public Object run(CPGroupId groupId, long commitIndex) {
        AtomicRef ref = getAtomicRef(groupId);
        Data currentData = ref.get();
        Data newData = callFunction(currentData);

        if (alter) {
            ref.set(newData);
        }

        if (returnValueType == ReturnValueType.NO_RETURN_VALUE) {
            return null;
        }

        return returnValueType == ReturnValueType.RETURN_OLD_VALUE ? currentData : newData;
    }

    private Data callFunction(Data currentData) {
        NodeEngine nodeEngine = getNodeEngine();
        IFunction func = nodeEngine.toObject(function);
        Object input = nodeEngine.toObject(currentData);
        //noinspection unchecked
        Object output = func.apply(input);
        return nodeEngine.toData(output);
    }

    @Override
    public int getClassId() {
        return AtomicRefDataSerializerHook.APPLY_OP;
    }

    @Override
    public void writeData(ObjectDataOutput out)
            throws IOException {
        super.writeData(out);
        IOUtil.writeData(out, function);
        out.writeString(returnValueType.name());
        out.writeBoolean(alter);
    }

    @Override
    public void readData(ObjectDataInput in)
            throws IOException {
        super.readData(in);
        function = IOUtil.readData(in);
        returnValueType = ReturnValueType.valueOf(in.readString());
        alter = in.readBoolean();
    }
}
