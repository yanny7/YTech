package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraft.data.loot.BlockLootSubProvider;
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
    @NotNull private final BiConsumer<Holder.SimpleBlockHolder, BlockStateProvider> modelGetter;
    @NotNull private final BiConsumer<Holder.SimpleBlockHolder, BlockLootSubProvider> lootGetter;

    SimpleBlockType(@NotNull String key, @NotNull String name, @NotNull Supplier<Block> blockSupplier,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, BlockStateProvider> modelGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, BlockLootSubProvider> lootGetter) {
        this.key = key;
        this.name = name;
        this.blockSupplier = blockSupplier;
        this.modelGetter = modelGetter;
        this.lootGetter = lootGetter;
    }

    public void registerModel(Holder.SimpleBlockHolder holder, BlockStateProvider provider) {
        modelGetter.accept(holder, provider);
    }

    public void registerLoot(Holder.SimpleBlockHolder holder, BlockLootSubProvider provider) {
        lootGetter.accept(holder, provider);
    }
}
