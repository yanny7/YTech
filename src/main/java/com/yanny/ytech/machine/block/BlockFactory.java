package com.yanny.ytech.machine.block;

import com.yanny.ytech.configuration.YTechConfigLoader;

public class BlockFactory {
    public static YTechBlock create(YTechConfigLoader.Machine machine, YTechConfigLoader.Tier tier) {
        return switch (machine.machineType()) {
            case FURNACE -> new FurnaceBlock(machine, tier);
            case CRUSHER -> switch (tier.tierType()) {
                case STONE -> new KineticCrusherBlock(machine, tier);
                case STEAM -> new CrusherBlock(machine, tier);
            };
        };
    }
}
