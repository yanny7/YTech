package com.yanny.ytech.network.generic.common;

public abstract class CommonNetwork {
    private final int networkId;

    public CommonNetwork(int networkId) {
        this.networkId = networkId;
    }

    public int getNetworkId() {
        return networkId;
    }
}
