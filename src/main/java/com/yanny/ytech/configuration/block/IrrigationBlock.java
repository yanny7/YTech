package com.yanny.ytech.configuration.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.yanny.ytech.network.irrigation.IIrrigationBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public abstract class IrrigationBlock extends BaseEntityBlock {
    public static final Map<Direction, Integer> ANGLE_BY_DIRECTION = ImmutableMap.copyOf(Util.make(Maps.newEnumMap(Direction.class), (enumMap) -> {
        enumMap.put(Direction.NORTH, 0);
        enumMap.put(Direction.EAST, 90);
        enumMap.put(Direction.SOUTH, 180);
        enumMap.put(Direction.WEST, 270);
    }));
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = ImmutableMap.copyOf(Util.make(Maps.newEnumMap(Direction.class), (enumMap) -> {
        enumMap.put(Direction.NORTH, NORTH);
        enumMap.put(Direction.EAST, EAST);
        enumMap.put(Direction.SOUTH, SOUTH);
        enumMap.put(Direction.WEST, WEST);
    }));

    public IrrigationBlock(@NotNull Properties properties) {
        super(properties);
    }

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

    @Override
    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new RuntimeException("Not implemented yet!");
    }

    static boolean isValidForConnection(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull Direction direction) {
        BlockPos neighborPos = pos.offset(direction.getNormal());
        BlockState blockState = level.getBlockState(neighborPos);

        if (blockState.getBlock() instanceof IrrigationBlock irrigationBlock) {
            return irrigationBlock.getValidNeighbors(blockState, neighborPos).contains(pos);
        }

        return false;
    }
}
