package com.yanny.ytech.network.generic.message;

import com.yanny.ytech.network.generic.common.AbstractNetwork;
import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class LevelSyncMessage<T extends AbstractNetwork<T, O>, O extends INetworkBlockEntity> {
    @NotNull public final Map<Integer, T> networkMap;
    @NotNull private final BiConsumer<FriendlyByteBuf, T> networkEncode;

    public LevelSyncMessage(@NotNull Map<Integer, T> networkMap, @NotNull BiConsumer<FriendlyByteBuf, T> networkEncode) {
        this.networkMap = networkMap;
        this.networkEncode = networkEncode;
    }

    public LevelSyncMessage(@NotNull FriendlyByteBuf buf, @NotNull BiConsumer<FriendlyByteBuf, T> networkEncode, @NotNull Function<FriendlyByteBuf, T> networkDecode) {
        this.networkMap = buf.readMap(FriendlyByteBuf::readInt, networkDecode::apply);
        this.networkEncode = networkEncode;
    }

    public void encode(@NotNull FriendlyByteBuf buf) {
        buf.writeMap(networkMap, FriendlyByteBuf::writeInt, networkEncode::accept);
    }
}
