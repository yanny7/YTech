package com.yanny.ytech.network.irrigation;

import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import org.jetbrains.annotations.NotNull;

public interface IIrrigationBlockEntity extends INetworkBlockEntity {
    int getFlow();
    @NotNull NetworkType getNetworkType();
}
