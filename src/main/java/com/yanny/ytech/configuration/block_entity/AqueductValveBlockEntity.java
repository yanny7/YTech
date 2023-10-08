package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block.AqueductValveBlock;
import com.yanny.ytech.network.irrigation.NetworkType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class AqueductValveBlockEntity extends IrrigationBlockEntity {
    private static final String TAG_FLOW = "flow";

    private int flow = 0;

    public AqueductValveBlockEntity(@NotNull BlockEntityType<? extends BlockEntity> entityType, @NotNull BlockPos pos, @NotNull BlockState blockState) {
        super(entityType, pos, blockState, ((AqueductValveBlock)blockState.getBlock()).getValidNeighbors(blockState, pos));
    }

    @Override
    public void onLoad() {
        super.onLoad();
        flow = getFlow();
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        flow = tag.getInt(TAG_FLOW);
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
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
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(TAG_FLOW, flow);
    }

    public void neighborChanged() {
        if (level != null && !level.isClientSide) {
            int oldValue = flow;

            flow = getValidNeighbors().stream().anyMatch((pos) -> {
                BlockState blockState = level.getBlockState(pos);
                return blockState.getBlock() == Blocks.WATER || (blockState.hasProperty(WATERLOGGED) && blockState.getValue(WATERLOGGED));
            }) ? YTechMod.CONFIGURATION.getValveFillAmount() : 0;

            if (oldValue != flow) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
    }
}
