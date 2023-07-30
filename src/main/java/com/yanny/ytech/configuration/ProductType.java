package com.yanny.ytech.configuration;

import com.google.gson.annotations.SerializedName;

public enum ProductType {
    @SerializedName("ingot") INGOT,
    @SerializedName("dust") DUST,
    @SerializedName("raw_material") RAW_MATERIAL,
    @SerializedName("plate") PLATE,
    @SerializedName("stone_ore") STONE_ORE,
    @SerializedName("netherrack_ore") NETHERRACK_ORE,
    @SerializedName("deepslate_ore") DEEPSLATE_ORE,
    @SerializedName("storage_block") STORAGE_BLOCK,
    @SerializedName("raw_storage_block") RAW_STORAGE_BLOCK,
    @SerializedName("fluid") FLUID,
}
