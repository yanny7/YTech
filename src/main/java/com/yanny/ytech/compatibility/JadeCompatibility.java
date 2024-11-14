package com.yanny.ytech.compatibility;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block.*;
import com.yanny.ytech.configuration.block_entity.*;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

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
                IElementHelper elements = iTooltip.getElementHelper();
                iTooltip.add(elements.item(blockEntity.getItemStackHandler().getStackInSlot(0)));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return YTechBlocks.BRONZE_ANVIL.getId();
        }
    }

    private static class DryingRackProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
        private static final DryingRackProvider INSTANCE = new DryingRackProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            if (blockAccessor.getServerData().contains("Progress")) {
                iTooltip.add(Component.translatable("text.ytech.top.drying_rack.progress", blockAccessor.getServerData().get("Progress")));
            }
        }

        @Override
        public void appendServerData(CompoundTag compoundTag, BlockAccessor accessor) {
            DryingRackBlockEntity blockEntity = (DryingRackBlockEntity) accessor.getBlockEntity();

            if (!blockEntity.getItem().isEmpty()) {
                compoundTag.putInt("Progress", blockEntity.getProgress());
            }
        }

        @Override
        public ResourceLocation getUid() {
            return Utils.modLoc("drying_racks");
        }
    }

    private static class MillstoneProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
        private static final MillstoneProvider INSTANCE = new MillstoneProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            if (blockAccessor.getServerData().contains("Item")) {
                IElementHelper elements = iTooltip.getElementHelper();
                iTooltip.add(elements.item(ItemStack.of(blockAccessor.getServerData().getCompound("Item"))));
            }
        }

        @Override
        public void appendServerData(CompoundTag compoundTag, BlockAccessor accessor) {
            MillstoneBlockEntity blockEntity = (MillstoneBlockEntity) accessor.getBlockEntity();
            compoundTag.put("Item", blockEntity.getInputItem().save(new CompoundTag()));
        }

        @Override
        public ResourceLocation getUid() {
            return YTechBlocks.MILLSTONE.getId();
        }
    }

    private static class TanningRackProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
        private static final TanningRackProvider INSTANCE = new TanningRackProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            if (blockAccessor.getServerData().contains("Progress")) {
                iTooltip.add(Component.translatable("text.ytech.top.tanning_rack.progress", blockAccessor.getServerData().get("Progress")));
            }
        }

        @Override
        public void appendServerData(CompoundTag compoundTag, BlockAccessor accessor) {
            TanningRackBlockEntity blockEntity = (TanningRackBlockEntity) accessor.getBlockEntity();

            if (!blockEntity.getItem().isEmpty()) {
                compoundTag.putInt("Progress", blockEntity.getProgress());
            }
        }

        @Override
        public ResourceLocation getUid() {
            return Utils.modLoc("tanning_racks");
        }
    }

    private static class PrimitiveMachineProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
        private static final PrimitiveMachineProvider INSTANCE = new PrimitiveMachineProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor accessor, IPluginConfig iPluginConfig) {
            if (accessor.getServerData().contains("Progress")) {
                iTooltip.add(Component.translatable("text.ytech.top.smelter.progress", accessor.getServerData().get("Progress")));
            }
            if (accessor.getServerData().contains("Temperature")) {
                iTooltip.add(Component.translatable("text.ytech.top.smelter.temperature", accessor.getServerData().get("Temperature")));
            }
        }

        @Override
        public void appendServerData(CompoundTag compoundTag, BlockAccessor accessor) {
            AbstractPrimitiveMachineBlockEntity blockEntity = (AbstractPrimitiveMachineBlockEntity) accessor.getBlockEntity();

            if (blockEntity.hasActiveRecipe()) {
                compoundTag.putInt("Progress", blockEntity.progress());
            }

            compoundTag.putInt("Temperature", blockEntity.temperature());
        }

        @Override
        public ResourceLocation getUid() {
            return Utils.modLoc("primitive_machines");
        }
    }

    private static class IrrigationProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
        private static final IrrigationProvider INSTANCE = new IrrigationProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor accessor, IPluginConfig iPluginConfig) {
            if (accessor.getServerData().contains("Production")) {
                iTooltip.add(Component.translatable("text.ytech.top.irrigation.production", accessor.getServerData().get("Production")));
            }
            if (accessor.getServerData().contains("Hydrating")) {
                iTooltip.add(Component.translatable("text.ytech.top.irrigation.hydrating"));
            }
            if (accessor.getServerData().contains("Fertilizing")) {
                iTooltip.add(Component.translatable("text.ytech.top.irrigation.fertilizing"));
            }
            if (accessor.getServerData().contains("Amount") && accessor.getServerData().contains("Capacity")) {
                iTooltip.add(Component.translatable("text.ytech.top.irrigation.network", accessor.getServerData().get("Amount"), accessor.getServerData().get("Capacity")));
            }
        }

        @Override
        public void appendServerData(CompoundTag compoundTag, BlockAccessor accessor) {
            IrrigationBlockEntity blockEntity = (IrrigationBlockEntity) accessor.getBlockEntity();
            switch (blockEntity.getNetworkType()) {
                case PROVIDER -> compoundTag.putInt("Production", Math.round(blockEntity.getFlow() * (20 / (float) YTechMod.CONFIGURATION.getValveFillPerNthTick())));
                case CONSUMER -> {
                    if (blockEntity instanceof AqueductHydratorBlockEntity hydratorBlock && hydratorBlock.isHydrating()) {
                        compoundTag.putBoolean("Hydrating", true);
                    }
                    if (blockEntity instanceof AqueductFertilizerBlockEntity fertilizerBlock && fertilizerBlock.isFertilizing()) {
                        compoundTag.putBoolean("Fertilizing", true);
                    }
                }
            }

            IrrigationServerNetwork network = YTechMod.IRRIGATION_PROPAGATOR.server().getNetwork(blockEntity);

            if (network != null) {
                compoundTag.putInt("Amount", network.getFluidHandler().getFluidAmount());
                compoundTag.putInt("Capacity", network.getFluidHandler().getCapacity());
            }
        }

        @Override
        public ResourceLocation getUid() {
            return Utils.modLoc("irrigation");
        }
    }

    private static class PottersWheelProvider implements IBlockComponentProvider {
        private static final PottersWheelProvider INSTANCE = new PottersWheelProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            if (blockAccessor.getBlockEntity() instanceof PottersWheelBlockEntity blockEntity && !blockEntity.getItem().isEmpty()) {
                IElementHelper elements = iTooltip.getElementHelper();
                iTooltip.add(elements.item(blockEntity.getItem()));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return YTechBlocks.POTTERS_WHEEL.getId();
        }
    }

    private static class FirePitProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
        private static final FirePitProvider INSTANCE = new FirePitProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            if (blockAccessor.getServerData().contains("Progress")) {
                iTooltip.add(Component.translatable("text.ytech.top.fire_pit.progress", blockAccessor.getServerData().get("Progress")));
            }
        }

        @Override
        public void appendServerData(CompoundTag compoundTag, BlockAccessor accessor) {
            FirePitBlockEntity blockEntity = (FirePitBlockEntity) accessor.getBlockEntity();

            if (!blockEntity.getItem().isEmpty()) {
                compoundTag.putInt("Progress", blockEntity.getProgress());
            }
        }

        @Override
        public ResourceLocation getUid() {
            return YTechBlocks.FIRE_PIT.getId();
        }
    }

    private static class TreeStumpProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
        private static final TreeStumpProvider INSTANCE = new TreeStumpProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            if (blockAccessor.getServerData().contains("Progress")) {
                iTooltip.add(Component.translatable("text.ytech.top.tree_stump.progress", blockAccessor.getServerData().get("Progress")));
            }
        }

        @Override
        public void appendServerData(CompoundTag compoundTag, BlockAccessor accessor) {
            TreeStumpBlockEntity blockEntity = (TreeStumpBlockEntity) accessor.getBlockEntity();

            if (!blockEntity.getItem().isEmpty()) {
                compoundTag.putInt("Progress", blockEntity.getProgress());
            }
        }

        @Override
        public ResourceLocation getUid() {
            return YTechBlocks.TREE_STUMP.getId();
        }
    }

    private static class AmphoraProvider implements IBlockComponentProvider {
        private static final AmphoraProvider INSTANCE = new AmphoraProvider();

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            if (blockAccessor.getBlockEntity() instanceof AmphoraBlockEntity blockEntity && !blockEntity.getItem().isEmpty()) {
                IElementHelper elements = iTooltip.getElementHelper();
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
                IElementHelper elements = iTooltip.getElementHelper();
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
                IElementHelper elements = iTooltip.getElementHelper();
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
