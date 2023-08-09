package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.network.kinetic.KineticUtils;
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
    private final float stressMultiplier;
    private int producedStress;
    private final List<BlockPos> validNeighbors;
    private RotationDirection rotationDirection = RotationDirection.NONE;

    public WaterWheelBlockEntity(BlockEntityType<? extends BlockEntity> entityType, BlockPos pos, BlockState blockState, float stressMultiplier) {
        super(entityType, pos, blockState);
        this.stressMultiplier = stressMultiplier;
        producedStress = 0;
        validNeighbors = KineticUtils.getDirections(List.of(Direction.EAST, Direction.WEST), pos, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    @NotNull
    @Override
    public RotationDirection getRotationDirection() {
        return rotationDirection;
    }

    @NotNull
    @Override
    public List<BlockPos> getValidNeighbors() {
        return validNeighbors;
    }

    @NotNull
    @Override
    public KineticNetworkType getKineticNetworkType() {
        return KineticNetworkType.PROVIDER;
    }

    @Override
    public int getStress() {
        return producedStress;
    }

    @Override
    public void onChangedState(BlockState oldBlockState, BlockState newBlockState) {
        //FIXME debounce
        if (level != null && !level.isClientSide) {
            RotationDirection oldRotationDirection = rotationDirection;
            int oldStress = producedStress;
            producedStress = Math.round(getProducedStress(newBlockState, worldPosition, level) * stressMultiplier);

            if (producedStress == 0) {
                rotationDirection = RotationDirection.NONE;
            } else {
                rotationDirection = producedStress > 0 ? RotationDirection.CW : RotationDirection.CCW;
                producedStress = Math.abs(producedStress);
            }

            if (oldStress != producedStress || oldRotationDirection != rotationDirection) {
                YTechMod.KINETIC_PROPAGATOR.server().changed(this);
                setChanged();
            }
        }
    }

    @Override
    public void onLoad() {
        if (level != null && !level.isClientSide) {
            producedStress = Math.round(getProducedStress(getBlockState(), worldPosition, level) * stressMultiplier);

            if (producedStress == 0) {
                rotationDirection = RotationDirection.NONE;
            } else {
                rotationDirection = producedStress > 0 ? RotationDirection.CW : RotationDirection.CCW;
                producedStress = Math.abs(producedStress);
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
                    strength = (int) Math.round(8 * getFlowInDirection(fluidState.getFlow(level, pos), direction));
                }
            } else {
                if (fluidState.hasProperty(BlockStateProperties.LEVEL_FLOWING)) {
                    double flowStrength = getFlowInDirection(fluidState.getFlow(level, pos), direction);
                    strength = (int) Math.round(flowStrength * fluidState.getValue(BlockStateProperties.LEVEL_FLOWING));
                } else if (fluidState.hasProperty(BlockStateProperties.FALLING)) {
                    strength = fluidState.getValue(BlockStateProperties.FALLING) ? 8 : 0;
                }
            }

            strength *= 1000.0 / Math.max(1, fluidState.getFluidType().getViscosity());
        }

        return strength;
    }

    private static double getFlowInDirection(Vec3 v, Direction direction) {
        return switch (direction.getAxis()) {
            case X -> v.x;
            case Y -> v.y;
            case Z -> v.z;
        };
    }
}
