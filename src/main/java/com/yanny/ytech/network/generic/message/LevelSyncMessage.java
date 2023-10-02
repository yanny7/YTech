package com.yanny.ytech.network.generic.message;

import com.yanny.ytech.network.generic.common.AbstractNetwork;
import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public record LevelSyncMessage<T extends AbstractNetwork<T, O>, O extends INetworkBlockEntity>(
        @NotNull Map<Integer, T> networkMap
) {
    public static <T extends AbstractNetwork<T, O>, O extends INetworkBlockEntity> void encode(@NotNull LevelSyncMessage<T, O> msg,
                                                                                               @NotNull FriendlyByteBuf buffer,
                                                                                               @NotNull BiConsumer<FriendlyByteBuf, T> networkEncode) {
        buffer.writeMap(msg.networkMap, FriendlyByteBuf::writeInt, networkEncode::accept);
    }

    public static <T extends AbstractNetwork<T, O>, O extends INetworkBlockEntity> LevelSyncMessage<T, O> decode(@NotNull FriendlyByteBuf buffer,
                                                                                                                 @NotNull Function<FriendlyByteBuf, T> networkDecode) {
        return new LevelSyncMessage<>(buffer.readMap(FriendlyByteBuf::readInt, networkDecode::apply));
    }
}
