package com.yanny.ytech.network.generic.message;

import com.yanny.ytech.network.generic.common.AbstractNetwork;
import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class NetworkAddedOrUpdatedMessage<N extends AbstractNetwork<N, O>, O extends INetworkBlockEntity> {
    @NotNull public final N network;
    @NotNull private final BiConsumer<FriendlyByteBuf, N> networkEncode;

    public NetworkAddedOrUpdatedMessage(@NotNull N network, @NotNull BiConsumer<FriendlyByteBuf, N> networkEncode) {
        this.network = network;
        this.networkEncode = networkEncode;
    }

    public NetworkAddedOrUpdatedMessage(@NotNull FriendlyByteBuf buf, @NotNull BiConsumer<FriendlyByteBuf, N> networkEncode, @NotNull Function<FriendlyByteBuf, N> networkDecode) {
        this.networkEncode = networkEncode;
        network = networkDecode.apply(buf);
    }

    public void encode(@NotNull FriendlyByteBuf buf) {
        networkEncode.accept(buf, network);
    }
}
