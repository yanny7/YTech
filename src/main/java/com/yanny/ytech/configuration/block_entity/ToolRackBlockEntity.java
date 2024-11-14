package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.block.ToolRackBlock;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class ToolRackBlockEntity extends BlockEntity {
    private static final String TAG_ITEMS = "Items";

    private final MyItemStackHandler itemHandler = new MyItemStackHandler(16);

    public ToolRackBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(YTechBlockEntityTypes.TOOL_RACK.get(), pPos, pBlockState);
    }

    public NonNullList<ItemStack> getItems() {
        return itemHandler.getItems();
    }

    public ItemStack getItem(BlockPos pos, Direction direction, Vec3 hitVec) {
        int[] position = ToolRackBlock.getPosition(new BlockHitResult(hitVec, direction, pos, false));

        if (position != null) {
            return itemHandler.getStackInSlot(ToolRackBlock.getIndex(position));
        }

        return ItemStack.EMPTY;
    }

    public InteractionResult onUse(@NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                   @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        int[] hitPos = ToolRackBlock.getPosition(hitResult);
        ItemStack holdingItem = player.getItemInHand(hand);

        if (hitPos != null) {
            int index = ToolRackBlock.getIndex(hitPos);
            ItemStack item = itemHandler.getStackInSlot(index);

            if (item.isEmpty()) {
                if (!holdingItem.isEmpty()) {
                    itemHandler.setStackInSlot(index, holdingItem.copyAndClear());
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            } else {
                if (holdingItem.isEmpty()) {
                    Block.popResourceFromFace(level, pos, hitResult.getDirection(), itemHandler.extractItem(index, player.isCrouching() ? item.getMaxStackSize() : 1, false));
                } else {
                    player.setItemInHand(hand, itemHandler.insertItem(index, holdingItem, false));
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound(TAG_ITEMS));
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

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(TAG_ITEMS, itemHandler.serializeNBT());
    }

    private class MyItemStackHandler extends ItemStackHandler {
        public MyItemStackHandler(int size) {
            super(size);
        }

        @Override
        protected void onContentsChanged(int slot) {
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                level.blockEntityChanged(worldPosition);
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return !stack.isStackable();
        }

        public NonNullList<ItemStack> getItems() {
            return stacks;
        }
    }
}
