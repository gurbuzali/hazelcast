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

package com.hazelcast.client.impl.protocol.codec;

import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.client.impl.protocol.Generated;
import com.hazelcast.client.impl.protocol.codec.builtin.*;
import com.hazelcast.client.impl.protocol.codec.custom.*;

import javax.annotation.Nullable;

import static com.hazelcast.client.impl.protocol.ClientMessage.*;
import static com.hazelcast.client.impl.protocol.codec.builtin.FixedSizeTypesCodec.*;

/*
 * This file is auto-generated by the Hazelcast Client Protocol Code Generator.
 * To change this file, edit the templates or the protocol
 * definitions on the https://github.com/hazelcast/hazelcast-client-protocol
 * and regenerate it.
 */

/**
 * Adds all of the elements in the specified collection to this set if they're not already present
 * (optional operation). If the specified collection is also a set, the addAll operation effectively modifies this
 * set so that its value is the union of the two sets. The behavior of this operation is undefined if the specified
 * collection is modified while the operation is in progress.
 */
@Generated("4b9413f116c3d88f6bc6d0aafcba4fc9")
public final class SetAddAllCodec {
    //hex: 0x060600
    public static final int REQUEST_MESSAGE_TYPE = 394752;
    //hex: 0x060601
    public static final int RESPONSE_MESSAGE_TYPE = 394753;
    private static final int REQUEST_INITIAL_FRAME_SIZE = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int RESPONSE_RESPONSE_FIELD_OFFSET = RESPONSE_BACKUP_ACKS_FIELD_OFFSET + BYTE_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_RESPONSE_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;

    private SetAddAllCodec() {
    }

    public static ClientMessage encodeRequest(String name, java.util.Collection<com.hazelcast.internal.serialization.Data> valueList) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        clientMessage.setRetryable(false);
        clientMessage.setOperationName("Set.AddAll");
        Frame initialFrame = new Frame(new byte[REQUEST_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, REQUEST_MESSAGE_TYPE);
        encodeInt(initialFrame.content, PARTITION_ID_FIELD_OFFSET, -1);
        clientMessage.add(initialFrame);
        StringCodec.encode(clientMessage, name);
        ListMultiFrameCodec.encode(clientMessage, valueList, DataCodec::encode);
        return clientMessage;
    }


    /**
     * True if this set changed as a result of the call
     */
    public static boolean decodeResponse(ClientMessage clientMessage) {
        ForwardFrameIterator iterator = clientMessage.frameIterator();
        Frame initialFrame = iterator.next();
        return decodeBoolean(initialFrame.content, RESPONSE_RESPONSE_FIELD_OFFSET);
    }

}