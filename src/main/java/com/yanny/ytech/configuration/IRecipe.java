package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.FinishedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface IRecipe<T extends Holder> {
    void registerRecipe(@NotNull T holder, @NotNull Consumer<FinishedRecipe> recipeConsumer);

    static <T extends Holder> void noRecipe(T holder, Consumer<FinishedRecipe> recipeConsumer) {}
}
