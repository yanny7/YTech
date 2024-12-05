package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block.AqueductConsumerBlock;
import com.yanny.ytech.network.irrigation.NetworkType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class AqueductConsumerBlockEntity extends IrrigationBlockEntity {
    public AqueductConsumerBlockEntity(@NotNull BlockEntityType<? extends BlockEntity> entityType, @NotNull BlockPos pos, @NotNull BlockState blockState) {
        super(entityType, pos, blockState, ((AqueductConsumerBlock) blockState.getBlock()).getValidNeighbors(blockState, pos));
    }

    @Override
    public @NotNull NetworkType getNetworkType() {
        return NetworkType.CONSUMER;
    }

    public void neighborChanged() {
        if (level != null && !level.isClientSide) {
            YTechMod.IRRIGATION_PROPAGATOR.server().changed(this);
        }
    }

    public boolean isHydrating() {
        return getBlockState().getValue(AqueductConsumerBlock.ACTIVATED);
    }

    public abstract void tick(@NotNull ServerLevel level);
}
