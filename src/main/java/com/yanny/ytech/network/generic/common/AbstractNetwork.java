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

public abstract class AbstractNetwork<N extends AbstractNetwork<N, O>, O extends INetworkBlockEntity> {
    protected static final Logger LOGGER = LogUtils.getLogger();

    protected final Consumer<Integer> onRemove;
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

    public abstract boolean canAttach(@NotNull O entity);

    public abstract boolean canAttach(@NotNull N network);

    public void add(@NotNull O entity) {
        entity.setNetworkId(networkId);
    }

    public void remove(@NotNull O entity) {
        entity.setNetworkId(-1);

        if (!isNotEmpty()) {
            onRemove.accept(networkId);
        }
    }

    public boolean canConnect(@NotNull O blockEntity) {
        return blockEntity.getValidNeighbors().stream().anyMatch(pos -> isValidPosition(blockEntity, pos));
    }

    protected abstract void load(CompoundTag tag);

    public abstract CompoundTag save();

    public int getNetworkId() {
        return networkId;
    }

    public abstract void addAll(N network, Level level);

    public abstract boolean update(O entity);

    public abstract  List<N> remove(Function<Integer, List<Integer>> idsGetter, Consumer<Integer> onRemove, O blockEntity, SimpleChannel channel);

    public abstract boolean isNotEmpty();

    protected abstract boolean isValidPosition(O blockEntity, BlockPos pos);

    public interface Factory<T extends AbstractNetwork<T, O>, O extends INetworkBlockEntity> {
        T create(CompoundTag tag, int networkId, Consumer<Integer> onRemove);
        T create(int networkId, Consumer<Integer> onRemove);
    }
}
