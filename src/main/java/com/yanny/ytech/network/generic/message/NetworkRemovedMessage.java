package com.yanny.ytech.network.generic.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public abstract class NetworkRemovedMessage {
    public final int networkId;

    public NetworkRemovedMessage(int networkId) {
        this.networkId = networkId;
    }

    public NetworkRemovedMessage(@NotNull RegistryFriendlyByteBuf buf) {
        this.networkId = buf.readInt();
    }

    protected void write(@NotNull RegistryFriendlyByteBuf buf) {
        buf.writeInt(networkId);
    }
}
