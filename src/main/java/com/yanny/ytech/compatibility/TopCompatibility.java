package com.yanny.ytech.compatibility;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block_entity.*;
import com.yanny.ytech.configuration.entity.AurochsEntity;
import com.yanny.ytech.configuration.entity.FowlEntity;
import com.yanny.ytech.configuration.entity.MouflonEntity;
import com.yanny.ytech.configuration.entity.WildBoarEntity;
import com.yanny.ytech.network.irrigation.IIrrigationBlockEntity;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import mcjty.theoneprobe.api.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class TopCompatibility {
    public static void register() {
        if (!ModList.get().isLoaded("theoneprobe")) {
            return;
        }
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTheOneProbe::new);
    }

    public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {
        public static ITheOneProbe probe;

        @Nullable
        @Override
        public Void apply(ITheOneProbe theOneProbe) {
            probe = theOneProbe;
            probe.registerProvider(new IProbeInfoProvider() {
                @Override
                public ResourceLocation getID() {
                    return new ResourceLocation(YTechMod.MOD_ID , "theoneprobe");
                }

                @Override
                public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level level, BlockState blockState, IProbeHitData probeHitData) {
                    if (!level.isClientSide) {
                        BlockEntity entity = level.getBlockEntity(probeHitData.getPos());

                        if (entity instanceof BronzeAnvilBlockEntity blockEntity && blockEntity.getItemStackHandler().getStackInSlot(0).getCount() >= 0) {
                            probeInfo.horizontal().item(blockEntity.getItemStackHandler().getStackInSlot(0));
                        } else if (entity instanceof DryingRackBlockEntity blockEntity && !blockEntity.getItem().isEmpty()) {
                            probeInfo.horizontal().text(Component.translatable("text.ytech.top.drying_rack.progress", Integer.toString(blockEntity.getProgress())));
                        } else if (entity instanceof MillstoneBlockEntity blockEntity && !blockEntity.getInputItem().isEmpty()) {
                            probeInfo.horizontal().item(blockEntity.getInputItem());
                        } else if (entity instanceof TanningRackBlockEntity blockEntity && !blockEntity.getItem().isEmpty()) {
                            probeInfo.horizontal().text(Component.translatable("text.ytech.top.tanning_rack.progress", Integer.toString(blockEntity.getProgress())));
                        } else if (entity instanceof AbstractPrimitiveMachineBlockEntity blockEntity) {
                            addPrimitiveSmelterInfo(probeInfo, blockEntity);
                        } else if (entity instanceof IIrrigationBlockEntity blockEntity) {
                            addIrrigationInfo(probeInfo, blockEntity);
                        } else if (entity instanceof PottersWheelBlockEntity blockEntity) {
                            probeInfo.horizontal().item(blockEntity.getItem());
                        } else if (entity instanceof FirePitBlockEntity blockEntity && !blockEntity.getItem().isEmpty()) {
                            probeInfo.horizontal().text(Component.translatable("text.ytech.top.fire_pit.progress", Integer.toString(blockEntity.getProgress())));
                        }
                    }
                }
            });
            probe.registerEntityProvider(new IProbeInfoEntityProvider() {
                @Override
                public String getID() {
                    return YTechMod.MOD_ID;
                }

                @Override
                public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player player, Level level, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
                    if (!level.isClientSide) {
                        if (entity instanceof AurochsEntity aurochsEntity) {
                            iProbeInfo.horizontal().text(Component.translatable("text.ytech.top.entity.generation", aurochsEntity.getGeneration()));
                        } else if (entity instanceof MouflonEntity mouflonEntity) {
                            iProbeInfo.horizontal().text(Component.translatable("text.ytech.top.entity.generation", mouflonEntity.getGeneration()));
                        } else if (entity instanceof FowlEntity fowlEntity) {
                            iProbeInfo.horizontal().text(Component.translatable("text.ytech.top.entity.generation", fowlEntity.getGeneration()));
                        } else if (entity instanceof WildBoarEntity wildBoarEntity) {
                            iProbeInfo.horizontal().text(Component.translatable("text.ytech.top.entity.generation", wildBoarEntity.getGeneration()));
                        }
                    }
                }
            });
            return null;
        }
    }

    private static void addIrrigationInfo(IProbeInfo probeInfo, IIrrigationBlockEntity blockEntity) {
        switch (blockEntity.getNetworkType()) {
            case PROVIDER -> {
                int perSec = Math.round(blockEntity.getFlow() * (20 / (float) YTechMod.CONFIGURATION.getValveFillPerNthTick()));
                probeInfo.horizontal().text(Component.translatable("text.ytech.top.irrigation.production", Integer.toString(perSec)));
            }
            case CONSUMER -> {
                if (blockEntity instanceof AqueductHydratorBlockEntity hydratorBlock && hydratorBlock.isHydrating()) {
                    probeInfo.horizontal().text(Component.translatable("text.ytech.top.irrigation.hydrating"));
                }
                if (blockEntity instanceof AqueductFertilizerBlockEntity fertilizerBlock && fertilizerBlock.isFertilizing()) {
                    probeInfo.horizontal().text(Component.translatable("text.ytech.top.irrigation.fertilizing"));
                }
            }
        }

        IrrigationServerNetwork network = YTechMod.IRRIGATION_PROPAGATOR.server().getNetwork(blockEntity);

        if (network != null) {
            probeInfo.horizontal().text(Component.translatable("text.ytech.top.irrigation.network", Integer.toString(network.getFluidHandler().getFluidAmount()),
                    Integer.toString(network.getFluidHandler().getCapacity())));
        }
    }

    private static void addPrimitiveSmelterInfo(IProbeInfo probeInfo, AbstractPrimitiveMachineBlockEntity blockEntity) {
        IProbeInfo verticalLayout = probeInfo.vertical();

        if (blockEntity.hasActiveRecipe()) {
            verticalLayout.horizontal().text(Component.translatable("text.ytech.top.smelter.progress", Integer.toString(blockEntity.progress())));
        }

        verticalLayout.horizontal().text(Component.translatable("text.ytech.top.smelter.temperature", Integer.toString(blockEntity.temperature())));
    }
}
