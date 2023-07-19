package com.yanny.ytech.network.kinetic.common;

public enum KineticBlockType {
    SHAFT("shaft"),
    WATER_WHEEL("water_wheel")
    ;

    public final String id;

    KineticBlockType(String id) {
        this.id = id;
    }
}
