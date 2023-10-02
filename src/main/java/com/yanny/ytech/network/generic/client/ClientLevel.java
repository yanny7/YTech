package com.yanny.ytech.network.generic.client;

import com.yanny.ytech.network.generic.common.AbstractNetwork;
import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientLevel<N extends AbstractNetwork<N, O>, O extends INetworkBlockEntity> {
    @NotNull private final ConcurrentHashMap<Integer, N> networkMap;

    ClientLevel(@NotNull Map<Integer, N> networkMap) {
        this.networkMap = new ConcurrentHashMap<>(networkMap);
    }

    ClientLevel() {
        networkMap = new ConcurrentHashMap<>();
    }

    @NotNull
    public N getNetwork(@NotNull O blockEntity) {
        return networkMap.get(blockEntity.getNetworkId());
    }

    void onNetworkAddedOrUpdated(@NotNull N network) {
        networkMap.put(network.getNetworkId(), network);
    }

    void onNetworkRemoved(int networkId) {
        networkMap.remove(networkId);
    }
}
