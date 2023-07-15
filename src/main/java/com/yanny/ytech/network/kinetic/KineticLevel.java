package com.yanny.ytech.network.kinetic;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class KineticLevel extends SavedData {
    private static final String TAG_NETWORKS = "networks";
    private static final String TAG_NETWORK = "network";
    private static final String TAG_NETWORK_ID = "networkId";
    private static final Logger LOGGER = LogUtils.getLogger();

    private final ConcurrentHashMap<Integer, KineticNetwork> networkMap = new ConcurrentHashMap<>();

    KineticLevel(CompoundTag tag) {
        load(tag);
    }

    KineticLevel() {}

    void add(IKineticBlockEntity blockEntity) {
        final int networkId = blockEntity.getNetworkId();
        KineticNetwork resultNetwork;

        if (networkId >= 0) {
            resultNetwork = networkMap.get(networkId);
        } else {
            //TODO possible performance improvement: first check network area intersection
            List<KineticNetwork> networks = networkMap.values().stream().filter((n) -> n.canConnect(blockEntity)).toList();

            if (networks.size() == 0) {
                KineticNetwork network = new KineticNetwork(getUniqueId(), networkMap::remove);
                networkMap.put(network.getNetworkId(), network);
                resultNetwork = network;
            } else if (networks.size() == 1) {
                resultNetwork = networks.get(0);
            } else {
                ArrayList<KineticNetwork> distinctNetworks = networks.stream().filter(distinctByKey(KineticNetwork::getNetworkId)).collect(Collectors.toCollection(ArrayList::new));

                if (distinctNetworks.size() == 1) {
                    resultNetwork = distinctNetworks.get(0);
                } else {
                    KineticNetwork network = distinctNetworks.remove(0);

                    do {
                        KineticNetwork toRemove = distinctNetworks.remove(0);
                        network.addAll(toRemove, blockEntity.getLevel());
                        networkMap.remove(toRemove.getNetworkId());
                    } while (distinctNetworks.size() > 0);

                    resultNetwork = network;
                }
            }
        }

        blockEntity.getKineticType().addEntity.accept(resultNetwork, blockEntity);
        setDirty();
    }

    void remove(IKineticBlockEntity blockEntity) {
        KineticNetwork network = getNetwork(blockEntity);

        if (network != null) {
            List<KineticNetwork> networks = network.remove(this::getUniqueIds, networkMap::remove, blockEntity);
            networkMap.putAll(networks.stream().collect(Collectors.toMap(KineticNetwork::getNetworkId, n -> n)));
            setDirty();
        } else {
            LOGGER.warn("Can't get network for block {} at {}", blockEntity, blockEntity.getBlockPos());
        }
    }

    @Nullable
    KineticNetwork getNetwork(IKineticBlockEntity blockEntity) {
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
                networkMap.put(networkId, new KineticNetwork(itemHolder.getCompound(TAG_NETWORK), networkId, networkMap::remove));
            });

            LOGGER.info("Loaded {} rotary networks", networkMap.size());
        } else {
            LOGGER.info("No rotary network loaded");
        }
    }

    private int getUniqueId() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (!networkMap.containsKey(i)) {
                return i;
            }
        }

        LOGGER.error("Network keys overflow!");
        throw new IllegalStateException("Can't generate new ID for network!");
    }

    private List<Integer> getUniqueIds(int count) {
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (!networkMap.containsKey(i)) {
                result.add(i);

                if (result.size() == count) {
                    return result;
                }
            }
        }

        LOGGER.error("Network keys overflow!");
        throw new IllegalStateException("Can't generate new ID for network!");
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        return t -> ConcurrentHashMap.newKeySet().add(keyExtractor.apply(t));
    }

    @Override
    public void setDirty() {
        super.setDirty();
        LOGGER.warn("Updated network. Total: {}", networkMap.size());
        networkMap.forEach((i, n) -> LOGGER.info(n.toString()));
    }
}
