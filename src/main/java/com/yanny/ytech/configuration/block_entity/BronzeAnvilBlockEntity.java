package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.registration.YTechBlockEntityTypes;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.entity.LivingEntity.getSlotForHand;

public class BronzeAnvilBlockEntity extends BlockEntity {
    private static final String TAG_ITEMS = "items";
    private static final int SLOT_INPUT = 0;

    @NotNull protected final ItemStackHandler itemStackHandler;

    public BronzeAnvilBlockEntity(BlockPos pos, BlockState blockState) {
        super(YTechBlockEntityTypes.BRONZE_ANVIL.get(), pos, blockState);
        itemStackHandler = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    public InteractionResult onUse(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                   @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level instanceof ServerLevel serverLevel) {
            ItemStack holdingItemStack = player.getItemInHand(hand);
            ItemStack hammeringItem = itemStackHandler.getStackInSlot(0);

            if (hammeringItem.isEmpty()) {
                serverLevel.recipeAccess().getRecipeFor(YTechRecipeTypes.HAMMERING.get(), new SingleRecipeInput(holdingItemStack), level).ifPresent((recipe) -> {
                    itemStackHandler.setStackInSlot(SLOT_INPUT, holdingItemStack.split(holdingItemStack.getMaxStackSize()));
                    setChanged(level, pos, Blocks.AIR.defaultBlockState());
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                });
            } else {
                if (hammeringItem.is(holdingItemStack.getItem()) && hammeringItem.getCount() < hammeringItem.getMaxStackSize()) {
                    hammeringItem.grow(holdingItemStack.split(hammeringItem.getMaxStackSize() - hammeringItem.getCount()).getCount());
                    setChanged(level, pos, Blocks.AIR.defaultBlockState());
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                } else if (holdingItemStack.isEmpty()) {
                    Block.popResourceFromFace(level, pos, hitResult.getDirection(), itemStackHandler.extractItem(0, hammeringItem.getMaxStackSize(), false));
                    setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                } else {
                    serverLevel.recipeAccess().getRecipeFor(YTechRecipeTypes.HAMMERING.get(), new SingleRecipeInput(hammeringItem), level).ifPresent((recipe) -> {
                        if (recipe.value().tool().test(holdingItemStack)) {
                            hammeringItem.split(1);
                            holdingItemStack.hurtAndBreak(1, player, getSlotForHand(hand));
                            level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0f, 0.8f + level.random.nextFloat() * 0.4f);
                            Block.popResourceFromFace(level, pos, hitResult.getDirection(), recipe.value().result().copy());
                            setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
                            level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                        }
                    });
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);

        if (tag.contains(TAG_ITEMS)) {
            itemStackHandler.deserializeNBT(provider, tag.getCompound(TAG_ITEMS));
        }
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag(@NotNull HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        saveAdditional(tag, provider);
        return tag;
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @NotNull
    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put(TAG_ITEMS, itemStackHandler.serializeNBT(provider));
    }
}
