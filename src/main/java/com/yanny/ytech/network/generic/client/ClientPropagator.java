package com.yanny.ytech.network.generic.client;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.network.generic.NetworkUtils;
import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public abstract class ClientPropagator<N extends ClientNetwork, B extends INetworkBlockEntity> {
    protected static final Logger LOGGER = LogUtils.getLogger();

    @NotNull private final HashMap<ResourceLocation, ClientLevelData<N, B>> levelMap = new HashMap<>();
    @NotNull private final String networkName;

    public ClientPropagator(@NotNull String networkName) {
        this.networkName = networkName;
    }

    public void onLevelLoad(@NotNull ClientLevel level) {
        ResourceLocation id = NetworkUtils.getLevelId(level);

        LOGGER.debug("[{}] Preparing propagators for {}", networkName, id);
        levelMap.put(id, new ClientLevelData<>());
        LOGGER.debug("[{}] Prepared propagators for {}", networkName, id);
    }

    public void onLevelUnload(@NotNull ClientLevel level) {
        ResourceLocation id = NetworkUtils.getLevelId(level);

        LOGGER.debug("[{}] Removing propagator for {}", networkName, id);
        levelMap.remove(id);
        LOGGER.debug("[{}] Removed propagator for {}", networkName, id);
    }

    public void syncLevel(@NotNull Map<Integer, N> networkMap) {
        if (Minecraft.getInstance().level != null) {
            levelMap.clear(); // client have only one instance of level
            levelMap.put(NetworkUtils.getLevelId(Minecraft.getInstance().level), new ClientLevelData<>(networkMap));
            LOGGER.debug("[{}] Synced ClientLevel ({} networks)", networkName, networkMap.size());
        } else {
            LOGGER.warn("[{}] Invalid ClientLevel reference!", networkName);
        }
    }

    public void addOrUpdateNetwork(@NotNull N network) {
        if (Minecraft.getInstance().level != null) {
            ClientLevelData<N, B> level = levelMap.get(NetworkUtils.getLevelId(Minecraft.getInstance().level));

            if (level != null) {
                level.onNetworkAddedOrUpdated(network);
                LOGGER.debug("[{}] Added or updated network {} ({})", networkName, network.getNetworkId(), network);
            } else {
                LOGGER.warn("[{}] No level stored for {}", networkName, Minecraft.getInstance().level);
            }
        } else {
            LOGGER.warn("[{}] Invalid ClientLevel reference!", networkName);
        }
    }

    public void deletedNetwork(int networkId) {
        if (Minecraft.getInstance().level != null) {
            ClientLevelData<N, B> level = levelMap.get(NetworkUtils.getLevelId(Minecraft.getInstance().level));

            if (level != null) {
                level.onNetworkRemoved(networkId);
                LOGGER.debug("[{}] Removed network {}", networkName, networkId);
            } else {
                LOGGER.warn("[{}] No level stored for {}", networkName, Minecraft.getInstance().level);
            }
        } else {
            LOGGER.warn("[{}] Invalid ClientLevel reference!", networkName);
        }
    }

    @Nullable
    public N getNetwork(@NotNull B blockEntity) {
        if (blockEntity.getLevel() instanceof ClientLevel level) {
            ClientLevelData<N, B> levelData = levelMap.get(NetworkUtils.getLevelId(level));

            if (levelData != null) {
                return levelData.getNetwork(blockEntity);
            } else {
                LOGGER.warn("[{}] No network for level {}", networkName, level);
                return null;
            }
        } else {
            LOGGER.warn("[{}] Invalid ClientLevel reference: {}", networkName, blockEntity.getLevel());
            return null;
        }
    }
}
