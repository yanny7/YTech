package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block.AqueductConsumerBlock;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.FarmlandWaterManager;
import net.neoforged.neoforge.common.ticket.AABBTicket;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class AqueductHydratorBlockEntity extends AqueductConsumerBlockEntity {
    private static final String TAG_TIMER = "timer";

    protected int timer = 0;

    private AABBTicket ticket = null;

    public AqueductHydratorBlockEntity(@NotNull BlockEntityType<? extends BlockEntity> entityType, @NotNull BlockPos pos, @NotNull BlockState blockState) {
        super(entityType, pos, blockState);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        timer = tag.getInt(TAG_TIMER);
    }

    public void tick(@NotNull ServerLevel level) {
        if (ticket == null) {
            ticket = createTicket(level);
        }

        if ((!isHydrating() || timer == 0) && level.getGameTime() % 20 == 0) {
            if (drainLiquid(level)) {
                BlockState blockState = getBlockState().setValue(AqueductConsumerBlock.ACTIVATED, true);

                if (ticket == null) {
                    ticket = createTicket(level);
                } else {
                    ticket.validate();
                }

                timer = YTechMod.CONFIGURATION.getHydratorDrainPerNthTick();
                level.setBlock(worldPosition, blockState, Block.UPDATE_ALL);
                YTechMod.IRRIGATION_PROPAGATOR.server().changed(this);
            }
        } else if (timer > 0) {
            timer--;

            if (timer == 0) {
                if (!drainLiquid(level)) {
                    BlockState blockState = getBlockState().setValue(AqueductConsumerBlock.ACTIVATED, false);

                    if (ticket != null) {
                        ticket.invalidate();
                    }

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

    protected boolean drainLiquid(@NotNull ServerLevel level) {
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

    private AABBTicket createTicket(ServerLevel level) {
        return FarmlandWaterManager.addAABBTicket(level, AABB.ofSize(worldPosition.getCenter(), 9, 1, 9));
    }
}
