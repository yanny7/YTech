package com.yanny.ytech.network.kinetic.client;

import com.yanny.ytech.network.kinetic.common.KineticLevel;
import com.yanny.ytech.network.kinetic.common.KineticNetwork;

import java.util.Map;

public class ClientKineticLevel extends KineticLevel {
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
}
