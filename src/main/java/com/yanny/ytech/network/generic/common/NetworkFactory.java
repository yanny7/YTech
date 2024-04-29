package com.yanny.ytech.network.generic.common;

import com.yanny.ytech.network.generic.server.ServerNetwork;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface NetworkFactory<T extends ServerNetwork<T, O>, O extends INetworkBlockEntity> {
    @NotNull T createNetwork(@NotNull CompoundTag tag, int networkId, @NotNull Consumer<Integer> onChange,
                             @NotNull BiConsumer<Integer, ChunkPos> onRemove, HolderLookup.Provider provider);
    @NotNull T createNetwork(int networkId, @NotNull Consumer<Integer> onChange, @NotNull BiConsumer<Integer, ChunkPos> onRemove);
    void sendRemoved(@NotNull ServerPlayer player, int networkId);
    void sendUpdated(@NotNull ServerPlayer player, @NotNull T network);
    void sendLevelSync(@NotNull ServerPlayer player, @NotNull Map<Integer, T> networkMap);
}
