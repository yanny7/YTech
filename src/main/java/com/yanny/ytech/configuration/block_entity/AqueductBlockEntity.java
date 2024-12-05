package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block.AqueductBlock;
import com.yanny.ytech.network.irrigation.NetworkType;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AqueductBlockEntity extends IrrigationBlockEntity {
    public static final String TAG_CAPACITY = "capacity";

    private int capacity;

    public AqueductBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        this(pos, blockState, 0);
    }

    public AqueductBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState, int capacity) {
        super(YTechBlockEntityTypes.AQUEDUCT.get(), pos, blockState, ((AqueductBlock)blockState.getBlock()).getValidNeighbors(blockState, pos));
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
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
                    && biome.value().getPrecipitationAt(worldPosition, level.getSeaLevel()) == Biome.Precipitation.RAIN);
        }

        return false;
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        capacity = tag.getInt(TAG_CAPACITY);
    }

    public void onRandomTick() {
        YTechMod.IRRIGATION_PROPAGATOR.server().changed(this);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt(TAG_CAPACITY, capacity);
    }
}
