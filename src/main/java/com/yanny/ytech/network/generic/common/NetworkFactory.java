package com.yanny.ytech.network.generic.common;

import com.yanny.ytech.network.generic.message.LevelSyncMessage;
import com.yanny.ytech.network.generic.message.NetworkAddedOrUpdatedMessage;
import com.yanny.ytech.network.generic.message.NetworkRemovedMessage;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

public interface NetworkFactory<T extends AbstractNetwork<T, O>, O extends INetworkBlockEntity> {
    @NotNull T createNetwork(@NotNull CompoundTag tag, int networkId, @NotNull Consumer<Integer> onRemove);
    @NotNull T createNetwork(int networkId, @NotNull Consumer<Integer> onRemove);
    @NotNull NetworkRemovedMessage createNetworkRemoveMessage(int networkId);
    @NotNull NetworkAddedOrUpdatedMessage<T, O> createNetworkAddedOrUpdatedMessage(@NotNull T network);
    @NotNull LevelSyncMessage<T, O> createLevelSyncMessage(@NotNull Map<Integer, T> networkMap);
}
