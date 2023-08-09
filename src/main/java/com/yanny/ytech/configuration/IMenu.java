package com.yanny.ytech.configuration;

import com.yanny.ytech.configuration.screen.BaseScreen;
import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface IMenu {
    AbstractContainerMenu getContainerMenu(Holder holder, int windowId, Inventory inv, BlockPos data);
    BaseScreen getScreen(AbstractContainerMenu container, Inventory inventory, Component title);
}
