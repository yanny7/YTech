package com.yanny.ytech.machine.screen;

import com.yanny.ytech.machine.container.YTechContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenFactory {
    public static YTechScreen create(YTechContainerMenu container, Inventory inventory, Component title) {
        return switch (container.machine.machineType()) {
            case FURNACE -> new FurnaceScreen(container, inventory, title);
            case CRUSHER -> new CrusherScreen(container, inventory, title);
        };
    }
}
