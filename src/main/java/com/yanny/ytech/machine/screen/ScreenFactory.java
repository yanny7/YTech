package com.yanny.ytech.machine.screen;

import com.yanny.ytech.machine.container.MachineContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenFactory {
    public static MachineScreen create(MachineContainerMenu container, Inventory inventory, Component title) {
        return switch (container.machine) {
            case FURNACE -> new FurnaceScreen(container, inventory, title);
            case CRUSHER -> new CrusherScreen(container, inventory, title);
        };
    }
}
