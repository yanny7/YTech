package com.yanny.ytech.machine.block_entity;

import com.yanny.ytech.configuration.YTechConfigLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CrusherBlockEntity extends YTechBlockEntity {
    public CrusherBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState, YTechConfigLoader.Tier tier) {
        super(blockEntityType, pos, blockState, tier);
    }
}
