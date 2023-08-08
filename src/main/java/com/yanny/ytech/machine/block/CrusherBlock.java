package com.yanny.ytech.machine.block;

import com.yanny.ytech.machine.MachineType;
import com.yanny.ytech.machine.TierType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

class CrusherBlock extends MachineBlock {
    public CrusherBlock(Supplier<BlockEntityType<? extends BlockEntity>> entityType, MachineType machine, TierType tier) {
        super(entityType, machine, tier);
    }
}
