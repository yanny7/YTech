package com.yanny.ytech.network.kinetic;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.network.generic.NetworkUtils;
import com.yanny.ytech.network.generic.client.ClientPropagator;
import com.yanny.ytech.network.generic.common.CommonNetwork;
import com.yanny.ytech.network.generic.common.NetworkFactory;
import com.yanny.ytech.network.generic.message.LevelSyncMessage;
import com.yanny.ytech.network.generic.message.NetworkAddedOrUpdatedMessage;
import com.yanny.ytech.network.generic.message.NetworkRemovedMessage;
import com.yanny.ytech.network.generic.server.ServerPropagator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class KineticUtils {
    private KineticUtils() {}

    @NotNull
    public static YTechMod.DistHolder<ClientPropagator<KineticClientNetwork, IKineticBlockEntity>, ServerPropagator<KineticServerNetwork, IKineticBlockEntity>> registerKineticPropagator(@NotNull SimpleChannel channel) {
        return DistExecutor.unsafeRunForDist(() -> () -> registerClientKineticPropagator(channel), () -> () -> registerServerKineticPropagator(channel));
    }

    @NotNull
    private static YTechMod.DistHolder<ClientPropagator<KineticClientNetwork, IKineticBlockEntity>, ServerPropagator<KineticServerNetwork, IKineticBlockEntity>> registerClientKineticPropagator(@NotNull SimpleChannel channel) {
        KineticClientPropagator client = new KineticClientPropagator();
        ServerPropagator<KineticServerNetwork, IKineticBlockEntity> server = new ServerPropagator<>(new Factory(channel), "kinetic");

        channel.registerMessage(NetworkUtils.getMessageId(), MyLevelSyncMessage.class, MyLevelSyncMessage::encode, MyLevelSyncMessage::new, client::onSyncLevel);
        channel.registerMessage(NetworkUtils.getMessageId(), MyNetworkUpdatedMessage.class, MyNetworkUpdatedMessage::encode, MyNetworkUpdatedMessage::new, client::onNetworkAddedOrUpdated);
        channel.registerMessage(NetworkUtils.getMessageId(), MyNetworkRemoveMessage.class, MyNetworkRemoveMessage::encode, MyNetworkRemoveMessage::new, client::onNetworkRemoved);
        return new YTechMod.DistHolder<>(client, server);
    }

    @NotNull
    private static YTechMod.DistHolder<ClientPropagator<KineticClientNetwork, IKineticBlockEntity>, ServerPropagator<KineticServerNetwork, IKineticBlockEntity>> registerServerKineticPropagator(@NotNull SimpleChannel channel) {
        ServerPropagator<KineticServerNetwork, IKineticBlockEntity> server = new ServerPropagator<>(new Factory(channel), "kinetic");

        channel.registerMessage(NetworkUtils.getMessageId(), MyLevelSyncMessage.class, MyLevelSyncMessage::encode, MyLevelSyncMessage::new, (m, c) -> {});
        channel.registerMessage(NetworkUtils.getMessageId(), MyNetworkUpdatedMessage.class, MyNetworkUpdatedMessage::encode, MyNetworkUpdatedMessage::new, (m, c) -> {});
        channel.registerMessage(NetworkUtils.getMessageId(), MyNetworkRemoveMessage.class, MyNetworkRemoveMessage::encode, MyNetworkRemoveMessage::new, (m, c) -> {});
        return new YTechMod.DistHolder<>(null, server);
    }

    private static class KineticClientPropagator extends ClientPropagator<KineticClientNetwork, IKineticBlockEntity> {
        public KineticClientPropagator() {
            super("kinetic");
        }
        public void onSyncLevel(@NotNull MyLevelSyncMessage msg, @NotNull Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> syncLevel(msg.networkMap.entrySet().stream().map((entry) -> {
                Payload payload = entry.getValue();
                return new KineticClientNetwork(entry.getKey(), payload.stressCapacity, payload.stress, payload.rotationDirection);
            }).collect(Collectors.toMap(CommonNetwork::getNetworkId, (b) -> b))));
            context.setPacketHandled(true);
        }

        public void onNetworkAddedOrUpdated(@NotNull MyNetworkUpdatedMessage msg, @NotNull Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                Payload payload = msg.payload;
                addOrUpdateNetwork(new KineticClientNetwork(payload.networkId, payload.stressCapacity, payload.stress, payload.rotationDirection));
            });
            context.setPacketHandled(true);
        }

        public void onNetworkRemoved(@NotNull MyNetworkRemoveMessage msg, @NotNull Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> deletedNetwork(msg.networkId));
            context.setPacketHandled(true);
        }
    }

    private record Factory(@NotNull SimpleChannel channel) implements NetworkFactory<KineticServerNetwork, IKineticBlockEntity> {
        @Override
        public @NotNull KineticServerNetwork createNetwork(@NotNull CompoundTag tag, int networkId, @NotNull Consumer<Integer> onChange, @NotNull Consumer<Integer> onRemove) {
            return new KineticServerNetwork(tag, networkId, onChange, onRemove);
        }

        @Override
        public @NotNull KineticServerNetwork createNetwork(int networkId, @NotNull Consumer<Integer> onChange, @NotNull Consumer<Integer> onRemove) {
            return new KineticServerNetwork(networkId, onChange, onRemove);
        }

        @Override
        public void sendRemoved(@NotNull PacketDistributor.PacketTarget target, int networkId) {
            channel.send(target, new MyNetworkRemoveMessage(networkId));
        }

        @Override
        public void sendUpdated(@NotNull PacketDistributor.PacketTarget target, @NotNull KineticServerNetwork network) {
            channel.send(target, new MyNetworkUpdatedMessage(
                    new Payload(network.getNetworkId(), network.getStressCapacity(), network.getStress(), network.getRotationDirection())));
        }

        @Override
        public void sendLevelSync(@NotNull PacketDistributor.PacketTarget target, @NotNull Map<Integer, KineticServerNetwork> networkMap) {
            channel.send(target, new MyLevelSyncMessage(networkMap.entrySet().stream().map((entry) -> {
                KineticServerNetwork network = entry.getValue();
                return new Payload(entry.getKey(), network.getStressCapacity(), network.getStress(), network.getRotationDirection());
            }).collect(Collectors.toMap((a) -> a.networkId, (b) -> b))));
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

    static class MyNetworkUpdatedMessage extends NetworkAddedOrUpdatedMessage<Payload> {

        public MyNetworkUpdatedMessage(@NotNull Payload payload) {
            super(payload);
        }

        public MyNetworkUpdatedMessage(@NotNull FriendlyByteBuf buf) {
            super(buf, (buffer) -> new Payload(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readEnum(RotationDirection.class)));
        }

        public void encode(@NotNull FriendlyByteBuf buf) {
            super.encode(buf, (buffer, payload) -> {
                buffer.writeInt(payload.networkId);
                buffer.writeInt(payload.stressCapacity);
                buffer.writeInt(payload.stress);
                buffer.writeEnum(payload.rotationDirection);
            });
        }
    }

    static class MyLevelSyncMessage extends LevelSyncMessage<Payload> {
        public MyLevelSyncMessage(@NotNull Map<Integer, Payload> networkMap) {
            super(networkMap);
        }

        public MyLevelSyncMessage(@NotNull FriendlyByteBuf buf) {
            super(buf, (buffer) -> new Payload(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readEnum(RotationDirection.class)));
        }

        public void encode(@NotNull FriendlyByteBuf buf) {
            super.encode(buf, (buffer, payload) -> {
                buffer.writeInt(payload.networkId);
                buffer.writeInt(payload.stressCapacity);
                buffer.writeInt(payload.stress);
                buffer.writeEnum(payload.rotationDirection);
            });
        }
    }

    record Payload(int networkId, int stressCapacity, int stress, @NotNull RotationDirection rotationDirection) {}
}
