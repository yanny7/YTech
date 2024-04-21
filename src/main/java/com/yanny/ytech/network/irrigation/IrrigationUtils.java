package com.yanny.ytech.network.irrigation;

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
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class IrrigationUtils {
    public static YTechMod.DistHolder<ClientPropagator<IrrigationClientNetwork, IIrrigationBlockEntity>, ServerPropagator<IrrigationServerNetwork, IIrrigationBlockEntity>> registerIrrigationPropagator(SimpleChannel channel) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return registerClientIrrigationPropagator(channel);
        } else {
            return registerServerIrrigationPropagator(channel);
        }
    }

    private static YTechMod.DistHolder<ClientPropagator<IrrigationClientNetwork, IIrrigationBlockEntity>, ServerPropagator<IrrigationServerNetwork, IIrrigationBlockEntity>> registerClientIrrigationPropagator(SimpleChannel channel) {
        IrrigationClientPropagator client = new IrrigationClientPropagator();
        ServerPropagator<IrrigationServerNetwork, IIrrigationBlockEntity> server = new ServerPropagator<>(new Factory(channel), "irrigation");

        channel.registerMessage(NetworkUtils.getMessageId(), MyLevelSyncMessage.class, MyLevelSyncMessage::encode, MyLevelSyncMessage::new, client::onSyncLevel);
        channel.registerMessage(NetworkUtils.getMessageId(), MyNetworkUpdatedMessage.class, MyNetworkUpdatedMessage::encode, MyNetworkUpdatedMessage::new, client::onNetworkAddedOrUpdated);
        channel.registerMessage(NetworkUtils.getMessageId(), MyNetworkRemoveMessage.class, MyNetworkRemoveMessage::encode, MyNetworkRemoveMessage::new, client::onNetworkRemoved);
        return new YTechMod.DistHolder<>(client, server);
    }

    private static YTechMod.DistHolder<ClientPropagator<IrrigationClientNetwork, IIrrigationBlockEntity>, ServerPropagator<IrrigationServerNetwork, IIrrigationBlockEntity>> registerServerIrrigationPropagator(SimpleChannel channel) {
        ServerPropagator<IrrigationServerNetwork, IIrrigationBlockEntity> server = new ServerPropagator<>(new Factory(channel), "irrigation");

        channel.registerMessage(NetworkUtils.getMessageId(), MyLevelSyncMessage.class, MyLevelSyncMessage::encode, MyLevelSyncMessage::new, (m, c) -> {});
        channel.registerMessage(NetworkUtils.getMessageId(), MyNetworkUpdatedMessage.class, MyNetworkUpdatedMessage::encode, MyNetworkUpdatedMessage::new, (m, c) -> {});
        channel.registerMessage(NetworkUtils.getMessageId(), MyNetworkRemoveMessage.class, MyNetworkRemoveMessage::encode, MyNetworkRemoveMessage::new, (m, c) -> {});
        return new YTechMod.DistHolder<>(null, server);
    }

    private static class IrrigationClientPropagator extends ClientPropagator<IrrigationClientNetwork, IIrrigationBlockEntity> {
        public IrrigationClientPropagator() {
            super("irrigation");
        }
        public void onSyncLevel(@NotNull IrrigationUtils.MyLevelSyncMessage msg, @NotNull Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> syncLevel(msg.networkMap.entrySet().stream().map((entry) -> {
                Payload payload = entry.getValue();
                return new IrrigationClientNetwork(entry.getKey(), payload.amount, payload.capacity);
            }).collect(Collectors.toMap(CommonNetwork::getNetworkId, (b) -> b))));
            context.setPacketHandled(true);
        }

        public void onNetworkAddedOrUpdated(@NotNull IrrigationUtils.MyNetworkUpdatedMessage msg, @NotNull Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                IrrigationUtils.Payload payload = msg.payload;
                addOrUpdateNetwork(new IrrigationClientNetwork(payload.networkId, payload.amount, payload.capacity));
            });
            context.setPacketHandled(true);
        }

        public void onNetworkRemoved(@NotNull IrrigationUtils.MyNetworkRemoveMessage msg, @NotNull Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> deletedNetwork(msg.networkId));
            context.setPacketHandled(true);
        }
    }

    private record Factory(@NotNull SimpleChannel channel) implements NetworkFactory<IrrigationServerNetwork, IIrrigationBlockEntity> {
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
            channel.send(target, new IrrigationUtils.MyNetworkRemoveMessage(networkId));
        }

        @Override
        public void sendUpdated(@NotNull PacketDistributor.PacketTarget target, @NotNull IrrigationServerNetwork network) {
            channel.send(target, new IrrigationUtils.MyNetworkUpdatedMessage(
                    new IrrigationUtils.Payload(network.getNetworkId(), network.getFluidHandler().getFluidAmount(), network.getFluidHandler().getCapacity())));
        }

        @Override
        public void sendLevelSync(@NotNull PacketDistributor.PacketTarget target, @NotNull Map<Integer, IrrigationServerNetwork> networkMap) {
            channel.send(target, new IrrigationUtils.MyLevelSyncMessage(networkMap.entrySet().stream().map((entry) -> {
                IrrigationServerNetwork network = entry.getValue();
                return new IrrigationUtils.Payload(entry.getKey(), network.getFluidHandler().getFluidAmount(), network.getFluidHandler().getCapacity());
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

    static class MyNetworkUpdatedMessage extends NetworkAddedOrUpdatedMessage<IrrigationUtils.Payload> {

        public MyNetworkUpdatedMessage(@NotNull IrrigationUtils.Payload payload) {
            super(payload);
        }

        public MyNetworkUpdatedMessage(@NotNull FriendlyByteBuf buf) {
            super(buf, (buffer) -> new IrrigationUtils.Payload(buffer.readInt(), buffer.readInt(), buffer.readInt()));
        }

        public void encode(@NotNull FriendlyByteBuf buf) {
            super.encode(buf, (buffer, payload) -> {
                buffer.writeInt(payload.networkId);
                buffer.writeInt(payload.amount);
                buffer.writeInt(payload.capacity);
            });
        }
    }

    static class MyLevelSyncMessage extends LevelSyncMessage<IrrigationUtils.Payload> {
        public MyLevelSyncMessage(@NotNull Map<Integer, IrrigationUtils.Payload> networkMap) {
            super(networkMap);
        }

        public MyLevelSyncMessage(@NotNull FriendlyByteBuf buf) {
            super(buf, (buffer) -> new IrrigationUtils.Payload(buffer.readInt(), buffer.readInt(), buffer.readInt()));
        }

        public void encode(@NotNull FriendlyByteBuf buf) {
            super.encode(buf, (buffer, payload) -> {
                buffer.writeInt(payload.networkId);
                buffer.writeInt(payload.amount);
                buffer.writeInt(payload.capacity);
            });
        }
    }

    record Payload(int networkId, int amount, int capacity) {}
}
