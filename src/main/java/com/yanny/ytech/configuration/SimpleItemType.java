package com.yanny.ytech.configuration;

import org.jetbrains.annotations.NotNull;

public enum SimpleItemType {
    GRASS_FIBERS("grass_fibers", "Grass Fibers"),
    GRASS_TWINE("grass_twine", "Grass Twine"),
    ;

    @NotNull public final String key;
    @NotNull public final String name;

    SimpleItemType(@NotNull String key, @NotNull String name) {
        this.key = key;
        this.name = name;
    }
}
