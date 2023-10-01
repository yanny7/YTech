package com.yanny.ytech.network.generic.client;

import com.yanny.ytech.network.generic.common.AbstractNetwork;
import com.yanny.ytech.network.generic.common.INetworkBlockEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientLevel<T extends AbstractNetwork> {
    protected final ConcurrentHashMap<Integer, T> networkMap;

    ClientLevel(Map<Integer, T> networkMap) {
        this.networkMap = new ConcurrentHashMap<>(networkMap);
    }

    ClientLevel() {
        networkMap = new ConcurrentHashMap<>();
    }

    public T getNetwork(INetworkBlockEntity blockEntity) {
        return networkMap.get(blockEntity.getNetworkId());
    }

    void onNetworkAddedOrUpdated(T network) {
        networkMap.put(network.getNetworkId(), network);
    }

    void onNetworkRemoved(int networkId) {
        networkMap.remove(networkId);
    }
}
