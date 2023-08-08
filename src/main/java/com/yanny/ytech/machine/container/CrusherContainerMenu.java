package com.yanny.ytech.machine.container;

import com.yanny.ytech.machine.MachineType;
import com.yanny.ytech.machine.TierType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

class CrusherContainerMenu extends MachineContainerMenu {
    public CrusherContainerMenu(int windowId, Player player, BlockPos pos, MachineType machine, TierType tier) {
        super(windowId, player, pos, machine, tier);
    }
}
