package com.yanny.ytech.network.kinetic.server;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.network.kinetic.common.IKineticBlockEntity;
import com.yanny.ytech.network.kinetic.common.KineticNetwork;
import com.yanny.ytech.network.kinetic.message.NetworkAddedOrUpdatedMessage;
import com.yanny.ytech.network.kinetic.message.NetworkRemovedMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ServerKineticLevel extends SavedData {
    protected static final String TAG_NETWORKS = "networks";
    protected static final String TAG_NETWORK = "network";
    protected static final String TAG_NETWORK_ID = "networkId";
    protected static final Logger LOGGER = LogUtils.getLogger();

    private final ConcurrentHashMap<Integer, KineticNetwork> networkMap = new ConcurrentHashMap<>();
    private final SimpleChannel channel;

    ServerKineticLevel(CompoundTag tag, SimpleChannel channel) {
        load(tag);
        this.channel = channel;
    }

    ServerKineticLevel(SimpleChannel channel) {
        super();
        this.channel = channel;
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

    void add(IKineticBlockEntity blockEntity) {
        final int networkId = blockEntity.getNetworkId();
        KineticNetwork resultNetwork;

        if (networkId >= 0) {
            resultNetwork = networkMap.get(networkId);

            if (!resultNetwork.canAttach(blockEntity)) {
                LOGGER.warn("Can't attach block {} to network at {} - {} <> {}", blockEntity, blockEntity.getBlockPos(), blockEntity.getRotationDirection(), resultNetwork.getRotationDirection());
                blockEntity.getLevel().destroyBlock(blockEntity.getBlockPos(), true);
                return;
            }
        } else {
            //TODO possible performance improvement: first check network area intersection
            List<KineticNetwork> networks = networkMap.values().stream().filter((n) -> n.canConnect(blockEntity)).toList();

            if (networks.size() == 0) {
                KineticNetwork network = new KineticNetwork(getUniqueId(), this::onRemove);
                networkMap.put(network.getNetworkId(), network);
                resultNetwork = network;
            } else if (networks.size() == 1) {
                resultNetwork = networks.get(0);

                if (!resultNetwork.canAttach(blockEntity)) {
                    LOGGER.warn("Can't attach block {} to network at {} - {} <> {}", blockEntity, blockEntity.getBlockPos(), blockEntity.getRotationDirection(), resultNetwork.getRotationDirection());
                    blockEntity.getLevel().destroyBlock(blockEntity.getBlockPos(), true);
                    return;
                }
            } else {
                ArrayList<KineticNetwork> distinctNetworks = networks.stream().filter(distinctByKey(KineticNetwork::getNetworkId)).collect(Collectors.toCollection(ArrayList::new));

                if (distinctNetworks.size() == 1) {
                    resultNetwork = distinctNetworks.get(0);

                    if (!resultNetwork.canAttach(blockEntity)) {
                        LOGGER.warn("Can't attach block {} to network at {} - {} <> {}", blockEntity, blockEntity.getBlockPos(), blockEntity.getRotationDirection(), resultNetwork.getRotationDirection());
                        blockEntity.getLevel().destroyBlock(blockEntity.getBlockPos(), true);
                        return;
                    }
                } else {
                    KineticNetwork network = distinctNetworks.remove(0);

                    if (!network.canAttach(blockEntity) || !distinctNetworks.stream().allMatch((n) -> n.canAttach(blockEntity) && n.canAttach(network))) {
                        LOGGER.warn("Can't attach block {} to network at {}", blockEntity, blockEntity.getBlockPos());
                        blockEntity.getLevel().destroyBlock(blockEntity.getBlockPos(), true);
                        return;
                    }

                    do {
                        KineticNetwork toRemove = distinctNetworks.remove(0);
                        network.addAll(toRemove, blockEntity.getLevel());
                        networkMap.remove(toRemove.getNetworkId());
                        channel.send(PacketDistributor.ALL.noArg(), new NetworkRemovedMessage(toRemove.getNetworkId()));
                    } while (distinctNetworks.size() > 0);

                    resultNetwork = network;
                }
            }
        }

        blockEntity.getKineticNetworkType().addEntity.accept(resultNetwork, blockEntity);
        setDirty();
        channel.send(PacketDistributor.ALL.noArg(), new NetworkAddedOrUpdatedMessage(resultNetwork));
    }

    public void update(IKineticBlockEntity blockEntity) {
        KineticNetwork network = getNetwork(blockEntity);

        if (network != null) {
            if (network.canAttach(blockEntity)) {
                if (network.update(blockEntity)) {
                    setDirty();
                    channel.send(PacketDistributor.ALL.noArg(), new NetworkAddedOrUpdatedMessage(network));
                }
            } else {
                List<KineticNetwork> networks = network.remove(this::getUniqueIds, this::onRemove, blockEntity, channel);
                networkMap.putAll(networks.stream().collect(Collectors.toMap(KineticNetwork::getNetworkId, n -> n)));
                blockEntity.getLevel().destroyBlock(blockEntity.getBlockPos(), true);
                LOGGER.warn("Removed block {} from network at {} because started rotating to wrong direction", blockEntity, blockEntity.getBlockPos());
                setDirty();

                if (network.isNotEmpty()) {
                    channel.send(PacketDistributor.ALL.noArg(), new NetworkAddedOrUpdatedMessage(network));
                }
            }
        } else {
            LOGGER.warn("UPDATE: Can't get network for block {} at {}", blockEntity, blockEntity.getBlockPos());
        }
    }

    void remove(IKineticBlockEntity blockEntity) {
        KineticNetwork network = getNetwork(blockEntity);

        if (network != null) {
            List<KineticNetwork> networks = network.remove(this::getUniqueIds, this::onRemove, blockEntity, channel);
            networkMap.putAll(networks.stream().collect(Collectors.toMap(KineticNetwork::getNetworkId, n -> n)));
            setDirty();

            if (network.isNotEmpty()) {
                channel.send(PacketDistributor.ALL.noArg(), new NetworkAddedOrUpdatedMessage(network));
            }
        } else {
            LOGGER.warn("REMOVE: Can't get network for block {} at {}", blockEntity, blockEntity.getBlockPos());
        }
    }

    Map<Integer, KineticNetwork> getNetworks() {
        return networkMap;
    }

    private void onRemove(int networkId) {
        networkMap.remove(networkId);
        channel.send(PacketDistributor.ALL.noArg(), new NetworkRemovedMessage(networkId));
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

    private void load(@NotNull CompoundTag tag) {
        if (tag.contains(TAG_NETWORKS)) {
            ListTag list = tag.getList(TAG_NETWORKS, CompoundTag.TAG_COMPOUND);

            list.forEach((listItem) -> {
                CompoundTag itemHolder = (CompoundTag) listItem;
                int networkId = itemHolder.getInt(TAG_NETWORK_ID);
                networkMap.put(networkId, new KineticNetwork(itemHolder.getCompound(TAG_NETWORK), networkId, this::onRemove));
            });

            LOGGER.info("Loaded {} rotary networks", networkMap.size());
        } else {
            LOGGER.info("No rotary network loaded");
        }
    }

    @Nullable
    public KineticNetwork getNetwork(IKineticBlockEntity blockEntity) {
        return networkMap.get(blockEntity.getNetworkId());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        return t -> ConcurrentHashMap.newKeySet().add(keyExtractor.apply(t));
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
