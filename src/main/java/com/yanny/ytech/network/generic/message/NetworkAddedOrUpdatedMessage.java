package com.yanny.ytech.network.generic.message;

import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class NetworkAddedOrUpdatedMessage<T> {
    public final T payload;

    public NetworkAddedOrUpdatedMessage(T payload) {
        this.payload = payload;
    }

    public NetworkAddedOrUpdatedMessage(@NotNull FriendlyByteBuf buf, Function<FriendlyByteBuf, T> valueReader) {
        this.payload = valueReader.apply(buf);
    }

    public void encode(@NotNull FriendlyByteBuf buf, BiConsumer<FriendlyByteBuf, T> valueWriter) {
        valueWriter.accept(buf, payload);
    }
}
