package com.yanny.ytech.network.generic.message;

import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public abstract class NetworkRemovedMessage {
    public final int networkId;

    public NetworkRemovedMessage(int networkId) {
        this.networkId = networkId;
    }

    public NetworkRemovedMessage(@NotNull FriendlyByteBuf buf) {
        this.networkId = buf.readInt();
    }

    public void encode(@NotNull FriendlyByteBuf buf) {
        buf.writeInt(networkId);
    }
}
