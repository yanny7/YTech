package com.yanny.ytech.configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record NameHolder(
        @Nullable String prefix,
        @Nullable String suffix
){
    @NotNull public static NameHolder prefix(@NotNull String name) {
        return new NameHolder(name, null);
    }

    @NotNull public static NameHolder suffix(@NotNull String name) {
        return new NameHolder(null, name);
    }

    @NotNull public static NameHolder both(@NotNull String prefix, @NotNull String suffix) {
        return new NameHolder(prefix, suffix);
    }
}
