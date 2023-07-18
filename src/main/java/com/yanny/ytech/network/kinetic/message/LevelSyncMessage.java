package com.yanny.ytech.network.kinetic.message;

import com.yanny.ytech.network.kinetic.common.KineticNetwork;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Map;

public record LevelSyncMessage(
        Map<Integer, KineticNetwork> networkMap
) {
    public static void encode(LevelSyncMessage msg, FriendlyByteBuf buffer) {
        buffer.writeMap(msg.networkMap, FriendlyByteBuf::writeInt, KineticNetwork::encode);
    }

    public static LevelSyncMessage decode(FriendlyByteBuf buffer) {
        return new LevelSyncMessage(buffer.readMap(FriendlyByteBuf::readInt, KineticNetwork::decode));
    }
}
