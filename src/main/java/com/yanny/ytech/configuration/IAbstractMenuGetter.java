package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import org.jetbrains.annotations.NotNull;

interface IAbstractMenuGetter {
    AbstractContainerMenu getMenu(@NotNull Holder holder, int windowId, @NotNull Inventory inv, @NotNull BlockPos pos,
                                  @NotNull MachineItemStackHandler itemStackHandler, @NotNull ContainerData data);
}

