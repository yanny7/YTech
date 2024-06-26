package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block.AqueductValveBlock;
import com.yanny.ytech.network.irrigation.NetworkType;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AqueductValveBlockEntity extends IrrigationBlockEntity {
    private static final String TAG_FLOW = "flow";

    private int flow = 0;

    public AqueductValveBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        super(YTechBlockEntityTypes.AQUEDUCT_VALVE.get(), pos, blockState, ((AqueductValveBlock)blockState.getBlock()).getValidNeighbors(blockState, pos));
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        flow = tag.getInt(TAG_FLOW);
    }

    @Override
    protected void onLevelSet(@NotNull ServerLevel level) {
        flow = calculateFlow(level);
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag(@NotNull HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        saveAdditional(tag, provider);
        return tag;
    }

    @Override
    public int getFlow() {
        return flow;
    }

    @Override
    public boolean validForRainFilling() {
        return false;
    }

    @Override
    public @NotNull NetworkType getNetworkType() {
        return NetworkType.PROVIDER;
    }

    @Override
    public void onChangedState(@NotNull BlockState oldBlockState, @NotNull BlockState newBlockState) {
        super.onChangedState(oldBlockState, newBlockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt(TAG_FLOW, flow);
    }

    public void neighborChanged() {
        if (level != null && !level.isClientSide) {
            int oldValue = flow;

            flow = calculateFlow((ServerLevel) level);

            if (oldValue != flow) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }

            YTechMod.IRRIGATION_PROPAGATOR.server().changed(this);
        }
    }

    private int calculateFlow(@NotNull ServerLevel level) {
        return getValidNeighbors().stream().anyMatch((pos) -> {
            BlockState blockState = level.getBlockState(pos);
            return blockState.getBlock() == Blocks.WATER && blockState.getFluidState().isSource();
        }) ? YTechMod.CONFIGURATION.getValveFillAmount() : 0;
    }
}
