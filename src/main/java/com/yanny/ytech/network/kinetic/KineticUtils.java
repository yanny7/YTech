package com.yanny.ytech.network.kinetic;

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

public class KineticUtils {
    private KineticUtils() {}

    @NotNull
    public static YTechMod.DistHolder<ClientPropagator<KineticNetwork, IKineticBlockEntity>, ServerPropagator<KineticNetwork, IKineticBlockEntity>> registerKineticPropagator(@NotNull SimpleChannel channel) {
        return DistExecutor.unsafeRunForDist(() -> () -> registerClientKineticPropagator(channel), () -> () -> registerServerKineticPropagator(channel));
    }

    @NotNull
    private static YTechMod.DistHolder<ClientPropagator<KineticNetwork, IKineticBlockEntity>, ServerPropagator<KineticNetwork, IKineticBlockEntity>> registerClientKineticPropagator(@NotNull SimpleChannel channel) {
        ClientPropagator<KineticNetwork, IKineticBlockEntity> client = new ClientPropagator<>("kinetic");
        ServerPropagator<KineticNetwork, IKineticBlockEntity> server = new ServerPropagator<>(channel, new Factory(), "kinetic");

        channel.registerMessage(NetworkUtils.getMessageId(), MyLevelSyncMessage.class, MyLevelSyncMessage::encode, MyLevelSyncMessage::new, client::onSyncLevel);
        channel.registerMessage(NetworkUtils.getMessageId(), MyNetworkAddedOrUpdatedMessage.class, MyNetworkAddedOrUpdatedMessage::encode,
                MyNetworkAddedOrUpdatedMessage::new, client::onNetworkAddedOrUpdated);
        channel.registerMessage(NetworkUtils.getMessageId(), MyNetworkRemoveMessage.class, MyNetworkRemoveMessage::encode,
                MyNetworkRemoveMessage::new, client::onNetworkRemoved);
        return new YTechMod.DistHolder<>(client, server);
    }

    @NotNull
    private static YTechMod.DistHolder<ClientPropagator<KineticNetwork, IKineticBlockEntity>, ServerPropagator<KineticNetwork, IKineticBlockEntity>> registerServerKineticPropagator(@NotNull SimpleChannel channel) {
        ServerPropagator<KineticNetwork, IKineticBlockEntity> server = new ServerPropagator<>(channel, new Factory(), "kinetic");

        channel.registerMessage(NetworkUtils.getMessageId(), MyLevelSyncMessage.class, MyLevelSyncMessage::encode, MyLevelSyncMessage::new, (m, c) -> {});
        channel.registerMessage(NetworkUtils.getMessageId(), MyNetworkAddedOrUpdatedMessage.class, MyNetworkAddedOrUpdatedMessage::encode,
                MyNetworkAddedOrUpdatedMessage::new, (m, c) -> {});
        channel.registerMessage(NetworkUtils.getMessageId(), MyNetworkRemoveMessage.class, MyNetworkRemoveMessage::encode, MyNetworkRemoveMessage::new, (m, c) -> {});
        return new YTechMod.DistHolder<>(null, server);
    }

    private static class Factory implements NetworkFactory<KineticNetwork, IKineticBlockEntity> {
        @Override
        public @NotNull KineticNetwork createNetwork(@NotNull CompoundTag tag, int networkId, @NotNull Consumer<Integer> onRemove) {
            return new KineticNetwork(tag, networkId, onRemove);
        }

        @Override
        public @NotNull KineticNetwork createNetwork(int networkId, @NotNull Consumer<Integer> onRemove) {
            return new KineticNetwork(networkId, onRemove);
        }

        @NotNull
        @Override
        public NetworkRemovedMessage createNetworkRemoveMessage(int networkId) {
            return new MyNetworkRemoveMessage(networkId);
        }

        @NotNull
        @Override
        public NetworkAddedOrUpdatedMessage<KineticNetwork, IKineticBlockEntity> createNetworkAddedOrUpdatedMessage(@NotNull KineticNetwork network) {
            return new MyNetworkAddedOrUpdatedMessage(network);
        }

        @Override
        public @NotNull LevelSyncMessage<KineticNetwork, IKineticBlockEntity> createLevelSyncMessage(@NotNull Map<Integer, KineticNetwork> networkMap) {
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

    static class MyNetworkAddedOrUpdatedMessage extends NetworkAddedOrUpdatedMessage<KineticNetwork, IKineticBlockEntity> {

        public MyNetworkAddedOrUpdatedMessage(@NotNull KineticNetwork network) {
            super(network, KineticNetwork::encode);
        }

        public MyNetworkAddedOrUpdatedMessage(@NotNull FriendlyByteBuf buf) {
            super(buf, KineticNetwork::encode, KineticNetwork::decode);
        }
    }

    static class MyLevelSyncMessage extends LevelSyncMessage<KineticNetwork, IKineticBlockEntity> {
        public MyLevelSyncMessage(@NotNull Map<Integer, KineticNetwork> networkMap) {
            super(networkMap, KineticNetwork::encode);
        }

        public MyLevelSyncMessage(@NotNull FriendlyByteBuf buf) {
            super(buf, KineticNetwork::encode, KineticNetwork::decode);
        }
    }
}
