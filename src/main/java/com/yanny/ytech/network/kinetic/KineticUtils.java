package com.yanny.ytech.network.kinetic;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.network.kinetic.client.ClientKineticPropagator;
import com.yanny.ytech.network.kinetic.message.LevelSyncMessage;
import com.yanny.ytech.network.kinetic.message.NetworkAddedOrUpdatedMessage;
import com.yanny.ytech.network.kinetic.message.NetworkRemovedMessage;
import com.yanny.ytech.network.kinetic.server.ServerKineticPropagator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;
import java.util.stream.Collectors;

public class KineticUtils {

    private KineticUtils() {}

    public static List<BlockPos> getDirections(List<Direction> validDirections, BlockPos position, Direction currentDirection) {
        return validDirections.stream().map((direction -> position.offset(rotateDirection(currentDirection, direction).getNormal()))).collect(Collectors.toList());
    }

    public static Direction rotateDirection(Direction currentDirection, Direction baseDirection) {
        return Direction.from2DDataValue((currentDirection.get2DDataValue() + baseDirection.get2DDataValue()) % 4);
    }

    public static YTechMod.DistHolder<ClientKineticPropagator, ServerKineticPropagator> registerKineticPropagator(SimpleChannel channel) {
        return DistExecutor.unsafeRunForDist(() -> () -> registerClientKineticPropagator(channel), () -> () -> registerServerKineticPropagator(channel));
    }

    private static YTechMod.DistHolder<ClientKineticPropagator, ServerKineticPropagator> registerClientKineticPropagator(SimpleChannel channel) {
        int messageId = 0;
        ClientKineticPropagator client = new ClientKineticPropagator();
        ServerKineticPropagator server = new ServerKineticPropagator(channel);

        channel.registerMessage(messageId++, LevelSyncMessage.class, LevelSyncMessage::encode, LevelSyncMessage::decode, client::onSyncLevel);
        channel.registerMessage(messageId++, NetworkAddedOrUpdatedMessage.class, NetworkAddedOrUpdatedMessage::encode, NetworkAddedOrUpdatedMessage::decode, client::onNetworkAddedOrUpdated);
        channel.registerMessage(messageId++, NetworkRemovedMessage.class, NetworkRemovedMessage::encode, NetworkRemovedMessage::decode, client::onNetworkRemoved);
        return new YTechMod.DistHolder<>(client, server);
    }

    private static YTechMod.DistHolder<ClientKineticPropagator, ServerKineticPropagator> registerServerKineticPropagator(SimpleChannel channel) {
        int messageId = 0;
        ServerKineticPropagator server = new ServerKineticPropagator(channel);

        channel.registerMessage(messageId++, LevelSyncMessage.class, LevelSyncMessage::encode, LevelSyncMessage::decode, (m, c) -> {});
        channel.registerMessage(messageId++, NetworkAddedOrUpdatedMessage.class, NetworkAddedOrUpdatedMessage::encode, NetworkAddedOrUpdatedMessage::decode, (m, c) -> {});
        channel.registerMessage(messageId++, NetworkRemovedMessage.class, NetworkRemovedMessage::encode, NetworkRemovedMessage::decode, (m, c) -> {});
        return new YTechMod.DistHolder<>(null, server);
    }
}
