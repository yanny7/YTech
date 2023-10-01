package com.yanny.ytech.network.generic.message;

import com.yanny.ytech.network.generic.common.AbstractNetwork;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public record LevelSyncMessage<T extends AbstractNetwork>(
        Map<Integer, T> networkMap
) {
    public static <T extends AbstractNetwork> void encode(LevelSyncMessage<T> msg, FriendlyByteBuf buffer, BiConsumer<FriendlyByteBuf, T> networkEncode) {
        buffer.writeMap(msg.networkMap, FriendlyByteBuf::writeInt, networkEncode::accept);
    }

    public static <T extends AbstractNetwork> LevelSyncMessage<T> decode(FriendlyByteBuf buffer, Function<FriendlyByteBuf, T> networkDecode) {
        return new LevelSyncMessage<T>(buffer.readMap(FriendlyByteBuf::readInt, networkDecode::apply));
    }
}
