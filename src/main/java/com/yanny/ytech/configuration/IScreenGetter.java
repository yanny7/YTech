package com.yanny.ytech.configuration;

import com.yanny.ytech.configuration.screen.BaseScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

interface IScreenGetter {
    BaseScreen getScreen(AbstractContainerMenu container, Inventory inventory, Component title);
}