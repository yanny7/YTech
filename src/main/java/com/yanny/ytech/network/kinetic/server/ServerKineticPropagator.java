package com.yanny.ytech.network.kinetic.server;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.network.kinetic.common.IKineticBlockEntity;
import com.yanny.ytech.network.kinetic.common.KineticPropagator;
import com.yanny.ytech.network.kinetic.message.LevelSyncMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class ServerKineticPropagator extends KineticPropagator<ServerLevel, ServerKineticLevel> {
    private final Set<Player> players = new HashSet<>();
    private final SimpleChannel channel;

    public ServerKineticPropagator(SimpleChannel channel) {
        this.channel = channel;
    }

    public void add(@NotNull IKineticBlockEntity blockEntity) {
        levelMap.get(blockEntity.getLevel()).add(blockEntity);
    }

    public void remove(@NotNull IKineticBlockEntity blockEntity) {
        levelMap.get(blockEntity.getLevel()).remove(blockEntity);
    }

    public void onLevelLoad(@NotNull ServerLevel level) {
        LOGGER.debug("Preparing rotary propagator for {}", getLevelId(level));
        levelMap.put(level, level.getDataStorage().computeIfAbsent(this::load, () -> new ServerKineticLevel(channel), YTechMod.MOD_ID + "_rotary"));
        LOGGER.debug("Prepared rotary propagator for {}", getLevelId(level));
    }

    public void onLevelUnload(@NotNull ServerLevel level) {
        LOGGER.debug("Removing rotary propagator for {}", getLevelId(level));
        levelMap.remove(level);
        LOGGER.debug("Removed rotary propagator for {}", getLevelId(level));
    }

    public void onPlayerLogIn(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            players.add(serverPlayer);
            channel.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new LevelSyncMessage(levelMap.get(serverPlayer.level()).getNetworks()));
        }
    }

    public void onPlayerLogOut(Player player) {
        if (player instanceof ServerPlayer) {
            players.remove(player);
        }
    }

    @NotNull
    private ServerKineticLevel load(@NotNull CompoundTag tag) {
        return new ServerKineticLevel(tag, channel);
    }
}
