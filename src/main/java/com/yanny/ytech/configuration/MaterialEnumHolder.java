package com.yanny.ytech.configuration;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface MaterialEnumHolder {
    @NotNull NameHolder getKeyHolder();
    @NotNull NameHolder getNameHolder();
    @NotNull Set<Integer> getTintIndices();

    @NotNull static NameHolder prefix(@NotNull String name) {
        return new NameHolder(name, null);
    }

    @NotNull static NameHolder suffix(@NotNull String name) {
        return new NameHolder(null, name);
    }

    @NotNull static NameHolder both(@NotNull String prefix, @NotNull String suffix) {
        return new NameHolder(prefix, suffix);
    }
}
