package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.recipe.TanningRecipe;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import com.yanny.ytech.registration.YTechRecipeTypes;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TanningRackBlockEntity extends BlockEntity {
    private static final String TAG_ITEMS = "items";
    private static final String TAG_RESULT = "result";
    private static final String TAG_HIT_LEFT = "hitLeft";

    private final ItemStackHandler items;

    @Nullable private ItemStack result = null;
    private int hitLeft = -1;

    public TanningRackBlockEntity(BlockPos pos, BlockState blockState) {
        super(YTechBlockEntityTypes.TANNING_RACK.get(), pos, blockState);
        this.items = new ItemStackHandler(1) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }

    public InteractionResult onUse(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                   @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide) {
            ItemStack holdingItemStack = player.getItemInHand(hand);
            ItemStack tanningItem = items.getStackInSlot(0);

            if (tanningItem.isEmpty()) {
                Optional<RecipeHolder<TanningRecipe>> tanningRecipe = level.getRecipeManager().getRecipeFor(YTechRecipeTypes.TANNING.get(), new SimpleContainer(holdingItemStack), level);

                tanningRecipe.ifPresent((recipe) -> {
                    EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;

                    result = recipe.value().result().copy();
                    hitLeft = recipe.value().hitCount();
                    player.setItemSlot(slot, items.insertItem(0, holdingItemStack, false));
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                    setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
                });
            } else {
                if (result != null) {
                    Optional<RecipeHolder<TanningRecipe>> tanningRecipe = level.getRecipeManager().getRecipeFor(YTechRecipeTypes.TANNING.get(), new SimpleContainer(items.getStackInSlot(0)), level);

                    tanningRecipe.ifPresent((recipe) -> {
                        if (recipe.value().tool().isEmpty() || recipe.value().tool().test(holdingItemStack)) {
                            player.getItemInHand(hand).hurtAndBreak(1, player, (e) -> e.broadcastBreakEvent(hand));
                            hitLeft--;

                            if (hitLeft == 0) {
                                items.setStackInSlot(0, result);
                                result = null;
                                hitLeft = -1;
                                level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                                setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
                            }
                        }
                    });
                } else {
                    Block.popResourceFromFace(level, pos, hitResult.getDirection(), items.extractItem(0, tanningItem.getMaxStackSize(), false));
                    result = null;
                    hitLeft = -1;
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                    setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
                }
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (tag.contains(TAG_ITEMS)) {
            items.deserializeNBT(tag.getCompound(TAG_ITEMS));
        }

        if (tag.contains(TAG_RESULT)) {
            result = ItemStack.of(tag.getCompound(TAG_RESULT));
        } else {
            result = null;
        }

        hitLeft = tag.getInt(TAG_HIT_LEFT);
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

    public int getHitLeft() {
        return hitLeft;
    }

    public ItemStack getItem() {
        return items.getStackInSlot(0);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(TAG_ITEMS, items.serializeNBT());
        tag.putInt(TAG_HIT_LEFT, hitLeft);

        if (result != null) {
            CompoundTag itemStack = new CompoundTag();

            result.save(itemStack);
            tag.put(TAG_RESULT, itemStack);
        }
    }
}
