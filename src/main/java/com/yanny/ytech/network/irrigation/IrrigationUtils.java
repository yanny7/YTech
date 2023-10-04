package com.yanny.ytech.network.irrigation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.network.generic.NetworkUtils;
import com.yanny.ytech.network.generic.client.ClientPropagator;
import com.yanny.ytech.network.generic.common.NetworkFactory;
import com.yanny.ytech.network.generic.message.LevelSyncMessage;
import com.yanny.ytech.network.generic.message.NetworkAddedOrUpdatedMessage;
import com.yanny.ytech.network.generic.message.NetworkRemovedMessage;
import com.yanny.ytech.network.generic.server.ServerPropagator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

public class IrrigationUtils {
    public static YTechMod.DistHolder<ClientPropagator<IrrigationNetwork, IIrrigationBlockEntity>, ServerPropagator<IrrigationNetwork, IIrrigationBlockEntity>> registerIrrigationPropagator(SimpleChannel channel) {
        return DistExecutor.unsafeRunForDist(() -> () -> registerClientIrrigationPropagator(channel), () -> () -> registerServerIrrigationPropagator(channel));
    }

    private static YTechMod.DistHolder<ClientPropagator<IrrigationNetwork, IIrrigationBlockEntity>, ServerPropagator<IrrigationNetwork, IIrrigationBlockEntity>> registerClientIrrigationPropagator(SimpleChannel channel) {
        ClientPropagator<IrrigationNetwork, IIrrigationBlockEntity> client = new ClientPropagator<>("irrigation");
        ServerPropagator<IrrigationNetwork, IIrrigationBlockEntity> server = new ServerPropagator<>(channel, new Factory(), "irrigation");

        channel.registerMessage(NetworkUtils.getMessageId(), MyLevelSyncMessage.class, MyLevelSyncMessage::encode, MyLevelSyncMessage::new, client::onSyncLevel);
        channel.registerMessage(NetworkUtils.getMessageId(), MyNetworkAddedOrUpdatedMessage.class, MyNetworkAddedOrUpdatedMessage::encode,
                MyNetworkAddedOrUpdatedMessage::new, client::onNetworkAddedOrUpdated);
        channel.registerMessage(NetworkUtils.getMessageId(), MyNetworkRemoveMessage.class, MyNetworkRemoveMessage::encode, MyNetworkRemoveMessage::new, client::onNetworkRemoved);
        return new YTechMod.DistHolder<>(client, server);
    }

    private static YTechMod.DistHolder<ClientPropagator<IrrigationNetwork, IIrrigationBlockEntity>, ServerPropagator<IrrigationNetwork, IIrrigationBlockEntity>> registerServerIrrigationPropagator(SimpleChannel channel) {
        ServerPropagator<IrrigationNetwork, IIrrigationBlockEntity> server = new ServerPropagator<>(channel, new Factory(), "irrigation");

        channel.registerMessage(NetworkUtils.getMessageId(), MyLevelSyncMessage.class, MyLevelSyncMessage::encode, MyLevelSyncMessage::new, (m, c) -> {});
        channel.registerMessage(NetworkUtils.getMessageId(), MyNetworkAddedOrUpdatedMessage.class, MyNetworkAddedOrUpdatedMessage::encode,
                MyNetworkAddedOrUpdatedMessage::new, (m, c) -> {});
        channel.registerMessage(NetworkUtils.getMessageId(), MyNetworkRemoveMessage.class, MyNetworkRemoveMessage::encode, MyNetworkRemoveMessage::new, (m, c) -> {});
        return new YTechMod.DistHolder<>(null, server);
    }

    private static class Factory implements NetworkFactory<IrrigationNetwork, IIrrigationBlockEntity> {
        @Override
        public @NotNull IrrigationNetwork createNetwork(@NotNull CompoundTag tag, int networkId, @NotNull Consumer<Integer> onRemove) {
            return new IrrigationNetwork(tag, networkId, onRemove);
        }

        @Override
        public @NotNull IrrigationNetwork createNetwork(int networkId, @NotNull Consumer<Integer> onRemove) {
            return new IrrigationNetwork(networkId, onRemove);
        }

        @NotNull
        @Override
        public NetworkRemovedMessage createNetworkRemoveMessage(int networkId) {
            return new MyNetworkRemoveMessage(networkId);
        }

        @NotNull
        @Override
        public NetworkAddedOrUpdatedMessage<IrrigationNetwork, IIrrigationBlockEntity> createNetworkAddedOrUpdatedMessage(@NotNull IrrigationNetwork network) {
            return new MyNetworkAddedOrUpdatedMessage(network);
        }

        @Override
        public @NotNull LevelSyncMessage<IrrigationNetwork, IIrrigationBlockEntity> createLevelSyncMessage(@NotNull Map<Integer, IrrigationNetwork> networkMap) {
            return new MyLevelSyncMessage(networkMap);
        }
    }

    static class MyNetworkRemoveMessage extends NetworkRemovedMessage {
        public MyNetworkRemoveMessage(int networkId) {
            super(networkId);
        }

        public MyNetworkRemoveMessage(@NotNull FriendlyByteBuf buf) {
            super(buf);
        }
    }

    static class MyNetworkAddedOrUpdatedMessage extends NetworkAddedOrUpdatedMessage<IrrigationNetwork, IIrrigationBlockEntity> {
        public MyNetworkAddedOrUpdatedMessage(@NotNull IrrigationNetwork network) {
            super(network, IrrigationNetwork::encode);
        }

        public MyNetworkAddedOrUpdatedMessage(@NotNull FriendlyByteBuf buf) {
            super(buf, IrrigationNetwork::encode, IrrigationNetwork::decode);
        }
    }

    static class MyLevelSyncMessage extends LevelSyncMessage<IrrigationNetwork, IIrrigationBlockEntity> {
        public MyLevelSyncMessage(@NotNull Map<Integer, IrrigationNetwork> networkMap) {
            super(networkMap, IrrigationNetwork::encode);
        }

        public MyLevelSyncMessage(@NotNull FriendlyByteBuf buf) {
            super(buf, IrrigationNetwork::encode, IrrigationNetwork::decode);
        }
    }
}
