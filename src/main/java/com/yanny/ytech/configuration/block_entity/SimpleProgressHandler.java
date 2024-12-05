package com.yanny.ytech.configuration.block_entity;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

class SimpleProgressHandler<R extends Recipe<SingleRecipeInput>> {
    private static final String TAG_ITEM = "Item";
    private static final String TAG_TIME = "Time";
    private static final String TAG_TOTAL_TIME = "TotalTime";

    private ItemStack item = ItemStack.EMPTY;
    private float progress = 0;
    private int total = 0;
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
        return Math.round(progress / total * 100);
    }

    public void clear() {
        total = 0;
        progress = 0;
        item = ItemStack.EMPTY;
    }

    public boolean setupCrafting(@NotNull ServerLevel level, ItemStack input, Function<R, Integer> recipeTimeGetter) {
        Optional<RecipeHolder<R>> recipeHolder = quickCheck.getRecipeFor(new SingleRecipeInput(input), level);

        if (recipeHolder.isPresent()) {
            total = recipeTimeGetter.apply(recipeHolder.get().value());
            item = input.split(1);
            progress = 0;
            return true;
        } else {
            return false;
        }
    }

    public boolean tick(@NotNull ServerLevel level, Function<R, Boolean> canProcess, Function<R, Float> recipeStepGetter, BiConsumer<SingleRecipeInput, R> onFinish) {
        if (!item.isEmpty()) {
            SingleRecipeInput recipeInput = new SingleRecipeInput(item);
            Optional<RecipeHolder<R>> recipeHolder =  quickCheck.getRecipeFor(recipeInput, level);

            if (recipeHolder.isPresent()) {
                R recipe = recipeHolder.get().value();

                if (canProcess.apply(recipe)) {

                    progress += recipeStepGetter.apply(recipe);

                    if (progress >= total) {
                        ItemStack result = recipe.assemble(recipeInput, level.registryAccess());

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
            progress = tag.getFloat(TAG_TIME);
        } else {
            progress = 0;
        }

        if (tag.contains(TAG_TOTAL_TIME)) {
            total = tag.getInt(TAG_TOTAL_TIME);
        } else {
            total = 0;
        }
    }

    public void save(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        if (!item.isEmpty()) {
            tag.put(TAG_ITEM, item.save(provider, tag));
        }

        tag.putFloat(TAG_TIME, progress);
        tag.putInt(TAG_TOTAL_TIME, total);
    }
}
