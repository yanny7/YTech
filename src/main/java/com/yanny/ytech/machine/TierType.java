package com.yanny.ytech.machine;

import java.util.HashMap;
import java.util.Map;

public enum TierType {
    STONE("stone"),
    STEAM("steam");

    private static final Map<String, TierType> TIER_TYPE_MAP = new HashMap<>();

    public final String id;

    static {
        for(final TierType TierType : TierType.values()) {
            TIER_TYPE_MAP.put(TierType.id, TierType);
        }
    }

    TierType(String id) {
        this.id = id;
    }

    public static TierType fromConfiguration(String id) {
        return TIER_TYPE_MAP.get(id);
    }
}
