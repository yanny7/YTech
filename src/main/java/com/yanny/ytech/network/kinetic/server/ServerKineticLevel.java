package com.yanny.ytech.network.kinetic.server;

import com.yanny.ytech.network.kinetic.common.IKineticBlockEntity;
import com.yanny.ytech.network.kinetic.common.KineticLevel;
import com.yanny.ytech.network.kinetic.common.KineticNetwork;
import com.yanny.ytech.network.kinetic.message.NetworkAddedOrUpdatedMessage;
import com.yanny.ytech.network.kinetic.message.NetworkRemovedMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ServerKineticLevel extends KineticLevel {
    private final SimpleChannel channel;

    ServerKineticLevel(CompoundTag tag, SimpleChannel channel) {
        load(tag);
        this.channel = channel;
    }

    ServerKineticLevel(SimpleChannel channel) {
        super();
        this.channel = channel;
    }

    void add(IKineticBlockEntity blockEntity) {
        final int networkId = blockEntity.getNetworkId();
        KineticNetwork resultNetwork;

        if (networkId >= 0) {
            resultNetwork = networkMap.get(networkId);
        } else {
            //TODO possible performance improvement: first check network area intersection
            List<KineticNetwork> networks = networkMap.values().stream().filter((n) -> n.canConnect(blockEntity)).toList();

            if (networks.size() == 0) {
                KineticNetwork network = new KineticNetwork(getUniqueId(), this::onRemove);
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
                        channel.send(PacketDistributor.ALL.noArg(), new NetworkRemovedMessage(toRemove.getNetworkId()));
                    } while (distinctNetworks.size() > 0);

                    resultNetwork = network;
                }
            }
        }

        blockEntity.getKineticType().addEntity.accept(resultNetwork, blockEntity);
        setDirty();
        channel.send(PacketDistributor.ALL.noArg(), new NetworkAddedOrUpdatedMessage(resultNetwork));
    }

    void remove(IKineticBlockEntity blockEntity) {
        KineticNetwork network = getNetwork(blockEntity);

        if (network != null) {
            List<KineticNetwork> networks = network.remove(this::getUniqueIds, this::onRemove, blockEntity, channel);
            networkMap.putAll(networks.stream().collect(Collectors.toMap(KineticNetwork::getNetworkId, n -> n)));
            channel.send(PacketDistributor.ALL.noArg(), new NetworkAddedOrUpdatedMessage(network));
            setDirty();
        } else {
            LOGGER.warn("Can't get network for block {} at {}", blockEntity, blockEntity.getBlockPos());
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

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        return t -> ConcurrentHashMap.newKeySet().add(keyExtractor.apply(t));
    }
}
