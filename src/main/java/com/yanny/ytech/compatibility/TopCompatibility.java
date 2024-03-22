package com.yanny.ytech.compatibility;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block_entity.*;
import com.yanny.ytech.network.irrigation.IIrrigationBlockEntity;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import com.yanny.ytech.network.kinetic.IKineticBlockEntity;
import com.yanny.ytech.network.kinetic.KineticServerNetwork;
import mcjty.theoneprobe.api.*;
import net.minecraft.resources.ResourceLocation;
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
                        } else if (entity instanceof DryingRackBlockEntity blockEntity && blockEntity.getDryingLeft() >= 0) {
                            probeInfo.horizontal().text("Remaining: ").text(Integer.toString(Math.round(blockEntity.getDryingLeft() / 20f))).text("s");
                        } else if (entity instanceof IKineticBlockEntity blockEntity) {
                            addKineticInfo(probeInfo, blockEntity);
                        } else if (entity instanceof MillstoneBlockEntity blockEntity && !blockEntity.getInputItem().isEmpty()) {
                            probeInfo.horizontal().item(blockEntity.getInputItem());
                        } else if (entity instanceof TanningRackBlockEntity blockEntity && blockEntity.getHitLeft() > 0) {
                            probeInfo.horizontal().text("Hit left: ").text(Integer.toString(blockEntity.getHitLeft())).text(" times");
                        } else if (entity instanceof AbstractPrimitiveMachineBlockEntity blockEntity) {
                            addPrimitiveSmelterInfo(probeInfo, blockEntity);
                        } else if (entity instanceof IIrrigationBlockEntity blockEntity) {
                            addIrrigationInfo(probeInfo, blockEntity);
                        }
                    }
                }
            });
            return null;
        }
    }

    private static void addKineticInfo(IProbeInfo probeInfo, IKineticBlockEntity blockEntity) {
        switch (blockEntity.getNetworkType()) {
            case PROVIDER -> probeInfo.horizontal().text("Producing: ").text(Integer.toString(blockEntity.getStress())).text(" units");
            case CONSUMER -> probeInfo.horizontal().text("Consuming: ").text(Integer.toString(blockEntity.getStress())).text(" units");
        }

        KineticServerNetwork network = YTechMod.KINETIC_PROPAGATOR.server().getNetwork(blockEntity);

        if (network != null) {
            probeInfo.horizontal().text("Network: ").text(Integer.toString(network.getStress())).text("/").text(Integer.toString(network.getStressCapacity()));
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
