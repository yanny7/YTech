package com.yanny.ytech.configuration;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

interface IScreenGetter {
    AbstractContainerScreen<?> getScreen(AbstractContainerMenu container, Inventory inventory, Component title);
}