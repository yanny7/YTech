package com.yanny.ytech.machine.block;

import com.yanny.ytech.configuration.YTechConfigLoader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class BlockFactory {
    public static MachineBlock create(Supplier<BlockEntityType<? extends BlockEntity>> entityTypeSupplier, YTechConfigLoader.Machine machine, YTechConfigLoader.Tier tier) {
        return switch (machine.machineType()) {
            case FURNACE -> new FurnaceBlock(entityTypeSupplier, machine, tier);
            case CRUSHER -> switch (tier.tierType()) {
                case STONE -> new KineticMachineBlock(entityTypeSupplier, machine, tier);
                case STEAM -> new CrusherBlock(entityTypeSupplier, machine, tier);
            };
        };
    }
}
