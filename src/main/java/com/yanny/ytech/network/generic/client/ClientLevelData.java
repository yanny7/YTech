package com.yanny.ytech.network.generic.client;

import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientLevelData<N extends ClientNetwork, O extends INetworkBlockEntity> {
    @NotNull private final ConcurrentHashMap<Integer, N> networkMap;

    public ClientLevelData(@NotNull Map<Integer, N> networkMap) {
        this.networkMap = new ConcurrentHashMap<>(networkMap);
    }

    public ClientLevelData() {
        networkMap = new ConcurrentHashMap<>();
    }

    @NotNull
    public N getNetwork(@NotNull O blockEntity) {
        return networkMap.get(blockEntity.getNetworkId());
    }

    public void onNetworkAddedOrUpdated(@NotNull N network) {
        networkMap.put(network.getNetworkId(), network);
    }

    public void onNetworkRemoved(int networkId) {
        networkMap.remove(networkId);
    }
}
