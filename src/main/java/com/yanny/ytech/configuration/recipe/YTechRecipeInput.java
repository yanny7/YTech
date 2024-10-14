package com.yanny.ytech.configuration.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public class YTechRecipeInput implements RecipeInput {
    private final ItemStack[] items;

    public YTechRecipeInput(@NotNull ItemStack ...itemStacks) {
        items = itemStacks;
    }

    @NotNull
    @Override
    public ItemStack getItem(int index) {
        return items[index];
    }

    @Override
    public int size() {
        return items.length;
    }
}
