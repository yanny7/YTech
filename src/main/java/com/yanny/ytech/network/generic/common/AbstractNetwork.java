package com.yanny.ytech.network.generic.common;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractNetwork<N extends AbstractNetwork<N, O>, O extends INetworkBlockEntity> {
    @NotNull private final Consumer<Integer> onRemove;
    private final int networkId;

    public AbstractNetwork(int networkId, @NotNull Consumer<Integer> onRemove) {
        this.networkId = networkId;
        this.onRemove = onRemove;
    }

    public abstract boolean canAttach(@NotNull O entity);

    public abstract boolean canAttach(@NotNull N network);

    public abstract void load(@NotNull CompoundTag tag);

    @NotNull
    public abstract CompoundTag save();

    public abstract void addAll(@NotNull N network, @NotNull Level level);

    public abstract boolean update(@NotNull O entity);

    @NotNull
    public abstract  List<N> remove(@NotNull Function<Integer, List<Integer>> idsGetter, @NotNull Consumer<Integer> onRemove,
                                    @NotNull O blockEntity, @NotNull SimpleChannel channel);

    public abstract boolean isNotEmpty();

    protected abstract boolean isValidPosition(@NotNull O blockEntity, @NotNull BlockPos pos);

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

    public int getNetworkId() {
        return networkId;
    }
}
