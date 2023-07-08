package com.yanny.ytech.machine.container;

import com.yanny.ytech.configuration.YTechConfigLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

class FurnaceContainerMenu extends YTechContainerMenu {
    public FurnaceContainerMenu(int windowId, Player player, BlockPos pos, YTechConfigLoader.Machine machine, YTechConfigLoader.Tier tier) {
        super(windowId, player, pos, machine, tier);
    }
}
