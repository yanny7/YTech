package com.yanny.ytech.network.irrigation;

import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public interface IIrrigationBlockEntity extends INetworkBlockEntity {
    void onRemove();
    void onChangedState(@NotNull BlockState oldBlockState, @NotNull BlockState newBlockState);
    @NotNull NetworkType getNetworkType();

    default int getFlow() {
        return 0;
    }

    default int getCapacity() {
        return 0;
    }

    default boolean validForRainFilling() {
        return false;
    }
}
