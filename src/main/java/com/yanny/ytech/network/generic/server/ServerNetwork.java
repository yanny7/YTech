package com.yanny.ytech.network.generic.server;

import com.yanny.ytech.network.generic.common.CommonNetwork;
import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ServerNetwork<N extends ServerNetwork<N, O>, O extends INetworkBlockEntity> extends CommonNetwork {
    @NotNull protected final Consumer<Integer> onChange;
    @NotNull protected final Consumer<Integer> onRemove;

    public ServerNetwork(int networkId, @NotNull Consumer<Integer> onChange, @NotNull Consumer<Integer> onRemove) {
        super(networkId);
        this.onChange = onChange;
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
    public abstract List<N> remove(@NotNull Function<Integer, List<Integer>> idsGetter, @NotNull Consumer<Integer> onRemove, @NotNull O blockEntity);

    public abstract boolean isNotEmpty();

    protected abstract boolean isValidPosition(@NotNull O blockEntity, @NotNull BlockPos pos);

    public void add(@NotNull O entity) {
        entity.setNetworkId(getNetworkId());
    }

    public void remove(@NotNull O entity) {
        entity.setNetworkId(-1);

        if (!isNotEmpty()) {
            onRemove.accept(getNetworkId());
        }
    }

    public boolean canConnect(@NotNull O blockEntity) {
        return blockEntity.getValidNeighbors().stream().anyMatch(pos -> isValidPosition(blockEntity, pos));
    }
}
