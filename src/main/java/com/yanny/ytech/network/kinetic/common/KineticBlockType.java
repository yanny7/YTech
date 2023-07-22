package com.yanny.ytech.network.kinetic.common;

public enum KineticBlockType {
    SHAFT("shaft", "Shaft"),
    WATER_WHEEL("water_wheel", "Water Wheel")
    ;

    public final String id;
    public final String lang;

    KineticBlockType(String id, String lang) {
        this.id = id;
        this.lang = lang;
    }
}
