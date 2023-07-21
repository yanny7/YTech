package com.yanny.ytech.network.kinetic.common;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.network.kinetic.message.NetworkAddedOrUpdatedMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class KineticNetwork {
    private static final String TAG_PROVIDERS = "providers";
    private static final String TAG_CONSUMERS = "consumers";
    private static final String TAG_DIR_PROVIDERS = "dirProviders";
    private static final String TAG_STRESS_CAPACITY = "stressCapacity";
    private static final String TAG_STRESS = "stress";
    private static final String TAG_BLOCK_POS = "pos";
    private static final String TAG_DIRECTION = "direction";
    private static final Logger LOGGER = LogUtils.getLogger();

    private final HashMap<BlockPos, Integer> providers = new HashMap<>();
    private final HashMap<BlockPos, Integer> consumers = new HashMap<>();
    private final Set<BlockPos> directionProviders = new HashSet<>();
    private final Consumer<Integer> onRemove;

    private final int networkId;
    private int stressCapacity;
    private int stress;
    private RotationDirection rotationDirection = RotationDirection.NONE;

    public KineticNetwork(CompoundTag tag, int networkId, Consumer<Integer> onRemove) {
        load(tag);
        this.networkId = networkId;
        this.onRemove = onRemove;
    }

    public KineticNetwork(int networkId, Consumer<Integer> onRemove) {
        this.networkId = networkId;
        this.onRemove = onRemove;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Id:{0,number}, C:{1,number}, P:{2,number}, K: {3, number} {4,number}/{5,number}, {6}",
                networkId, consumers.size(), providers.size(), directionProviders.size(), stress, stressCapacity, rotationDirection);
    }

    public int getStress() {
        return stress;
    }

    public int getStressCapacity() {
        return stressCapacity;
    }

    public RotationDirection getRotationDirection() {
        return rotationDirection;
    }

    public boolean canAttach(@NotNull IKineticBlockEntity entity) {
        RotationDirection entityRotationDirection = entity.getRotationDirection();

        return entityRotationDirection == RotationDirection.NONE || rotationDirection == RotationDirection.NONE || entityRotationDirection == rotationDirection;
    }

    public boolean canAttach(@NotNull KineticNetwork network) {
        return rotationDirection == RotationDirection.NONE || network.rotationDirection == RotationDirection.NONE || rotationDirection == network.rotationDirection;
    }

    void addProvider(@NotNull IKineticBlockEntity entity) {
        RotationDirection entityRotationDirection = entity.getRotationDirection();
        BlockPos blockPos = entity.getBlockPos();
        int stressValue = entity.getStress();

        providers.put(blockPos, stressValue);
        entity.setNetworkId(networkId);
        stressCapacity += stressValue;

        if (entityRotationDirection != RotationDirection.NONE) {
            if (rotationDirection != RotationDirection.NONE && rotationDirection != entityRotationDirection) {
                throw new IllegalStateException("Invalid rotation direction provided!");
            }

            directionProviders.add(blockPos);
            rotationDirection = entityRotationDirection;
        }
    }

    void addConsumer(@NotNull IKineticBlockEntity entity) {
        RotationDirection entityRotationDirection = entity.getRotationDirection();
        BlockPos blockPos = entity.getBlockPos();
        int stressValue = entity.getStress();

        consumers.put(blockPos, stressValue);
        entity.setNetworkId(networkId);
        stress += stressValue;

        if (entityRotationDirection != RotationDirection.NONE) {
            if (rotationDirection != RotationDirection.NONE && rotationDirection != entityRotationDirection) {
                throw new IllegalStateException("Invalid rotation direction provided!");
            }

            directionProviders.add(blockPos);
            rotationDirection = entityRotationDirection;
        }
    }

    void removeProvider(@NotNull IKineticBlockEntity entity) {
        BlockPos blockPos = entity.getBlockPos();

        providers.remove(blockPos);
        entity.setNetworkId(-1);
        stressCapacity -= entity.getStress();

        if (directionProviders.remove(blockPos) && directionProviders.isEmpty()) {
            rotationDirection = RotationDirection.NONE;
        }

        if (providers.isEmpty() && consumers.isEmpty()) {
            onRemove.accept(networkId);
        }
    }

    void removeConsumer(@NotNull IKineticBlockEntity entity) {
        BlockPos blockPos = entity.getBlockPos();

        consumers.remove(blockPos);
        entity.setNetworkId(-1);
        stress -= entity.getStress();

        if (directionProviders.remove(blockPos) && directionProviders.isEmpty()) {
            rotationDirection = RotationDirection.NONE;
        }

        if (providers.isEmpty() && consumers.isEmpty()) {
            onRemove.accept(networkId);
        }
    }

    public boolean canConnect(@NotNull IKineticBlockEntity blockEntity) {
        return blockEntity.getValidNeighbors().stream().anyMatch(pos -> isValidPosition(blockEntity, pos));
    }

    void load(CompoundTag tag) {
        if (tag.contains(TAG_PROVIDERS) && tag.getTagType(TAG_PROVIDERS) != 0) {
            tag.getList(TAG_PROVIDERS, ListTag.TAG_COMPOUND).forEach((t) -> providers.put(loadBlockPos(((CompoundTag) t).getCompound(TAG_BLOCK_POS)), ((CompoundTag) t).getInt(TAG_STRESS)));
        }
        if (tag.contains(TAG_CONSUMERS) && tag.getTagType(TAG_CONSUMERS) != 0) {
            tag.getList(TAG_CONSUMERS, ListTag.TAG_COMPOUND).forEach((t) -> consumers.put(loadBlockPos(((CompoundTag) t).getCompound(TAG_BLOCK_POS)), ((CompoundTag) t).getInt(TAG_STRESS)));
        }
        if (tag.contains(TAG_DIR_PROVIDERS) && tag.getTagType(TAG_DIR_PROVIDERS) != 0) {
            tag.getList(TAG_DIR_PROVIDERS, ListTag.TAG_COMPOUND).forEach((t) -> directionProviders.add(loadBlockPos((CompoundTag) t)));
        }
        if (tag.contains(TAG_STRESS_CAPACITY) && tag.getTagType(TAG_STRESS_CAPACITY) != 0) {
            stressCapacity = tag.getInt(TAG_STRESS_CAPACITY);
        }
        if (tag.contains(TAG_STRESS) && tag.getTagType(TAG_STRESS) != 0) {
            stress = tag.getInt(TAG_STRESS);
        }
        if (tag.contains(TAG_DIRECTION) && tag.getTagType(TAG_DIRECTION) != 0) {
            try {
                rotationDirection = RotationDirection.valueOf(tag.getString(TAG_DIRECTION));
            } catch (Exception ignored) {}
        }

        LOGGER.debug("Network {}: Loaded {} providers, {} consumers, {} dirProviders, {}", networkId, providers.size(), consumers.size(), directionProviders.size(), rotationDirection);
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag providersTag = new ListTag();
        ListTag consumersTag = new ListTag();
        ListTag dirProvidersTag = new ListTag();

        providers.forEach((pos, stress) -> {
            CompoundTag t = new CompoundTag();
            t.put(TAG_BLOCK_POS, saveBlockPos(pos));
            t.putInt(TAG_STRESS, stress);
            providersTag.add(t);
        });
        consumers.forEach((pos, stress) -> {
            CompoundTag t = new CompoundTag();
            t.put(TAG_BLOCK_POS, saveBlockPos(pos));
            t.putInt(TAG_STRESS, stress);
            consumersTag.add(t);
        });
        directionProviders.forEach((pos) -> dirProvidersTag.add(saveBlockPos(pos)));
        tag.put(TAG_PROVIDERS, providersTag);
        tag.put(TAG_CONSUMERS, consumersTag);
        tag.put(TAG_DIR_PROVIDERS, dirProvidersTag);
        tag.putInt(TAG_STRESS_CAPACITY, stressCapacity);
        tag.putInt(TAG_STRESS, stress);
        tag.putString(TAG_DIRECTION, rotationDirection.name());
        return tag;
    }

    public int getNetworkId() {
        return networkId;
    }

    public void addAll(KineticNetwork network, Level level) {
        if (network.rotationDirection != RotationDirection.NONE && rotationDirection != RotationDirection.NONE && rotationDirection != network.rotationDirection) {
            throw new IllegalStateException("Invalid rotation direction provided!");
        }

        network.providers.forEach((pos, value) -> {
            providers.put(pos, value);
            stressCapacity += value;

            if (level.getBlockEntity(pos) instanceof IKineticBlockEntity kineticBlockEntity) {
                kineticBlockEntity.setNetworkId(networkId);
            }
        });
        network.consumers.forEach((pos, value) -> {
            consumers.put(pos, value);
            stress += value;

            if (level.getBlockEntity(pos) instanceof IKineticBlockEntity kineticBlockEntity) {
                kineticBlockEntity.setNetworkId(networkId);
            }
        });

        directionProviders.addAll(network.directionProviders);
        rotationDirection = network.rotationDirection;
    }

    public boolean changed(IKineticBlockEntity entity) {
        RotationDirection entityRotationDirection = entity.getRotationDirection();
        BlockPos blockPos = entity.getBlockPos();
        boolean wasChange = false;
        int value = entity.getStress();

        switch (entity.getKineticNetworkType()) {
            case PROVIDER -> {
                int oldCapacity = stressCapacity;

                stressCapacity = stressCapacity - providers.get(blockPos) + value;
                providers.put(blockPos, value);

                if (oldCapacity != stressCapacity) {
                    wasChange = true;
                }
            }
            case CONSUMER -> {
                int oldStress = stress;

                stress = stress - providers.get(blockPos) + value;
                consumers.put(blockPos, value);

                if (oldStress != stress) {
                    wasChange = true;
                }
            }
        }

        if (directionProviders.remove(entity.getBlockPos())) {
            if (directionProviders.isEmpty()) {
                rotationDirection = RotationDirection.NONE;
            }

            wasChange = true;
        }

        if (entityRotationDirection != RotationDirection.NONE) {
            if (rotationDirection != RotationDirection.NONE && rotationDirection != entityRotationDirection) {
                throw new IllegalStateException("Invalid rotation direction provided!");
            }

            directionProviders.add(blockPos);
            rotationDirection = entityRotationDirection;
            wasChange = true;
        }

        return wasChange;
    }

    public List<KineticNetwork> remove(Function<Integer, List<Integer>> idsGetter, Consumer<Integer> onRemove, IKineticBlockEntity blockEntity, SimpleChannel channel) {
        Map<BlockPos, Integer> providerBlocks = new HashMap<>(providers);
        Map<BlockPos, Integer> consumerBlocks = new HashMap<>(consumers);
        BlockPos blockPos = blockEntity.getBlockPos();

        // remove splitting block
        providerBlocks.remove(blockPos);
        consumerBlocks.remove(blockPos);

        if ((blockEntity.getValidNeighbors().stream().filter(pos -> providerBlocks.containsKey(pos) || consumerBlocks.containsKey(pos)).toList().size() == 1) || (providerBlocks.isEmpty() && consumerBlocks.isEmpty())) { // if we are not splitting
            blockEntity.getKineticNetworkType().removeEntity.accept(this, blockEntity);
            return List.of();
        }

        List<Integer> ids = idsGetter.apply(blockEntity.getValidNeighbors().size() - 1);
        List<BlockPos> neighbors = blockEntity.getValidNeighbors();
        BlockPos neighbor = neighbors.remove(0); // remove first network (will be our network)

        clear();
        insertConnectedPositions(this, providerBlocks, consumerBlocks, neighbor, blockEntity.getLevel()); // re-insert blocks

        return neighbors.stream().map((pos) -> {
            if ((providerBlocks.isEmpty() && consumerBlocks.isEmpty()) || (!providerBlocks.containsKey(pos) && !consumerBlocks.containsKey(pos))) {
                return null;
            }

            KineticNetwork network = new KineticNetwork(ids.remove(0), onRemove);
            insertConnectedPositions(network, providerBlocks, consumerBlocks, pos, blockEntity.getLevel());
            channel.send(PacketDistributor.ALL.noArg(), new NetworkAddedOrUpdatedMessage(network));
            return network;
        }).filter(Objects::nonNull).toList();
    }

    public boolean isEmpty() {
        return providers.isEmpty() && consumers.isEmpty();
    }

    public static void encode(FriendlyByteBuf buffer, KineticNetwork level) {
        buffer.writeInt(level.networkId);
        buffer.writeInt(level.stressCapacity);
        buffer.writeInt(level.stress);
        buffer.writeEnum(level.rotationDirection);
    }

    @NotNull
    public static KineticNetwork decode(FriendlyByteBuf buffer) {
        int networkId = buffer.readInt();
        KineticNetwork network = new KineticNetwork(networkId, (i) -> {});

        network.stressCapacity = buffer.readInt();
        network.stress = buffer.readInt();
        network.rotationDirection = buffer.readEnum(RotationDirection.class);
        return network;
    }

    private void insertConnectedPositions(KineticNetwork network, Map<BlockPos, Integer> providerBlocks, Map<BlockPos, Integer> consumerBlocks, BlockPos from, Level level) {
        BlockEntity blockEntity = level.getBlockEntity(from);

        if (blockEntity instanceof IKineticBlockEntity block) {
            block.getKineticNetworkType().addEntity.accept(network, block); // insert from to new network
            providerBlocks.remove(from);
            consumerBlocks.remove(from);
            block.getValidNeighbors().forEach((pos) -> {
                if (providerBlocks.containsKey(pos) || consumerBlocks.containsKey(pos)) {
                    insertConnectedPositions(network, providerBlocks, consumerBlocks, pos, level);
                }
            });
        }
    }

    private BlockPos loadBlockPos(CompoundTag tag) {
        return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
    }

    private CompoundTag saveBlockPos(BlockPos pos) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
        return tag;
    }

    private boolean isValidPosition(IKineticBlockEntity blockEntity, BlockPos pos) {
        if (providers.containsKey(pos) || consumers.containsKey(pos)) {
            Level level = blockEntity.getLevel();

            if (level != null && level.isLoaded(pos)) {
                if (level.getBlockEntity(pos) instanceof IKineticBlockEntity IKineticBlockEntity) {
                    return IKineticBlockEntity.getValidNeighbors().contains(blockEntity.getBlockPos());
                }
            }
        }

        return false;
    }

    private void clear() {
        consumers.clear();
        providers.clear();
        directionProviders.clear();
        stressCapacity = 0;
        stress = 0;
        rotationDirection = RotationDirection.NONE;
    }
}
