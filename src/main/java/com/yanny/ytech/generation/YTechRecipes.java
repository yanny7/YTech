package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.SimpleItemType;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
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

        splitBySawRecipes(recipeConsumer, Items.ACACIA_PLANKS, Items.ACACIA_SLAB, 2);
        splitBySawRecipes(recipeConsumer, Items.BIRCH_PLANKS, Items.BIRCH_SLAB, 2);
        splitBySawRecipes(recipeConsumer, Items.CHERRY_PLANKS, Items.CHERRY_SLAB, 2);
        splitBySawRecipes(recipeConsumer, Items.JUNGLE_PLANKS, Items.JUNGLE_SLAB, 2);
        splitBySawRecipes(recipeConsumer, Items.OAK_PLANKS, Items.OAK_SLAB, 2);
        splitBySawRecipes(recipeConsumer, Items.DARK_OAK_PLANKS, Items.DARK_OAK_SLAB, 2);
        splitBySawRecipes(recipeConsumer, Items.MANGROVE_PLANKS, Items.MANGROVE_SLAB, 2);
        splitBySawRecipes(recipeConsumer, Items.SPRUCE_PLANKS, Items.SPRUCE_SLAB, 2);

        splitBySawRecipes(recipeConsumer, Items.ACACIA_LOG, Items.ACACIA_PLANKS, 2);
        splitBySawRecipes(recipeConsumer, Items.BIRCH_LOG, Items.BIRCH_PLANKS, 2);
        splitBySawRecipes(recipeConsumer, Items.CHERRY_LOG, Items.CHERRY_PLANKS, 2);
        splitBySawRecipes(recipeConsumer, Items.JUNGLE_LOG, Items.JUNGLE_PLANKS, 2);
        splitBySawRecipes(recipeConsumer, Items.OAK_LOG, Items.OAK_PLANKS, 2);
        splitBySawRecipes(recipeConsumer, Items.DARK_OAK_LOG, Items.DARK_OAK_PLANKS, 2);
        splitBySawRecipes(recipeConsumer, Items.MANGROVE_LOG, Items.MANGROVE_PLANKS, 2);
        splitBySawRecipes(recipeConsumer, Items.SPRUCE_LOG, Items.SPRUCE_PLANKS, 2);
    }

    private void splitBySawRecipes(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item input, @NotNull Item result, int count) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, count)
                .requires(input)
                .requires(SimpleItemType.FLINT_SAW.itemTag)
                .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result)).getPath()));
    }
}
