package com.yanny.ytech.network.kinetic.block_entity;

import com.yanny.ytech.network.kinetic.common.IKineticBlockEntity;
import com.yanny.ytech.network.kinetic.common.KineticNetworkType;
import com.yanny.ytech.network.kinetic.common.RotationDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WaterWheelBlockEntity extends KineticBlockEntity implements IKineticBlockEntity {
    public WaterWheelBlockEntity(BlockEntityType<? extends BlockEntity> entityType, BlockPos pos, BlockState blockState) {
        super(entityType, pos, blockState, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING), List.of(Direction.EAST, Direction.WEST), KineticNetworkType.PROVIDER, 0);
    }

    @Override
    public @NotNull RotationDirection getRotationDirection() {
        return getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getAxisDirection() == Direction.AxisDirection.POSITIVE ? RotationDirection.CW : RotationDirection.CCW;
    }

    @Override
    public int getStress() {
        if (level != null) {
            return getProducedStress(getBlockState(), worldPosition, level);
        } else {
            return 0;
        }
    }

    private static int getProducedStress(BlockState blockState, BlockPos pos, Level level) {
        if (!level.isClientSide) {
            Direction direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            return getFlowRate(pos.above(),level) + getFlowRate(pos.below(), level) + getFlowRate(pos.relative(direction), level) + getFlowRate(pos.relative(direction.getOpposite()), level);
        } else {
            return 0;
        }
    }

    private static int getFlowRate(BlockPos pos, Level level) {
        FluidState fluidState = level.getBlockState(pos).getFluidState();

        if (!fluidState.is(Fluids.EMPTY)) {
            Vec3 vec3 = fluidState.getFlow(level, pos);
            int strength = fluidState.getValue(BlockStateProperties.LEVEL_FLOWING);

            return (int) Math.round(vec3.x * strength);
        }

        return 0;
    }

    @Override
    public void onChangedState(BlockState oldBlockState, BlockState newBlockState) {
        if (level != null) {
            int oldStress = stress;
            stress = getProducedStress(newBlockState, worldPosition, level);

            if (oldStress != stress) {
                setChanged();
            }
        }
    }
}
