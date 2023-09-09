package com.yanny.ytech.configuration.screen;

import com.yanny.ytech.configuration.container.CrusherContainerMenu;
import com.yanny.ytech.configuration.screen.components.MachineScreenHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class CrusherScreen extends MachineScreen<CrusherContainerMenu> {
    public CrusherScreen(AbstractContainerMenu container, Inventory inventory, Component title) {
        super((CrusherContainerMenu) container, inventory, title);
    }

    public MachineScreenHandler<CrusherContainerMenu> getScreenHandler() {
        return new MachineScreenHandler.Builder<>(menu).build();
    }
}
