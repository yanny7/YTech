package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.MachineItemStackHandler;
import com.yanny.ytech.network.kinetic.KineticUtils;
import com.yanny.ytech.network.kinetic.common.KineticNetworkType;
import com.yanny.ytech.network.kinetic.common.RotationDirection;
import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StoneCrusherBlockEntity extends KineticMachineBlockEntity {
    private final List<BlockPos> validNeighbors;

    public StoneCrusherBlockEntity(Holder holder, BlockEntityType<? extends BlockEntity> blockEntityType, BlockPos pos, BlockState blockState) {
        super(holder, blockEntityType, pos, blockState);
        validNeighbors = KineticUtils.getDirections(List.of(Direction.EAST, Direction.WEST, Direction.NORTH), pos, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Override
    public @NotNull List<BlockPos> getValidNeighbors() {
        return validNeighbors;
    }

    @Override
    public @NotNull KineticNetworkType getKineticNetworkType() {
        return KineticNetworkType.CONSUMER;
    }

    @Override
    public int getStress() {
        return 8; //TODO
    }

    @NotNull
    @Override
    public RotationDirection getRotationDirection() {
        return RotationDirection.NONE;
    }

    @NotNull
    @Override
    protected MachineItemStackHandler getContainerHandler() {
        return new MachineItemStackHandler.Builder()
                .addInputSlot(32, 32, (itemStack) -> true)
                .addOutputSlot(64, 32)
                .setOnChangeListener(this::setChanged)
                .build();
    }
}
