package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.recipe.MillingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MillstoneBlockEntity extends BlockEntity implements BlockEntityTicker<MillstoneBlockEntity> {
    private static final String TAG_RESULT = "result";
    private static final String TAG_MILLING_TIME = "millingTime";
    private static final String TAG_SPIN_TIMEOUT = "spinTimeout";

    @Nullable private ItemStack result = null;
    private int millingTime = -1;
    private int spinTimeout = -1;
    private int clientRotation = 0;

    public MillstoneBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }

    public InteractionResult onUse(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                   @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide) {
            ItemStack holdingItemStack = player.getItemInHand(hand);

            if (result == null) {
                Optional<RecipeHolder<MillingRecipe>> millingRecipe = level.getRecipeManager().getRecipeFor(MillingRecipe.RECIPE_TYPE, new SimpleContainer(holdingItemStack), level);

                millingRecipe.ifPresent((r) -> {
                    EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;

                    result = r.value().result().copy();
                    millingTime = r.value().millingTime();
                    spinTimeout = 10;
                    player.setItemSlot(slot, holdingItemStack.copyWithCount(holdingItemStack.getCount() - 1));
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                    setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
                });
            } else {
                spinTimeout = 10;

                level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull MillstoneBlockEntity blockEntity) {
        if (!level.isClientSide) {
            if (result != null && spinTimeout > 0) {
                if (millingTime > 0) {
                    millingTime--;
                    spinTimeout--;

                    if (spinTimeout == 0) {
                        level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                        setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
                    }
                } else {
                    Block.popResource(level, pos, result);
                    result = null;
                    millingTime = -1;
                    spinTimeout = -1;

                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                    setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
                }
            }
        } else {
            if (spinTimeout > 0 && millingTime > 0) {
                clientRotation++;
            }
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (tag.contains(TAG_RESULT)) {
            result = ItemStack.of(tag.getCompound(TAG_RESULT));
        } else {
            result = null;
        }

        millingTime = tag.getInt(TAG_MILLING_TIME);
        spinTimeout = tag.getInt(TAG_SPIN_TIMEOUT);
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public int getMillingTime() {
        return millingTime;
    }

    public int getSpinTimeout() {
        return spinTimeout;
    }

    public int getClientRotation() {
        return clientRotation;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(TAG_MILLING_TIME, millingTime);
        tag.putInt(TAG_SPIN_TIMEOUT, spinTimeout);

        if (result != null) {
            CompoundTag itemStack = new CompoundTag();

            result.save(itemStack);
            tag.put(TAG_RESULT, itemStack);
        }
    }
}
