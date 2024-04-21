package com.yanny.ytech.configuration.screen;

import com.yanny.ytech.configuration.container.AqueductFertilizerMenu;
import com.yanny.ytech.configuration.screen.components.AbstractScreenHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class AqueductFertilizerScreen extends AbstractScreen<AqueductFertilizerMenu> {
    public AqueductFertilizerScreen(AbstractContainerMenu container, Inventory inventory, Component title) {
        super((AqueductFertilizerMenu) container, inventory, title);
    }

    @Override
    public AbstractScreenHandler<AqueductFertilizerMenu> getScreenHandler() {
        return new AbstractScreenHandler.Builder<AqueductFertilizerMenu>(menu.getBlockEntity().getItemStackHandler()).build();
    }
}
