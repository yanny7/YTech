package com.yanny.ytech.machine.screen;

import com.yanny.ytech.machine.container.YTechContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

class FurnaceScreen extends YTechScreen {
    public FurnaceScreen(YTechContainerMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
    }
}
