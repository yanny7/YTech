package com.yanny.ytech.configuration;

import com.yanny.ytech.configuration.item.SharpFlintItem;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum SimpleToolType {
    SHARP_FLINT("sharp_flint", "Sharp Flint", SharpFlintItem::new),
    ;

    @NotNull public final String key;
    @NotNull public final String name;
    @NotNull public final Supplier<Item> toolSupplier;

    SimpleToolType(@NotNull String key, @NotNull String name, @NotNull Supplier<Item> toolSupplier) {
        this.key = key;
        this.name = name;
        this.toolSupplier = toolSupplier;
    }
}
