package com.yanny.ytech.machine;

import org.jetbrains.annotations.NotNull;

public enum TierType {
    STONE("stone", "Stone"),
    STEAM("steam", "Steam"),
    ;

    @NotNull public final String key;
    @NotNull public final String name;

    TierType(@NotNull String key, @NotNull String name) {
        this.key = key;
        this.name = name;
        ;
    }
}
