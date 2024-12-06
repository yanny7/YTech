package com.yanny.ytech.configuration.block_entity;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.util.DataComponentUtil;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class AmphoraBlockEntity extends BlockEntity {
    private static final String TAG_ITEM = "Item";
    private static final int STACK_MULTIPLIER = 8; // How many times the base item max stack size is Amphora storing

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Codec<ItemStack> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                    Item.CODEC.fieldOf("id").forGetter(ItemStack::getItemHolder),
                    ExtraCodecs.intRange(1, 64 * STACK_MULTIPLIER).fieldOf("count").orElse(1).forGetter(ItemStack::getCount),
                    DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(ItemStack::getComponentsPatch)
            ).apply(instance, ItemStack::new)
    );

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

    public InteractionResult useItemOn(@NotNull ItemStack holdingItem, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                       @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        ItemStack item = itemHandler.getStackInSlot(0);

        if (item.isEmpty()) {
            if (!holdingItem.isEmpty()) {
                itemHandler.setStackInSlot(0, holdingItem.copyAndClear());
                return InteractionResult.SUCCESS;
            }
        } else {
            if (holdingItem.isEmpty()) {
                Block.popResourceFromFace(level, pos, hitResult.getDirection(), itemHandler.extractItem(0, player.isCrouching() ? item.getMaxStackSize() : 1, false));
            } else {
                player.setItemInHand(hand, itemHandler.insertItem(0, holdingItem, false));
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains(TAG_ITEM)) {
            if (tag.getCompound(TAG_ITEM).isEmpty()) {
                itemHandler.setStackInSlot(0, ItemStack.EMPTY);
            } else {
                itemHandler.setStackInSlot(0, CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag.getCompound(TAG_ITEM)).resultOrPartial((error) -> {
                    LOGGER.error("Tried to load invalid item: '{}'", error);
                }).orElse(ItemStack.EMPTY));
            }
        }
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ItemStack itemStack = itemHandler.getStackInSlot(0);

        if (itemStack.isEmpty()) {
            tag.put(TAG_ITEM, new CompoundTag());
        } else {
            tag.put(TAG_ITEM, DataComponentUtil.wrapEncodingExceptions(itemStack, CODEC, provider, new CompoundTag()));
        }
    }
}
