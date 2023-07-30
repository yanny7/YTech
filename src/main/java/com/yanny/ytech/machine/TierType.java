package com.yanny.ytech.machine;

import com.google.gson.annotations.SerializedName;

public enum TierType {
    @SerializedName("stone") STONE,
    @SerializedName("steam") STEAM,
    ;

    public final String id;

    TierType() {
        id = name().toLowerCase();
    }
}
