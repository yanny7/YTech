package com.yanny.ytech.network.irrigation;

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
import net.neoforged.fml.DistExecutor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class IrrigationUtils {
    public static YTechMod.DistHolder<ClientPropagator<IrrigationClientNetwork, IIrrigationBlockEntity>, ServerPropagator<IrrigationServerNetwork, IIrrigationBlockEntity>> registerIrrigationPropagator(IPayloadRegistrar channel) {
        return DistExecutor.unsafeRunForDist(() -> () -> registerClientIrrigationPropagator(channel), () -> () -> registerServerIrrigationPropagator(channel));
    }

    private static YTechMod.DistHolder<ClientPropagator<IrrigationClientNetwork, IIrrigationBlockEntity>, ServerPropagator<IrrigationServerNetwork, IIrrigationBlockEntity>> registerClientIrrigationPropagator(IPayloadRegistrar channel) {
        IrrigationClientPropagator client = new IrrigationClientPropagator();
        ServerPropagator<IrrigationServerNetwork, IIrrigationBlockEntity> server = new ServerPropagator<>(new Factory(), "irrigation");

        channel.play(MyLevelSyncMessage.ID, MyLevelSyncMessage::new, handler -> handler.client(client::onSyncLevel));
        channel.play(MyNetworkUpdatedMessage.ID, MyNetworkUpdatedMessage::new, handler -> handler.client(client::onNetworkAddedOrUpdated));
        channel.play(MyNetworkRemoveMessage.ID, MyNetworkRemoveMessage::new, handler -> handler.client(client::onNetworkRemoved));

        return new YTechMod.DistHolder<>(client, server);
    }

    private static YTechMod.DistHolder<ClientPropagator<IrrigationClientNetwork, IIrrigationBlockEntity>, ServerPropagator<IrrigationServerNetwork, IIrrigationBlockEntity>> registerServerIrrigationPropagator(IPayloadRegistrar channel) {
        ServerPropagator<IrrigationServerNetwork, IIrrigationBlockEntity> server = new ServerPropagator<>(new Factory(), "irrigation");

        channel.play(MyLevelSyncMessage.ID, MyLevelSyncMessage::new, handler -> {});
        channel.play(MyNetworkUpdatedMessage.ID, MyNetworkUpdatedMessage::new, handler -> {});
        channel.play(MyNetworkRemoveMessage.ID, MyNetworkRemoveMessage::new, handler -> {});
        return new YTechMod.DistHolder<>(null, server);
    }

    private static class IrrigationClientPropagator extends ClientPropagator<IrrigationClientNetwork, IIrrigationBlockEntity> {
        public IrrigationClientPropagator() {
            super("irrigation");
        }
        public void onSyncLevel(@NotNull IrrigationUtils.MyLevelSyncMessage msg, @NotNull PlayPayloadContext context) {
            context.workHandler().execute(() -> syncLevel(msg.networkMap.entrySet().stream().map((entry) -> {
                Payload payload = entry.getValue();
                return new IrrigationClientNetwork(entry.getKey(), payload.amount, payload.capacity);
            }).collect(Collectors.toMap(CommonNetwork::getNetworkId, (b) -> b))));
        }

        public void onNetworkAddedOrUpdated(@NotNull IrrigationUtils.MyNetworkUpdatedMessage msg, @NotNull PlayPayloadContext context) {
            context.workHandler().execute(() -> {
                IrrigationUtils.Payload payload = msg.payload;
                addOrUpdateNetwork(new IrrigationClientNetwork(payload.networkId, payload.amount, payload.capacity));
            });
        }

        public void onNetworkRemoved(@NotNull IrrigationUtils.MyNetworkRemoveMessage msg, @NotNull PlayPayloadContext context) {
            context.workHandler().execute(() -> deletedNetwork(msg.networkId));
        }
    }

    private static class Factory implements NetworkFactory<IrrigationServerNetwork, IIrrigationBlockEntity> {
        @Override
        public @NotNull IrrigationServerNetwork createNetwork(@NotNull CompoundTag tag, int networkId, @NotNull Consumer<Integer> onChange, @NotNull BiConsumer<Integer, ChunkPos> onRemove) {
            return new IrrigationServerNetwork(tag, networkId, onChange, onRemove);
        }

        @Override
        public @NotNull IrrigationServerNetwork createNetwork(int networkId, @NotNull Consumer<Integer> onChange, @NotNull BiConsumer<Integer, ChunkPos> onRemove) {
            return new IrrigationServerNetwork(networkId, onChange, onRemove);
        }

        @Override
        public void sendRemoved(@NotNull PacketDistributor.PacketTarget target, int networkId) {
            target.send(new IrrigationUtils.MyNetworkRemoveMessage(networkId));
        }

        @Override
        public void sendUpdated(@NotNull PacketDistributor.PacketTarget target, @NotNull IrrigationServerNetwork network) {
            target.send(new IrrigationUtils.MyNetworkUpdatedMessage(
                    new IrrigationUtils.Payload(network.getNetworkId(), network.getFluidHandler().getFluidAmount(), network.getFluidHandler().getCapacity())));
        }

        @Override
        public void sendLevelSync(@NotNull PacketDistributor.PacketTarget target, @NotNull Map<Integer, IrrigationServerNetwork> networkMap) {
            target.send(new IrrigationUtils.MyLevelSyncMessage(networkMap.entrySet().stream().map((entry) -> {
                IrrigationServerNetwork network = entry.getValue();
                return new IrrigationUtils.Payload(entry.getKey(), network.getFluidHandler().getFluidAmount(), network.getFluidHandler().getCapacity());
            }).collect(Collectors.toMap((a) -> a.networkId, (b) -> b))));
        }
    }

    static class MyNetworkRemoveMessage extends NetworkRemovedMessage implements CustomPacketPayload {
        private static final ResourceLocation ID = Utils.modLoc("irrigation_network_removed");

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

    static class MyNetworkUpdatedMessage extends NetworkAddedOrUpdatedMessage<IrrigationUtils.Payload> implements CustomPacketPayload {
        private static final ResourceLocation ID = Utils.modLoc("irrigation_network_updated");

        public MyNetworkUpdatedMessage(@NotNull IrrigationUtils.Payload payload) {
            super(payload);
        }

        public MyNetworkUpdatedMessage(@NotNull FriendlyByteBuf buf) {
            super(buf, (buffer) -> new IrrigationUtils.Payload(buffer.readInt(), buffer.readInt(), buffer.readInt()));
        }

        @Override
        public void write(@NotNull FriendlyByteBuf buf) {
            super.write(buf, (buffer, payload) -> {
                buffer.writeInt(payload.networkId);
                buffer.writeInt(payload.amount);
                buffer.writeInt(payload.capacity);
            });
        }

        @Override
        @NotNull
        public ResourceLocation id() {
            return ID;
        }
    }

    static class MyLevelSyncMessage extends LevelSyncMessage<IrrigationUtils.Payload> implements CustomPacketPayload {
        private static final ResourceLocation ID = Utils.modLoc("irrigation_level_sync");

        public MyLevelSyncMessage(@NotNull Map<Integer, IrrigationUtils.Payload> networkMap) {
            super(networkMap);
        }

        public MyLevelSyncMessage(@NotNull FriendlyByteBuf buf) {
            super(buf, (buffer) -> new IrrigationUtils.Payload(buffer.readInt(), buffer.readInt(), buffer.readInt()));
        }

        @Override
        public void write(@NotNull FriendlyByteBuf buf) {
            super.write(buf, (buffer, payload) -> {
                buffer.writeInt(payload.networkId);
                buffer.writeInt(payload.amount);
                buffer.writeInt(payload.capacity);
            });
        }

        @Override
        @NotNull
        public ResourceLocation id() {
            return ID;
        }
    }

    record Payload(int networkId, int amount, int capacity) {}
}
