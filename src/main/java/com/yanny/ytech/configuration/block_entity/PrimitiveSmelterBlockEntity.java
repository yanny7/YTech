package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.MachineItemStackHandler;
import com.yanny.ytech.configuration.container.PrimitiveSmelterContainerMenu;
import com.yanny.ytech.configuration.recipe.SmeltingRecipe;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class PrimitiveSmelterBlockEntity extends AbstractPrimitiveMachineBlockEntity {
    private static final String TAG_RECIPE_INPUT = "recipeInput";
    private static final int SLOT_INPUT = 0;
    private static final int SLOT_FUEL = 1;
    private static final int SLOT_OUTPUT = 2;

    @Nullable private ItemStack recipeInput = ItemStack.EMPTY;

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
                .addInputSlot(55, 16, this::canInput)
                .addInputSlot(55, 52, (itemStackHandler, slot, itemStack) -> itemStack.getBurnTime(RecipeType.BLASTING) > 0)
                .addOutputSlot(116, 35)
                .setOnChangeListener(this::setChanged)
                .build();
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);

        if (level == null || !level.isClientSide) { // server side
            if (tag.contains(TAG_RECIPE_INPUT)) {
                recipeInput = ItemStack.parseOptional(provider, tag.getCompound(TAG_RECIPE_INPUT));
            } else {
                recipeInput = null;
            }
        }
    }

    @Override
    public boolean hasActiveRecipe() {
        return recipeInput != null;
    }

    @Nullable
    public ItemStack processingItem() {
        return recipeInput;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);

        if (level == null || !level.isClientSide) {
            if (recipeInput != null) {
                tag.put(TAG_RECIPE_INPUT, recipeInput.save(provider));
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

            level.getRecipeManager().getRecipeFor(YTechRecipeTypes.SMELTING.get(), new SingleRecipeInput(input), level).ifPresent((recipe) -> {
                ItemStack result = itemStackHandler.getStackInSlot(SLOT_OUTPUT);
                SmeltingRecipe r = recipe.value();

                if (r.minTemperature() <= temperature && (result.isEmpty() || (ItemStack.isSameItemSameComponents(result, r.result()) && result.getMaxStackSize() > result.getCount()))) {
                    recipeInput = input.split(1);
                    leftSmelting = smeltingTime = r.smeltingTime();
                    recipeTemperature = r.minTemperature();
                    hasChanged.set(true);
                }
            });
        }
    }

    @Override
    protected void finishRecipe() {
        if (level != null && recipeInput != null) {
            ItemStack result = itemStackHandler.getStackInSlot(SLOT_OUTPUT);

            level.getRecipeManager().getRecipeFor(YTechRecipeTypes.SMELTING.get(), new SingleRecipeInput(recipeInput), level).ifPresent((r) -> {
                if (result.isEmpty()) {
                    itemStackHandler.setStackInSlot(SLOT_OUTPUT, r.value().result().copy());
                } else {
                    result.grow(1);
                }
            });
            recipeInput = null;
        }
    }

    @Override
    protected boolean isValidRecipeInInput() {
        if (level != null) {
            ItemStack itemStack = itemStackHandler.getStackInSlot(SLOT_INPUT);
            return level.getRecipeManager().getRecipeFor(YTechRecipeTypes.SMELTING.get(), new SingleRecipeInput(itemStack), level).isPresent();
        }

        return false;
    }

    private boolean canInput(@NotNull MachineItemStackHandler itemStackHandler, int slot, @NotNull ItemStack itemStack) {
        if (level != null) {
            return level.getRecipeManager().getRecipeFor(YTechRecipeTypes.SMELTING.get(), new SingleRecipeInput(itemStack), level).isPresent();
        } else {
            return false;
        }
    }
}
