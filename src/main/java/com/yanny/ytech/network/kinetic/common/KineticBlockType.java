package com.yanny.ytech.network.kinetic.common;

import java.util.HashMap;
import java.util.Map;

public enum KineticBlockType {
    SHAFT("shaft", "Shaft"),
    WATER_WHEEL("water_wheel", "Water Wheel")
    ;

    private static final Map<String, KineticBlockType> KINETIC_TYPE_MAP = new HashMap<>();

    public final String id;
    public final String lang;

    static {
        for(final KineticBlockType kineticBlockType : KineticBlockType.values()) {
            KINETIC_TYPE_MAP.put(kineticBlockType.id, kineticBlockType);
        }
    }

    KineticBlockType(String id, String lang) {
        this.id = id;
        this.lang = lang;
    }

    public static KineticBlockType fromConfiguration(String id) {
        return KINETIC_TYPE_MAP.get(id);
    }
}
