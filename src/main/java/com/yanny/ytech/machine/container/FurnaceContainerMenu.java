package com.yanny.ytech.machine.container;

import com.yanny.ytech.configuration.ConfigLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

class FurnaceContainerMenu extends MachineContainerMenu {
    public FurnaceContainerMenu(int windowId, Player player, BlockPos pos, ConfigLoader.Machine machine, ConfigLoader.Tier tier) {
        super(windowId, player, pos, machine, tier);
    }
}
