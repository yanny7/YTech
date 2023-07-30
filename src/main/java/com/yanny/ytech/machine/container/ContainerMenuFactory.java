package com.yanny.ytech.machine.container;

import com.yanny.ytech.configuration.YTechConfigLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuFactory {
    public static MachineContainerMenu create(int windowId, Player player, BlockPos pos, YTechConfigLoader.Machine machine, YTechConfigLoader.Tier tier) {
        return switch (machine.id()) {
            case FURNACE -> new FurnaceContainerMenu(windowId, player, pos, machine, tier);
            case CRUSHER -> new CrusherContainerMenu(windowId, player, pos, machine, tier);
        };
    }
}
