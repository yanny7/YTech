package com.yanny.ytech.configuration.block_entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

class SimpleProgressHandler<R extends Recipe<Container>> {
    private static final String TAG_ITEM = "Item";
    private static final String TAG_TIME = "Time";
    private static final String TAG_TOTAL_TIME = "TotalTime";

    private ItemStack item = ItemStack.EMPTY;
    private float cookingProgress = 0;
    private int cookingTime = 0;
    private final RecipeManager.CachedCheck<Container, R> quickCheck;

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
        quickCheck.getRecipeFor(new SimpleContainer(input), level).ifPresent((recipe) -> {
            cookingTime = recipeTimeGetter.apply(recipe);
            item = input.split(1);
        });
    }

    public boolean tick(@NotNull Level level, Function<R, Boolean> canProcess, Function<R, Float> recipeStepGetter, BiConsumer<Container, R> onFinish) {
        if (!item.isEmpty()) {
            Container container = new SimpleContainer(item);
            Optional<R> recipeHolder =  quickCheck.getRecipeFor(container, level);

            if (recipeHolder.isPresent()) {
                R recipe = recipeHolder.get();

                if (canProcess.apply(recipe)) {

                    cookingProgress += recipeStepGetter.apply(recipe);

                    if (cookingProgress >= cookingTime) {
                        ItemStack result = recipe.getResultItem(level.registryAccess());

                        if (result.isItemEnabled(level.enabledFeatures())) {
                            clear();
                            onFinish.accept(container, recipe);
                        }
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public void load(@NotNull CompoundTag tag) {
        if (tag.contains(TAG_ITEM)) {
            item = ItemStack.of(tag.getCompound(TAG_ITEM));
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

    public void save(@NotNull CompoundTag tag) {
        tag.put(TAG_ITEM, item.save(new CompoundTag()));
        tag.putFloat(TAG_TIME, cookingProgress);
        tag.putInt(TAG_TOTAL_TIME, cookingTime);
    }
}
