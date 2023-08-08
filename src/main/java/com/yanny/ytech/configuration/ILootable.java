package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraft.data.loot.LootTableSubProvider;
import org.jetbrains.annotations.NotNull;

public interface ILootable<T extends Holder, U extends LootTableSubProvider> {
    void registerLoot(@NotNull T holder, @NotNull U provider);
}
