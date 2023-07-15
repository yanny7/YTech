package com.yanny.ytech.machine.block_entity;

import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.machine.IMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class YTechBlockEntity extends BlockEntity implements IMachineBlockEntity {
    protected final YTechConfigLoader.Tier tier;

    public YTechBlockEntity(BlockEntityType<? extends BlockEntity> blockEntityType, BlockPos pos, BlockState blockState, YTechConfigLoader.Tier tier) {
        super(blockEntityType, pos, blockState);
        this.tier = tier;
    }

    @Override
    public YTechConfigLoader.Tier getTier() {
        return tier;
    }
}
