package com.yanny.ytech.machine.screen;

import com.yanny.ytech.machine.container.YTechContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

class CrusherScreen extends YTechScreen {
    public CrusherScreen(YTechContainerMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
    }
}
