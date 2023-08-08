package com.yanny.ytech.machine.container;

import com.yanny.ytech.machine.MachineType;
import com.yanny.ytech.machine.TierType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuFactory {
    public static MachineContainerMenu create(int windowId, Player player, BlockPos pos, MachineType machine, TierType tier) {
        return switch (machine) {
            case FURNACE -> new FurnaceContainerMenu(windowId, player, pos, machine, tier);
            case CRUSHER -> new CrusherContainerMenu(windowId, player, pos, machine, tier);
        };
    }
}
