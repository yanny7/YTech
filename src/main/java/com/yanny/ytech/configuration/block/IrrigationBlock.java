package com.yanny.ytech.configuration.block;

import com.yanny.ytech.network.irrigation.IIrrigationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class IrrigationBlock extends BaseEntityBlock {

    public IrrigationBlock(@NotNull Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState oldBlockState, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newBlockState, boolean movedByPiston) {
        if (!level.isClientSide) {
            if (oldBlockState.hasBlockEntity() && (!oldBlockState.is(newBlockState.getBlock()) || !newBlockState.hasBlockEntity())) {
                if (level.getBlockEntity(pos) instanceof IIrrigationBlockEntity kineticBlockEntity && kineticBlockEntity.getNetworkId() >= 0) {
                    kineticBlockEntity.onRemove();
                }
            } else if (oldBlockState.hasBlockEntity() && oldBlockState.is(newBlockState.getBlock())) {
                if (level.getBlockEntity(pos) instanceof IIrrigationBlockEntity kineticBlockEntity) {
                    kineticBlockEntity.onChangedState(oldBlockState, newBlockState);
                }
            }
        }

        super.onRemove(oldBlockState, level, pos, newBlockState, movedByPiston);
    }
}