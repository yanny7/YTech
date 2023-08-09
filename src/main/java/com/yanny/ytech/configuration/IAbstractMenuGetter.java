package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

interface IAbstractMenuGetter {
    AbstractContainerMenu getMenu(Holder holder, int windowId, Inventory inv, BlockPos data);
}

