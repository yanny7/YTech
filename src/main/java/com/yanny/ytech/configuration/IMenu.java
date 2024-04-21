package com.yanny.ytech.configuration;

import com.yanny.ytech.configuration.container.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import org.jetbrains.annotations.NotNull;

public interface IMenu<T extends AbstractContainerMenu> {
    T getContainerMenu(int windowId, @NotNull Inventory inv, @NotNull BlockPos pos, @NotNull MachineItemStackHandler itemStackHandler, @NotNull ContainerData data);

    default T getContainerMenu(int windowId, @NotNull Inventory inv, @NotNull BlockPos pos) {
        return getContainerMenu(windowId, inv, pos, Utils.getMachineBlockEntity(inv.player, pos).createItemStackHandler(),
                new SimpleContainerData(Utils.getMachineBlockEntity(inv.player, pos).getDataSize()));
    }
}
