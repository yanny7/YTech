package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.MachineItemStackHandler;
import net.minecraft.world.inventory.ContainerData;
import org.jetbrains.annotations.NotNull;

public interface IMenuBlockEntity {
    @NotNull MachineItemStackHandler createItemStackHandler();
    @NotNull MachineItemStackHandler getItemStackHandler();
    @NotNull ContainerData createContainerData();
    int getDataSize();
}
