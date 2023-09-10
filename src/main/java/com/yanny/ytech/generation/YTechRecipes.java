package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.configuration.MaterialItemType;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.SimpleItemType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.DryingRecipe;
import com.yanny.ytech.configuration.recipe.SmeltingRecipe;
import com.yanny.ytech.registration.Registration;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
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

        splitBySawRecipe(recipeConsumer, Items.ACACIA_PLANKS, Items.ACACIA_SLAB);
        splitBySawRecipe(recipeConsumer, Items.BIRCH_PLANKS, Items.BIRCH_SLAB);
        splitBySawRecipe(recipeConsumer, Items.CHERRY_PLANKS, Items.CHERRY_SLAB);
        splitBySawRecipe(recipeConsumer, Items.JUNGLE_PLANKS, Items.JUNGLE_SLAB);
        splitBySawRecipe(recipeConsumer, Items.OAK_PLANKS, Items.OAK_SLAB);
        splitBySawRecipe(recipeConsumer, Items.DARK_OAK_PLANKS, Items.DARK_OAK_SLAB);
        splitBySawRecipe(recipeConsumer, Items.MANGROVE_PLANKS, Items.MANGROVE_SLAB);
        splitBySawRecipe(recipeConsumer, Items.SPRUCE_PLANKS, Items.SPRUCE_SLAB);

        splitBySawRecipe(recipeConsumer, Items.ACACIA_LOG, Items.ACACIA_PLANKS);
        splitBySawRecipe(recipeConsumer, Items.BIRCH_LOG, Items.BIRCH_PLANKS);
        splitBySawRecipe(recipeConsumer, Items.CHERRY_LOG, Items.CHERRY_PLANKS);
        splitBySawRecipe(recipeConsumer, Items.JUNGLE_LOG, Items.JUNGLE_PLANKS);
        splitBySawRecipe(recipeConsumer, Items.OAK_LOG, Items.OAK_PLANKS);
        splitBySawRecipe(recipeConsumer, Items.DARK_OAK_LOG, Items.DARK_OAK_PLANKS);
        splitBySawRecipe(recipeConsumer, Items.MANGROVE_LOG, Items.MANGROVE_PLANKS);
        splitBySawRecipe(recipeConsumer, Items.SPRUCE_LOG, Items.SPRUCE_PLANKS);

        splitByHammerRecipe(recipeConsumer, Items.ANDESITE, Items.ANDESITE_SLAB);
        splitByHammerRecipe(recipeConsumer, Items.COBBLESTONE, Items.COBBLESTONE_SLAB);
        splitByHammerRecipe(recipeConsumer, Items.DIORITE, Items.DIORITE_SLAB);
        splitByHammerRecipe(recipeConsumer, Items.GRANITE, Items.GRANITE_SLAB);
        splitByHammerRecipe(recipeConsumer, Items.SMOOTH_STONE, Items.SMOOTH_STONE_SLAB);
        splitByHammerRecipe(recipeConsumer, Items.STONE, Items.STONE_SLAB);

        smeltingRecipe(recipeConsumer, Tags.Items.RAW_MATERIALS_COPPER, Registration.item(MaterialItemType.INGOT, MaterialType.ARSENICAL_BRONZE), 900, 600);

        DryingRecipe.Builder.drying(Items.KELP, 20 * 60, Items.DRIED_KELP)
                .unlockedBy(RecipeProvider.getHasName(Items.KELP), has(Items.KELP))
                .save(recipeConsumer, Utils.modLoc(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(Items.DRIED_KELP)).getPath()));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SimpleItemType.BREAD_DOUGH.itemTag), RecipeCategory.FOOD, Items.BREAD, 0.1f, 200)
                .unlockedBy(getHasName(Registration.item(SimpleItemType.BREAD_DOUGH)), has(SimpleItemType.BREAD_DOUGH.itemTag))
                .save(recipeConsumer, Utils.modLoc(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(Items.BREAD)).getPath()));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SimpleItemType.UNFIRED_BRICK.itemTag), RecipeCategory.MISC, Items.BRICK, 0.3f, 200)
                .unlockedBy(getHasName(Registration.item(SimpleItemType.UNFIRED_BRICK)), has(SimpleItemType.UNFIRED_BRICK.itemTag))
                .save(recipeConsumer, Utils.modLoc(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(Items.BRICK)).getPath()));
    }

    private void splitBySawRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item input, @NotNull Item result) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result)
                .requires(input)
                .requires(MaterialItemType.SAW.groupTag)
                .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                .save(recipeConsumer, Utils.modLoc(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result)).getPath()));
    }

    private void splitByHammerRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item input, @NotNull Item result) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result)
                .requires(input)
                .requires(MaterialItemType.HAMMER.groupTag)
                .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                .save(recipeConsumer, Utils.modLoc(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result)).getPath()));
    }

    private void smeltingRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull TagKey<Item> input, @NotNull Item result, int temperature, int smeltingTime) {
        SmeltingRecipe.Builder.smelting(input, temperature, smeltingTime, result)
                .unlockedBy(Utils.getHasName(), has(input))
                .save(recipeConsumer, Utils.modLoc(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result)).getPath()));
    }
}
