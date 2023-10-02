package com.yanny.ytech.network.kinetic;

import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public interface IKineticBlockEntity extends INetworkBlockEntity {
    void onRemove();
    void onChangedState(@NotNull BlockState oldBlockState, @NotNull BlockState newBlockState);
    int getStress();
    @NotNull NetworkType getNetworkType();
    @NotNull RotationDirection getRotationDirection();
}
