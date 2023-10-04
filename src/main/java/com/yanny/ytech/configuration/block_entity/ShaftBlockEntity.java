package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.network.generic.NetworkUtils;
import com.yanny.ytech.network.kinetic.IKineticBlockEntity;
import com.yanny.ytech.network.kinetic.NetworkType;
import com.yanny.ytech.network.kinetic.RotationDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShaftBlockEntity extends KineticBlockEntity implements IKineticBlockEntity {
    private static final float BASE_STRESS = 1f;

    private final float stressMultiplier;
    private final List<BlockPos> validNeighbors;

    public ShaftBlockEntity(BlockEntityType<? extends BlockEntity> entityType, BlockPos pos, BlockState blockState, float stressMultiplier) {
        super(entityType, pos, blockState);
        this.stressMultiplier = stressMultiplier;
        validNeighbors = NetworkUtils.getDirections(List.of(Direction.EAST, Direction.WEST), pos, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    @NotNull
    @Override
    public List<BlockPos> getValidNeighbors() {
        return validNeighbors;
    }

    @NotNull
    @Override
    public NetworkType getNetworkType() {
        return NetworkType.CONSUMER;
    }

    @Override
    public int getStress() {
        return Math.round(BASE_STRESS * stressMultiplier);
    }

    @NotNull
    @Override
    public RotationDirection getRotationDirection() {
        return RotationDirection.NONE;
    }
}
