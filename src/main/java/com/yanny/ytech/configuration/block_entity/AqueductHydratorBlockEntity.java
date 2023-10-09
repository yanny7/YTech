package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block.AqueductHydratorBlock;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import com.yanny.ytech.network.irrigation.NetworkType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class AqueductHydratorBlockEntity extends IrrigationBlockEntity {
    private static final String TAG_TIMER = "timer";

    private int timer = 0;

    public AqueductHydratorBlockEntity(@NotNull BlockEntityType<? extends BlockEntity> entityType, @NotNull BlockPos pos, @NotNull BlockState blockState) {
        super(entityType, pos, blockState, ((AqueductHydratorBlock) blockState.getBlock()).getValidNeighbors(blockState, pos));
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        timer = tag.getInt(TAG_TIMER);
    }

    @Override
    public int getFlow() {
        return 0;
    }

    @Override
    public boolean validForRainFilling() {
        return false;
    }

    @Override
    public @NotNull NetworkType getNetworkType() {
        return NetworkType.CONSUMER;
    }

    public void neighborChanged() {
        if (level != null && !level.isClientSide) {
            YTechMod.IRRIGATION_PROPAGATOR.server().changed(this);
        }
    }

    public boolean isHydrating() {
        return getBlockState().getValue(BlockStateProperties.WATERLOGGED);
    }

    public void tick(@NotNull ServerLevel level) {
        if ((!isHydrating() || timer == 0) && level.getGameTime() % 20 == 0) {
            if (drainLiquid(level)) {
                BlockState blockState = getBlockState().setValue(BlockStateProperties.WATERLOGGED, true);

                timer = YTechMod.CONFIGURATION.getHydratorDrainPerNthTick();
                level.setBlock(worldPosition, blockState, Block.UPDATE_ALL);
                YTechMod.IRRIGATION_PROPAGATOR.server().changed(this);
            }
        } else if (timer > 0) {
            timer--;

            if (timer == 0) {
                if (!drainLiquid(level)) {
                    BlockState blockState = getBlockState().setValue(BlockStateProperties.WATERLOGGED, false);

                    level.setBlock(worldPosition, blockState, Block.UPDATE_ALL);
                    YTechMod.IRRIGATION_PROPAGATOR.server().changed(this);
                } else {
                    timer = YTechMod.CONFIGURATION.getHydratorDrainPerNthTick();
                }
            } else {
                setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(TAG_TIMER, timer);
    }

    private boolean drainLiquid(@NotNull ServerLevel level) {
        IrrigationServerNetwork network = YTechMod.IRRIGATION_PROPAGATOR.server().getNetwork(this);

        if (network != null) {
            int amount = YTechMod.CONFIGURATION.getHydratorDrainAmount();

            if (network.getFluidHandler().drain(new FluidStack(Fluids.WATER, amount), IFluidHandler.FluidAction.SIMULATE).getAmount() == amount) {
                network.getFluidHandler().drain(new FluidStack(Fluids.WATER, amount), IFluidHandler.FluidAction.EXECUTE);
                setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
                return true;
            }
        }

        return false;
    }
}
