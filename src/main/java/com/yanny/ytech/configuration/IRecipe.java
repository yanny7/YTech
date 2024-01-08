package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.RecipeOutput;
import org.jetbrains.annotations.NotNull;

public interface IRecipe<T extends Holder> {
    void registerRecipe(@NotNull T holder, @NotNull RecipeOutput recipeConsumer);

    static <T extends Holder> void noRecipe(T holder, RecipeOutput recipeConsumer) {}
}
