package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public enum SimpleBlockType {
    ;

    @NotNull public final String key;
    @NotNull public final String name;
    @NotNull public final Supplier<Block> blockSupplier;
    @NotNull public final BiConsumer<BlockStateProvider, Holder.SimpleBlockHolder> modelConsumer;

    SimpleBlockType(@NotNull String key, @NotNull String name, @NotNull Supplier<Block> blockSupplier,
                    @NotNull BiConsumer<BlockStateProvider, Holder.SimpleBlockHolder> modelConsumer) {
        this.key = key;
        this.name = name;
        this.blockSupplier = blockSupplier;
        this.modelConsumer = modelConsumer;
    }
}
