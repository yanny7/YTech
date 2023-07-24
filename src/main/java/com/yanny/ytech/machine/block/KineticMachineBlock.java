package com.yanny.ytech.machine.block;

import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.machine.block_entity.KineticMachineBlockEntity;
import com.yanny.ytech.network.kinetic.common.IKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

class KineticMachineBlock extends MachineBlock {
    public KineticMachineBlock(Supplier<BlockEntityType<? extends BlockEntity>> entityType, YTechConfigLoader.Machine machine, YTechConfigLoader.Tier tier) {
        super(entityType, machine, tier);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> entityType) {
        return level.isClientSide ? null : createTickerHelper(entityType, entityType, KineticMachineBlock::createMachineTicker);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState oldBlockState, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newBlockState, boolean b) {
        if (!level.isClientSide) {
            if (oldBlockState.hasBlockEntity() && (!oldBlockState.is(newBlockState.getBlock()) || !newBlockState.hasBlockEntity())) {
                if (level.getBlockEntity(pos) instanceof IKineticBlockEntity kineticBlockEntity) {
                    kineticBlockEntity.onRemove();
                }
            } else if (oldBlockState.hasBlockEntity() && oldBlockState.is(newBlockState.getBlock())) {
                if (level.getBlockEntity(pos) instanceof IKineticBlockEntity kineticBlockEntity) {
                    kineticBlockEntity.onChangedState(oldBlockState, newBlockState);
                }
            }
        }

        super.onRemove(oldBlockState, level, pos, newBlockState, b);
    }

    public static void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntity blockEntity) {
        if (blockEntity instanceof KineticMachineBlockEntity block) {
            block.tick(level, pos, state, block);
        }
    }
}
