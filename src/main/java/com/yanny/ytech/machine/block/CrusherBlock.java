package com.yanny.ytech.machine.block;

import com.yanny.ytech.configuration.YTechConfigLoader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

class CrusherBlock extends MachineBlock {
    public CrusherBlock(Supplier<BlockEntityType<? extends BlockEntity>> entityType, YTechConfigLoader.Machine machine, YTechConfigLoader.Tier tier) {
        super(entityType, machine, tier);
    }
}
