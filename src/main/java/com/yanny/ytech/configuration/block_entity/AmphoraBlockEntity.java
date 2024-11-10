package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.registration.YTechBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class AmphoraBlockEntity extends BlockEntity {
    private static final String TAG_ITEM = "Item";
    private static final String TAG_COUNT = "Count";
    private static final int STACK_MULTIPLIER = 8; // How many times the base item max stack size is Amphora storing

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            if (level != null) {
                level.blockEntityChanged(worldPosition);
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.isStackable();
        }

        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return stack.getMaxStackSize() * STACK_MULTIPLIER;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64 * STACK_MULTIPLIER;
        }
    };

    public AmphoraBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(YTechBlockEntityTypes.AMPHORA.get(), pPos, pBlockState);
    }

    public ItemStack getItem() {
        return itemHandler.getStackInSlot(0);
    }

    public InteractionResult onUse(@NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                   @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        ItemStack item = itemHandler.getStackInSlot(0);
        ItemStack holdingItem = player.getItemInHand(hand);

        if (item.isEmpty()) {
            if (!holdingItem.isEmpty()) {
                itemHandler.setStackInSlot(0, holdingItem.copyAndClear());
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        } else {
            if (holdingItem.isEmpty() && player.isCrouching()) {
                Block.popResourceFromFace(level, pos, hitResult.getDirection(), itemHandler.extractItem(0, item.getMaxStackSize(), false));
            } else {
                player.setItemInHand(hand, itemHandler.insertItem(0, holdingItem, false));
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.contains(TAG_ITEM) && tag.contains(TAG_COUNT)) {
            ItemStack itemStack = ItemStack.of(tag.getCompound(TAG_ITEM));
            int count = tag.getInt(TAG_COUNT);

            itemStack.setCount(count);
            itemHandler.setStackInSlot(0, itemStack);
        }
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        ItemStack itemStack = itemHandler.getStackInSlot(0);

        tag.put(TAG_ITEM, itemStack.save(new CompoundTag()));
        tag.putInt(TAG_COUNT, itemStack.getCount());
    }
}
