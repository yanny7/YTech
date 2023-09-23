package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.configuration.SimpleItemType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.*;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.yanny.ytech.configuration.MaterialItemType.*;
import static com.yanny.ytech.configuration.MaterialType.*;
import static com.yanny.ytech.configuration.SimpleItemType.*;
import static com.yanny.ytech.registration.Registration.HOLDER;
import static com.yanny.ytech.registration.Registration.item;

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

        alloyingRecipe(recipeConsumer, INGOT.itemTag.get(COPPER), 9, INGOT.itemTag.get(TIN), 1, item(INGOT, BRONZE), 10, Math.max(COPPER.meltingTemp, TIN.meltingTemp), 200);

        smeltingRecipe(recipeConsumer, CRUSHED_MATERIAL.itemTag.get(COPPER), item(INGOT, COPPER), COPPER.meltingTemp, 200);
        smeltingRecipe(recipeConsumer, CRUSHED_MATERIAL.itemTag.get(GOLD), item(INGOT, GOLD), GOLD.meltingTemp, 200);
        smeltingRecipe(recipeConsumer, CRUSHED_MATERIAL.itemTag.get(CASSITERITE), item(INGOT, TIN), CASSITERITE.meltingTemp, 200);
        smeltingRecipe(recipeConsumer, CRUSHED_MATERIAL.itemTag.get(GALENA), item(INGOT, LEAD), GALENA.meltingTemp, 200);

        hammeringRecipe(recipeConsumer, IRON_BLOOM.itemTag, Items.IRON_INGOT);

        blockHitRecipe(recipeConsumer, Items.FLINT, Tags.Items.STONE, item(SHARP_FLINT));

        dryingRecipe(recipeConsumer, Items.KELP, Items.DRIED_KELP, 1200);

        cookingRecipe(recipeConsumer, RecipeCategory.FOOD, BREAD_DOUGH, Items.BREAD, 0.1f, 200);
        cookingRecipe(recipeConsumer, RecipeCategory.MISC, UNFIRED_BRICK, Items.BRICK, 0.3f, 200);

        removeVanillaRecipes(recipeConsumer);
    }

    private void removeVanillaRecipes(@NotNull Consumer<FinishedRecipe> recipeConsumer) {
        removeVanillaRecipe(recipeConsumer, Items.WOODEN_AXE);
        removeVanillaRecipe(recipeConsumer, Items.WOODEN_HOE);
        removeVanillaRecipe(recipeConsumer, Items.WOODEN_PICKAXE);
        removeVanillaRecipe(recipeConsumer, Items.WOODEN_SHOVEL);
        removeVanillaRecipe(recipeConsumer, Items.WOODEN_SWORD);

        removeVanillaRecipe(recipeConsumer, Items.STONE_AXE);
        removeVanillaRecipe(recipeConsumer, Items.STONE_HOE);
        removeVanillaRecipe(recipeConsumer, Items.STONE_PICKAXE);
        removeVanillaRecipe(recipeConsumer, Items.STONE_SHOVEL);
        removeVanillaRecipe(recipeConsumer, Items.STONE_SWORD);

        removeVanillaRecipe(recipeConsumer, Items.ACACIA_SLAB);
        removeVanillaRecipe(recipeConsumer, Items.BIRCH_SLAB);
        removeVanillaRecipe(recipeConsumer, Items.CHERRY_SLAB);
        removeVanillaRecipe(recipeConsumer, Items.JUNGLE_SLAB);
        removeVanillaRecipe(recipeConsumer, Items.OAK_SLAB);
        removeVanillaRecipe(recipeConsumer, Items.DARK_OAK_SLAB);
        removeVanillaRecipe(recipeConsumer, Items.MANGROVE_SLAB);
        removeVanillaRecipe(recipeConsumer, Items.SPRUCE_SLAB);

        removeVanillaRecipe(recipeConsumer, Items.ANDESITE_SLAB);
        removeVanillaRecipe(recipeConsumer, Items.COBBLESTONE_SLAB);
        removeVanillaRecipe(recipeConsumer, Items.DIORITE_SLAB);
        removeVanillaRecipe(recipeConsumer, Items.GRANITE_SLAB);
        removeVanillaRecipe(recipeConsumer, Items.SMOOTH_STONE_SLAB);
        removeVanillaRecipe(recipeConsumer, Items.STONE_SLAB);

        removeVanillaRecipe(recipeConsumer, Items.ACACIA_PLANKS);
        removeVanillaRecipe(recipeConsumer, Items.BIRCH_PLANKS);
        removeVanillaRecipe(recipeConsumer, Items.CHERRY_PLANKS);
        removeVanillaRecipe(recipeConsumer, Items.JUNGLE_PLANKS);
        removeVanillaRecipe(recipeConsumer, Items.OAK_PLANKS);
        removeVanillaRecipe(recipeConsumer, Items.DARK_OAK_PLANKS);
        removeVanillaRecipe(recipeConsumer, Items.MANGROVE_PLANKS);
        removeVanillaRecipe(recipeConsumer, Items.SPRUCE_PLANKS);

        removeVanillaSmeltingBlastingRecipe(recipeConsumer, Items.COPPER_INGOT, Items.RAW_COPPER);
        removeVanillaSmeltingBlastingRecipe(recipeConsumer, Items.COPPER_INGOT, Items.COPPER_ORE);
        removeVanillaSmeltingBlastingRecipe(recipeConsumer, Items.COPPER_INGOT, Items.DEEPSLATE_COPPER_ORE);

        removeVanillaSmeltingBlastingRecipe(recipeConsumer, Items.GOLD_INGOT, Items.RAW_GOLD);
        removeVanillaSmeltingBlastingRecipe(recipeConsumer, Items.GOLD_INGOT, Items.GOLD_ORE);
        removeVanillaSmeltingBlastingRecipe(recipeConsumer, Items.GOLD_INGOT, Items.DEEPSLATE_GOLD_ORE);
        removeVanillaSmeltingBlastingRecipe(recipeConsumer, Items.GOLD_INGOT, Items.NETHER_GOLD_ORE);

        removeVanillaSmeltingBlastingRecipe(recipeConsumer, Items.IRON_INGOT, Items.RAW_IRON);
        removeVanillaSmeltingBlastingRecipe(recipeConsumer, Items.IRON_INGOT, Items.IRON_ORE);
        removeVanillaSmeltingBlastingRecipe(recipeConsumer, Items.IRON_INGOT, Items.DEEPSLATE_IRON_ORE);

        removeVanillaRecipe(recipeConsumer, Items.BREAD);
        removeVanillaRecipe(recipeConsumer, Items.BRICK);
    }

    private void removeVanillaRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, Item item) {
        SpecialRecipeBuilder.special(RecipeSerializer.TIPPED_ARROW).save(recipeConsumer, Utils.loc(item).toString());
    }

    private void removeVanillaSmeltingBlastingRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, Item to, Item from) {
        SpecialRecipeBuilder.special(RecipeSerializer.TIPPED_ARROW).save(recipeConsumer,
                Utils.mcLoc(Utils.loc(to).getPath() + "_from_smelting_" + Utils.loc(from).getPath()).toString());
        SpecialRecipeBuilder.special(RecipeSerializer.TIPPED_ARROW).save(recipeConsumer,
                Utils.mcLoc(Utils.loc(to).getPath() + "_from_blasting_" + Utils.loc(from).getPath()).toString());
    }

    private void splitBySawRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item input, @NotNull Item result) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result)
                .requires(input)
                .requires(SAW.groupTag)
                .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath()));
    }

    private void splitByHammerRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item input, @NotNull Item result) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result)
                .requires(input)
                .requires(HAMMER.groupTag)
                .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath()));
    }

    @SuppressWarnings("SameParameterValue")
    private void smeltingRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull TagKey<Item> input, @NotNull Item result, int temperature, int smeltingTime) {
        SmeltingRecipe.Builder.smelting(input, temperature, smeltingTime, result)
                .unlockedBy(Utils.getHasName(), has(input))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath() + "_from_smelting"));
    }

    @SuppressWarnings("SameParameterValue")
    private void blockHitRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item input, @NotNull TagKey<Item> block, @NotNull Item result) {
        BlockHitRecipe.Builder.blockUse(input, block, result)
                .unlockedBy(Utils.getHasName(), has(input))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath()));
    }

    @SuppressWarnings("SameParameterValue")
    private void cookingRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull RecipeCategory category,
                               @NotNull SimpleItemType input, @NotNull Item result, float xp, int cookingTime) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input.itemTag), category, result, xp, cookingTime)
                .unlockedBy(Utils.getHasName(), has(input.itemTag))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath()));
    }

    @SuppressWarnings("SameParameterValue")
    private void cookingRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull RecipeCategory category,
                               @NotNull TagKey<Item> input, @NotNull Item result, float xp, int cookingTime) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), category, result, xp, cookingTime)
                .unlockedBy(Utils.getHasName(), has(input))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath()));
    }

    @SuppressWarnings("SameParameterValue")
    private void alloyingRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull TagKey<Item> input1, int count1,
                                @NotNull TagKey<Item> input2, int count2, @NotNull Item result, int count, int temp, int smeltingTime) {
        AlloyingRecipe.Builder.alloying(input1, count1, input2, count2, temp, smeltingTime, result, count)
                .unlockedBy(Utils.getHasName(), has(input1))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath() + "_from_alloying"));
    }

    @SuppressWarnings("SameParameterValue")
    private void alloyingRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull TagKey<Item> input1, int count1,
                                @NotNull ItemLike input2, int count2, @NotNull Item result, int count, int temp, int smeltingTime) {
        AlloyingRecipe.Builder.alloying(input1, count1, input2, count2, temp, smeltingTime, result, count)
                .unlockedBy(Utils.getHasName(), has(input1))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath() + "_from_alloying"));
    }

    @SuppressWarnings("SameParameterValue")
    private void dryingRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item input, @NotNull Item result, int dryingTime) {
        DryingRecipe.Builder.drying(input, dryingTime, result)
                .unlockedBy(RecipeProvider.getHasName(input), has(input))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath()));
    }

    @SuppressWarnings("SameParameterValue")
    private void hammeringRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull TagKey<Item> input, @NotNull Item result) {
        HammeringRecipe.Builder.hammering(input, result)
                .tool(Ingredient.of(HAMMER.groupTag))
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(input))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath()));
    }
}
