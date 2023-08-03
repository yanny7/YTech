package com.yanny.ytech.configuration;

import com.google.gson.annotations.SerializedName;

public enum BlockObjectType {
    @SerializedName("stone_ore") STONE_ORE,
    @SerializedName("netherrack_ore") NETHERRACK_ORE,
    @SerializedName("deepslate_ore") DEEPSLATE_ORE,
    @SerializedName("storage_block") STORAGE_BLOCK,
    @SerializedName("raw_storage_block") RAW_STORAGE_BLOCK,
}
