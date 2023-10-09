package com.yanny.ytech.configuration.screen;

import com.yanny.ytech.configuration.container.CrusherContainerMenu;
import com.yanny.ytech.configuration.screen.components.AbstractScreenHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class CrusherScreen extends AbstractScreen<CrusherContainerMenu> {
    public CrusherScreen(AbstractContainerMenu container, Inventory inventory, Component title) {
        super((CrusherContainerMenu) container, inventory, title);
    }

    public AbstractScreenHandler<CrusherContainerMenu> getScreenHandler() {
        return new AbstractScreenHandler.Builder<CrusherContainerMenu>(menu.getBlockEntity().getItemStackHandler()).build();
    }
}
