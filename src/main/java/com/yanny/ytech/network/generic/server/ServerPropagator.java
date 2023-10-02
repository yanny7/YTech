package com.yanny.ytech.network.generic.server;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.network.generic.common.AbstractNetwork;
import com.yanny.ytech.network.generic.common.INetworkBlockEntity;
import com.yanny.ytech.network.generic.message.LevelSyncMessage;
import com.yanny.ytech.network.kinetic.KineticUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;

public class ServerPropagator<N extends AbstractNetwork<N, O>, O extends INetworkBlockEntity> {
    protected static final Logger LOGGER = LogUtils.getLogger();
    private final HashMap<LevelAccessor, ServerLevel<N, O>> levelMap = new HashMap<>();
    private final SimpleChannel channel;
    private final AbstractNetwork.Factory<N, O> networkFactory;

    public ServerPropagator(SimpleChannel channel, AbstractNetwork.Factory<N, O> networkFactory) {
        this.channel = channel;
        this.networkFactory = networkFactory;
    }

    public void add(@NotNull O blockEntity) {
        levelMap.get(blockEntity.getLevel()).add(blockEntity);
    }

    public void changed(O blockEntity) {
        levelMap.get(blockEntity.getLevel()).update(blockEntity);
    }

    public void remove(@NotNull O blockEntity) {
        levelMap.get(blockEntity.getLevel()).remove(blockEntity);
    }

    public void onLevelLoad(@NotNull net.minecraft.server.level.ServerLevel level) {
        LOGGER.debug("Preparing rotary propagator for {}", KineticUtils.getLevelId(level));
        levelMap.put(level, level.getDataStorage().computeIfAbsent((tag) -> new ServerLevel<>(tag, channel, networkFactory),
                () -> new ServerLevel<>(channel, networkFactory), YTechMod.MOD_ID + "_rotary"));
        LOGGER.debug("Prepared rotary propagator for {}", KineticUtils.getLevelId(level));
    }

    public void onLevelUnload(@NotNull net.minecraft.server.level.ServerLevel level) {
        LOGGER.debug("Removing rotary propagator for {}", KineticUtils.getLevelId(level));
        levelMap.remove(level);
        LOGGER.debug("Removed rotary propagator for {}", KineticUtils.getLevelId(level));
    }

    public void onPlayerLogIn(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            channel.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new LevelSyncMessage<>(levelMap.get(serverPlayer.level()).getNetworks()));
        }
    }

    @Nullable
    public N getNetwork(@NotNull O blockEntity) {
        ServerLevel<N, O> level = levelMap.get(blockEntity.getLevel());

        if (level != null) {
            return level.getNetwork(blockEntity);
        } else {
            LOGGER.warn("No kinetic network for level {}", blockEntity.getLevel());
            return null;
        }
    }
}
