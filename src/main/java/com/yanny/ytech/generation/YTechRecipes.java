package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.configuration.SimpleItemType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.*;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
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

        /*
         * MODIFIED VANILLA RECIPES
         */

        mcSplitBySawRecipe(recipeConsumer, Items.ACACIA_PLANKS, Items.ACACIA_SLAB);
        mcSplitBySawRecipe(recipeConsumer, Items.BIRCH_PLANKS, Items.BIRCH_SLAB);
        mcSplitBySawRecipe(recipeConsumer, Items.CHERRY_PLANKS, Items.CHERRY_SLAB);
        mcSplitBySawRecipe(recipeConsumer, Items.JUNGLE_PLANKS, Items.JUNGLE_SLAB);
        mcSplitBySawRecipe(recipeConsumer, Items.OAK_PLANKS, Items.OAK_SLAB);
        mcSplitBySawRecipe(recipeConsumer, Items.DARK_OAK_PLANKS, Items.DARK_OAK_SLAB);
        mcSplitBySawRecipe(recipeConsumer, Items.MANGROVE_PLANKS, Items.MANGROVE_SLAB);
        mcSplitBySawRecipe(recipeConsumer, Items.SPRUCE_PLANKS, Items.SPRUCE_SLAB);
        mcSplitBySawRecipe(recipeConsumer, Items.CRIMSON_PLANKS, Items.CRIMSON_SLAB);
        mcSplitBySawRecipe(recipeConsumer, Items.BAMBOO_PLANKS, Items.BAMBOO_SLAB);
        mcSplitBySawRecipe(recipeConsumer, Items.WARPED_PLANKS, Items.WARPED_SLAB);

        mcSplitBySawRecipe(recipeConsumer, Items.ACACIA_LOG, Items.ACACIA_PLANKS);
        mcSplitBySawRecipe(recipeConsumer, Items.BIRCH_LOG, Items.BIRCH_PLANKS);
        mcSplitBySawRecipe(recipeConsumer, Items.CHERRY_LOG, Items.CHERRY_PLANKS);
        mcSplitBySawRecipe(recipeConsumer, Items.JUNGLE_LOG, Items.JUNGLE_PLANKS);
        mcSplitBySawRecipe(recipeConsumer, Items.OAK_LOG, Items.OAK_PLANKS);
        mcSplitBySawRecipe(recipeConsumer, Items.DARK_OAK_LOG, Items.DARK_OAK_PLANKS);
        mcSplitBySawRecipe(recipeConsumer, Items.MANGROVE_LOG, Items.MANGROVE_PLANKS);
        mcSplitBySawRecipe(recipeConsumer, Items.SPRUCE_LOG, Items.SPRUCE_PLANKS);
        mcSplitBySawRecipe(recipeConsumer, Items.CRIMSON_STEM, Items.CRIMSON_PLANKS);
        mcSplitBySawRecipe(recipeConsumer, Items.BAMBOO_BLOCK, Items.BAMBOO_PLANKS);
        mcSplitBySawRecipe(recipeConsumer, Items.WARPED_STEM, Items.WARPED_PLANKS);

        mcSplitByAxeRecipe(recipeConsumer, Items.ACACIA_PLANKS, Items.ACACIA_SLAB);
        mcSplitByAxeRecipe(recipeConsumer, Items.BIRCH_PLANKS, Items.BIRCH_SLAB);
        mcSplitByAxeRecipe(recipeConsumer, Items.CHERRY_PLANKS, Items.CHERRY_SLAB);
        mcSplitByAxeRecipe(recipeConsumer, Items.JUNGLE_PLANKS, Items.JUNGLE_SLAB);
        mcSplitByAxeRecipe(recipeConsumer, Items.OAK_PLANKS, Items.OAK_SLAB);
        mcSplitByAxeRecipe(recipeConsumer, Items.DARK_OAK_PLANKS, Items.DARK_OAK_SLAB);
        mcSplitByAxeRecipe(recipeConsumer, Items.MANGROVE_PLANKS, Items.MANGROVE_SLAB);
        mcSplitByAxeRecipe(recipeConsumer, Items.SPRUCE_PLANKS, Items.SPRUCE_SLAB);
        mcSplitByAxeRecipe(recipeConsumer, Items.CRIMSON_PLANKS, Items.CRIMSON_SLAB);
        mcSplitByAxeRecipe(recipeConsumer, Items.BAMBOO_PLANKS, Items.BAMBOO_SLAB);
        mcSplitByAxeRecipe(recipeConsumer, Items.WARPED_PLANKS, Items.WARPED_SLAB);

        mcSplitByAxeRecipe(recipeConsumer, Items.ACACIA_LOG, Items.ACACIA_PLANKS);
        mcSplitByAxeRecipe(recipeConsumer, Items.BIRCH_LOG, Items.BIRCH_PLANKS);
        mcSplitByAxeRecipe(recipeConsumer, Items.CHERRY_LOG, Items.CHERRY_PLANKS);
        mcSplitByAxeRecipe(recipeConsumer, Items.JUNGLE_LOG, Items.JUNGLE_PLANKS);
        mcSplitByAxeRecipe(recipeConsumer, Items.OAK_LOG, Items.OAK_PLANKS);
        mcSplitByAxeRecipe(recipeConsumer, Items.DARK_OAK_LOG, Items.DARK_OAK_PLANKS);
        mcSplitByAxeRecipe(recipeConsumer, Items.MANGROVE_LOG, Items.MANGROVE_PLANKS);
        mcSplitByAxeRecipe(recipeConsumer, Items.SPRUCE_LOG, Items.SPRUCE_PLANKS);
        mcSplitByAxeRecipe(recipeConsumer, Items.CRIMSON_STEM, Items.CRIMSON_PLANKS);
        mcSplitByAxeRecipe(recipeConsumer, Items.BAMBOO_BLOCK, Items.BAMBOO_PLANKS);
        mcSplitByAxeRecipe(recipeConsumer, Items.WARPED_STEM, Items.WARPED_PLANKS);

        mcFenceRecipe(recipeConsumer, Items.ACACIA_PLANKS, Items.ACACIA_FENCE);
        mcFenceRecipe(recipeConsumer, Items.BIRCH_PLANKS, Items.BIRCH_FENCE);
        mcFenceRecipe(recipeConsumer, Items.CHERRY_PLANKS, Items.CHERRY_FENCE);
        mcFenceRecipe(recipeConsumer, Items.JUNGLE_PLANKS, Items.JUNGLE_FENCE);
        mcFenceRecipe(recipeConsumer, Items.OAK_PLANKS, Items.OAK_FENCE);
        mcFenceRecipe(recipeConsumer, Items.DARK_OAK_PLANKS, Items.DARK_OAK_FENCE);
        mcFenceRecipe(recipeConsumer, Items.MANGROVE_PLANKS, Items.MANGROVE_FENCE);
        mcFenceRecipe(recipeConsumer, Items.SPRUCE_PLANKS, Items.SPRUCE_FENCE);
        mcFenceRecipe(recipeConsumer, Items.CRIMSON_PLANKS, Items.CRIMSON_FENCE);
        mcFenceRecipe(recipeConsumer, Items.BAMBOO_PLANKS, Items.BAMBOO_FENCE);
        mcFenceRecipe(recipeConsumer, Items.WARPED_PLANKS, Items.WARPED_FENCE);

        mcFenceGateRecipe(recipeConsumer, Items.ACACIA_PLANKS, Items.ACACIA_FENCE_GATE);
        mcFenceGateRecipe(recipeConsumer, Items.BIRCH_PLANKS, Items.BIRCH_FENCE_GATE);
        mcFenceGateRecipe(recipeConsumer, Items.CHERRY_PLANKS, Items.CHERRY_FENCE_GATE);
        mcFenceGateRecipe(recipeConsumer, Items.JUNGLE_PLANKS, Items.JUNGLE_FENCE_GATE);
        mcFenceGateRecipe(recipeConsumer, Items.OAK_PLANKS, Items.OAK_FENCE_GATE);
        mcFenceGateRecipe(recipeConsumer, Items.DARK_OAK_PLANKS, Items.DARK_OAK_FENCE_GATE);
        mcFenceGateRecipe(recipeConsumer, Items.MANGROVE_PLANKS, Items.MANGROVE_FENCE_GATE);
        mcFenceGateRecipe(recipeConsumer, Items.SPRUCE_PLANKS, Items.SPRUCE_FENCE_GATE);
        mcFenceGateRecipe(recipeConsumer, Items.CRIMSON_PLANKS, Items.CRIMSON_FENCE_GATE);
        mcFenceGateRecipe(recipeConsumer, Items.BAMBOO_PLANKS, Items.BAMBOO_FENCE_GATE);
        mcFenceGateRecipe(recipeConsumer, Items.WARPED_PLANKS, Items.WARPED_FENCE_GATE);

        mcDoorRecipe(recipeConsumer, Items.ACACIA_PLANKS, Items.ACACIA_DOOR);
        mcDoorRecipe(recipeConsumer, Items.BIRCH_PLANKS, Items.BIRCH_DOOR);
        mcDoorRecipe(recipeConsumer, Items.CHERRY_PLANKS, Items.CHERRY_DOOR);
        mcDoorRecipe(recipeConsumer, Items.JUNGLE_PLANKS, Items.JUNGLE_DOOR);
        mcDoorRecipe(recipeConsumer, Items.OAK_PLANKS, Items.OAK_DOOR);
        mcDoorRecipe(recipeConsumer, Items.DARK_OAK_PLANKS, Items.DARK_OAK_DOOR);
        mcDoorRecipe(recipeConsumer, Items.MANGROVE_PLANKS, Items.MANGROVE_DOOR);
        mcDoorRecipe(recipeConsumer, Items.SPRUCE_PLANKS, Items.SPRUCE_DOOR);
        mcDoorRecipe(recipeConsumer, Items.CRIMSON_PLANKS, Items.CRIMSON_DOOR);
        mcDoorRecipe(recipeConsumer, Items.BAMBOO_PLANKS, Items.BAMBOO_DOOR);
        mcDoorRecipe(recipeConsumer, Items.WARPED_PLANKS, Items.WARPED_DOOR);

        mcTrapdoorRecipe(recipeConsumer, Items.ACACIA_PLANKS, Items.ACACIA_TRAPDOOR);
        mcTrapdoorRecipe(recipeConsumer, Items.BIRCH_PLANKS, Items.BIRCH_TRAPDOOR);
        mcTrapdoorRecipe(recipeConsumer, Items.CHERRY_PLANKS, Items.CHERRY_TRAPDOOR);
        mcTrapdoorRecipe(recipeConsumer, Items.JUNGLE_PLANKS, Items.JUNGLE_TRAPDOOR);
        mcTrapdoorRecipe(recipeConsumer, Items.OAK_PLANKS, Items.OAK_TRAPDOOR);
        mcTrapdoorRecipe(recipeConsumer, Items.DARK_OAK_PLANKS, Items.DARK_OAK_TRAPDOOR);
        mcTrapdoorRecipe(recipeConsumer, Items.MANGROVE_PLANKS, Items.MANGROVE_TRAPDOOR);
        mcTrapdoorRecipe(recipeConsumer, Items.SPRUCE_PLANKS, Items.SPRUCE_TRAPDOOR);
        mcTrapdoorRecipe(recipeConsumer, Items.CRIMSON_PLANKS, Items.CRIMSON_TRAPDOOR);
        mcTrapdoorRecipe(recipeConsumer, Items.BAMBOO_PLANKS, Items.BAMBOO_TRAPDOOR);
        mcTrapdoorRecipe(recipeConsumer, Items.WARPED_PLANKS, Items.WARPED_TRAPDOOR);

        mcPressurePlateRecipe(recipeConsumer, Items.ACACIA_PLANKS, Items.ACACIA_PRESSURE_PLATE);
        mcPressurePlateRecipe(recipeConsumer, Items.BIRCH_PLANKS, Items.BIRCH_PRESSURE_PLATE);
        mcPressurePlateRecipe(recipeConsumer, Items.CHERRY_PLANKS, Items.CHERRY_PRESSURE_PLATE);
        mcPressurePlateRecipe(recipeConsumer, Items.JUNGLE_PLANKS, Items.JUNGLE_PRESSURE_PLATE);
        mcPressurePlateRecipe(recipeConsumer, Items.OAK_PLANKS, Items.OAK_PRESSURE_PLATE);
        mcPressurePlateRecipe(recipeConsumer, Items.DARK_OAK_PLANKS, Items.DARK_OAK_PRESSURE_PLATE);
        mcPressurePlateRecipe(recipeConsumer, Items.MANGROVE_PLANKS, Items.MANGROVE_PRESSURE_PLATE);
        mcPressurePlateRecipe(recipeConsumer, Items.SPRUCE_PLANKS, Items.SPRUCE_PRESSURE_PLATE);
        mcPressurePlateRecipe(recipeConsumer, Items.CRIMSON_PLANKS, Items.CRIMSON_PRESSURE_PLATE);
        mcPressurePlateRecipe(recipeConsumer, Items.BAMBOO_PLANKS, Items.BAMBOO_PRESSURE_PLATE);
        mcPressurePlateRecipe(recipeConsumer, Items.WARPED_PLANKS, Items.WARPED_PRESSURE_PLATE);

        mcButtonRecipe(recipeConsumer, Items.ACACIA_PLANKS, Items.ACACIA_BUTTON);
        mcButtonRecipe(recipeConsumer, Items.BIRCH_PLANKS, Items.BIRCH_BUTTON);
        mcButtonRecipe(recipeConsumer, Items.CHERRY_PLANKS, Items.CHERRY_BUTTON);
        mcButtonRecipe(recipeConsumer, Items.JUNGLE_PLANKS, Items.JUNGLE_BUTTON);
        mcButtonRecipe(recipeConsumer, Items.OAK_PLANKS, Items.OAK_BUTTON);
        mcButtonRecipe(recipeConsumer, Items.DARK_OAK_PLANKS, Items.DARK_OAK_BUTTON);
        mcButtonRecipe(recipeConsumer, Items.MANGROVE_PLANKS, Items.MANGROVE_BUTTON);
        mcButtonRecipe(recipeConsumer, Items.SPRUCE_PLANKS, Items.SPRUCE_BUTTON);
        mcButtonRecipe(recipeConsumer, Items.CRIMSON_PLANKS, Items.CRIMSON_BUTTON);
        mcButtonRecipe(recipeConsumer, Items.BAMBOO_PLANKS, Items.BAMBOO_BUTTON);
        mcButtonRecipe(recipeConsumer, Items.WARPED_PLANKS, Items.WARPED_BUTTON);

        mcBedRecipe(recipeConsumer, Items.BLACK_WOOL, Items.BLACK_BED);
        mcBedRecipe(recipeConsumer, Items.BLUE_WOOL, Items.BLUE_BED);
        mcBedRecipe(recipeConsumer, Items.BROWN_WOOL, Items.BROWN_BED);
        mcBedRecipe(recipeConsumer, Items.WHITE_WOOL, Items.WHITE_BED);
        mcBedRecipe(recipeConsumer, Items.CYAN_WOOL, Items.CYAN_BED);
        mcBedRecipe(recipeConsumer, Items.GRAY_WOOL, Items.GRAY_BED);
        mcBedRecipe(recipeConsumer, Items.GREEN_WOOL, Items.GREEN_BED);
        mcBedRecipe(recipeConsumer, Items.LIME_WOOL, Items.LIME_BED);
        mcBedRecipe(recipeConsumer, Items.MAGENTA_WOOL, Items.MAGENTA_BED);
        mcBedRecipe(recipeConsumer, Items.ORANGE_WOOL, Items.ORANGE_BED);
        mcBedRecipe(recipeConsumer, Items.PINK_WOOL, Items.PINK_BED);
        mcBedRecipe(recipeConsumer, Items.PURPLE_WOOL, Items.PURPLE_BED);
        mcBedRecipe(recipeConsumer, Items.RED_WOOL, Items.RED_BED);
        mcBedRecipe(recipeConsumer, Items.YELLOW_WOOL, Items.YELLOW_BED);
        mcBedRecipe(recipeConsumer, Items.LIGHT_BLUE_WOOL, Items.LIGHT_BLUE_BED);
        mcBedRecipe(recipeConsumer, Items.LIGHT_GRAY_WOOL, Items.LIGHT_GRAY_BED);

        mcBoatRecipe(recipeConsumer, Items.ACACIA_PLANKS, Items.ACACIA_BOAT);
        mcBoatRecipe(recipeConsumer, Items.BIRCH_PLANKS, Items.BIRCH_BOAT);
        mcBoatRecipe(recipeConsumer, Items.CHERRY_PLANKS, Items.CHERRY_BOAT);
        mcBoatRecipe(recipeConsumer, Items.JUNGLE_PLANKS, Items.JUNGLE_BOAT);
        mcBoatRecipe(recipeConsumer, Items.OAK_PLANKS, Items.OAK_BOAT);
        mcBoatRecipe(recipeConsumer, Items.DARK_OAK_PLANKS, Items.DARK_OAK_BOAT);
        mcBoatRecipe(recipeConsumer, Items.MANGROVE_PLANKS, Items.MANGROVE_BOAT);
        mcBoatRecipe(recipeConsumer, Items.SPRUCE_PLANKS, Items.SPRUCE_BOAT);

        mcSplitByHammerRecipe(recipeConsumer, Items.ANDESITE, Items.ANDESITE_SLAB);
        mcSplitByHammerRecipe(recipeConsumer, Items.COBBLESTONE, Items.COBBLESTONE_SLAB);
        mcSplitByHammerRecipe(recipeConsumer, Items.DIORITE, Items.DIORITE_SLAB);
        mcSplitByHammerRecipe(recipeConsumer, Items.GRANITE, Items.GRANITE_SLAB);
        mcSplitByHammerRecipe(recipeConsumer, Items.SMOOTH_STONE, Items.SMOOTH_STONE_SLAB);
        mcSplitByHammerRecipe(recipeConsumer, Items.STONE, Items.STONE_SLAB);

        mcCookingRecipe(recipeConsumer, RecipeCategory.FOOD, BREAD_DOUGH, Items.BREAD, 0.1f, 200);
        mcCookingRecipe(recipeConsumer, RecipeCategory.MISC, UNFIRED_BRICK, Items.BRICK, 0.3f, 200);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.WOODEN_SHOVEL)
                .define('#', WOODEN_PLATE.itemTag)
                .define('S', Items.STICK)
                .pattern("#")
                .pattern("S")
                .pattern("S")
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(recipeConsumer, Utils.loc(Items.WOODEN_SHOVEL));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CHEST)
                .define('#', WOODEN_PLATE.itemTag)
                .define('B', WOODEN_BOLT.itemTag)
                .define('S', SAW.groupTag)
                .pattern("B#B")
                .pattern("#S#")
                .pattern("B#B")
                .unlockedBy(Utils.getHasName(), has(WOODEN_PLATE.itemTag))
                .save(recipeConsumer, Utils.loc(Items.CHEST));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.LEATHER_BOOTS)
                .define('#', FLINT_KNIFE.itemTag)
                .define('S', LEATHER_STRIPS.itemTag)
                .define('L', Items.LEATHER)
                .pattern(" # ")
                .pattern("LSL")
                .pattern("LSL")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(recipeConsumer, Utils.loc(Items.LEATHER_BOOTS));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.LEATHER_HELMET)
                .define('#', FLINT_KNIFE.itemTag)
                .define('S', LEATHER_STRIPS.itemTag)
                .define('L', Items.LEATHER)
                .pattern(" # ")
                .pattern("LLL")
                .pattern("LSL")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(recipeConsumer, Utils.loc(Items.LEATHER_HELMET));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.LEATHER_LEGGINGS)
                .define('#', FLINT_KNIFE.itemTag)
                .define('S', LEATHER_STRIPS.itemTag)
                .define('L', Items.LEATHER)
                .pattern("LLL")
                .pattern("L#L")
                .pattern("LSL")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(recipeConsumer, Utils.loc(Items.LEATHER_LEGGINGS));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.LEATHER_CHESTPLATE)
                .define('#', FLINT_KNIFE.itemTag)
                .define('S', LEATHER_STRIPS.itemTag)
                .define('L', Items.LEATHER)
                .pattern("L#L")
                .pattern("LSL")
                .pattern("LLL")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(recipeConsumer, Utils.loc(Items.LEATHER_CHESTPLATE));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.LEATHER_HORSE_ARMOR)
                .define('#', FLINT_KNIFE.itemTag)
                .define('S', LEATHER_STRIPS.itemTag)
                .define('L', Items.LEATHER)
                .pattern("L#L")
                .pattern("LLL")
                .pattern("LSL")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(recipeConsumer, Utils.loc(Items.LEATHER_HORSE_ARMOR));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BOW)
                .define('#', FLINT_KNIFE.itemTag)
                .define('S', Items.STICK)
                .define('W', Items.STRING)
                .pattern(" SW")
                .pattern("S#W")
                .pattern(" SW")
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(recipeConsumer, Utils.loc(Items.BOW));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BOWL)
                .define('#', FLINT_KNIFE.itemTag)
                .define('P', ItemTags.PLANKS)
                .pattern("P#P")
                .pattern(" P ")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(recipeConsumer, Utils.loc(Items.BOWL));

        /*
         * MOD RECIPES
         */

        alloyingRecipe(recipeConsumer, INGOT.itemTag.get(COPPER), 9, INGOT.itemTag.get(TIN), 1, item(INGOT, BRONZE), 10, Math.max(COPPER.meltingTemp, TIN.meltingTemp), 200);

        smeltingRecipe(recipeConsumer, CRUSHED_MATERIAL.itemTag.get(COPPER), item(INGOT, COPPER), COPPER.meltingTemp, 200);
        smeltingRecipe(recipeConsumer, CRUSHED_MATERIAL.itemTag.get(GOLD), item(INGOT, GOLD), GOLD.meltingTemp, 200);
        smeltingRecipe(recipeConsumer, CRUSHED_MATERIAL.itemTag.get(CASSITERITE), item(INGOT, TIN), CASSITERITE.meltingTemp, 200);
        smeltingRecipe(recipeConsumer, CRUSHED_MATERIAL.itemTag.get(GALENA), item(INGOT, LEAD), GALENA.meltingTemp, 200);

        hammeringRecipe(recipeConsumer, IRON_BLOOM.itemTag, Items.IRON_INGOT);

        blockHitRecipe(recipeConsumer, Items.FLINT, Tags.Items.STONE, item(SHARP_FLINT));

        dryingRecipe(recipeConsumer, Items.KELP, Items.DRIED_KELP, 1200);

        removeVanillaRecipes(recipeConsumer);
    }

    private void removeVanillaRecipes(@NotNull Consumer<FinishedRecipe> recipeConsumer) {
        removeVanillaRecipe(recipeConsumer, Items.WOODEN_AXE);
        removeVanillaRecipe(recipeConsumer, Items.WOODEN_HOE);
        removeVanillaRecipe(recipeConsumer, Items.WOODEN_PICKAXE);
        removeVanillaRecipe(recipeConsumer, Items.WOODEN_SWORD);

        removeVanillaRecipe(recipeConsumer, Items.STONE_AXE);
        removeVanillaRecipe(recipeConsumer, Items.STONE_PICKAXE);
        removeVanillaRecipe(recipeConsumer, Items.STONE_SHOVEL);
        removeVanillaRecipe(recipeConsumer, Items.STONE_SWORD);

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

    private void mcSplitBySawRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item input, @NotNull Item result) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 2)
                .requires(input)
                .requires(SAW.groupTag)
                .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcSplitByAxeRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item input, @NotNull Item result) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result)
                .requires(input)
                .requires(AXE.groupTag)
                .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath()));
    }

    private void mcSplitByHammerRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item input, @NotNull Item result) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result)
                .requires(input)
                .requires(HAMMER.groupTag)
                .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcFenceRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item planks, @NotNull Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 2)
                .define('H', HAMMER.groupTag)
                .define('W', SAW.groupTag)
                .define('P', planks)
                .define('S', Items.STICK)
                .pattern("H W")
                .pattern("PSP")
                .pattern("PSP")
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcFenceGateRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item planks, @NotNull Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 2)
                .define('H', HAMMER.groupTag)
                .define('W', SAW.groupTag)
                .define('B', WOODEN_BOLT.itemTag)
                .define('P', planks)
                .define('S', Items.STICK)
                .pattern("H W")
                .pattern("BPB")
                .pattern("SPS")
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcDoorRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item planks, @NotNull Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result)
                .define('H', HAMMER.groupTag)
                .define('W', SAW.groupTag)
                .define('B', WOODEN_BOLT.itemTag)
                .define('P', planks)
                .pattern("PPH")
                .pattern("PPB")
                .pattern("PPW")
                .unlockedBy(getHasName(planks), has(planks))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcTrapdoorRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item planks, @NotNull Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 2)
                .define('H', HAMMER.groupTag)
                .define('W', SAW.groupTag)
                .define('B', WOODEN_BOLT.itemTag)
                .define('P', planks)
                .pattern("HBW")
                .pattern("PPP")
                .pattern("PPP")
                .unlockedBy(getHasName(planks), has(planks))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcPressurePlateRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item planks, @NotNull Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result)
                .define('H', HAMMER.groupTag)
                .define('W', SAW.groupTag)
                .define('B', WOODEN_BOLT.itemTag)
                .define('P', planks)
                .pattern("H W")
                .pattern("BBB")
                .pattern("PPP")
                .unlockedBy(getHasName(planks), has(planks))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcButtonRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item planks, @NotNull Item result) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, result)
                .requires(HAMMER.groupTag)
                .requires(SAW.groupTag)
                .requires(planks)
                .unlockedBy(getHasName(planks), has(planks))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcBedRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item wool, @NotNull Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result)
                .define('H', HAMMER.groupTag)
                .define('S', SAW.groupTag)
                .define('W', wool)
                .define('P', ItemTags.PLANKS)
                .pattern("H S")
                .pattern("WWW")
                .pattern("PPP")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcBoatRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull Item planks, @NotNull Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result)
                .define('H', HAMMER.groupTag)
                .define('S', SAW.groupTag)
                .define('F', FILE.groupTag)
                .define('I', ROD.groupTag)
                .define('P', planks)
                .pattern("HFS")
                .pattern("PIP")
                .pattern("PPP")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(recipeConsumer, Utils.loc(result));
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
    private void mcCookingRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull RecipeCategory category,
                               @NotNull SimpleItemType input, @NotNull Item result, float xp, int cookingTime) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input.itemTag), category, result, xp, cookingTime)
                .unlockedBy(Utils.getHasName(), has(input.itemTag))
                .save(recipeConsumer, Utils.loc(result));
    }

    @SuppressWarnings("SameParameterValue")
    private void alloyingRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer, @NotNull TagKey<Item> input1, int count1,
                                @NotNull TagKey<Item> input2, int count2, @NotNull Item result, int count, int temp, int smeltingTime) {
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
