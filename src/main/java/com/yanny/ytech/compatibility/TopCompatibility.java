package com.yanny.ytech.compatibility;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.*;
import com.yanny.ytech.configuration.entity.AurochsEntity;
import com.yanny.ytech.configuration.entity.FowlEntity;
import com.yanny.ytech.configuration.entity.MouflonEntity;
import com.yanny.ytech.configuration.entity.WildBoarEntity;
import com.yanny.ytech.network.irrigation.IIrrigationBlockEntity;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import mcjty.theoneprobe.api.*;
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
                    return Utils.modLoc("theoneprobe");
                }

                @Override
                public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level level, BlockState blockState, IProbeHitData probeHitData) {
                    if (!level.isClientSide) {
                        BlockEntity entity = level.getBlockEntity(probeHitData.getPos());

                        if (entity instanceof BronzeAnvilBlockEntity blockEntity && blockEntity.getItemStackHandler().getStackInSlot(0).getCount() >= 0) {
                            probeInfo.horizontal().item(blockEntity.getItemStackHandler().getStackInSlot(0));
                        } else if (entity instanceof DryingRackBlockEntity blockEntity && blockEntity.getDryingLeft() >= 0) {
                            probeInfo.horizontal().text("Remaining: ").text(Integer.toString(Math.round(blockEntity.getDryingLeft() / 20f))).text("s");
                        } else if (entity instanceof MillstoneBlockEntity blockEntity && !blockEntity.getInputItem().isEmpty()) {
                            probeInfo.horizontal().item(blockEntity.getInputItem());
                        } else if (entity instanceof TanningRackBlockEntity blockEntity && blockEntity.getHitLeft() > 0) {
                            probeInfo.horizontal().text("Hit left: ").text(Integer.toString(blockEntity.getHitLeft())).text(" times");
                        } else if (entity instanceof AbstractPrimitiveMachineBlockEntity blockEntity) {
                            addPrimitiveSmelterInfo(probeInfo, blockEntity);
                        } else if (entity instanceof IIrrigationBlockEntity blockEntity) {
                            addIrrigationInfo(probeInfo, blockEntity);
                        } else if (entity instanceof PottersWheelBlockEntity blockEntity) {
                            probeInfo.horizontal().item(blockEntity.getItem());
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
                            iProbeInfo.horizontal().text("Generation: " + aurochsEntity.getGeneration());
                        } else if (entity instanceof MouflonEntity mouflonEntity) {
                            iProbeInfo.horizontal().text("Generation: " + mouflonEntity.getGeneration());
                        } else if (entity instanceof FowlEntity fowlEntity) {
                            iProbeInfo.horizontal().text("Generation: " + fowlEntity.getGeneration());
                        } else if (entity instanceof WildBoarEntity wildBoarEntity) {
                            iProbeInfo.horizontal().text("Generation: " + wildBoarEntity.getGeneration());
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
                probeInfo.horizontal().text("Producing: ").text(Integer.toString(perSec)).text(" mb/s");
            }
            case CONSUMER -> {
                if (blockEntity instanceof AqueductHydratorBlockEntity hydratorBlock && hydratorBlock.isHydrating()) {
                    probeInfo.horizontal().text("Hydrating");
                }
                if (blockEntity instanceof AqueductFertilizerBlockEntity fertilizerBlock && fertilizerBlock.isFertilizing()) {
                    probeInfo.horizontal().text("Fertilizing");
                }
            }
        }

        IrrigationServerNetwork network = YTechMod.IRRIGATION_PROPAGATOR.server().getNetwork(blockEntity);

        if (network != null) {
            probeInfo.horizontal().text("Network: ").text(Integer.toString(network.getFluidHandler().getFluidAmount())).text("/")
                    .text(Integer.toString(network.getFluidHandler().getCapacity()));
        }
    }

    private static void addPrimitiveSmelterInfo(IProbeInfo probeInfo, AbstractPrimitiveMachineBlockEntity blockEntity) {
        IProbeInfo verticalLayout = probeInfo.vertical();

        if (blockEntity.hasActiveRecipe()) {
            verticalLayout.horizontal().text("Progress: ").horizontal().text(Integer.toString(blockEntity.progress())).text("%");
        }

        verticalLayout.horizontal().text("Temperature: ").horizontal().text(Integer.toString(blockEntity.temperature())).text("°C");
    }
}
