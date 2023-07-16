package com.yanny.ytech.network.kinetic.message;

import com.yanny.ytech.network.kinetic.common.KineticLevel;
import com.yanny.ytech.network.kinetic.common.KineticNetwork;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Map;

public record LevelSyncMessage(
        Map<Integer, KineticNetwork> networkMap
) {
    public static void encode(LevelSyncMessage msg, FriendlyByteBuf buffer) {
        KineticLevel.encode(msg.networkMap, buffer);
    }

    public static LevelSyncMessage decode(FriendlyByteBuf buffer) {
        return new LevelSyncMessage(KineticLevel.decode(buffer));
    }
}
