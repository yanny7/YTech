package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BrickChimneyBlockEntity extends BlockEntity {
    private static final String TAG_MASTER_POSITION = "masterPos";

    @Nullable private BlockPos masterPos = null;

    public BrickChimneyBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (tag.contains(TAG_MASTER_POSITION)) {
            masterPos = Utils.loadBlockPos(tag.getCompound(TAG_MASTER_POSITION));
        } else {
            masterPos = null;
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (level != null && !level.isClientSide && masterPos == null) {
            if (level.getBlockEntity(worldPosition.below()) instanceof PrimitiveSmelterBlockEntity blockEntity) {
                masterPos = blockEntity.getBlockPos();
                blockEntity.chimneyAdded();
                setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
            } else if (level.getBlockEntity(worldPosition.below()) instanceof BrickChimneyBlockEntity chimney && chimney.masterPos != null &&
                    level.getBlockEntity(chimney.masterPos) instanceof PrimitiveSmelterBlockEntity blockEntity) {
                masterPos = chimney.masterPos;
                blockEntity.chimneyAdded();
                setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
            }

            // propagate above
            if (masterPos != null && level.getBlockEntity(worldPosition.above()) instanceof BrickChimneyBlockEntity chimney) {
                chimney.setMaster(masterPos);
            }
        }
    }

    public void setMaster(@NotNull BlockPos blockPos) {
        if (level != null && level.getBlockEntity(blockPos) instanceof PrimitiveSmelterBlockEntity blockEntity) {
            masterPos = blockPos;
            blockEntity.chimneyAdded();
            setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());

            // propagate above
            if (level.getBlockEntity(worldPosition.above()) instanceof BrickChimneyBlockEntity chimney) {
                chimney.setMaster(masterPos);
            }
        }
    }

    public void onRemove() {
        if (level != null && masterPos != null && level.getBlockEntity(masterPos) instanceof PrimitiveSmelterBlockEntity blockEntity) {
            masterPos = null;
            blockEntity.chimneyRemoved();
            setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());

            // propagate above
            if (level.getBlockEntity(worldPosition.above()) instanceof BrickChimneyBlockEntity chimney) {
                chimney.onRemove();
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        if (masterPos != null) {
            tag.put(TAG_MASTER_POSITION, Utils.saveBlockPos(masterPos));
        }
    }
}
