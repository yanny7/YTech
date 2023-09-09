package com.yanny.ytech.configuration.screen;

import com.yanny.ytech.configuration.container.PrimitiveSmelterContainerMenu;
import com.yanny.ytech.configuration.screen.components.FireComponent;
import com.yanny.ytech.configuration.screen.components.MachineScreenHandler;
import com.yanny.ytech.configuration.screen.components.ProgressComponent;
import com.yanny.ytech.configuration.screen.components.TemperatureComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class PrimitiveSmelterScreen extends MachineScreen<PrimitiveSmelterContainerMenu> {
    public PrimitiveSmelterScreen(AbstractContainerMenu container, Inventory inventory, Component title) {
        super((PrimitiveSmelterContainerMenu) container, inventory, title);
    }

    public MachineScreenHandler<PrimitiveSmelterContainerMenu> getScreenHandler() {
        return new MachineScreenHandler.Builder<>(menu)
                .addComponent(new FireComponent(56, 36, menu::getFuelLeft, menu::burning))
                .addComponent(new TemperatureComponent(8, 15, menu::getMaxTemperature, menu::getTemperature))
                .addComponent(new ProgressComponent(79, 34, menu::getSmeltingProgress, menu::inProgress))
                .build();
    }
}
