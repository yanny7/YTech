package com.yanny.ytech.network.kinetic.common;

import java.util.function.BiConsumer;

public enum KineticNetworkType {
    PROVIDER(KineticNetwork::addProvider, KineticNetwork::removeProvider),
    CONSUMER(KineticNetwork::addConsumer, KineticNetwork::removeConsumer);

    public final BiConsumer<KineticNetwork, IKineticBlockEntity> addEntity;
    public final BiConsumer<KineticNetwork, IKineticBlockEntity> removeEntity;

    KineticNetworkType(BiConsumer<KineticNetwork, IKineticBlockEntity> addEntity, BiConsumer<KineticNetwork, IKineticBlockEntity> removeEntity) {
        this.addEntity = addEntity;
        this.removeEntity = removeEntity;
    }
}
