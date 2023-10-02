package com.yanny.ytech.network.generic.message;

import com.yanny.ytech.network.generic.common.AbstractNetwork;
import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Function;

public record NetworkAddedOrUpdatedMessage<N extends AbstractNetwork<N, O>, O extends INetworkBlockEntity>(
        N network
) {
    public static <N extends AbstractNetwork<N, O>, O extends INetworkBlockEntity> void encode(NetworkAddedOrUpdatedMessage<N, O> msg, FriendlyByteBuf buffer, BiConsumer<FriendlyByteBuf, N> networkEncode) {
        networkEncode.accept(buffer, msg.network);
    }

    public static <N extends AbstractNetwork<N, O>, O extends INetworkBlockEntity> NetworkAddedOrUpdatedMessage<N, O> decode(FriendlyByteBuf buffer, Function<FriendlyByteBuf, N> networkDecode) {
        return new NetworkAddedOrUpdatedMessage<>(networkDecode.apply(buffer));
    }
}
