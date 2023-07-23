package com.yanny.ytech.network.kinetic.block_entity;

import com.yanny.ytech.network.kinetic.common.IKineticBlockEntity;
import com.yanny.ytech.network.kinetic.common.KineticNetworkType;
import com.yanny.ytech.network.kinetic.common.RotationDirection;
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

    public ShaftBlockEntity(BlockEntityType<? extends BlockEntity> entityType, BlockPos pos, BlockState blockState, float stressMultiplier) {
        super(entityType, pos, blockState, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING), List.of(Direction.EAST, Direction.WEST), KineticNetworkType.CONSUMER, Math.round(BASE_STRESS * stressMultiplier));
    }

    @NotNull
    @Override
    public RotationDirection getRotationDirection() {
        return RotationDirection.NONE;
    }
}
