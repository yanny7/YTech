package com.yanny.ytech.configuration.container;

import com.yanny.ytech.configuration.MachineItemStackHandler;
import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import org.jetbrains.annotations.NotNull;

public class FurnaceContainerMenu extends MachineContainerMenu {
    public FurnaceContainerMenu(@NotNull Holder holder, int windowId, @NotNull Player player, @NotNull BlockPos pos,
                                @NotNull MachineItemStackHandler itemStackHandler, @NotNull ContainerData data) {
        super(holder, windowId, player, pos, itemStackHandler, data);
    }
}
