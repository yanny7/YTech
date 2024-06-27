package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.recipe.DryingRecipe;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DryingRackBlockEntity extends BlockEntity implements BlockEntityTicker<DryingRackBlockEntity> {
    private static final String TAG_ITEMS = "items";
    private static final String TAG_RESULT = "result";
    private static final String TAG_DRYING_LEFT = "dryingLeft";

    private final ItemStackHandler items;

    @Nullable private ItemStack result = null;
    private int dryingLeft = -1;

    public DryingRackBlockEntity(BlockPos pos, BlockState blockState) {
        super(YTechBlockEntityTypes.DRYING_RACK.get(), pos, blockState);
        this.items = new ItemStackHandler(1) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }

    public ItemInteractionResult onUse(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                       @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide) {
            ItemStack holdingItemStack = player.getItemInHand(hand);
            ItemStack dryingItem = items.getStackInSlot(0);

            if (dryingItem.isEmpty()) {
                Optional<RecipeHolder<DryingRecipe>> dryingRecipe = level.getRecipeManager().getRecipeFor(YTechRecipeTypes.DRYING.get(), new SingleRecipeInput(holdingItemStack), level);

                dryingRecipe.ifPresent((r) -> {
                    EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                    result = r.value().result().copy();
                    dryingLeft = r.value().dryingTime();
                    player.setItemSlot(slot, items.insertItem(0, holdingItemStack, false));
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                    setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
                });
            } else {
                Block.popResourceFromFace(level, pos, hitResult.getDirection(), items.extractItem(0, dryingItem.getMaxStackSize(), false));
                result = null;
                dryingLeft = -1;
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
            }
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull DryingRackBlockEntity blockEntity) {
        if (result != null) {
            if (dryingLeft > 0) {
                if (!(level.isRaining() && YTechMod.CONFIGURATION.noDryingDuringRain())) {
                    Holder<Biome> biome = level.getBiome(pos);

                    if (biome.tags().anyMatch(t -> YTechMod.CONFIGURATION.getSlowDryingBiomes().contains(t)) && level.getGameTime() % 2 == 0) {
                        dryingLeft--;
                    } else if (biome.tags().anyMatch(t -> YTechMod.CONFIGURATION.getFastDryingBiomes().contains(t))) {
                        dryingLeft -= 2;
                    } else {
                        dryingLeft--;
                    }
                }
            } else {
                items.setStackInSlot(0, result);
                result = null;
                dryingLeft = -1;

                level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
            }
        }
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

        dryingLeft = tag.getInt(TAG_DRYING_LEFT);
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

    public int getDryingLeft() {
        return dryingLeft;
    }

    public ItemStack getItem() {
        return items.getStackInSlot(0);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put(TAG_ITEMS, items.serializeNBT(provider));
        tag.putInt(TAG_DRYING_LEFT, dryingLeft);

        if (result != null) {
            tag.put(TAG_RESULT, result.saveOptional(provider));
        }
    }
}
