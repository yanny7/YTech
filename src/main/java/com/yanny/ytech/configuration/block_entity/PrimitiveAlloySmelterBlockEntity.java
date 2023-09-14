package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.MachineItemStackHandler;
import com.yanny.ytech.configuration.recipe.AlloyingRecipe;
import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class PrimitiveAlloySmelterBlockEntity extends AbstractPrimitiveMachineBlockEntity {
    private static final String TAG_RECIPE_INPUT_LEFT = "recipeInputLeft";
    private static final String TAG_RECIPE_INPUT_RIGHT = "recipeInputRight";
    private static final int SLOT_INPUT_LEFT = 0;
    private static final int SLOT_INPUT_RIGHT = 1;
    private static final int SLOT_FUEL = 2;
    private static final int SLOT_OUTPUT = 3;

    @Nullable private ItemStack recipeInputLeft = ItemStack.EMPTY;

    @Nullable private ItemStack recipeInputRight = ItemStack.EMPTY;

    public PrimitiveAlloySmelterBlockEntity(Holder holder, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(holder, blockEntityType, pos, blockState);
    }

    @Override
    public @NotNull MachineItemStackHandler createItemStackHandler() {
        return new MachineItemStackHandler.Builder()
                .addInputSlot(45, 16, this::canInput)
                .addInputSlot(65, 16, this::canInput)
                .addInputSlot(55, 52, (itemStackHandler, slot, itemStack) -> ForgeHooks.getBurnTime(itemStack, RecipeType.BLASTING) > 0)
                .addOutputSlot(116, 35)
                .setOnChangeListener(this::setChanged)
                .build();
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (level == null || !level.isClientSide) { // server side
            if (tag.contains(TAG_RECIPE_INPUT_LEFT)) {
                recipeInputLeft = ItemStack.of(tag.getCompound(TAG_RECIPE_INPUT_LEFT));
            } else {
                recipeInputLeft = null;
            }

            if (tag.contains(TAG_RECIPE_INPUT_RIGHT)) {
                recipeInputRight = ItemStack.of(tag.getCompound(TAG_RECIPE_INPUT_RIGHT));
            } else {
                recipeInputRight = null;
            }
        }
    }

    @Override
    public boolean hasActiveRecipe() {
        return recipeInputLeft != null && recipeInputRight != null;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        if (level == null || !level.isClientSide) {
            if (recipeInputLeft != null) {
                CompoundTag itemStack = new CompoundTag();

                recipeInputLeft.save(itemStack);
                tag.put(TAG_RECIPE_INPUT_LEFT, itemStack);
            }

            if (recipeInputRight != null) {
                CompoundTag itemStack = new CompoundTag();

                recipeInputRight.save(itemStack);
                tag.put(TAG_RECIPE_INPUT_RIGHT, itemStack);
            }
        }
    }

    @Override
    protected boolean hasItemsInInput() {
        return !itemStackHandler.getStackInSlot(SLOT_INPUT_LEFT).isEmpty() && !itemStackHandler.getStackInSlot(SLOT_INPUT_RIGHT).isEmpty();
    }

    @Override
    protected int getFuelSlot() {
        return SLOT_FUEL;
    }

    @Override
    protected void startRecipe(@NotNull AtomicBoolean hasChanged) {
        if (level != null) {
            ItemStack inputLeft = itemStackHandler.getStackInSlot(SLOT_INPUT_LEFT);
            ItemStack inputRight = itemStackHandler.getStackInSlot(SLOT_INPUT_RIGHT);

            level.getRecipeManager().getRecipeFor(AlloyingRecipe.RECIPE_TYPE, new SimpleContainer(inputLeft, inputRight), level).ifPresent((r) -> {
                ItemStack result = itemStackHandler.getStackInSlot(SLOT_OUTPUT);

                if (r.minTemperature() <= temperature && (result.isEmpty() || (ItemStack.isSameItemSameTags(result, r.result()) && result.getMaxStackSize() > result.getCount()))) {
                    if (r.matchesIngredient1(inputLeft, false) && r.matchesIngredient2(inputRight, false)) {
                        recipeInputLeft = inputLeft.split(r.getInput1Count());
                        recipeInputRight = inputRight.split(r.getInput2Count());
                    } else {
                        recipeInputLeft = inputLeft.split(r.getInput2Count());
                        recipeInputRight = inputRight.split(r.getInput1Count());
                    }

                    leftSmelting = smeltingTime = r.smeltingTime();
                    recipeTemperature = r.minTemperature();
                    hasChanged.set(true);
                }
            });
        }
    }

    @Override
    protected void finishRecipe() {
        if (level != null) {
            ItemStack result = itemStackHandler.getStackInSlot(SLOT_OUTPUT);

            level.getRecipeManager().getRecipeFor(AlloyingRecipe.RECIPE_TYPE, new SimpleContainer(recipeInputLeft, recipeInputRight), level).ifPresent((r) -> {
                if (result.isEmpty()) {
                    itemStackHandler.setStackInSlot(SLOT_OUTPUT, r.result().copy());
                } else {
                    result.grow(r.result().getCount());
                }
            });
            recipeInputLeft = null;
            recipeInputRight = null;
        }
    }

    @Override
    protected boolean isValidRecipeInInput() {
        if (level != null) {
            ItemStack inputLeft = itemStackHandler.getStackInSlot(SLOT_INPUT_LEFT);
            ItemStack inputRight = itemStackHandler.getStackInSlot(SLOT_INPUT_RIGHT);

            return level.getRecipeManager().getAllRecipesFor(AlloyingRecipe.RECIPE_TYPE).stream()
                    .anyMatch((recipe) -> recipe.matchesFully(inputLeft, inputRight, false));
        }

        return false;
    }

    private boolean canInput(@NotNull MachineItemStackHandler itemStackHandler, int slot, @NotNull ItemStack itemStack) {
        ItemStack itemStack1 = slot != SLOT_INPUT_LEFT ? itemStackHandler.getStackInSlot(SLOT_INPUT_LEFT) : itemStack;
        ItemStack itemStack2 = slot != SLOT_INPUT_RIGHT ? itemStackHandler.getStackInSlot(SLOT_INPUT_RIGHT) : itemStack;
        boolean isPartial = itemStack1.isEmpty() || itemStack2.isEmpty();

        if (level != null) {
            return level.getRecipeManager().getAllRecipesFor(AlloyingRecipe.RECIPE_TYPE).stream().anyMatch((recipe) -> {
                if (isPartial) {
                    return recipe.matchesPartially(itemStack1, itemStack2, true);
                } else {
                    return recipe.matchesFully(itemStack1, itemStack2, true);
                }
            });
        } else {
            return false;
        }
    }
}
