package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.recipe.PotteryRecipe;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PottersWheelBlockEntity extends BlockEntity {
    private static final String TAG_ITEMS = "items";
    private static final String TAG_RESULT = "result";

    private final ItemStackHandler items;
    private final RecipeManager.CachedCheck<RecipeInput, PotteryRecipe> quickCheck;

    @Nullable
    private ItemStack result = null;

    public PottersWheelBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(YTechBlockEntityTypes.POTTERS_WHEEL.get(), pPos, pBlockState);
        this.items = new ItemStackHandler(1);
        quickCheck = RecipeManager.createCheck(YTechRecipeTypes.POTTERY.get());
    }

    public InteractionResult onUse(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                   @NotNull InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel) {
            ItemStack holdingItemStack = player.getItemInHand(hand);

            if (player.isCrouching()) {
                Block.popResourceFromFace(level, pos, Direction.UP, items.getStackInSlot(0));
                items.setStackInSlot(0, ItemStack.EMPTY);
                result = null;
                level.playSound(null, pos, SoundEvents.SLIME_SQUISH, SoundSource.BLOCKS, level.random.nextFloat() * 0.25F + 0.75F, 1.0f);
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
            } else {
                if ((holdingItemStack.is(Items.CLAY) || holdingItemStack.is(Items.CLAY_BALL))) {
                    ItemStack inHand = holdingItemStack.copyWithCount(1);

                    if (inHand.is(Items.CLAY)) {
                        inHand = Items.CLAY_BALL.getDefaultInstance();
                        inHand.setCount(4);
                    }

                    if (items.insertItem(0, inHand, true).isEmpty()) {
                        items.insertItem(0, inHand, false);
                        holdingItemStack.shrink(1);
                        level.playSound(null, pos, SoundEvents.SLIME_SQUISH, SoundSource.BLOCKS, level.random.nextFloat() * 0.25F + 0.75F, 1.0f);

                        Optional<RecipeHolder<PotteryRecipe>> recipes = quickCheck.getRecipeFor(new SingleRecipeInput(items.getStackInSlot(0)), level);

                        result = recipes.map((m) -> m.value().result()).orElse(null);
                        player.causeFoodExhaustion(4f);
                        level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                        setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
                    }
                } else if (holdingItemStack.isEmpty() && result != null) {
                    items.setStackInSlot(0, ItemStack.EMPTY);
                    Block.popResourceFromFace(level, pos, Direction.UP, result);
                    result = null;
                    level.playSound(null, pos, SoundEvents.SLIME_SQUISH, SoundSource.BLOCKS, level.random.nextFloat() * 0.25F + 0.75F, 1.0f);
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                    player.causeFoodExhaustion(4f);
                    setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);

        if (tag.contains(TAG_ITEMS)) {
            items.deserializeNBT(provider, tag.getCompound(TAG_ITEMS));
        }

        if (tag.contains(TAG_RESULT)) {
            result = ItemStack.parseOptional(provider, tag.getCompound(TAG_RESULT));
        } else {
            result = null;
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

    public ItemStack getItem() {
        return items.getStackInSlot(0);
    }

    @Nullable
    public ItemStack getResult() {
        return result;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put(TAG_ITEMS, items.serializeNBT(provider));

        if (result != null) {
            tag.put(TAG_RESULT, result.saveOptional(provider));
        }
    }
}
