package com.yanny.ytech.machine;

import com.google.gson.annotations.SerializedName;

public enum MachineType {
    @SerializedName("furnace") FURNACE,
    @SerializedName("crusher") CRUSHER,
    ;

    public final String id;

    MachineType() {
        id = name().toLowerCase();
    }
}
