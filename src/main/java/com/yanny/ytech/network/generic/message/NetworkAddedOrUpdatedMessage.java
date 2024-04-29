package com.yanny.ytech.network.generic.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class NetworkAddedOrUpdatedMessage<T> {
    public final T payload;

    public NetworkAddedOrUpdatedMessage(T payload) {
        this.payload = payload;
    }

    public NetworkAddedOrUpdatedMessage(@NotNull RegistryFriendlyByteBuf buf, Function<FriendlyByteBuf, T> valueReader) {
        this.payload = valueReader.apply(buf);
    }

    protected void write(@NotNull RegistryFriendlyByteBuf buf, BiConsumer<FriendlyByteBuf, T> valueWriter) {
        valueWriter.accept(buf, payload);
    }
}
