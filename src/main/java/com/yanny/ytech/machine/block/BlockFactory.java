package com.yanny.ytech.machine.block;

import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.machine.MachineType;

public class BlockFactory {
    public static YTechBlock create(YTechConfigLoader.Machine machine, YTechConfigLoader.Tier tier) {
        return switch (MachineType.fromConfiguration(machine.id())) {
            case FURNACE -> new FurnaceBlock(machine, tier);
            case CRUSHER -> new CrusherBlock(machine, tier);
        };
    }
}
