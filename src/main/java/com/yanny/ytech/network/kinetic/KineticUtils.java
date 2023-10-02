package com.yanny.ytech.network.kinetic;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.network.generic.client.ClientPropagator;
import com.yanny.ytech.network.generic.message.LevelSyncMessage;
import com.yanny.ytech.network.generic.message.NetworkAddedOrUpdatedMessage;
import com.yanny.ytech.network.generic.message.NetworkRemovedMessage;
import com.yanny.ytech.network.generic.server.ServerPropagator;
import com.yanny.ytech.network.kinetic.common.IKineticBlockEntity;
import com.yanny.ytech.network.kinetic.common.KineticNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public static YTechMod.DistHolder<ClientPropagator<KineticNetwork, IKineticBlockEntity>, ServerPropagator<KineticNetwork, IKineticBlockEntity>> registerKineticPropagator(SimpleChannel channel) {
        return DistExecutor.unsafeRunForDist(() -> () -> registerClientKineticPropagator(channel), () -> () -> registerServerKineticPropagator(channel));
    }

    @Nullable
    public static ResourceLocation getLevelId(@NotNull LevelAccessor level) {
        return level.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).getKey(level.dimensionType());
    }

    private static YTechMod.DistHolder<ClientPropagator<KineticNetwork, IKineticBlockEntity>, ServerPropagator<KineticNetwork, IKineticBlockEntity>> registerClientKineticPropagator(SimpleChannel channel) {
        int messageId = 0;
        ClientPropagator<KineticNetwork, IKineticBlockEntity> client = new ClientPropagator<>();
        ServerPropagator<KineticNetwork, IKineticBlockEntity> server = new ServerPropagator<>(channel, KineticNetwork.FACTORY);

        channel.registerMessage(messageId++, LevelSyncMessage.class, (m, b) -> LevelSyncMessage.encode(m, b, KineticNetwork::encode),
                (b) -> LevelSyncMessage.decode(b, KineticNetwork::decode), client::onSyncLevel);
        channel.registerMessage(messageId++, NetworkAddedOrUpdatedMessage.class, (m, b) -> NetworkAddedOrUpdatedMessage.encode(m, b, KineticNetwork::encode),
                (b) -> NetworkAddedOrUpdatedMessage.decode(b, KineticNetwork::decode), client::onNetworkAddedOrUpdated);
        channel.registerMessage(messageId++, NetworkRemovedMessage.class, NetworkRemovedMessage::encode, NetworkRemovedMessage::decode, client::onNetworkRemoved);
        return new YTechMod.DistHolder<>(client, server);
    }

    private static YTechMod.DistHolder<ClientPropagator<KineticNetwork, IKineticBlockEntity>, ServerPropagator<KineticNetwork, IKineticBlockEntity>> registerServerKineticPropagator(SimpleChannel channel) {
        int messageId = 0;
        ServerPropagator<KineticNetwork, IKineticBlockEntity> server = new ServerPropagator<>(channel, KineticNetwork.FACTORY);

        channel.registerMessage(messageId++, LevelSyncMessage.class, (m, b) -> LevelSyncMessage.encode(m, b, KineticNetwork::encode),
                (b) -> LevelSyncMessage.decode(b, KineticNetwork::decode), (m, c) -> {});
        channel.registerMessage(messageId++, NetworkAddedOrUpdatedMessage.class, (m, b) -> NetworkAddedOrUpdatedMessage.encode(m, b, KineticNetwork::encode),
                (b) -> NetworkAddedOrUpdatedMessage.decode(b, KineticNetwork::decode), (m, c) -> {});
        channel.registerMessage(messageId++, NetworkRemovedMessage.class, NetworkRemovedMessage::encode, NetworkRemovedMessage::decode, (m, c) -> {});
        return new YTechMod.DistHolder<>(null, server);
    }
}
