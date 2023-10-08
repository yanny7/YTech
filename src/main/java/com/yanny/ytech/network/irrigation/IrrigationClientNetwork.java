package com.yanny.ytech.network.irrigation;

import com.yanny.ytech.network.generic.client.ClientNetwork;

import java.text.MessageFormat;

public class IrrigationClientNetwork extends ClientNetwork {
    private final int amount;
    private final int capacity;

    public IrrigationClientNetwork(int networkId, int amount, int capacity) {
        super(networkId);
        this.amount = amount;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Id:{0,number}, {1,number}/{2,number}",
                getNetworkId(), amount, capacity);
    }

    public int getAmount() {
        return amount;
    }

    public int getCapacity() {
        return capacity;
    }
}
