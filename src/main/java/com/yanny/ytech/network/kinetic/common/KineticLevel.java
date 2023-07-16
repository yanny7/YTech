package com.yanny.ytech.network.kinetic.common;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class KineticLevel extends SavedData {
    protected static final String TAG_NETWORKS = "networks";
    protected static final String TAG_NETWORK = "network";
    protected static final String TAG_NETWORK_ID = "networkId";
    protected static final Logger LOGGER = LogUtils.getLogger();

    protected final ConcurrentHashMap<Integer, KineticNetwork> networkMap = new ConcurrentHashMap<>();

    public KineticLevel() {}

    @NotNull
    @Override
    public CompoundTag save(@NotNull CompoundTag tag) {
        ListTag list = new ListTag();
        AtomicInteger index = new AtomicInteger();

        networkMap.forEach((networkId, network) -> {
            CompoundTag itemHolder = new CompoundTag();
            itemHolder.putInt(TAG_NETWORK_ID, networkId);
            itemHolder.put(TAG_NETWORK, network.save());
            list.add(index.getAndIncrement(), itemHolder);
        });
        tag.put(TAG_NETWORKS, list);
        return tag;
    }

    @Nullable
    protected KineticNetwork getNetwork(IKineticBlockEntity blockEntity) {
        return networkMap.get(blockEntity.getNetworkId());
    }

    public static void encode(Map<Integer, KineticNetwork> networkMap, FriendlyByteBuf buffer) {
        buffer.writeMap(networkMap, FriendlyByteBuf::writeInt, KineticNetwork::encode);
    }

    @NotNull
    public static Map<Integer, KineticNetwork> decode(FriendlyByteBuf buffer) {
        return buffer.readMap(FriendlyByteBuf::readInt, KineticNetwork::decode);
    }

    /**
     * FIXME REMOVE - Testing
     */
    @Override
    public void setDirty() {
        super.setDirty();
        LOGGER.warn("Updated network. Total: {}", networkMap.size());
        networkMap.forEach((i, n) -> LOGGER.info(n.toString()));
    }
}
