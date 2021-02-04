/*
 * Copyright (c) 2008-2020, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.jet.impl;

import com.hazelcast.cluster.ClusterState;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.instance.impl.DefaultNodeExtension;
import com.hazelcast.instance.impl.Node;
import com.hazelcast.internal.nio.Packet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JetConfig;
import com.hazelcast.spi.impl.NodeEngineImpl.JetPacketConsumer;
import com.hazelcast.spi.merge.DiscardMergePolicy;

import java.util.Map;

import static com.hazelcast.jet.core.JetProperties.JOB_RESULTS_TTL_SECONDS;
import static com.hazelcast.jet.impl.JobRepository.INTERNAL_JET_OBJECTS_PREFIX;
import static com.hazelcast.jet.impl.JobRepository.JOB_METRICS_MAP_NAME;
import static com.hazelcast.jet.impl.JobRepository.JOB_RESULTS_MAP_NAME;

public class JetNodeExtension extends DefaultNodeExtension implements JetPacketConsumer {
    private final NodeExtensionCommon extCommon;

    public JetNodeExtension(Node node) {
        super(node);
        extCommon = new NodeExtensionCommon(node);
    }

    @Override
    public void beforeStart() {
        Config config = node.getConfig();
        JetConfig jetConfig = config.getJetConfig();
        if (jetConfig.getInstanceConfig().isLosslessRestartEnabled()) {
            throw new UnsupportedOperationException("Lossless Restart is not available in the open-source version of "
                    + "Hazelcast Jet");
        }
        super.beforeStart();
    }

    @Override
    public void afterStart() {
        super.afterStart();
        extCommon.afterStart();
    }

    @Override
    public void beforeClusterStateChange(ClusterState currState, ClusterState requestedState, boolean isTransient) {
        super.beforeClusterStateChange(currState, requestedState, isTransient);
        extCommon.beforeClusterStateChange(requestedState);
    }

    @Override
    public void onClusterStateChange(ClusterState newState, boolean isTransient) {
        super.onClusterStateChange(newState, isTransient);
        extCommon.onClusterStateChange(newState);
    }

    @Override
    public void beforeShutdown() {
        extCommon.beforeShutdown();
        super.beforeShutdown();
    }

    @Override
    public Map<String, Object> createExtensionServices() {
        return extCommon.createExtensionServices();
    }

    @Override
    public void printNodeInfo() {
        extCommon.printNodeInfo(systemLogger, "");
    }

    @Override
    public void accept(Packet packet) {
        extCommon.handlePacket(packet);
    }

    @Override
    public JetInstance getJetInstance() {
        return extCommon.getJetInstance();
    }
}
