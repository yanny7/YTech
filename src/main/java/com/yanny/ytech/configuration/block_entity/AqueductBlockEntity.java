package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block.AqueductBlock;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import com.yanny.ytech.network.irrigation.NetworkType;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AqueductBlockEntity extends IrrigationBlockEntity {
    @NotNull private final AABB renderBox;
    @Nullable private LazyOptional<IFluidHandler> lazyFluidHandler;

    public AqueductBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        super(YTechBlockEntityTypes.AQUEDUCT.get(), pos, blockState, ((AqueductBlock)blockState.getBlock()).getValidNeighbors(blockState, pos));
        renderBox = new AABB(pos, pos.offset(1, 1, 1));
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (level != null && !level.isClientSide) {
            IrrigationServerNetwork network = YTechMod.IRRIGATION_PROPAGATOR.server().getNetwork(this);

            if (network != null) {
                lazyFluidHandler = LazyOptional.of(network::getFluidHandler);
            }
        }
    }

    @Override
    public int getFlow() {
        return 0;
    }

    @Override
    public @NotNull NetworkType getNetworkType() {
        return NetworkType.STORAGE;
    }

    @Override
    public boolean validForRainFilling() {
        if (level instanceof ServerLevel) {
            Holder<Biome> biome = level.getBiome(worldPosition);
            return !YTechMod.CONFIGURATION.isValidBlockForRaining() || (level.canSeeSky(worldPosition.above())
                    && biome.get().getPrecipitationAt(worldPosition) == Biome.Precipitation.RAIN);
        }

        return false;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return renderBox;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (lazyFluidHandler != null) {
            return ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, lazyFluidHandler);
        } else {
            return LazyOptional.empty();
        }
    }

    public void onRandomTick() {
        YTechMod.IRRIGATION_PROPAGATOR.server().changed(this);
    }
}
