package com.yanny.ytech.network.generic.client;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.network.generic.common.AbstractNetwork;
import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import com.yanny.ytech.network.generic.message.LevelSyncMessage;
import com.yanny.ytech.network.generic.message.NetworkAddedOrUpdatedMessage;
import com.yanny.ytech.network.generic.message.NetworkRemovedMessage;
import com.yanny.ytech.network.kinetic.KineticUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.function.Supplier;

public class ClientPropagator<N extends AbstractNetwork<N, B>, B extends INetworkBlockEntity> {
    private final Minecraft minecraft = Minecraft.getInstance();

    private static final Logger LOGGER = LogUtils.getLogger();
    private final HashMap<LevelAccessor, ClientLevel<N, B>> levelMap = new HashMap<>();

    public void onLevelLoad(@NotNull net.minecraft.client.multiplayer.ClientLevel level) {
        LOGGER.debug("Preparing rotary propagator for {}", KineticUtils.getLevelId(level));
        levelMap.put(level, new ClientLevel<>());
        LOGGER.debug("Prepared rotary propagator for {}", KineticUtils.getLevelId(level));
    }

    public void onLevelUnload(@NotNull net.minecraft.client.multiplayer.ClientLevel level) {
        LOGGER.debug("Removing rotary propagator for {}", KineticUtils.getLevelId(level));
        levelMap.remove(level);
        LOGGER.debug("Removed rotary propagator for {}", KineticUtils.getLevelId(level));
    }

    public void onSyncLevel(LevelSyncMessage<N, B> msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            levelMap.clear(); // client have only one instance of level
            levelMap.put(minecraft.level, new ClientLevel<>(msg.networkMap()));
        });
        context.setPacketHandled(true);
    }

    public void onNetworkAddedOrUpdated(NetworkAddedOrUpdatedMessage<N, B> msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ClientLevel<N, B> level = levelMap.get(minecraft.level);

            if (level != null) {
                level.onNetworkAddedOrUpdated(msg.network());
                LOGGER.info("Added or updated network {}" , msg.network());
            } else {
                LOGGER.warn("No level stored for {}", minecraft.level);
            }
        });
        context.setPacketHandled(true);
    }

    public void onNetworkRemoved(NetworkRemovedMessage msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ClientLevel<N, B> level = levelMap.get(minecraft.level);

            if (level != null) {
                level.onNetworkRemoved(msg.networkId());
                LOGGER.info("Removed network {}", msg.networkId());
            } else {
                LOGGER.warn("No level stored for {}", minecraft.level);
            }
        });
        context.setPacketHandled(true);
    }

    @Nullable
    public N getNetwork(@NotNull B blockEntity) {
        ClientLevel<N, B> level = levelMap.get(blockEntity.getLevel());

        if (level != null) {
            return level.getNetwork(blockEntity);
        } else {
            LOGGER.warn("No kinetic network for level {}", blockEntity.getLevel());
            return null;
        }
    }
}
