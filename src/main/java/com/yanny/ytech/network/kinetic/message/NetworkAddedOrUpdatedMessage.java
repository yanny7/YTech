package com.yanny.ytech.network.kinetic.message;

import com.yanny.ytech.network.kinetic.common.KineticNetwork;
import net.minecraft.network.FriendlyByteBuf;

public record NetworkAddedOrUpdatedMessage(
        KineticNetwork network
) {
    public static void encode(NetworkAddedOrUpdatedMessage msg, FriendlyByteBuf buffer) {
        KineticNetwork.encode(buffer, msg.network);
    }

    public static NetworkAddedOrUpdatedMessage decode(FriendlyByteBuf buffer) {
        return new NetworkAddedOrUpdatedMessage(KineticNetwork.decode(buffer));
    }
}
