package com.yanny.ytech.machine;

import org.jetbrains.annotations.NotNull;

public enum MachineType {
    FURNACE("furnace", "Furnace", TierType.STONE),
    CRUSHER("crusher", "Crusher", TierType.STONE),
    ;

    @NotNull public final String key;
    @NotNull public final String name;
    @NotNull public final TierType fromTier;

    MachineType(@NotNull String key, @NotNull String name, @NotNull TierType fromTier) {
        this.key = key;
        this.name = name;
        this.fromTier = fromTier;
    }
}
