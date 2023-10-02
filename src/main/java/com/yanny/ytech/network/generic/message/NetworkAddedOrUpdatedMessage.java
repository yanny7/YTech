package com.yanny.ytech.network.generic.message;

import com.yanny.ytech.network.generic.common.AbstractNetwork;
import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

public record NetworkAddedOrUpdatedMessage<N extends AbstractNetwork<N, O>, O extends INetworkBlockEntity>(
        @NotNull N network
) {
    public static <N extends AbstractNetwork<N, O>, O extends INetworkBlockEntity> void encode(@NotNull NetworkAddedOrUpdatedMessage<N, O> msg,
                                                                                               @NotNull FriendlyByteBuf buffer,
                                                                                               @NotNull BiConsumer<FriendlyByteBuf, N> networkEncode) {
        networkEncode.accept(buffer, msg.network);
    }

    public static <N extends AbstractNetwork<N, O>, O extends INetworkBlockEntity> NetworkAddedOrUpdatedMessage<N, O> decode(@NotNull FriendlyByteBuf buffer,
                                                                                                                             @NotNull Function<FriendlyByteBuf, N> networkDecode) {
        return new NetworkAddedOrUpdatedMessage<>(networkDecode.apply(buffer));
    }
}
