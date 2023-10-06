package com.yanny.ytech.network.generic.common;

import com.yanny.ytech.network.generic.server.ServerNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

public interface NetworkFactory<T extends ServerNetwork<T, O>, O extends INetworkBlockEntity> {
    @NotNull T createNetwork(@NotNull CompoundTag tag, int networkId, @NotNull Consumer<Integer> onChange, @NotNull Consumer<Integer> onRemove);
    @NotNull T createNetwork(int networkId, @NotNull Consumer<Integer> onChange, @NotNull Consumer<Integer> onRemove);
    void sendRemoved(@NotNull PacketDistributor.PacketTarget target, int networkId);
    void sendUpdated(@NotNull PacketDistributor.PacketTarget target, @NotNull T network);
    void sendLevelSync(@NotNull PacketDistributor.PacketTarget target, @NotNull Map<Integer, T> networkMap);
}
