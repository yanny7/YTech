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
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class IrrigationUtils {
    public static YTechMod.DistHolder<ClientPropagator<IrrigationClientNetwork, IIrrigationBlockEntity>, ServerPropagator<IrrigationServerNetwork, IIrrigationBlockEntity>> registerIrrigationPropagator(PayloadRegistrar channel) {
        ServerPropagator<IrrigationServerNetwork, IIrrigationBlockEntity> server = new ServerPropagator<>(new Factory(), "irrigation");
        IrrigationClientPropagator client;

        if (FMLEnvironment.dist == Dist.CLIENT) {
            client = new IrrigationClientPropagator();
        } else {
            client = null;
        }

        channel.playToClient(MyLevelSyncMessage.TYPE, MyLevelSyncMessage.CODEC, (msg, ctx) -> {
            if (client != null) {
                client.onSyncLevel(msg, ctx);
            }
        });
        channel.playToClient(MyNetworkUpdatedMessage.TYPE, MyNetworkUpdatedMessage.CODEC, (msg, ctx) -> {
            if (client != null) {
                client.onNetworkAddedOrUpdated(msg, ctx);
            }
        });
        channel.playToClient(MyNetworkRemoveMessage.TYPE, MyNetworkRemoveMessage.CODEC, (msg, ctx) -> {
            if (client != null) {
                client.onNetworkRemoved(msg, ctx);
            }
        });

        return new YTechMod.DistHolder<>(client, server);
    }

    private static class IrrigationClientPropagator extends ClientPropagator<IrrigationClientNetwork, IIrrigationBlockEntity> {
        public IrrigationClientPropagator() {
            super("irrigation");
        }
        public void onSyncLevel(@NotNull IrrigationUtils.MyLevelSyncMessage msg, @NotNull IPayloadContext context) {
            context.enqueueWork(() -> syncLevel(msg.networkMap.entrySet().stream().map((entry) -> {
                Payload payload = entry.getValue();
                return new IrrigationClientNetwork(entry.getKey(), payload.amount, payload.capacity);
            }).collect(Collectors.toMap(CommonNetwork::getNetworkId, (b) -> b))));
        }

        public void onNetworkAddedOrUpdated(@NotNull IrrigationUtils.MyNetworkUpdatedMessage msg, @NotNull IPayloadContext context) {
            context.enqueueWork(() -> {
                IrrigationUtils.Payload payload = msg.payload;
                addOrUpdateNetwork(new IrrigationClientNetwork(payload.networkId, payload.amount, payload.capacity));
            });
        }

        public void onNetworkRemoved(@NotNull IrrigationUtils.MyNetworkRemoveMessage msg, @NotNull IPayloadContext context) {
            context.enqueueWork(() -> deletedNetwork(msg.networkId));
        }
    }

    private static class Factory implements NetworkFactory<IrrigationServerNetwork, IIrrigationBlockEntity> {
        @Override
        public @NotNull IrrigationServerNetwork createNetwork(@NotNull CompoundTag tag, int networkId, @NotNull Consumer<Integer> onChange,
                                                              @NotNull BiConsumer<Integer, ChunkPos> onRemove, HolderLookup.Provider provider) {
            return new IrrigationServerNetwork(tag, networkId, onChange, onRemove, provider);
        }

        @Override
        public @NotNull IrrigationServerNetwork createNetwork(int networkId, @NotNull Consumer<Integer> onChange, @NotNull BiConsumer<Integer, ChunkPos> onRemove) {
            return new IrrigationServerNetwork(networkId, onChange, onRemove);
        }

        @Override
        public void sendRemoved(@NotNull ServerPlayer player, int networkId) {
            PacketDistributor.sendToPlayer(player, new IrrigationUtils.MyNetworkRemoveMessage(networkId));
        }

        @Override
        public void sendUpdated(@NotNull ServerPlayer player, @NotNull IrrigationServerNetwork network) {
            PacketDistributor.sendToPlayer(player, new IrrigationUtils.MyNetworkUpdatedMessage(
                    new IrrigationUtils.Payload(network.getNetworkId(), network.getFluidHandler().getFluidAmount(), network.getFluidHandler().getCapacity())));
        }

        @Override
        public void sendLevelSync(@NotNull ServerPlayer player, @NotNull Map<Integer, IrrigationServerNetwork> networkMap) {
            PacketDistributor.sendToPlayer(player, new IrrigationUtils.MyLevelSyncMessage(networkMap.entrySet().stream().map((entry) -> {
                IrrigationServerNetwork network = entry.getValue();
                return new IrrigationUtils.Payload(entry.getKey(), network.getFluidHandler().getFluidAmount(), network.getFluidHandler().getCapacity());
            }).collect(Collectors.toMap((a) -> a.networkId, (b) -> b))));
        }
    }

    static class MyNetworkRemoveMessage extends NetworkRemovedMessage implements CustomPacketPayload {
        public static final Type<MyNetworkRemoveMessage> TYPE = new Type<>(Utils.modLoc("irrigation_network_removed"));
        public static final StreamCodec<RegistryFriendlyByteBuf, MyNetworkRemoveMessage> CODEC = new StreamCodec<>() {
            @NotNull
            @Override
            public MyNetworkRemoveMessage decode(@NotNull RegistryFriendlyByteBuf byteBuf) {
                return new MyNetworkRemoveMessage(byteBuf);
            }

            @Override
            public void encode(@NotNull RegistryFriendlyByteBuf byteBuf, @NotNull MyNetworkRemoveMessage message) {
                message.write(byteBuf);
            }
        };

        public MyNetworkRemoveMessage(int networkId) {
            super(networkId);
        }

        public MyNetworkRemoveMessage(@NotNull RegistryFriendlyByteBuf buf) {
            super(buf);
        }

        @NotNull
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    static class MyNetworkUpdatedMessage extends NetworkAddedOrUpdatedMessage<IrrigationUtils.Payload> implements CustomPacketPayload {
        public static final Type<MyNetworkUpdatedMessage> TYPE = new Type<>(Utils.modLoc("irrigation_network_updated"));
        public static final StreamCodec<RegistryFriendlyByteBuf, MyNetworkUpdatedMessage> CODEC = new StreamCodec<>() {
            @NotNull
            @Override
            public MyNetworkUpdatedMessage decode(@NotNull RegistryFriendlyByteBuf buf) {
                return new MyNetworkUpdatedMessage(buf);
            }

            @Override
            public void encode(@NotNull RegistryFriendlyByteBuf buf, @NotNull MyNetworkUpdatedMessage message) {
                message.write(buf);
            }
        };

        public MyNetworkUpdatedMessage(@NotNull IrrigationUtils.Payload payload) {
            super(payload);
        }

        public MyNetworkUpdatedMessage(@NotNull RegistryFriendlyByteBuf buf) {
            super(buf, (buffer) -> new IrrigationUtils.Payload(buffer.readInt(), buffer.readInt(), buffer.readInt()));
        }

        public void write(@NotNull RegistryFriendlyByteBuf buf) {
            super.write(buf, (buffer, payload) -> {
                buffer.writeInt(payload.networkId);
                buffer.writeInt(payload.amount);
                buffer.writeInt(payload.capacity);
            });
        }

        @NotNull
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    static class MyLevelSyncMessage extends LevelSyncMessage<IrrigationUtils.Payload> implements CustomPacketPayload {
        public static final Type<MyLevelSyncMessage> TYPE = new Type<>(Utils.modLoc("irrigation_level_sync"));
        public static final StreamCodec<RegistryFriendlyByteBuf, MyLevelSyncMessage> CODEC = new StreamCodec<>() {
            @NotNull
            @Override
            public MyLevelSyncMessage decode(@NotNull RegistryFriendlyByteBuf buf) {
                return new MyLevelSyncMessage(buf);
            }

            @Override
            public void encode(@NotNull RegistryFriendlyByteBuf buf, @NotNull MyLevelSyncMessage message) {
                message.write(buf);
            }
        };

        public MyLevelSyncMessage(@NotNull Map<Integer, IrrigationUtils.Payload> networkMap) {
            super(networkMap);
        }

        public MyLevelSyncMessage(@NotNull RegistryFriendlyByteBuf buf) {
            super(buf, (buffer) -> new IrrigationUtils.Payload(buffer.readInt(), buffer.readInt(), buffer.readInt()));
        }

        public void write(@NotNull RegistryFriendlyByteBuf buf) {
            super.write(buf, (buffer, payload) -> {
                buffer.writeInt(payload.networkId);
                buffer.writeInt(payload.amount);
                buffer.writeInt(payload.capacity);
            });
        }

        @NotNull
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    record Payload(int networkId, int amount, int capacity) {}
}
