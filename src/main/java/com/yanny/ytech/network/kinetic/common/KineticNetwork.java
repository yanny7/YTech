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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KineticNetwork {
    private static final String TAG_PROVIDERS = "providers";
    private static final String TAG_CONSUMERS = "consumers";
    private static final String TAG_DIR_PROVIDERS = "dirProviders";
    private static final String TAG_STRESS_CAPACITY = "stressCapacity";
    private static final String TAG_STRESS = "stress";
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Set<BlockPos> providers = new HashSet<>();
    private final Set<BlockPos> consumers = new HashSet<>();
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
        return MessageFormat.format("Id:{0,number}, C:{1,number}, P:{2,number}, {3,number}/{4,number}, {5}",
                networkId, consumers.size(), providers.size(), stress, stressCapacity, rotationDirection);
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
        providers.add(entity.getBlockPos());
        entity.setNetworkId(networkId);
        stressCapacity += entity.getStress();

        if (entityRotationDirection != RotationDirection.NONE) {
            directionProviders.add(entity.getBlockPos());
            rotationDirection = entityRotationDirection;
        }
    }

    void addConsumer(@NotNull IKineticBlockEntity entity) {
        RotationDirection entityRotationDirection = entity.getRotationDirection();
        consumers.add(entity.getBlockPos());
        entity.setNetworkId(networkId);
        stress += entity.getStress();

        if (entityRotationDirection != RotationDirection.NONE) {
            directionProviders.add(entity.getBlockPos());
            rotationDirection = entityRotationDirection;
        }
    }

    void removeProvider(@NotNull IKineticBlockEntity entity) {
        providers.remove(entity.getBlockPos());
        entity.setNetworkId(-1);
        stressCapacity -= entity.getStress();

        if (directionProviders.remove(entity.getBlockPos()) && directionProviders.size() == 0) {
            rotationDirection = RotationDirection.NONE;
        }

        if (providers.isEmpty() && consumers.isEmpty()) {
            onRemove.accept(networkId);
        }
    }

    void removeConsumer(@NotNull IKineticBlockEntity entity) {
        consumers.remove(entity.getBlockPos());
        entity.setNetworkId(-1);
        stress -= entity.getStress();

        if (directionProviders.remove(entity.getBlockPos()) && directionProviders.size() == 0) {
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
            tag.getList(TAG_PROVIDERS, ListTag.TAG_COMPOUND).forEach((t) -> providers.add(loadBlockPos((CompoundTag) t)));
        }
        if (tag.contains(TAG_CONSUMERS) && tag.getTagType(TAG_CONSUMERS) != 0) {
            tag.getList(TAG_CONSUMERS, ListTag.TAG_COMPOUND).forEach((t) -> consumers.add(loadBlockPos((CompoundTag) t)));
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

        LOGGER.debug("Network {}: Loaded {} providers, {} consumers, {} dirProviders", networkId, providers.size(), consumers.size(), directionProviders.size());
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag providersTag = new ListTag();
        ListTag consumersTag = new ListTag();
        ListTag dirProvidersTag = new ListTag();

        providers.forEach((pos) -> providersTag.add(saveBlockPos(pos)));
        consumers.forEach((pos) -> consumersTag.add(saveBlockPos(pos)));
        directionProviders.forEach((pos) -> dirProvidersTag.add(saveBlockPos(pos)));
        tag.put(TAG_PROVIDERS, providersTag);
        tag.put(TAG_CONSUMERS, consumersTag);
        tag.put(TAG_DIR_PROVIDERS, dirProvidersTag);
        tag.putInt(TAG_STRESS_CAPACITY, stressCapacity);
        tag.putInt(TAG_STRESS, stress);
        return tag;
    }

    public int getNetworkId() {
        return networkId;
    }

    public void addAll(KineticNetwork network, Level level) {
        Stream.concat(network.providers.stream(), network.consumers.stream()).forEach((pos) -> addBlockEntity(pos, level));
    }

    public boolean changed(IKineticBlockEntity entity) {
        boolean wasChange = false;
        RotationDirection entityRotationDirection = entity.getRotationDirection();
        //TODO recalculate stress/stressCapacity
        wasChange = true; //FIXME forced
        directionProviders.remove(entity.getBlockPos());

        if (directionProviders.size() == 0) {
            rotationDirection = RotationDirection.NONE;
        }

        if (entityRotationDirection != RotationDirection.NONE) {
            directionProviders.add(entity.getBlockPos());
            rotationDirection = entityRotationDirection;
        }

        return wasChange;
    }

    public List<KineticNetwork> remove(Function<Integer, List<Integer>> idsGetter, Consumer<Integer> onRemove, IKineticBlockEntity blockEntity, SimpleChannel channel) {
        Set<BlockPos> blocks = Stream.concat(providers.stream(), consumers.stream()).collect(Collectors.toSet());

        blocks.remove(blockEntity.getBlockPos()); // remove splitting block

        if ((blockEntity.getValidNeighbors().stream().filter(blocks::contains).toList().size() == 1) || (blocks.size() == 0)) { // if we are not splitting
            blockEntity.getKineticNetworkType().removeEntity.accept(this, blockEntity);

            if (blocks.size() != 0) {
                channel.send(PacketDistributor.ALL.noArg(), new NetworkAddedOrUpdatedMessage(this));
            }

            return List.of();
        }

        List<Integer> ids = idsGetter.apply(blockEntity.getValidNeighbors().size() - 1);
        List<BlockPos> neighbors = blockEntity.getValidNeighbors();
        BlockPos neighbor = neighbors.remove(0); // remove first network (will be our network)

        clear();
        insertConnectedPositions(this, blocks, neighbor, blockEntity.getLevel()); // re-insert blocks

        return neighbors.stream().map((pos) -> {
            if (blocks.size() == 0 || !blocks.contains(pos)) {
                return null;
            }

            KineticNetwork network = new KineticNetwork(ids.remove(0), onRemove);
            insertConnectedPositions(network, blocks, pos, blockEntity.getLevel());
            channel.send(PacketDistributor.ALL.noArg(), new NetworkAddedOrUpdatedMessage(network));
            return network;
        }).filter(Objects::nonNull).toList();
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

    private void insertConnectedPositions(KineticNetwork network, Set<BlockPos> blocks, BlockPos from, Level level) {
        BlockEntity blockEntity = level.getBlockEntity(from);

        if (blockEntity instanceof IKineticBlockEntity block) {
            block.getKineticNetworkType().addEntity.accept(network, block); // insert from to new network
            blocks.remove(from);
            block.getValidNeighbors().forEach((pos) -> {
                if (blocks.contains(pos)) {
                    insertConnectedPositions(network, blocks, pos, level);
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
        if (providers.contains(pos) || consumers.contains(pos)) {
            Level level = blockEntity.getLevel();

            if (level != null && level.isLoaded(pos)) {
                if (level.getBlockEntity(pos) instanceof IKineticBlockEntity IKineticBlockEntity) {
                    return IKineticBlockEntity.getValidNeighbors().contains(blockEntity.getBlockPos());
                }
            }
        }

        return false;
    }

    private void addBlockEntity(BlockPos pos, Level level) {
        if (/*level.isLoaded(pos) && */level.getBlockEntity(pos) instanceof IKineticBlockEntity blockEntity) {
            blockEntity.getKineticNetworkType().addEntity.accept(this, blockEntity);
        } else {
            LOGGER.warn("Can't add BlockEntity for pos {} ({})", pos, level.getBlockEntity(pos));
        }
    }

    private void clear() {
        consumers.clear();
        providers.clear();
        directionProviders.clear();
        stressCapacity = 0;
        stress = 0;
    }
}
