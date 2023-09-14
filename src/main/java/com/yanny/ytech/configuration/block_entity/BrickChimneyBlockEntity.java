package com.yanny.ytech.configuration.block_entity;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block.ReinforcedBrickChimneyBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class BrickChimneyBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String TAG_MASTER_POSITION = "masterPos";
    private static final String TAG_HEIGHT_INDEX = "heightIndex";
    private static final int MAX_HEIGHT = 2;

    @Nullable private BlockPos masterPos = null;
    private int heightIndex = -1;

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

        if (tag.contains(TAG_HEIGHT_INDEX)) {
            heightIndex = tag.getInt(TAG_HEIGHT_INDEX);
        } else {
            heightIndex = -1;
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (level != null && !level.isClientSide && masterPos == null) {
            setMaster(worldPosition.below());
        }
    }

    public void setMaster(@NotNull BlockPos blockPos) {
        if (level != null) {
            if (level.getBlockEntity(blockPos) instanceof AbstractPrimitiveMachineBlockEntity primitiveMachine) {
                int height = -1;

                if (blockPos.equals(worldPosition.below())) {
                    height = MAX_HEIGHT;
                } else if (level.getBlockEntity(worldPosition.below()) instanceof BrickChimneyBlockEntity chimney) {
                    height = chimney.getNextHeight(getBlockState().getBlock() instanceof ReinforcedBrickChimneyBlock);
                } else {
                    LOGGER.warn("Cannot get chimney height index, should not happen! At {}, master at {}", worldPosition, blockPos);
                }

                if (height == -1) {
                    removeBlock();
                } else {
                    registerChimney(level, blockPos, primitiveMachine, height);

                    // propagate above
                    if (level.getBlockEntity(worldPosition.above()) instanceof BrickChimneyBlockEntity chimney) {
                        chimney.setMaster(blockPos);
                    }
                }
            } else if (level.getBlockEntity(blockPos) instanceof BrickChimneyBlockEntity blockEntity) {
                if (blockEntity.masterPos != null) {
                    setMaster(blockEntity.masterPos);
                }
            }
        }
    }

    public void onRemove() {
        if (level != null && masterPos != null && level.getBlockEntity(masterPos) instanceof AbstractPrimitiveMachineBlockEntity primitiveMachine) {
            masterPos = null;
            heightIndex = -1;
            primitiveMachine.chimneyRemoved();
            setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());

            // propagate above
            if (level.getBlockEntity(worldPosition.above()) instanceof BrickChimneyBlockEntity chimney) {
                chimney.onRemove();
            }
        }
    }

    public void removeBlock() {
        if (level != null) {
            level.destroyBlock(worldPosition, true);

            // propagate above
            if (level.getBlockEntity(worldPosition.above()) instanceof BrickChimneyBlockEntity chimney) {
                chimney.removeBlock();
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        if (masterPos != null) {
            tag.put(TAG_MASTER_POSITION, Utils.saveBlockPos(masterPos));
        }

        tag.putInt(TAG_HEIGHT_INDEX, heightIndex);
    }

    private int getNextHeight(boolean isReinforced) {
        if (heightIndex == -1) {
            return -1;
        }

        if (getBlockState().getBlock() instanceof ReinforcedBrickChimneyBlock) {
            if (heightIndex > 0) {
                if (isReinforced) {
                    return heightIndex - 1;
                } else {
                    return MAX_HEIGHT;
                }
            } else {
                if (isReinforced) {
                    return -1;
                } else {
                    return MAX_HEIGHT;
                }
            }
        } else {
            if (isReinforced) {
                return -1;
            }

            if (heightIndex > 0) {
                return heightIndex - 1;
            } else {
                return -1;
            }
        }
    }

    private void registerChimney(@NotNull Level level, @NotNull BlockPos masterPos, @NotNull AbstractPrimitiveMachineBlockEntity primitiveMachine, int height) {
        this.masterPos = masterPos;
        heightIndex = height;
        primitiveMachine.chimneyAdded();
        setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
    }
}
