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
 * Submits the task to partition for execution, partition is chosen based on multiple criteria of the given task.
 */
@Generated("5d3c1eec80a8e882825c032c83aefe1f")
public final class ServerScheduledExecutorSubmitToPartitionCodec {
    //hex: 0x1A0200
    public static final int REQUEST_MESSAGE_TYPE = 1704448;
    //hex: 0x1A0201
    public static final int RESPONSE_MESSAGE_TYPE = 1704449;
    private static final int REQUEST_TYPE_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_DELAY_IN_MILLIS_FIELD_OFFSET = REQUEST_TYPE_FIELD_OFFSET + BYTE_SIZE_IN_BYTES;
    private static final int REQUEST_PERIOD_IN_MILLIS_FIELD_OFFSET = REQUEST_INITIAL_DELAY_IN_MILLIS_FIELD_OFFSET + LONG_SIZE_IN_BYTES;
    private static final int REQUEST_AUTO_DISPOSABLE_FIELD_OFFSET = REQUEST_PERIOD_IN_MILLIS_FIELD_OFFSET + LONG_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_AUTO_DISPOSABLE_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_BACKUP_ACKS_FIELD_OFFSET + BYTE_SIZE_IN_BYTES;

    private ServerScheduledExecutorSubmitToPartitionCodec() {
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class RequestParameters {

        /**
         * The name of the scheduler.
         */
        public String schedulerName;

        /**
         * type of schedule logic, values 0 for SINGLE_RUN, 1 for AT_FIXED_RATE
         */
        public byte type;

        /**
         * The name of the task
         */
        public String taskName;

        /**
         * Name The name of the task
         */
        public com.hazelcast.internal.serialization.Data task;

        /**
         * initial delay in milliseconds
         */
        public long initialDelayInMillis;

        /**
         * period between each run in milliseconds
         */
        public long periodInMillis;

        /**
         * A boolean flag to indicate whether the task should be destroyed automatically after execution.
         */
        public boolean autoDisposable;

        /**
         * True if the autoDisposable is received from the client, false otherwise.
         * If this is false, autoDisposable has the default value for its type.
         */
        public boolean isAutoDisposableExists;
    }

    public static RequestParameters decodeRequest(ClientMessage clientMessage) {
        ForwardFrameIterator iterator = clientMessage.frameIterator();
        RequestParameters request = new RequestParameters();
        Frame initialFrame = iterator.next();
        request.type = decodeByte(initialFrame.content, REQUEST_TYPE_FIELD_OFFSET);
        request.initialDelayInMillis = decodeLong(initialFrame.content, REQUEST_INITIAL_DELAY_IN_MILLIS_FIELD_OFFSET);
        request.periodInMillis = decodeLong(initialFrame.content, REQUEST_PERIOD_IN_MILLIS_FIELD_OFFSET);
        if (initialFrame.content.length >= REQUEST_AUTO_DISPOSABLE_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES) {
            request.autoDisposable = decodeBoolean(initialFrame.content, REQUEST_AUTO_DISPOSABLE_FIELD_OFFSET);
            request.isAutoDisposableExists = true;
        } else {
            request.isAutoDisposableExists = false;
        }
        request.schedulerName = StringCodec.decode(iterator);
        request.taskName = StringCodec.decode(iterator);
        request.task = DataCodec.decode(iterator);
        return request;
    }

    public static ClientMessage encodeResponse() {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        Frame initialFrame = new Frame(new byte[RESPONSE_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, RESPONSE_MESSAGE_TYPE);
        clientMessage.add(initialFrame);

        return clientMessage;
    }

}