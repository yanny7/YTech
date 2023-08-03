package com.yanny.ytech.configuration;

import com.google.gson.annotations.SerializedName;

public enum ItemObjectType {
    @SerializedName("ingot") INGOT,
    @SerializedName("dust") DUST,
    @SerializedName("raw_material") RAW_MATERIAL,
    @SerializedName("plate") PLATE,
}
