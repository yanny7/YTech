package com.yanny.ytech.compatibility;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block.*;
import com.yanny.ytech.configuration.block_entity.*;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

import java.util.Optional;

@WailaPlugin
public class JadeCompatibility implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(DryingRackProvider.INSTANCE, DryingRackBlockEntity.class);
        registration.registerBlockDataProvider(MillstoneProvider.INSTANCE, MillstoneBlockEntity.class);
        registration.registerBlockDataProvider(TanningRackProvider.INSTANCE, TanningRackBlockEntity.class);
        registration.registerBlockDataProvider(PrimitiveMachineProvider.INSTANCE, AbstractPrimitiveMachineBlockEntity.class);
        registration.registerBlockDataProvider(IrrigationProvider.INSTANCE, IrrigationBlockEntity.class);
        registration.registerBlockDataProvider(FirePitProvider.INSTANCE, FirePitBlockEntity.class);
        registration.registerBlockDataProvider(TreeStumpProvider.INSTANCE, TreeStumpBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(BronzeAnvilProvider.INSTANCE, BronzeAnvilBlock.class);
        registration.registerBlockComponent(DryingRackProvider.INSTANCE, DryingRackBlock.class);
        registration.registerBlockComponent(MillstoneProvider.INSTANCE, MillstoneBlock.class);
        registration.registerBlockComponent(TanningRackProvider.INSTANCE, TanningRackBlock.class);
        registration.registerBlockComponent(PrimitiveMachineProvider.INSTANCE, AbstractPrimitiveMachineBlock.class);
        registration.registerBlockComponent(IrrigationProvider.INSTANCE, IrrigationBlock.class);
        registration.registerBlockComponent(PottersWheelProvider.INSTANCE, PottersWheelBlock.class);
        registration.registerBlockComponent(FirePitProvider.INSTANCE, FirePitBlock.class);
        registration.registerBlockComponent(TreeStumpProvider.INSTANCE, TreeStumpBlock.class);
        registration.registerBlockComponent(AmphoraProvider.INSTANCE, AmphoraBlock.class);
        registration.registerBlockComponent(WoodenBoxProvider.INSTANCE, WoodenBoxBlock.class);
        registration.registerBlockComponent(ToolRackProvider.INSTANCE, ToolRackBlock.class);
    }

    private static class BronzeAnvilProvider implements IBlockComponentProvider {
        private static final BronzeAnvilProvider INSTANCE = new BronzeAnvilProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            if (blockAccessor.getBlockEntity() instanceof BronzeAnvilBlockEntity blockEntity && blockEntity.getItemStackHandler().getStackInSlot(0).getCount() >= 0) {
                IElementHelper elements = IElementHelper.get();
                iTooltip.add(elements.item(blockEntity.getItemStackHandler().getStackInSlot(0)));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return YTechBlocks.BRONZE_ANVIL.getId();
        }
    }

    private static class DryingRackProvider implements IBlockComponentProvider, StreamServerDataProvider<BlockAccessor, DryingRackProvider.Data> {
        private static final DryingRackProvider INSTANCE = new DryingRackProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            decodeFromData(blockAccessor).ifPresent(data -> iTooltip.add(Component.translatable("text.ytech.top.drying_rack.progress", data.progress)));

        }

        @Override
        public @Nullable Data streamData(BlockAccessor accessor) {
            DryingRackBlockEntity blockEntity = (DryingRackBlockEntity)accessor.getBlockEntity();

            if (!blockEntity.getItem().isEmpty()) {
                return new Data(blockEntity.getProgress());
            } else {
                return null;
            }
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, Data> streamCodec() {
            return Data.STREAM_CODEC.cast();
        }

        @Override
        public ResourceLocation getUid() {
            return Utils.modLoc("drying_racks");
        }

        private record Data(int progress) {
            private static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, Data::progress, Data::new
            );
        }
    }

    private static class MillstoneProvider implements IBlockComponentProvider, StreamServerDataProvider<BlockAccessor, MillstoneProvider.Data> {
        private static final MillstoneProvider INSTANCE = new MillstoneProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            decodeFromData(blockAccessor).ifPresent((data) -> {
                if (!data.item.isEmpty()) {
                    IElementHelper elements = IElementHelper.get();
                    iTooltip.add(elements.item(data.item));
                }
            });
        }

        @Override
        public @Nullable Data streamData(BlockAccessor accessor) {
            MillstoneBlockEntity blockEntity = (MillstoneBlockEntity) accessor.getBlockEntity();
            return new Data(blockEntity.getInputItem());
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, Data> streamCodec() {
            return Data.STREAM_CODEC.cast();
        }

        @Override
        public ResourceLocation getUid() {
            return YTechBlocks.MILLSTONE.getId();
        }

        private record Data(ItemStack item) {
            private static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
                    ItemStack.OPTIONAL_STREAM_CODEC, Data::item, Data::new
            );
        }
    }

    private static class TanningRackProvider implements IBlockComponentProvider, StreamServerDataProvider<BlockAccessor, TanningRackProvider.Data> {
        private static final TanningRackProvider INSTANCE = new TanningRackProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            decodeFromData(blockAccessor).ifPresent(data -> iTooltip.add(Component.translatable("text.ytech.top.tanning_rack.progress", data.progress)));
        }

        @Override
        public @Nullable Data streamData(BlockAccessor accessor) {
            TanningRackBlockEntity blockEntity = (TanningRackBlockEntity)accessor.getBlockEntity();

            if (!blockEntity.getItem().isEmpty()) {
                return new Data(blockEntity.getProgress());
            } else {
                return null;
            }
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, Data> streamCodec() {
            return Data.STREAM_CODEC.cast();
        }

        @Override
        public ResourceLocation getUid() {
            return Utils.modLoc("tanning_racks");
        }

        private record Data(int progress) {
            private static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, Data::progress, Data::new
            );
        }
    }

    private static class PrimitiveMachineProvider implements IBlockComponentProvider, StreamServerDataProvider<BlockAccessor, PrimitiveMachineProvider.Data> {
        private static final PrimitiveMachineProvider INSTANCE = new PrimitiveMachineProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor accessor, IPluginConfig iPluginConfig) {
            decodeFromData(accessor).ifPresent((data) -> {
                if (data.activeRecipe) {
                    iTooltip.add(Component.translatable("text.ytech.top.smelter.progress", data.progress));
                }

                iTooltip.add(Component.translatable("text.ytech.top.smelter.temperature", data.temperature));
            });
        }

        @Override
        public @Nullable Data streamData(BlockAccessor accessor) {
            AbstractPrimitiveMachineBlockEntity blockEntity = (AbstractPrimitiveMachineBlockEntity)accessor.getBlockEntity();
            return new Data(blockEntity.progress(), blockEntity.temperature(), blockEntity.hasActiveRecipe());
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, Data> streamCodec() {
            return Data.STREAM_CODEC.cast();
        }

        @Override
        public ResourceLocation getUid() {
            return Utils.modLoc("primitive_machines");
        }

        private record Data(int progress, int temperature, boolean activeRecipe) {
            private static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, Data::progress, ByteBufCodecs.VAR_INT, Data::temperature, ByteBufCodecs.BOOL, Data::activeRecipe, Data::new
            );
        }
    }

    private static class IrrigationProvider implements IBlockComponentProvider, StreamServerDataProvider<BlockAccessor, IrrigationProvider.Data> {
        private static final IrrigationProvider INSTANCE = new IrrigationProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor accessor, IPluginConfig iPluginConfig) {
            decodeFromData(accessor).ifPresent((data) -> {
                data.production.ifPresent((value) -> iTooltip.add(Component.translatable("text.ytech.top.irrigation.production", value)));
                data.hydrating.ifPresent((x) -> iTooltip.add(Component.translatable("text.ytech.top.irrigation.hydrating")));
                data.fertilizing.ifPresent((x) -> iTooltip.add(Component.translatable("text.ytech.top.irrigation.fertilizing")));

                if (data.amount.isPresent() && data.capacity.isPresent()) {
                    iTooltip.add(Component.translatable("text.ytech.top.irrigation.network", data.amount.get(), data.capacity.get()));
                }
            });
        }

        @Override
        public @Nullable Data streamData(BlockAccessor accessor) {
            IrrigationBlockEntity blockEntity = (IrrigationBlockEntity) accessor.getBlockEntity();
            Optional<Integer> production = Optional.empty();
            Optional<Boolean> hydrating = Optional.empty();
            Optional<Boolean> fertilizing = Optional.empty();
            Optional<Integer> amount = Optional.empty();
            Optional<Integer> capacity = Optional.empty();

            switch (blockEntity.getNetworkType()) {
                case PROVIDER -> production = Optional.of(Math.round(blockEntity.getFlow() * (20 / (float) YTechMod.CONFIGURATION.getValveFillPerNthTick())));
                case CONSUMER -> {
                    if (blockEntity instanceof AqueductHydratorBlockEntity hydratorBlock && hydratorBlock.isHydrating()) {
                        hydrating = Optional.of(true);
                    }
                    if (blockEntity instanceof AqueductFertilizerBlockEntity fertilizerBlock && fertilizerBlock.isFertilizing()) {
                        fertilizing = Optional.of(true);
                    }
                }
            }

            IrrigationServerNetwork network = YTechMod.IRRIGATION_PROPAGATOR.server().getNetwork(blockEntity);

            if (network != null) {
                amount = Optional.of(network.getFluidHandler().getFluidAmount());
                capacity = Optional.of(network.getFluidHandler().getCapacity());
            }

            return new Data(production, hydrating, fertilizing, amount, capacity);
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, Data> streamCodec() {
            return Data.STREAM_CODEC.cast();
        }

        @Override
        public ResourceLocation getUid() {
            return Utils.modLoc("irrigation");
        }

        private record Data(Optional<Integer> production, Optional<Boolean> hydrating, Optional<Boolean> fertilizing, Optional<Integer> amount, Optional<Integer> capacity) {
            private static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), Data::production,
                    ByteBufCodecs.optional(ByteBufCodecs.BOOL), Data::hydrating,
                    ByteBufCodecs.optional(ByteBufCodecs.BOOL), Data::fertilizing,
                    ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), Data::amount,
                    ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), Data::capacity,
                    Data::new
            );
        }
    }

    private static class PottersWheelProvider implements IBlockComponentProvider {
        private static final PottersWheelProvider INSTANCE = new PottersWheelProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            if (blockAccessor.getBlockEntity() instanceof PottersWheelBlockEntity blockEntity && !blockEntity.getItem().isEmpty()) {
                IElementHelper elements = IElementHelper.get();
                iTooltip.add(elements.item(blockEntity.getItem()));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return YTechBlocks.POTTERS_WHEEL.getId();
        }
    }

    private static class FirePitProvider implements IBlockComponentProvider, StreamServerDataProvider<BlockAccessor, FirePitProvider.Data> {
        private static final FirePitProvider INSTANCE = new FirePitProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            decodeFromData(blockAccessor).ifPresent((data) -> iTooltip.add(Component.translatable("text.ytech.top.fire_pit.progress", data.progress)));
        }

        @Override
        public @Nullable Data streamData(BlockAccessor accessor) {
            FirePitBlockEntity blockEntity = (FirePitBlockEntity)accessor.getBlockEntity();

            if (!blockEntity.getItem().isEmpty()) {
                return new Data(blockEntity.getProgress());
            } else {
                return null;
            }
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, Data> streamCodec() {
            return Data.STREAM_CODEC.cast();
        }

        @Override
        public ResourceLocation getUid() {
            return YTechBlocks.FIRE_PIT.getId();
        }

        private record Data(int progress) {
            private static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, Data::progress, Data::new
            );
        }
    }

    private static class TreeStumpProvider implements IBlockComponentProvider, StreamServerDataProvider<BlockAccessor, TreeStumpProvider.Data> {
        private static final TreeStumpProvider INSTANCE = new TreeStumpProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            decodeFromData(blockAccessor).ifPresent((data) -> iTooltip.add(Component.translatable("text.ytech.top.tree_stump.progress", data.progress)));
        }

        @Override
        public @Nullable Data streamData(BlockAccessor accessor) {
            TreeStumpBlockEntity blockEntity = (TreeStumpBlockEntity) accessor.getBlockEntity();

            if (!blockEntity.getItem().isEmpty()) {
                return new Data(blockEntity.getProgress());
            } else {
                return null;
            }
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, Data> streamCodec() {
            return Data.STREAM_CODEC.cast();
        }

        @Override
        public ResourceLocation getUid() {
            return YTechBlocks.TREE_STUMP.getId();
        }

        private record Data(int progress) {
            private static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, Data::progress, Data::new
            );
        }
    }

    private static class AmphoraProvider implements IBlockComponentProvider {
        private static final AmphoraProvider INSTANCE = new AmphoraProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            if (blockAccessor.getBlockEntity() instanceof AmphoraBlockEntity blockEntity && !blockEntity.getItem().isEmpty()) {
                IElementHelper elements = IElementHelper.get();
                iTooltip.add(elements.item(blockEntity.getItem()));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return YTechBlocks.AMPHORA.getId();
        }
    }

    private static class WoodenBoxProvider implements IBlockComponentProvider {
        private static final WoodenBoxProvider INSTANCE = new WoodenBoxProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            if (blockAccessor.getBlockEntity() instanceof WoodenBoxBlockEntity blockEntity) {
                IElementHelper elements = IElementHelper.get();
                ItemStack item = blockEntity.getItem(blockAccessor.getPosition(), blockAccessor.getSide(), blockAccessor.getHitResult().getLocation());

                if (!item.isEmpty()) {
                    iTooltip.add(elements.item(item));
                    iTooltip.append(Component.translatable(item.getDescriptionId()));
                }
            }
        }

        @Override
        public ResourceLocation getUid() {
            return YTechBlocks.WOODEN_BOX.getId();
        }
    }

    private static class ToolRackProvider implements IBlockComponentProvider {
        private static final ToolRackProvider INSTANCE = new ToolRackProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            if (blockAccessor.getBlockEntity() instanceof ToolRackBlockEntity blockEntity) {
                IElementHelper elements = IElementHelper.get();
                ItemStack item = blockEntity.getItem(blockAccessor.getPosition(), blockAccessor.getSide(), blockAccessor.getHitResult().getLocation());

                if (!item.isEmpty()) {
                    iTooltip.add(elements.item(item));
                    iTooltip.append(Component.translatable(item.getDescriptionId()));
                }
            }
        }

        @Override
        public ResourceLocation getUid() {
            return YTechBlocks.TOOL_RACK.getId();
        }
    }
}
