package com.yanny.ytech.network.kinetic.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IKineticBlockEntity {
    @NotNull BlockPos getBlockPos();
    @Nullable Level getLevel();
    @NotNull List<BlockPos> getValidNeighbors();
    int getNetworkId();
    void setNetworkId(int id);
    void onRemove();
    void onChangedState(BlockState oldBlockState, BlockState newBlockState);
    @NotNull KineticNetworkType getKineticNetworkType();
    int getStress();
    @NotNull RotationDirection getRotationDirection();
}
