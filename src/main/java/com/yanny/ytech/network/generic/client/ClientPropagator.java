package com.yanny.ytech.network.generic.client;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.network.generic.NetworkUtils;
import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public abstract class ClientPropagator<N extends ClientNetwork, B extends INetworkBlockEntity> {
    protected static final Logger LOGGER = LogUtils.getLogger();

    @NotNull private final HashMap<LevelAccessor, ClientLevel<N, B>> levelMap = new HashMap<>();
    @NotNull private final String networkName;

    public ClientPropagator(@NotNull String networkName) {
        this.networkName = networkName;
    }

    public void onLevelLoad(@NotNull net.minecraft.client.multiplayer.ClientLevel level) {
        LOGGER.debug("[{}] Preparing propagators for {}", networkName, NetworkUtils.getLevelId(level));
        levelMap.put(level, new ClientLevel<>());
        LOGGER.debug("[{}] Prepared propagators for {}", networkName, NetworkUtils.getLevelId(level));
    }

    public void onLevelUnload(@NotNull net.minecraft.client.multiplayer.ClientLevel level) {
        LOGGER.debug("[{}] Removing propagator for {}", networkName, NetworkUtils.getLevelId(level));
        levelMap.remove(level);
        LOGGER.debug("[{}] Removed propagator for {}", networkName, NetworkUtils.getLevelId(level));
    }

    public void syncLevel(@NotNull Map<Integer, N> networkMap) {
        levelMap.clear(); // client have only one instance of level
        levelMap.put(Minecraft.getInstance().level, new ClientLevel<>(networkMap));
    }

    public void addOrUpdateNetwork(@NotNull N network) {
        ClientLevel<N, B> level = levelMap.get(Minecraft.getInstance().level);

        if (level != null) {
            level.onNetworkAddedOrUpdated(network);
            LOGGER.info("[{}] Added or updated network {}", networkName, network);
        } else {
            LOGGER.warn("[{}] No level stored for {}", networkName, Minecraft.getInstance().level);
        }
    }

    public void deletedNetwork(int networkId) {
        ClientLevel<N, B> level = levelMap.get(Minecraft.getInstance().level);

        if (level != null) {
            level.onNetworkRemoved(networkId);
            LOGGER.info("[{}] Removed network {}", networkName, networkId);
        } else {
            LOGGER.warn("[{}] No level stored for {}", networkName, Minecraft.getInstance().level);
        }
    }

    @Nullable
    public N getNetwork(@NotNull B blockEntity) {
        ClientLevel<N, B> level = levelMap.get(blockEntity.getLevel());

        if (level != null) {
            return level.getNetwork(blockEntity);
        } else {
            LOGGER.warn("[{}] No network for level {}", networkName, blockEntity.getLevel());
            return null;
        }
    }
}
