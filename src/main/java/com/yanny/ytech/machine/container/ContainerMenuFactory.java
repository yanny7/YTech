package com.yanny.ytech.machine.container;

import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.machine.MachineType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuFactory {
    public static YTechContainerMenu create(int windowId, Player player, BlockPos pos, YTechConfigLoader.Machine machine, YTechConfigLoader.Tier tier) {
        return switch (MachineType.fromConfiguration(machine.id())) {
            case FURNACE -> new FurnaceContainerMenu(windowId, player, pos, machine, tier);
            case CRUSHER -> new CrusherContainerMenu(windowId, player, pos, machine, tier);
        };
    }
}
