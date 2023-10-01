package com.yanny.ytech.network.generic.common;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractNetwork {
    protected static final Logger LOGGER = LogUtils.getLogger();

    private final Consumer<Integer> onRemove;
    private final int networkId;

    public AbstractNetwork(CompoundTag tag, int networkId, Consumer<Integer> onRemove) {
        load(tag);
        this.networkId = networkId;
        this.onRemove = onRemove;
    }

    public AbstractNetwork(int networkId, Consumer<Integer> onRemove) {
        this.networkId = networkId;
        this.onRemove = onRemove;
    }

    public abstract boolean canAttach(@NotNull INetworkBlockEntity entity);

    public abstract boolean canAttach(@NotNull AbstractNetwork network);

    void addProvider(@NotNull INetworkBlockEntity entity) {
        entity.setNetworkId(networkId);
    }

    void addConsumer(@NotNull INetworkBlockEntity entity) {
        entity.setNetworkId(networkId);
    }

    void removeProvider(@NotNull INetworkBlockEntity entity) {
        entity.setNetworkId(-1);

        if (entity.isEmpty()) {
            onRemove.accept(networkId);
        }
    }

    void removeConsumer(@NotNull INetworkBlockEntity entity) {
        entity.setNetworkId(-1);

        if (entity.isEmpty()) {
            onRemove.accept(networkId);
        }
    }

    public boolean canConnect(@NotNull INetworkBlockEntity blockEntity) {
        return blockEntity.getValidNeighbors().stream().anyMatch(pos -> isValidPosition(blockEntity, pos));
    }

    abstract void load(CompoundTag tag);

    public abstract CompoundTag save();

    public int getNetworkId() {
        return networkId;
    }

    public abstract void addAll(AbstractNetwork network, Level level);

    public abstract boolean update(INetworkBlockEntity entity);

    public abstract <T extends AbstractNetwork> List<T> remove(Function<Integer, List<Integer>> idsGetter, Consumer<Integer> onRemove, INetworkBlockEntity blockEntity, SimpleChannel channel);

    public abstract boolean isNotEmpty();

    abstract boolean isValidPosition(INetworkBlockEntity blockEntity, BlockPos pos);

    public interface Factory<T extends AbstractNetwork> {
        T create(CompoundTag tag, int networkId, Consumer<Integer> onRemove);
        T create(int networkId, Consumer<Integer> onRemove);
    }
}
