package com.yanny.ytech.network.generic.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class LevelSyncMessage<T> {
    @NotNull public final Map<Integer, T> networkMap;

    public LevelSyncMessage(@NotNull Map<Integer, T> networkMap) {
        this.networkMap = networkMap;
    }

    public LevelSyncMessage(@NotNull RegistryFriendlyByteBuf buf, Function<FriendlyByteBuf, T> valueReader) {
        networkMap = buf.readMap(FriendlyByteBuf::readInt, valueReader::apply);
    }

    protected void write(@NotNull RegistryFriendlyByteBuf buf, BiConsumer<FriendlyByteBuf, T> valueWriter) {
        buf.writeMap(networkMap, FriendlyByteBuf::writeInt, valueWriter::accept);
    }
}
