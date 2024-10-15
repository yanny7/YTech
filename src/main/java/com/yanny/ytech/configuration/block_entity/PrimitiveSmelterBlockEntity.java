package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.MachineItemStackHandler;
import com.yanny.ytech.configuration.container.PrimitiveSmelterContainerMenu;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import com.yanny.ytech.registration.YTechItemTags;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class PrimitiveSmelterBlockEntity extends AbstractPrimitiveMachineBlockEntity {
    private static final String TAG_RECIPE_INPUT = "recipeInput";
    private static final String TAG_RECIPE_MOLD = "recipeMold";
    private static final int SLOT_INPUT = 0;
    private static final int SLOT_FUEL = 1;
    private static final int SLOT_MOLD = 2;
    private static final int SLOT_OUTPUT = 3;

    @Nullable private ItemStack recipeInput = ItemStack.EMPTY;
    @Nullable private ItemStack recipeMold = ItemStack.EMPTY;

    public PrimitiveSmelterBlockEntity(BlockPos pos, BlockState blockState) {
        super(YTechBlockEntityTypes.PRIMITIVE_SMELTER.get(), pos, blockState, YTechRecipeTypes.SMELTING.get());
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.ytech.primitive_smelter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, @NotNull Inventory inventory, @NotNull Player player) {
        return new PrimitiveSmelterContainerMenu(windowId, inventory.player, worldPosition, itemStackHandler, containerData);
    }

    @NotNull
    @Override
    public MachineItemStackHandler createItemStackHandler() {
        return new MachineItemStackHandler.Builder()
                .addInputSlot(55, 16, (itemStackHandler, slot, itemStack) -> true)
                .addInputSlot(55, 52, (itemStackHandler, slot, itemStack) -> ForgeHooks.getBurnTime(itemStack, RecipeType.BLASTING) > 0)
                .addInputSlot(88, 52, (itemStackHandler, slot, itemStack) -> itemStack.is(YTechItemTags.MOLDS.tag) || itemStack.is(YTechItemTags.SAND_MOLDS.tag))
                .addOutputSlot(116, 35)
                .setOnChangeListener(this::setChanged)
                .build();
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (level == null || !level.isClientSide) { // server side
            if (tag.contains(TAG_RECIPE_INPUT)) {
                recipeInput = ItemStack.of(tag.getCompound(TAG_RECIPE_INPUT));
            } else {
                recipeInput = null;
            }

            if (tag.contains(TAG_RECIPE_MOLD)) {
                recipeMold = ItemStack.of(tag.getCompound(TAG_RECIPE_MOLD));
            } else {
                recipeMold = null;
            }
        }
    }

    @Override
    public boolean hasActiveRecipe() {
        return recipeInput != null;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        if (level == null || !level.isClientSide) {
            if (recipeInput != null) {
                CompoundTag itemStack = new CompoundTag();

                recipeInput.save(itemStack);
                tag.put(TAG_RECIPE_INPUT, itemStack);
            }

            if (recipeMold != null) {
                CompoundTag itemStack = new CompoundTag();

                recipeMold.save(itemStack);
                tag.put(TAG_RECIPE_MOLD, itemStack);
            }
        }
    }

    @Override
    protected boolean hasItemsInInput() {
        return !itemStackHandler.getStackInSlot(SLOT_INPUT).isEmpty();
    }

    @Override
    protected int getFuelSlot() {
        return SLOT_FUEL;
    }

    @Override
    protected void startRecipe(@NotNull AtomicBoolean hasChanged) {
        if (level != null) {
            ItemStack input = itemStackHandler.getStackInSlot(SLOT_INPUT);
            ItemStack mold = itemStackHandler.getStackInSlot(SLOT_MOLD);

            level.getRecipeManager().getRecipeFor(YTechRecipeTypes.SMELTING.get(), new SimpleContainer(input, mold), level).ifPresent((r) -> {
                ItemStack result = itemStackHandler.getStackInSlot(SLOT_OUTPUT);

                if (r.minTemperature() <= temperature && (result.isEmpty() || (ItemStack.isSameItemSameTags(result, r.result()) && result.getMaxStackSize() > result.getCount()))) {
                    recipeInput = input.split(r.inputCount());
                    recipeMold = mold.copy();
                    leftSmelting = smeltingTime = r.smeltingTime();
                    recipeTemperature = r.minTemperature();
                    hasChanged.set(true);

                    if (!r.mold().isEmpty()) {
                        if (mold.isDamageableItem()) {
                            if (mold.hurt(1, level.random, null)) {
                                mold.shrink(1);
                            }
                        } else {
                            mold.shrink(1);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void finishRecipe() {
        if (level != null) {
            ItemStack result = itemStackHandler.getStackInSlot(SLOT_OUTPUT);

            level.getRecipeManager().getRecipeFor(YTechRecipeTypes.SMELTING.get(), new SimpleContainer(recipeInput, recipeMold), level).ifPresent((r) -> {
                if (result.isEmpty()) {
                    itemStackHandler.setStackInSlot(SLOT_OUTPUT, r.result().copy());
                } else {
                    result.grow(1);
                }
            });
            recipeInput = null;
            recipeMold = null;
        }
    }

    @Override
    protected boolean isValidRecipeInInput() {
        if (level != null) {
            ItemStack itemStack = itemStackHandler.getStackInSlot(SLOT_INPUT);
            ItemStack mold = itemStackHandler.getStackInSlot(SLOT_MOLD);
            return level.getRecipeManager().getRecipeFor(YTechRecipeTypes.SMELTING.get(), new SimpleContainer(itemStack, mold), level).isPresent();
        }

        return false;
    }
}
