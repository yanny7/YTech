package com.yanny.ytech.network.generic.message;

import com.yanny.ytech.network.generic.common.AbstractNetwork;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Function;

public record NetworkAddedOrUpdatedMessage<N extends AbstractNetwork>(
        N network
) {
    public static <N extends AbstractNetwork> void encode(NetworkAddedOrUpdatedMessage<N> msg, FriendlyByteBuf buffer, BiConsumer<FriendlyByteBuf, N> networkEncode) {
        networkEncode.accept(buffer, msg.network);
    }

    public static <N extends AbstractNetwork> NetworkAddedOrUpdatedMessage<N> decode(FriendlyByteBuf buffer, Function<FriendlyByteBuf, N> networkDecode) {
        return new NetworkAddedOrUpdatedMessage<>(networkDecode.apply(buffer));
    }
}
