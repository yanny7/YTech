package com.yanny.ytech.machine.screen;

import com.yanny.ytech.machine.container.MachineContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

class CrusherScreen extends MachineScreen {
    public CrusherScreen(MachineContainerMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
    }
}
