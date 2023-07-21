package com.yanny.ytech.network.kinetic.block_entity;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.network.kinetic.common.IKineticBlockEntity;
import com.yanny.ytech.network.kinetic.common.KineticNetworkType;
import com.yanny.ytech.network.kinetic.common.RotationDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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
    private RotationDirection rotationDirection = RotationDirection.NONE;

    public WaterWheelBlockEntity(BlockEntityType<? extends BlockEntity> entityType, BlockPos pos, BlockState blockState) {
        super(entityType, pos, blockState, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING), List.of(Direction.EAST, Direction.WEST), KineticNetworkType.PROVIDER, 0);
    }

    @Override
    public @NotNull RotationDirection getRotationDirection() {
        return rotationDirection;
    }

    @Override
    public void onChangedState(BlockState oldBlockState, BlockState newBlockState) {
        //FIXME debounce
        if (level != null && !level.isClientSide) {
            RotationDirection oldRotationDirection = rotationDirection;
            int oldStress = stress;
            stress = getProducedStress(newBlockState, worldPosition, level);

            if (stress == 0) {
                rotationDirection = RotationDirection.NONE;
            } else {
                rotationDirection = stress > 0 ? RotationDirection.CW : RotationDirection.CCW;
                stress = Math.abs(stress);
            }

            if (oldStress != stress || oldRotationDirection != rotationDirection) {
                YTechMod.KINETIC_PROPAGATOR.server().changed(this);
                setChanged();
            }
        }
    }

    @Override
    public void onLoad() {
        if (level != null && !level.isClientSide) {
            stress = getProducedStress(getBlockState(), worldPosition, level);

            if (stress == 0) {
                rotationDirection = RotationDirection.NONE;
            } else {
                rotationDirection = stress > 0 ? RotationDirection.CW : RotationDirection.CCW;
                stress = Math.abs(stress);
            }
        }

        super.onLoad();
    }

    private static int getProducedStress(BlockState blockState, BlockPos pos, Level level) {
        if (!level.isClientSide) {
            Direction direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            int up = getFlowRate(pos.above(),level, direction);
            int down = -getFlowRate(pos.below(),level, direction);
            int front = getFlowRate(pos.relative(direction),level, direction);
            int back = -getFlowRate(pos.relative(direction.getOpposite()),level, direction);
            return up + down + front + back;
        } else {
            return 0;
        }
    }

    private static int getFlowRate(BlockPos pos, Level level, Direction direction) {
        FluidState fluidState = level.getBlockState(pos).getFluidState();
        int strength = 0;

        if (!fluidState.is(Fluids.EMPTY)) {
            if (fluidState.isSource()) {
                BlockState blockState = level.getBlockState(pos);

                if (blockState.is(Blocks.BUBBLE_COLUMN)) {
                    strength = blockState.getValue(BlockStateProperties.DRAG) ? 8 : -8;
                } else {
                    strength = 8;
                }
            } else {
                if (fluidState.hasProperty(BlockStateProperties.LEVEL_FLOWING)) {
                    double flowStrength = getFlow(fluidState.getFlow(level, pos), direction);
                    strength = (int) Math.round(flowStrength * fluidState.getValue(BlockStateProperties.LEVEL_FLOWING));
                } else if (fluidState.hasProperty(BlockStateProperties.FALLING)) {
                    strength = fluidState.getValue(BlockStateProperties.FALLING) ? 8 : 0;
                }
            }

            strength *= 1000.0 / Math.max(1, fluidState.getFluidType().getViscosity());
        }

        return strength;
    }

    private static double getFlow(Vec3 v, Direction direction) {
        return switch (direction.getAxis()) {
            case X -> v.x;
            case Y -> v.y;
            case Z -> v.z;
        };
    }
}
