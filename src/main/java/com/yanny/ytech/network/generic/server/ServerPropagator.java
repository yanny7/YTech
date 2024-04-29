package com.yanny.ytech.network.generic.server;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.network.generic.NetworkUtils;
import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import com.yanny.ytech.network.generic.common.NetworkFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ServerPropagator<N extends ServerNetwork<N, O>, O extends INetworkBlockEntity> {
    private static final Logger LOGGER = LogUtils.getLogger();

    @NotNull private final HashMap<ResourceLocation, ServerLevelData<N, O>> levelMap = new HashMap<>();
    @NotNull private final NetworkFactory<N, O> networkFactory;
    @NotNull private final String networkName;

    public ServerPropagator(@NotNull NetworkFactory<N, O> networkFactory, @NotNull String networkName) {
        this.networkFactory = networkFactory;
        this.networkName = networkName;
    }

    public void add(@NotNull O blockEntity) {
        if (blockEntity.getLevel() instanceof ServerLevel level) {
            levelMap.get(NetworkUtils.getLevelId(level)).add(blockEntity);
        } else {
            LOGGER.warn("[{}][add] Invalid ServerLevel reference: {}", networkName, blockEntity.getLevel());
        }
    }

    public void changed(@NotNull O blockEntity) {
        if (blockEntity.getLevel() instanceof ServerLevel level) {
            levelMap.get(NetworkUtils.getLevelId(level)).update(blockEntity);
        } else {
            LOGGER.warn("[{}][changed] Invalid ServerLevel reference: {}", networkName, blockEntity.getLevel());
        }
    }

    public void remove(@NotNull O blockEntity) {
        if (blockEntity.getLevel() instanceof ServerLevel level) {
            levelMap.get(NetworkUtils.getLevelId(level)).remove(blockEntity);
        } else {
            LOGGER.warn("[{}][remove] Invalid ServerLevel reference: {}", networkName, blockEntity.getLevel());
        }
    }

    public void onLevelLoad(@NotNull ServerLevel level) {
        ResourceLocation id = NetworkUtils.getLevelId(level);

        LOGGER.debug("[{}][onLevelLoad] Preparing propagators for {}", networkName, id);
        levelMap.put(id, level.getDataStorage().computeIfAbsent(new SavedData.Factory<>(
                () -> new ServerLevelData<>(id, level.getServer(), networkFactory, networkName),
                (tag, provider) -> new ServerLevelData<>(tag, id, level.getServer(), networkFactory, networkName, provider)
        ), YTechMod.MOD_ID + "_" + networkName));
        LOGGER.debug("[{}][onLevelLoad] Prepared propagators for {}", networkName, id);
    }

    public void onLevelUnload(@NotNull ServerLevel level) {
        ResourceLocation id = NetworkUtils.getLevelId(level);

        LOGGER.debug("[{}][onLevelUnload] Removing propagator for {}", networkName, id);
        levelMap.remove(id);
        LOGGER.debug("[{}][onLevelUnload] Removed propagator for {}", networkName, id);
    }

    public void onPlayerLogIn(@NotNull Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            LOGGER.debug("[{}][onPlayerLogIn] Connecting player {}", networkName, serverPlayer);
            networkFactory.sendLevelSync(serverPlayer, levelMap.get(NetworkUtils.getLevelId(serverPlayer.level())).getNetworks());
        }
    }

    @Nullable
    public N getNetwork(@NotNull O blockEntity) {
        if (blockEntity.getLevel() instanceof ServerLevel level) {
            ServerLevelData<N, O> levelData = levelMap.get(NetworkUtils.getLevelId(level));

            if (levelData != null) {
                return levelData.getNetwork(blockEntity);
            } else {
                LOGGER.warn("[{}][getNetwork] No " + networkName + " network for level {}", networkName, level);
                return null;
            }
        } else {
            LOGGER.warn("[{}][getNetwork] Invalid ServerLevel reference: {}", networkName, blockEntity.getLevel());
            return null;
        }
    }

    @NotNull
    public Map<Integer, N> getNetworks(@NotNull ServerLevel level) {
        ServerLevelData<N, O> serverLevelData = levelMap.get(NetworkUtils.getLevelId(level));

        if (serverLevelData != null) {
            return serverLevelData.getNetworks();
        } else {
            LOGGER.warn("[{}][getNetworks] No networks defined for level {}", networkName, level);
            return Map.of();
        }
    }

    public void tick(@NotNull ServerLevel level) {
        ServerLevelData<N, O> serverLevelData = levelMap.get(NetworkUtils.getLevelId(level));

        if (serverLevelData != null) {
            serverLevelData.tick(level.getChunkSource());
        } else {
            LOGGER.warn("[{}][tick] No networks defined for level {}", networkName, level);
        }
    }

    public void onChunkWatch(@NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull LevelChunk chunk) {
        ServerLevelData<N, O> serverLevelData = levelMap.get(NetworkUtils.getLevelId(level));

        if (serverLevelData != null) {
            serverLevelData.getNetworks().values().stream()
                    .filter((network) -> network.getChunks().contains(chunk.getPos()))
                    .forEach((network) -> networkFactory.sendUpdated(player, network));
        } else {
            LOGGER.warn("[{}][onChunkWatch] No networks defined for level {}", networkName, level);
        }
    }
}
