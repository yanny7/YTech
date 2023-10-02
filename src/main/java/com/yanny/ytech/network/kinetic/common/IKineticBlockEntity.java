package com.yanny.ytech.network.kinetic.common;

import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public interface IKineticBlockEntity extends INetworkBlockEntity {
    void onRemove();
    void onChangedState(BlockState oldBlockState, BlockState newBlockState);
    int getStress();
    NetworkType getNetworkType();
    @NotNull RotationDirection getRotationDirection();
}
