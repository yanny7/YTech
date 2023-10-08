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

    protected abstract boolean canAttach(@NotNull O blockEntity);

    protected abstract boolean canAttach(@NotNull N network);

    protected abstract void load(@NotNull CompoundTag tag);

    @NotNull
    protected abstract CompoundTag save();

    protected abstract void appendNetwork(@NotNull N network, @NotNull Level level);

    protected abstract boolean updateBlockEntity(@NotNull O blockEntity);

    @NotNull
    protected abstract List<N> removeBlockEntity(@NotNull Function<Integer, List<Integer>> idsGetter, @NotNull Consumer<Integer> onRemove, @NotNull O blockEntity);

    protected abstract boolean isNotEmpty();

    protected abstract boolean isValidPosition(@NotNull O blockEntity, @NotNull BlockPos pos);

    protected void addBlockEntity(@NotNull O blockEntity) {
        blockEntity.setNetworkId(getNetworkId());
    }

    protected void removeBlockEntity(@NotNull O blockEntity) {
        blockEntity.setNetworkId(-1);

        if (!isNotEmpty()) {
            onRemove.accept(getNetworkId());
        }
    }

    protected boolean canConnect(@NotNull O blockEntity) {
        return blockEntity.getValidNeighbors().stream().anyMatch(pos -> isValidPosition(blockEntity, pos));
    }
}
