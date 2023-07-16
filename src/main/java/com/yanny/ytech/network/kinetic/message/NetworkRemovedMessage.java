package com.yanny.ytech.network.kinetic.message;

import net.minecraft.network.FriendlyByteBuf;

public record NetworkRemovedMessage(
        int networkId
) {
    public static void encode(NetworkRemovedMessage msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.networkId);
    }

    public static NetworkRemovedMessage decode(FriendlyByteBuf buffer) {
        return new NetworkRemovedMessage(buffer.readInt());
    }
}
