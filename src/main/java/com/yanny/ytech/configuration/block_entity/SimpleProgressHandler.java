package com.yanny.ytech.configuration.block_entity;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

class SimpleProgressHandler<R extends Recipe<SingleRecipeInput>> {
    private static final String TAG_ITEM = "Item";
    private static final String TAG_TIME = "Time";
    private static final String TAG_TOTAL_TIME = "TotalTime";

    private ItemStack item = ItemStack.EMPTY;
    private float cookingProgress = 0;
    private int cookingTime = 0;
    private final RecipeManager.CachedCheck<SingleRecipeInput, R> quickCheck;

    public SimpleProgressHandler(RecipeType<R> recipeType) {
        quickCheck = RecipeManager.createCheck(recipeType);
    }

    public ItemStack getItem() {
        return item;
    }

    public boolean isEmpty() {
        return item.isEmpty();
    }

    public int getProgress() {
        return Math.round(cookingProgress / cookingTime * 100);
    }

    public void clear() {
        cookingTime = 0;
        cookingProgress = 0;
        item = ItemStack.EMPTY;
    }

    public void setupCrafting(@NotNull Level level, ItemStack input, Function<R, Integer> recipeTimeGetter) {
        cookingProgress = 0;
        quickCheck.getRecipeFor(new SingleRecipeInput(input), level).ifPresent((recipe) -> {
            cookingTime = recipeTimeGetter.apply(recipe.value());
            item = input.split(1);
        });
    }

    public boolean tick(@NotNull Level level, Function<R, Boolean> canProcess, Function<R, Float> recipeStepGetter, BiConsumer<SingleRecipeInput, R> onFinish) {
        if (!item.isEmpty()) {
            SingleRecipeInput recipeInput = new SingleRecipeInput(item);
            Optional<RecipeHolder<R>> recipeHolder =  quickCheck.getRecipeFor(recipeInput, level);

            if (recipeHolder.isPresent()) {
                R recipe = recipeHolder.get().value();

                if (canProcess.apply(recipe)) {

                    cookingProgress += recipeStepGetter.apply(recipe);

                    if (cookingProgress >= cookingTime) {
                        ItemStack result = recipe.getResultItem(level.registryAccess());

                        if (result.isItemEnabled(level.enabledFeatures())) {
                            clear();
                            onFinish.accept(recipeInput, recipe);
                        }
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public void load(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        if (tag.contains(TAG_ITEM)) {
            item = ItemStack.parse(provider, tag.getCompound(TAG_ITEM)).orElse(ItemStack.EMPTY);
        } else {
            item = ItemStack.EMPTY;
        }

        if (tag.contains(TAG_TIME)) {
            cookingProgress = tag.getFloat(TAG_TIME);
        }

        if (tag.contains(TAG_TOTAL_TIME)) {
            cookingTime = tag.getInt(TAG_TOTAL_TIME);
        }
    }

    public void save(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        if (!item.isEmpty()) {
            tag.put(TAG_ITEM, item.save(provider, new CompoundTag()));
        }

        tag.putFloat(TAG_TIME, cookingProgress);
        tag.putInt(TAG_TOTAL_TIME, cookingTime);
    }
}
