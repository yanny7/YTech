package com.yanny.ytech.network.generic.server;

import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.yanny.ytech.network.generic.NetworkUtils;
import com.yanny.ytech.network.generic.common.CommonNetwork;
import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ServerNetwork<N extends ServerNetwork<N, O>, O extends INetworkBlockEntity> extends CommonNetwork {
    private static final String TAG_CHUNK_MAP = "chunkMap";
    private static final String TAG_POS_MAP = "posMap";
    private static final String TAG_BLOCK_POS = "blockPos";
    private static final String TAG_CHUNK_POS = "chunkPos";
    private static final Logger LOGGER = LogUtils.getLogger();

    @NotNull protected final Consumer<Integer> onChange;
    @NotNull protected final BiConsumer<Integer, ChunkPos> onRemove;
    @NotNull private final Map<ChunkPos, Set<BlockPos>> chunkMap = new HashMap<>();

    private boolean dirty = false;

    public ServerNetwork(int networkId, @NotNull Consumer<Integer> onChange, @NotNull BiConsumer<Integer, ChunkPos> onRemove) {
        super(networkId);
        this.onChange = onChange;
        this.onRemove = onRemove;
    }

    protected abstract boolean canAttach(@NotNull O blockEntity);

    protected abstract boolean canAttach(@NotNull N network);

    protected void load(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        if (tag.contains(TAG_CHUNK_MAP) && tag.getTagType(TAG_CHUNK_MAP) != 0) {
            tag.getList(TAG_CHUNK_MAP, ListTag.TAG_COMPOUND).forEach((t) -> {
                Set<BlockPos> blockPosSet = new HashSet<>();
                ((CompoundTag) t).getList(TAG_POS_MAP, ListTag.TAG_COMPOUND).forEach((u) -> blockPosSet.add(NetworkUtils.loadBlockPos(((CompoundTag) u).getCompound(TAG_BLOCK_POS))));

                chunkMap.put(NetworkUtils.loadChunkPos(((CompoundTag) t).getCompound(TAG_CHUNK_POS)), blockPosSet);
            });
        }
    }

    @NotNull
    protected CompoundTag save(@NotNull HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag chunksTag = new ListTag();

        chunkMap.forEach((pos, set) -> {
            CompoundTag t = new CompoundTag();
            ListTag posTag = new ListTag();

            t.put(TAG_CHUNK_POS, NetworkUtils.saveChunkPos(pos));
            set.forEach((blockPos) -> {
                CompoundTag u = new CompoundTag();
                u.put(TAG_BLOCK_POS, NetworkUtils.saveBlockPos(blockPos));
                posTag.add(u);
            });
            t.put(TAG_POS_MAP, posTag);
            chunksTag.add(t);
        });
        tag.put(TAG_CHUNK_MAP, chunksTag);
        return tag;
    }

    protected abstract void appendNetwork(@NotNull N network, @NotNull Level level);

    protected abstract boolean updateBlockEntity(@NotNull O blockEntity);

    @NotNull
    protected abstract List<N> removeBlockEntity(@NotNull Function<Integer, List<Integer>> idsGetter, @NotNull BiConsumer<Integer, ChunkPos> onRemove, @NotNull O blockEntity);

    protected abstract boolean isNotEmpty();

    protected abstract boolean isValidPosition(@NotNull O blockEntity, @NotNull BlockPos pos);

    protected void addBlockEntity(@NotNull O blockEntity) {
        ChunkPos chunkPos = new ChunkPos(blockEntity.getBlockPos());

        blockEntity.setNetworkId(getNetworkId());

        if (chunkMap.containsKey(chunkPos)) {
            chunkMap.get(chunkPos).add(blockEntity.getBlockPos());
        } else {
            chunkMap.put(chunkPos, Sets.newHashSet(blockEntity.getBlockPos()));
        }
    }

    protected void removeBlockEntity(@NotNull O blockEntity) {
        ChunkPos chunkPos = new ChunkPos(blockEntity.getBlockPos());
        Set<BlockPos> blockPosSet = chunkMap.get(chunkPos);

        if (blockPosSet != null) {
            if (blockPosSet.isEmpty() && chunkMap.size() > 1) {
                chunkMap.remove(chunkPos);
            }
        } else {
            LOGGER.warn("{} NULL BlockPos", getNetworkId());
        }

        blockEntity.setNetworkId(-1);


        if (!isNotEmpty()) {
            onRemove.accept(getNetworkId(), chunkPos);
        }
    }

    protected boolean canConnect(@NotNull O blockEntity) {
        return blockEntity.getValidNeighbors().stream().anyMatch(pos -> isValidPosition(blockEntity, pos));
    }

    protected void setClean() {
        dirty = false;
    }

    protected void setDirty() {
        dirty = true;
    }

    protected boolean isDirty() {
        return dirty;
    }

    protected Set<ChunkPos> getChunks() {
        return chunkMap.keySet();
    }
}
