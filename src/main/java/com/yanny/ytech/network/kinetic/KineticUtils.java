package com.yanny.ytech.network.kinetic;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.network.generic.client.ClientPropagator;
import com.yanny.ytech.network.generic.common.CommonNetwork;
import com.yanny.ytech.network.generic.common.NetworkFactory;
import com.yanny.ytech.network.generic.message.LevelSyncMessage;
import com.yanny.ytech.network.generic.message.NetworkAddedOrUpdatedMessage;
import com.yanny.ytech.network.generic.message.NetworkRemovedMessage;
import com.yanny.ytech.network.generic.server.ServerPropagator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class KineticUtils {
    private KineticUtils() {}

    @NotNull
    public static YTechMod.DistHolder<ClientPropagator<KineticClientNetwork, IKineticBlockEntity>, ServerPropagator<KineticServerNetwork, IKineticBlockEntity>> registerKineticPropagator(@NotNull IPayloadRegistrar channel) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return registerClientKineticPropagator(channel);
        } else {
            return registerServerKineticPropagator(channel);
        }
    }

    @NotNull
    private static YTechMod.DistHolder<ClientPropagator<KineticClientNetwork, IKineticBlockEntity>, ServerPropagator<KineticServerNetwork, IKineticBlockEntity>> registerClientKineticPropagator(@NotNull IPayloadRegistrar channel) {
        KineticClientPropagator client = new KineticClientPropagator();
        ServerPropagator<KineticServerNetwork, IKineticBlockEntity> server = new ServerPropagator<>(new Factory(), "kinetic");

        channel.play(MyLevelSyncMessage.ID, MyLevelSyncMessage::new, handler -> handler.client(client::onSyncLevel));
        channel.play(MyNetworkUpdatedMessage.ID, MyNetworkUpdatedMessage::new, handler -> handler.client(client::onNetworkAddedOrUpdated));
        channel.play(MyNetworkRemoveMessage.ID, MyNetworkRemoveMessage::new, handler -> handler.client(client::onNetworkRemoved));

        return new YTechMod.DistHolder<>(client, server);
    }

    @NotNull
    private static YTechMod.DistHolder<ClientPropagator<KineticClientNetwork, IKineticBlockEntity>, ServerPropagator<KineticServerNetwork, IKineticBlockEntity>> registerServerKineticPropagator(@NotNull IPayloadRegistrar channel) {
        ServerPropagator<KineticServerNetwork, IKineticBlockEntity> server = new ServerPropagator<>(new Factory(), "kinetic");

        channel.play(MyLevelSyncMessage.ID, MyLevelSyncMessage::new, handler -> {});
        channel.play(MyNetworkUpdatedMessage.ID, MyNetworkUpdatedMessage::new, handler -> {});
        channel.play(MyNetworkRemoveMessage.ID, MyNetworkRemoveMessage::new, handler -> {});
        return new YTechMod.DistHolder<>(null, server);
    }

    private static class KineticClientPropagator extends ClientPropagator<KineticClientNetwork, IKineticBlockEntity> {
        public KineticClientPropagator() {
            super("kinetic");
        }
        public void onSyncLevel(@NotNull MyLevelSyncMessage msg, @NotNull PlayPayloadContext context) {
            context.workHandler().execute(() -> syncLevel(msg.networkMap.entrySet().stream().map((entry) -> {
                Payload payload = entry.getValue();
                return new KineticClientNetwork(entry.getKey(), payload.stressCapacity, payload.stress, payload.rotationDirection);
            }).collect(Collectors.toMap(CommonNetwork::getNetworkId, (b) -> b))));
        }

        public void onNetworkAddedOrUpdated(@NotNull MyNetworkUpdatedMessage msg, @NotNull PlayPayloadContext context) {
            context.workHandler().execute(() -> {
                Payload payload = msg.payload;
                addOrUpdateNetwork(new KineticClientNetwork(payload.networkId, payload.stressCapacity, payload.stress, payload.rotationDirection));
            });
        }

        public void onNetworkRemoved(@NotNull MyNetworkRemoveMessage msg, @NotNull PlayPayloadContext context) {
            context.workHandler().execute((() -> deletedNetwork(msg.networkId)));
        }
    }

    private static class Factory implements NetworkFactory<KineticServerNetwork, IKineticBlockEntity> {
        @Override
        public @NotNull KineticServerNetwork createNetwork(@NotNull CompoundTag tag, int networkId, @NotNull Consumer<Integer> onChange, @NotNull BiConsumer<Integer, ChunkPos> onRemove) {
            return new KineticServerNetwork(tag, networkId, onChange, onRemove);
        }

        @Override
        public @NotNull KineticServerNetwork createNetwork(int networkId, @NotNull Consumer<Integer> onChange, @NotNull BiConsumer<Integer, ChunkPos> onRemove) {
            return new KineticServerNetwork(networkId, onChange, onRemove);
        }

        @Override
        public void sendRemoved(@NotNull PacketDistributor.PacketTarget target, int networkId) {
            target.send(new MyNetworkRemoveMessage(networkId));
        }

        @Override
        public void sendUpdated(@NotNull PacketDistributor.PacketTarget target, @NotNull KineticServerNetwork network) {
            target.send(new MyNetworkUpdatedMessage(new Payload(network.getNetworkId(), network.getStressCapacity(), network.getStress(), network.getRotationDirection())));
        }

        @Override
        public void sendLevelSync(@NotNull PacketDistributor.PacketTarget target, @NotNull Map<Integer, KineticServerNetwork> networkMap) {
            target.send(new MyLevelSyncMessage(networkMap.entrySet().stream().map((entry) -> {
                KineticServerNetwork network = entry.getValue();
                return new Payload(entry.getKey(), network.getStressCapacity(), network.getStress(), network.getRotationDirection());
            }).collect(Collectors.toMap((a) -> a.networkId, (b) -> b))));
        }
    }

    static class MyNetworkRemoveMessage extends NetworkRemovedMessage implements CustomPacketPayload {
        private static final ResourceLocation ID = Utils.modLoc("kinetic_network_removed");

        public MyNetworkRemoveMessage(int networkId) {
            super(networkId);
        }

        public MyNetworkRemoveMessage(@NotNull FriendlyByteBuf buf) {
            super(buf);
        }

        @Override
        public void write(@NotNull FriendlyByteBuf friendlyByteBuf) {

        }

        @Override
        @NotNull
        public ResourceLocation id() {
            return ID;
        }
    }

    static class MyNetworkUpdatedMessage extends NetworkAddedOrUpdatedMessage<Payload> implements CustomPacketPayload {
        private static final ResourceLocation ID = Utils.modLoc("kinetic_network_updated");

        public MyNetworkUpdatedMessage(@NotNull Payload payload) {
            super(payload);
        }

        public MyNetworkUpdatedMessage(@NotNull FriendlyByteBuf buf) {
            super(buf, (buffer) -> new Payload(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readEnum(RotationDirection.class)));
        }

        @Override
        public void write(@NotNull FriendlyByteBuf buf) {
            super.write(buf, (buffer, payload) -> {
                buffer.writeInt(payload.networkId);
                buffer.writeInt(payload.stressCapacity);
                buffer.writeInt(payload.stress);
                buffer.writeEnum(payload.rotationDirection);
            });
        }

        @Override
        @NotNull
        public ResourceLocation id() {
            return ID;
        }
    }

    static class MyLevelSyncMessage extends LevelSyncMessage<Payload> implements CustomPacketPayload {
        private static final ResourceLocation ID = Utils.modLoc("kinetic_level_sync");

        public MyLevelSyncMessage(@NotNull Map<Integer, Payload> networkMap) {
            super(networkMap);
        }

        public MyLevelSyncMessage(@NotNull FriendlyByteBuf buf) {
            super(buf, (buffer) -> new Payload(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readEnum(RotationDirection.class)));
        }

        @Override
        public void write(@NotNull FriendlyByteBuf buf) {
            super.write(buf, (buffer, payload) -> {
                buffer.writeInt(payload.networkId);
                buffer.writeInt(payload.stressCapacity);
                buffer.writeInt(payload.stress);
                buffer.writeEnum(payload.rotationDirection);
            });
        }

        @Override
        @NotNull
        public ResourceLocation id() {
            return ID;
        }
    }

    record Payload(int networkId, int stressCapacity, int stress, @NotNull RotationDirection rotationDirection) {}
}
