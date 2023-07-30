package com.yanny.ytech.network.kinetic.common;

import com.google.gson.annotations.SerializedName;

public enum KineticBlockType {
    @SerializedName("shaft") SHAFT("Shaft"),
    @SerializedName("water_wheel") WATER_WHEEL("Water Wheel")
    ;

    public final String lang;
    public final String id;

    KineticBlockType(String lang) {
        this.lang = lang;
        id = name().toLowerCase();
    }
}
