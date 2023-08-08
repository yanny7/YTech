package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechRecipes extends RecipeProvider {
    public YTechRecipes(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> recipeConsumer) {
        GeneralUtils.mapToStream(HOLDER.items()).forEach((holder) -> holder.object.registerRecipe(holder, recipeConsumer));
        GeneralUtils.mapToStream(HOLDER.blocks()).forEach((holder) -> holder.object.registerRecipe(holder, recipeConsumer));
        HOLDER.simpleItems().values().forEach((holder) -> holder.object.registerRecipe(holder, recipeConsumer));
        HOLDER.simpleBlocks().values().forEach((holder) -> holder.object.registerRecipe(holder, recipeConsumer));
    }
}
