package com.yanny.ytech.network.generic.client;

import com.yanny.ytech.network.generic.common.AbstractNetwork;
import com.yanny.ytech.network.generic.common.INetworkBlockEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientLevel<N extends AbstractNetwork<N, O>, O extends INetworkBlockEntity> {
    protected final ConcurrentHashMap<Integer, N> networkMap;

    ClientLevel(Map<Integer, N> networkMap) {
        this.networkMap = new ConcurrentHashMap<>(networkMap);
    }

    ClientLevel() {
        networkMap = new ConcurrentHashMap<>();
    }

    public N getNetwork(O blockEntity) {
        return networkMap.get(blockEntity.getNetworkId());
    }

    void onNetworkAddedOrUpdated(N network) {
        networkMap.put(network.getNetworkId(), network);
    }

    void onNetworkRemoved(int networkId) {
        networkMap.remove(networkId);
    }
}
