package com.yanny.ytech.configuration.block;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public interface IMenuBlock {
    AbstractContainerScreen<AbstractContainerMenu> getScreen(@NotNull AbstractContainerMenu container, @NotNull Inventory inventory, @NotNull Component title);
}
