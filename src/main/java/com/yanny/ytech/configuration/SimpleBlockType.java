package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public enum SimpleBlockType implements IModel<Holder.SimpleBlockHolder, BlockStateProvider>, ILootable<Holder.SimpleBlockHolder, BlockLootSubProvider>,
        IRecipe<Holder.SimpleBlockHolder> {
    ;

    @NotNull public final String key;
    @NotNull public final String name;
    @NotNull public final Supplier<Block> blockSupplier;
    @NotNull private final BiConsumer<Holder.SimpleBlockHolder, BlockStateProvider> modelGetter;
    @NotNull private final BiConsumer<Holder.SimpleBlockHolder, BlockLootSubProvider> lootGetter;
    @NotNull private final BiConsumer<Holder.SimpleBlockHolder, Consumer<FinishedRecipe>> recipeGetter;

    SimpleBlockType(@NotNull String key, @NotNull String name, @NotNull Supplier<Block> blockSupplier,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, BlockStateProvider> modelGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, BlockLootSubProvider> lootGetter,
                    @NotNull BiConsumer<Holder.SimpleBlockHolder, Consumer<FinishedRecipe>> recipeGetter) {
        this.key = key;
        this.name = name;
        this.blockSupplier = blockSupplier;
        this.modelGetter = modelGetter;
        this.lootGetter = lootGetter;
        this.recipeGetter = recipeGetter;
    }

    @Override
    public void registerModel(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockStateProvider provider) {
        modelGetter.accept(holder, provider);
    }

    @Override
    public void registerLoot(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockLootSubProvider provider) {
        lootGetter.accept(holder, provider);
    }

    @Override
    public void registerRecipe(@NotNull Holder.SimpleBlockHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        recipeGetter.accept(holder, recipeConsumer);
    }

    private static void dropsSelfProvider(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockLootSubProvider provider) {
        provider.dropSelf(holder.block.get());
    }
}
