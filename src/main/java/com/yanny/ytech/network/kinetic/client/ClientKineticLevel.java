package com.yanny.ytech.network.kinetic.client;

import com.yanny.ytech.network.kinetic.common.IKineticBlockEntity;
import com.yanny.ytech.network.kinetic.common.KineticNetwork;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientKineticLevel {
    protected final ConcurrentHashMap<Integer, KineticNetwork> networkMap = new ConcurrentHashMap<>();

    public ClientKineticLevel(Map<Integer, KineticNetwork> networkMap) {
        super();
        this.networkMap.putAll(networkMap);
    }

    public ClientKineticLevel() {
        super();
    }

    void onNetworkAddedOrUpdated(KineticNetwork network) {
        networkMap.put(network.getNetworkId(), network);
    }

    void onNetworkRemoved(int networkId) {
        networkMap.remove(networkId);
    }

    @Nullable
    KineticNetwork getNetwork(IKineticBlockEntity blockEntity) {
        return networkMap.get(blockEntity.getNetworkId());
    }
}
