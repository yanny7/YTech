package com.yanny.ytech.machine.block;

import com.yanny.ytech.machine.MachineType;
import com.yanny.ytech.machine.TierType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class BlockFactory {
    public static MachineBlock create(Supplier<BlockEntityType<? extends BlockEntity>> entityTypeSupplier, MachineType machine, TierType tier) {
        return switch (machine) {
            case FURNACE -> new FurnaceBlock(entityTypeSupplier, machine, tier);
            case CRUSHER -> switch (tier) {
                case STONE -> new KineticMachineBlock(entityTypeSupplier, machine, tier);
                case STEAM -> new CrusherBlock(entityTypeSupplier, machine, tier);
            };
        };
    }
}
