package com.yanny.ytech.configuration.screen;

import com.yanny.ytech.configuration.container.StrainerContainerMenu;
import com.yanny.ytech.configuration.screen.components.AbstractScreenHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class StrainerScreen extends AbstractScreen<StrainerContainerMenu> {
    public StrainerScreen(AbstractContainerMenu container, Inventory inventory, Component title) {
        super((StrainerContainerMenu) container, inventory, title);
    }

    @Override
    AbstractScreenHandler<StrainerContainerMenu> getScreenHandler() {
        return new AbstractScreenHandler.Builder<StrainerContainerMenu>(menu.getBlockEntity().getItemStackHandler()).build();
    }
}
