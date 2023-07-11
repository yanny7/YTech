package com.yanny.ytech.network;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.machine.block_entity.KineticBlockEntity;
import com.yanny.ytech.machine.block_entity.YTechBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class KineticNetwork {
    private static final String TAG_PROVIDERS = "providers";
    private static final String TAG_CONSUMERS = "consumers";
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Set<BlockPos> providers = new HashSet<>();
    private final Set<BlockPos> consumers = new HashSet<>();
    private final Runnable markDirty;
    private final Consumer<Integer> onRemove;

    private final int networkId;

    KineticNetwork(CompoundTag tag, int networkId, Runnable markDirty, Consumer<Integer> onRemove) {
        load(tag);
        this.networkId = networkId;
        this.markDirty = markDirty;
        this.onRemove = onRemove;
    }

    KineticNetwork(int networkId, Runnable markDirty, Consumer<Integer> onRemove) {
        this.networkId = networkId;
        this.markDirty = markDirty;
        this.onRemove = onRemove;
    }

    public int addProvider(YTechBlockEntity entity) {
        providers.add(entity.getBlockPos());
        //TODO calculate
        markDirty.run();
        return networkId;
    }

    public int addConsumer(YTechBlockEntity entity) {
        consumers.add(entity.getBlockPos());
        //TODO calculate
        markDirty.run();
        return networkId;
    }

    public void removeProvider(KineticBlockEntity entity) {
        providers.remove(entity.getBlockPos());
        markDirty.run();

        //TODO calculate

        if (providers.isEmpty() && consumers.isEmpty()) {
            onRemove.accept(networkId);
        }
    }

    public void removeConsumer(KineticBlockEntity entity) {
        consumers.remove(entity.getBlockPos());
        markDirty.run();

        //TODO calculate

        if (providers.isEmpty() && consumers.isEmpty()) {
            onRemove.accept(networkId);
        }
    }

    boolean canConnect(KineticBlockEntity blockEntity) {
        return blockEntity.getValidNeighbors().stream().anyMatch(pos -> isValidPosition(blockEntity, pos));
    }

    void load(CompoundTag tag) {
        if (tag.contains(TAG_PROVIDERS) && tag.getTagType(TAG_PROVIDERS) != 0) {
            tag.getList(TAG_PROVIDERS, ListTag.TAG_COMPOUND).forEach((t) -> providers.add(loadBlockPos((CompoundTag) t)));
        }
        if (tag.contains(TAG_CONSUMERS) && tag.getTagType(TAG_CONSUMERS) != 0) {
            tag.getList(TAG_CONSUMERS, ListTag.TAG_COMPOUND).forEach((t) -> consumers.add(loadBlockPos((CompoundTag) t)));
        }

        LOGGER.debug("Network {}: Loaded {} providers and {} consumers", networkId, providers.size(), consumers.size());
    }

    CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag providersTag = new ListTag();
        ListTag consumersTag = new ListTag();

        providers.forEach((pos) -> providersTag.add(saveBlockPos(pos)));
        consumers.forEach((pos) -> consumersTag.add(saveBlockPos(pos)));
        tag.put(TAG_PROVIDERS, providersTag);
        tag.put(TAG_CONSUMERS, consumersTag);
        return tag;
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

    private boolean isValidPosition(KineticBlockEntity blockEntity, BlockPos pos) {
        if (providers.contains(pos) || consumers.contains(pos)) {
            Level level = blockEntity.getLevel();

            if (level != null && level.isLoaded(pos)) {
                if (level.getBlockEntity(pos) instanceof KineticBlockEntity kineticBlockEntity) {
                    return kineticBlockEntity.getValidNeighbors().contains(blockEntity.getBlockPos());
                }
            }
        }

        return false;
    }
}
