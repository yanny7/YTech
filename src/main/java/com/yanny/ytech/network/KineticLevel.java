package com.yanny.ytech.network;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.machine.block_entity.KineticBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class KineticLevel extends SavedData {
    private static final String TAG_NETWORKS = "networks";
    private static final String TAG_NETWORK = "network";
    private static final String TAG_NETWORK_ID = "networkId";
    private static final Logger LOGGER = LogUtils.getLogger();

    private final HashMap<Integer, KineticNetwork> networkMap;

    KineticLevel(CompoundTag tag) {
        networkMap = new HashMap<>();
        load(tag);
    }

    KineticLevel() {
        networkMap = new HashMap<>();
    }

    @NotNull
    KineticNetwork getOrCreateNetwork(KineticBlockEntity blockEntity) {
        final int networkId = blockEntity.getNetworkId();

        if (networkId >= 0) {
            return networkMap.get(networkId);
        } else {
            //FIXME possible performance improvement: first check network area intersection
            List<KineticNetwork> networks = networkMap.values().stream().filter((n) -> n.canConnect(blockEntity)).toList();
            //TODO try to connect to existing network
            if (networks.size() == 0) {
                final int id = networkMap.size();
                KineticNetwork network = new KineticNetwork(id, this::setDirty, networkMap::remove);
                networkMap.put(networkMap.size(), network);
                return network;
            } else if (networks.size() == 1) {
                return networks.get(0);
            } else {
                //TODO merge networks
                return null;
            }
        }
    }

    @Nullable
    KineticNetwork getNetwork(KineticBlockEntity blockEntity) {
        return networkMap.get(blockEntity.getNetworkId());
    }

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

    void load(@NotNull CompoundTag tag) {
        if (tag.contains(TAG_NETWORKS)) {
            ListTag list = tag.getList(TAG_NETWORKS, CompoundTag.TAG_COMPOUND);

            list.forEach((listItem) -> {
                CompoundTag itemHolder = (CompoundTag) listItem;
                int networkId = itemHolder.getInt(TAG_NETWORK_ID);
                networkMap.put(networkId, new KineticNetwork(itemHolder.getCompound(TAG_NETWORK), networkId, this::setDirty, networkMap::remove));
            });

            LOGGER.info("Loaded {} rotary networks", networkMap.size());
        } else {
            LOGGER.info("No rotary network loaded");
        }
    }
}
