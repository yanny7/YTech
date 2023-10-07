package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.YTechMod;
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
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AqueductBlockEntity extends IrrigationBlockEntity {
    @NotNull private final AABB renderBox;
    @NotNull private final List<BlockPos> validNeighbors;
    @Nullable private LazyOptional<IFluidHandler> lazyFluidHandler;

    public AqueductBlockEntity(@NotNull BlockEntityType<? extends BlockEntity> entityType, @NotNull BlockPos pos, @NotNull BlockState blockState) {
        super(entityType, pos, blockState);
        validNeighbors = Direction.Plane.HORIZONTAL.stream().map((dir) -> pos.offset(dir.getNormal())).toList();
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
    public @NotNull List<BlockPos> getValidNeighbors() {
        return validNeighbors;
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
            return !YTechMod.CONFIGURATION.isValidBlockForRaining() || (level.isRainingAt(worldPosition.above())
                    && biome.get().warmEnoughToRain(worldPosition) && !biome.is(Tags.Biomes.IS_DRY));
        }

        return false;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return renderBox;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, lazyFluidHandler);
    }

    public void onRandomTick() {
        YTechMod.IRRIGATION_PROPAGATOR.server().changed(this);
    }
}
