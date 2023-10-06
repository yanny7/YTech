package com.yanny.ytech.network.irrigation;

import com.yanny.ytech.network.generic.client.ClientNetwork;

public class IrrigationClientNetwork extends ClientNetwork {
    private final int amount;
    private final int capacity;

    public IrrigationClientNetwork(int networkId, int amount, int capacity) {
        super(networkId);
        this.amount = amount;
        this.capacity = capacity;
    }

    public int getAmount() {
        return amount;
    }

    public int getCapacity() {
        return capacity;
    }
}
