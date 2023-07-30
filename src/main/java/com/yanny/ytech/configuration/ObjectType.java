package com.yanny.ytech.configuration;

import com.google.gson.annotations.SerializedName;

public enum ObjectType {
    @SerializedName("item") ITEM,
    @SerializedName("block") BLOCK,
    @SerializedName("fluid") FLUID,
}
