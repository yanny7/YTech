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
import net.minecraftforge.network.PacketDistributor;
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
            LOGGER.warn("[{}] Invalid ServerLevel reference: {}", networkName, blockEntity.getLevel());
        }
    }

    public void changed(@NotNull O blockEntity) {
        if (blockEntity.getLevel() instanceof ServerLevel level) {
            levelMap.get(NetworkUtils.getLevelId(level)).update(blockEntity);
        } else {
            LOGGER.warn("[{}] Invalid ServerLevel reference: {}", networkName, blockEntity.getLevel());
        }
    }

    public void remove(@NotNull O blockEntity) {
        if (blockEntity.getLevel() instanceof ServerLevel level) {
            levelMap.get(NetworkUtils.getLevelId(level)).remove(blockEntity);
        } else {
            LOGGER.warn("[{}] Invalid ServerLevel reference: {}", networkName, blockEntity.getLevel());
        }
    }

    public void onLevelLoad(@NotNull ServerLevel level) {
        ResourceLocation id = NetworkUtils.getLevelId(level);

        LOGGER.debug("[{}] Preparing propagators for {}", networkName, id);
        levelMap.put(id, level.getDataStorage().computeIfAbsent((tag) -> new ServerLevelData<>(tag, networkFactory, networkName),
                () -> new ServerLevelData<>(networkFactory, networkName), YTechMod.MOD_ID + "_" + networkName));
        LOGGER.debug("[{}] Prepared propagators for {}", networkName, id);
    }

    public void onLevelUnload(@NotNull ServerLevel level) {
        ResourceLocation id = NetworkUtils.getLevelId(level);

        LOGGER.debug("[{}] Removing propagator for {}", networkName, id);
        levelMap.remove(id);
        LOGGER.debug("[{}] Removed propagator for {}", networkName, id);
    }

    public void onPlayerLogIn(@NotNull Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            networkFactory.sendLevelSync(PacketDistributor.PLAYER.with(() -> serverPlayer), levelMap.get(NetworkUtils.getLevelId(serverPlayer.level())).getNetworks());
        }
    }

    @Nullable
    public N getNetwork(@NotNull O blockEntity) {
        if (blockEntity.getLevel() instanceof ServerLevel level) {
            ServerLevelData<N, O> levelData = levelMap.get(NetworkUtils.getLevelId(level));

            if (levelData != null) {
                return levelData.getNetwork(blockEntity);
            } else {
                LOGGER.warn("[{}] No " + networkName + " network for level {}", networkName, level);
                return null;
            }
        } else {
            LOGGER.warn("[{}] Invalid ServerLevel reference: {}", networkName, blockEntity.getLevel());
            return null;
        }
    }

    @NotNull
    public Map<Integer, N> getNetworks(@NotNull ServerLevel level) {
        ServerLevelData<N, O> serverLevelData = levelMap.get(NetworkUtils.getLevelId(level));

        if (serverLevelData != null) {
            return serverLevelData.getNetworks();
        } else {
            LOGGER.warn("[{}] No networks defined for level {}", networkName, level);
            return Map.of();
        }
    }

    public void tick(@NotNull ServerLevel level) {
        ServerLevelData<N, O> serverLevelData = levelMap.get(NetworkUtils.getLevelId(level));

        if (serverLevelData != null) {
            serverLevelData.tick();
        } else {
            LOGGER.warn("[{}] No networks defined for level {}", networkName, level);
        }
    }
}
