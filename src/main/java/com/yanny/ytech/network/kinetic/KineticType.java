package com.yanny.ytech.network.kinetic;

import java.util.function.BiConsumer;

public enum KineticType {
    PROVIDER(KineticNetwork::addProvider, KineticNetwork::removeProvider),
    CONSUMER(KineticNetwork::addConsumer, KineticNetwork::removeConsumer);

    public final BiConsumer<KineticNetwork, IKineticBlockEntity> addEntity;
    public final BiConsumer<KineticNetwork, IKineticBlockEntity> removeEntity;

    KineticType(BiConsumer<KineticNetwork, IKineticBlockEntity> addEntity, BiConsumer<KineticNetwork, IKineticBlockEntity> removeEntity) {
        this.addEntity = addEntity;
        this.removeEntity = removeEntity;
    }
}
