package com.yanny.ytech.network.irrigation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.network.generic.client.ClientPropagator;
import com.yanny.ytech.network.generic.message.LevelSyncMessage;
import com.yanny.ytech.network.generic.message.NetworkAddedOrUpdatedMessage;
import com.yanny.ytech.network.generic.message.NetworkRemovedMessage;
import com.yanny.ytech.network.generic.server.ServerPropagator;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.simple.SimpleChannel;

public class IrrigationUtils {
    public static YTechMod.DistHolder<ClientPropagator<IrrigationNetwork, IIrrigationBlockEntity>, ServerPropagator<IrrigationNetwork, IIrrigationBlockEntity>> registerIrrigationPropagator(SimpleChannel channel) {
        return DistExecutor.unsafeRunForDist(() -> () -> registerClientIrrigationPropagator(channel), () -> () -> registerServerIrrigationPropagator(channel));
    }

    private static YTechMod.DistHolder<ClientPropagator<IrrigationNetwork, IIrrigationBlockEntity>, ServerPropagator<IrrigationNetwork, IIrrigationBlockEntity>> registerClientIrrigationPropagator(SimpleChannel channel) {
        int messageId = 0;
        ClientPropagator<IrrigationNetwork, IIrrigationBlockEntity> client = new ClientPropagator<>("irrigation");
        ServerPropagator<IrrigationNetwork, IIrrigationBlockEntity> server = new ServerPropagator<>(channel, IrrigationNetwork.FACTORY, "irrigation");

        channel.registerMessage(messageId++, LevelSyncMessage.class, (m, b) -> LevelSyncMessage.encode(m, b, IrrigationNetwork::encode),
                (b) -> LevelSyncMessage.decode(b, IrrigationNetwork::decode), client::onSyncLevel);
        channel.registerMessage(messageId++, NetworkAddedOrUpdatedMessage.class, (m, b) -> NetworkAddedOrUpdatedMessage.encode(m, b, IrrigationNetwork::encode),
                (b) -> NetworkAddedOrUpdatedMessage.decode(b, IrrigationNetwork::decode), client::onNetworkAddedOrUpdated);
        channel.registerMessage(messageId++, NetworkRemovedMessage.class, NetworkRemovedMessage::encode, NetworkRemovedMessage::decode, client::onNetworkRemoved);
        return new YTechMod.DistHolder<>(client, server);
    }

    private static YTechMod.DistHolder<ClientPropagator<IrrigationNetwork, IIrrigationBlockEntity>, ServerPropagator<IrrigationNetwork, IIrrigationBlockEntity>> registerServerIrrigationPropagator(SimpleChannel channel) {
        int messageId = 0;
        ServerPropagator<IrrigationNetwork, IIrrigationBlockEntity> server = new ServerPropagator<>(channel, IrrigationNetwork.FACTORY, "irrigation");

        channel.registerMessage(messageId++, LevelSyncMessage.class, (m, b) -> LevelSyncMessage.encode(m, b, IrrigationNetwork::encode),
                (b) -> LevelSyncMessage.decode(b, IrrigationNetwork::decode), (m, c) -> {});
        channel.registerMessage(messageId++, NetworkAddedOrUpdatedMessage.class, (m, b) -> NetworkAddedOrUpdatedMessage.encode(m, b, IrrigationNetwork::encode),
                (b) -> NetworkAddedOrUpdatedMessage.decode(b, IrrigationNetwork::decode), (m, c) -> {});
        channel.registerMessage(messageId++, NetworkRemovedMessage.class, NetworkRemovedMessage::encode, NetworkRemovedMessage::decode, (m, c) -> {});
        return new YTechMod.DistHolder<>(null, server);
    }
}
