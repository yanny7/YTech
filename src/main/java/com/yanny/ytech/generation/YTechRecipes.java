package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.configuration.MaterialItemType;
import com.yanny.ytech.configuration.SimpleItemType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.DryingRecipe;
import com.yanny.ytech.registration.Registration;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
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

        splitBySawRecipes(recipeConsumer, Items.ACACIA_PLANKS, Items.ACACIA_SLAB);
        splitBySawRecipes(recipeConsumer, Items.BIRCH_PLANKS, Items.BIRCH_SLAB);
        splitBySawRecipes(recipeConsumer, Items.CHERRY_PLANKS, Items.CHERRY_SLAB);
        splitBySawRecipes(recipeConsumer, Items.JUNGLE_PLANKS, Items.JUNGLE_SLAB);
        splitBySawRecipes(recipeConsumer, Items.OAK_PLANKS, Items.OAK_SLAB);
        splitBySawRecipes(recipeConsumer, Items.DARK_OAK_PLANKS, Items.DARK_OAK_SLAB);
        splitBySawRecipes(recipeConsumer, Items.MANGROVE_PLANKS, Items.MANGROVE_SLAB);
        splitBySawRecipes(recipeConsumer, Items.SPRUCE_PLANKS, Items.SPRUCE_SLAB);

        splitBySawRecipes(recipeConsumer, Items.ACACIA_LOG, Items.ACACIA_PLANKS);
        splitBySawRecipes(recipeConsumer, Items.BIRCH_LOG, Items.BIRCH_PLANKS);
        splitBySawRecipes(recipeConsumer, Items.CHERRY_LOG, Items.CHERRY_PLANKS);
        splitBySawRecipes(recipeConsumer, Items.JUNGLE_LOG, Items.JUNGLE_PLANKS);
        splitBySawRecipes(recipeConsumer, Items.OAK_LOG, Items.OAK_PLANKS);
        splitBySawRecipes(recipeConsumer, Items.DARK_OAK_LOG, Items.DARK_OAK_PLANKS);
        splitBySawRecipes(recipeConsumer, Items.MANGROVE_LOG, Items.MANGROVE_PLANKS);
        splitBySawRecipes(recipeConsumer, Items.SPRUCE_LOG, Items.SPRUCE_PLANKS);

        splitByHammerRecipes(recipeConsumer, Items.ANDESITE, Items.ANDESITE_SLAB);
        splitByHammerRecipes(recipeConsumer, Items.COBBLESTONE, Items.COBBLESTONE_SLAB);
        splitByHammerRecipes(recipeConsumer, Items.DIORITE, Items.DIORITE_SLAB);
        splitByHammerRecipes(recipeConsumer, Items.GRANITE, Items.GRANITE_SLAB);
        splitByHammerRecipes(recipeConsumer, Items.SMOOTH_STONE, Items.SMOOTH_STONE_SLAB);
        splitByHammerRecipes(recipeConsumer, Items.STONE, Items.STONE_SLAB);

        DryingRecipe.Builder.drying(Items.KELP, 20 * 60, Items.DRIED_KELP)
                .unlockedBy(RecipeProvider.getHasName(Items.KELP), has(Items.KELP))
                .save(recipeConsumer, Utils.modLoc(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(Items.DRIED_KELP)).getPath()));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SimpleItemType.BREAD_DOUGH.itemTag), RecipeCategory.FOOD, Items.BREAD, 0.1f, 200)
                .unlockedBy(getHasName(Registration.item(SimpleItemType.BREAD_DOUGH)), has(SimpleItemType.BREAD_DOUGH.itemTag))
                .save(recipeConsumer, Utils.modLoc(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(Items.BREAD)).getPath()));
    }

    private void splitBySawRecipes(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item input, @NotNull Item result) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result)
                .requires(input)
                .requires(MaterialItemType.SAW.groupTag)
                .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                .save(recipeConsumer, Utils.modLoc(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result)).getPath()));
    }

    private void splitByHammerRecipes(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item input, @NotNull Item result) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result)
                .requires(input)
                .requires(MaterialItemType.HAMMER.groupTag)
                .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                .save(recipeConsumer, Utils.modLoc(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result)).getPath()));
    }
}
