package com.yanny.ytech.network.generic.message;

import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public record NetworkRemovedMessage(
        int networkId
) {
    public static void encode(@NotNull NetworkRemovedMessage msg, @NotNull FriendlyByteBuf buffer) {
        buffer.writeInt(msg.networkId);
    }

    public static NetworkRemovedMessage decode(@NotNull FriendlyByteBuf buffer) {
        return new NetworkRemovedMessage(buffer.readInt());
    }
}
