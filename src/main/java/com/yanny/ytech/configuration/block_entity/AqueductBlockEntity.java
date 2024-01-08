package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block.AqueductBlock;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import com.yanny.ytech.network.irrigation.NetworkType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AqueductBlockEntity extends IrrigationBlockEntity {
    @Nullable private LazyOptional<IFluidHandler> lazyFluidHandler;

    public AqueductBlockEntity(@NotNull BlockEntityType<? extends BlockEntity> entityType, @NotNull BlockPos pos, @NotNull BlockState blockState) {
        super(entityType, pos, blockState, ((AqueductBlock)blockState.getBlock()).getValidNeighbors(blockState, pos));
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
                    && biome.value().getPrecipitationAt(worldPosition) == Biome.Precipitation.RAIN);
        }

        return false;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return Capabilities.FLUID_HANDLER.orEmpty(cap, lazyFluidHandler);
    }

    public void onRandomTick() {
        YTechMod.IRRIGATION_PROPAGATOR.server().changed(this);
    }
}
