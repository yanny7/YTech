package com.yanny.ytech.network.generic.common;

import java.util.function.BiConsumer;

public enum NetworkType {
    PROVIDER(AbstractNetwork::addProvider, AbstractNetwork::removeProvider),
    CONSUMER(AbstractNetwork::addConsumer, AbstractNetwork::removeConsumer);

    public final BiConsumer<AbstractNetwork, INetworkBlockEntity> addEntity;
    public final BiConsumer<AbstractNetwork, INetworkBlockEntity> removeEntity;

    NetworkType(BiConsumer<AbstractNetwork, INetworkBlockEntity> addEntity, BiConsumer<AbstractNetwork, INetworkBlockEntity> removeEntity) {
        this.addEntity = addEntity;
        this.removeEntity = removeEntity;
    }
}
