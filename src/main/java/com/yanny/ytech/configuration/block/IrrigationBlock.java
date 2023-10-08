package com.yanny.ytech.configuration.block;

import com.yanny.ytech.network.irrigation.IIrrigationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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

    abstract List<BlockPos> getValidNeighbors(@NotNull BlockState blockState, @NotNull BlockPos pos);

    public static boolean isValidNeighborBlock(@NotNull BlockPlaceContext blockPlaceContext, @NotNull BlockState blockState) {
        Level level = blockPlaceContext.getLevel();
        BlockPos pos = blockPlaceContext.getClickedPos();
        List<BlockPos> validNeighbors = ((IrrigationBlock) blockState.getBlock()).getValidNeighbors(blockState, pos);

        return Direction.Plane.HORIZONTAL.stream().allMatch((direction) -> {
            BlockPos neighborPos = pos.offset(direction.getNormal());
            BlockState neighborState = level.getBlockState(neighborPos);
            return !(neighborState.getBlock() instanceof IrrigationBlock irrigationBlock)
                    || (irrigationBlock.getValidNeighbors(neighborState, neighborPos).contains(pos) && validNeighbors.contains(neighborPos));
        });
    }
}
