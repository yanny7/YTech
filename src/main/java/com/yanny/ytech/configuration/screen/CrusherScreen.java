package com.yanny.ytech.configuration.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class CrusherScreen extends MachineScreen {
    public CrusherScreen(AbstractContainerMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
    }
}
