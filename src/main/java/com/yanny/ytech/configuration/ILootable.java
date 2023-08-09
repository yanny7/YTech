package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.IBlockHolder;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import org.jetbrains.annotations.NotNull;

public interface ILootable<T extends Holder, U extends LootTableSubProvider> {
    void registerLoot(@NotNull T holder, @NotNull U provider);

    static void dropsSelfProvider(@NotNull IBlockHolder holder, @NotNull BlockLootSubProvider provider) {
        provider.dropSelf(holder.getBlockRegistry().get());
    }
}
