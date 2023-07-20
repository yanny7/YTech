package com.yanny.ytech.network.kinetic.server;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.network.kinetic.KineticUtils;
import com.yanny.ytech.network.kinetic.common.IKineticBlockEntity;
import com.yanny.ytech.network.kinetic.message.LevelSyncMessage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.HashMap;

public class ServerKineticPropagator {
    protected static final Logger LOGGER = LogUtils.getLogger();
    private final HashMap<LevelAccessor, ServerKineticLevel> levelMap = new HashMap<>();
    private final SimpleChannel channel;

    public ServerKineticPropagator(SimpleChannel channel) {
        this.channel = channel;
    }

    public void add(@NotNull IKineticBlockEntity blockEntity) {
        levelMap.get(blockEntity.getLevel()).add(blockEntity);
    }

    public void changed(IKineticBlockEntity blockEntity) {
        levelMap.get(blockEntity.getLevel()).changed(blockEntity);
    }

    public void remove(@NotNull IKineticBlockEntity blockEntity) {
        levelMap.get(blockEntity.getLevel()).remove(blockEntity);
    }

    public void onLevelLoad(@NotNull ServerLevel level) {
        LOGGER.debug("Preparing rotary propagator for {}", KineticUtils.getLevelId(level));
        levelMap.put(level, level.getDataStorage().computeIfAbsent((tag) -> new ServerKineticLevel(tag, channel),
                () -> new ServerKineticLevel(channel), YTechMod.MOD_ID + "_rotary"));
        LOGGER.debug("Prepared rotary propagator for {}", KineticUtils.getLevelId(level));
    }

    public void onLevelUnload(@NotNull ServerLevel level) {
        LOGGER.debug("Removing rotary propagator for {}", KineticUtils.getLevelId(level));
        levelMap.remove(level);
        LOGGER.debug("Removed rotary propagator for {}", KineticUtils.getLevelId(level));
    }

    public void onPlayerLogIn(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            channel.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new LevelSyncMessage(levelMap.get(serverPlayer.level()).getNetworks()));
        }
    }
}
