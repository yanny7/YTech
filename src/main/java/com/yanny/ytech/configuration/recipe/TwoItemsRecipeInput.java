package com.yanny.ytech.configuration.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record TwoItemsRecipeInput(ItemStack item1, ItemStack item2) implements RecipeInput {
    @NotNull
    @Override
    public ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> item1;
            case 1 -> item2;
            default -> throw new IllegalArgumentException("Recipe does not contain slot " + index);
        };
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return item1.isEmpty() && item2.isEmpty();
    }
}
