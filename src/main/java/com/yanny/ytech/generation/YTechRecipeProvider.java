package com.yanny.ytech.generation;

import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block.*;
import com.yanny.ytech.configuration.recipe.*;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItemTags;
import com.yanny.ytech.registration.YTechItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.TippedArrowRecipe;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static com.yanny.ytech.configuration.MaterialType.*;

class YTechRecipeProvider extends RecipeProvider {
    public YTechRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeConsumer) {
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
        mcBoatRecipe(recipeConsumer, Items.BAMBOO_PLANKS, Items.BAMBOO_RAFT);

        mcSplitByHammerRecipe(recipeConsumer, Items.ANDESITE, Items.ANDESITE_SLAB);
        mcSplitByHammerRecipe(recipeConsumer, Items.COBBLESTONE, Items.COBBLESTONE_SLAB);
        mcSplitByHammerRecipe(recipeConsumer, Items.DIORITE, Items.DIORITE_SLAB);
        mcSplitByHammerRecipe(recipeConsumer, Items.GRANITE, Items.GRANITE_SLAB);
        mcSplitByHammerRecipe(recipeConsumer, Items.SMOOTH_STONE, Items.SMOOTH_STONE_SLAB);
        mcSplitByHammerRecipe(recipeConsumer, Items.STONE, Items.STONE_SLAB);

        mcHorseArmorRecipe(recipeConsumer, Items.LEATHER, Items.LEATHER_HORSE_ARMOR);
        mcHorseArmorRecipe(recipeConsumer, GOLD, Items.GOLDEN_HORSE_ARMOR);
        mcHorseArmorRecipe(recipeConsumer, IRON, Items.IRON_HORSE_ARMOR);

        mcCookingRecipe(recipeConsumer, RecipeCategory.FOOD, YTechItemTags.BREAD_DOUGHS, Items.BREAD, 0.1f, 200);
        mcCookingRecipe(recipeConsumer, RecipeCategory.MISC, YTechItemTags.UNFIRED_BRICKS, Items.BRICK, 0.3f, 200);

        RemainingShapedRecipe.Builder.shaped(RecipeCategory.TOOLS, Items.WOODEN_SHOVEL)
                .define('#', YTechItemTags.PLATES.of(MaterialType.WOODEN))
                .define('S', Items.STICK)
                .pattern("#")
                .pattern("S")
                .pattern("S")
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(recipeConsumer, Utils.loc(Items.WOODEN_SHOVEL));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.DECORATIONS, Items.CHEST)
                .define('#', YTechItemTags.PLATES.of(MaterialType.WOODEN))
                .define('B', YTechItemTags.BOLTS.of(MaterialType.WOODEN))
                .define('S', YTechItemTags.SAWS.tag)
                .pattern("B#B")
                .pattern("#S#")
                .pattern("B#B")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.of(MaterialType.WOODEN)))
                .save(recipeConsumer, Utils.loc(Items.CHEST));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.COMBAT, Items.LEATHER_BOOTS)
                .define('#', YTechItemTags.KNIVES.tag)
                .define('S', YTechItemTags.LEATHER_STRIPS)
                .define('L', Items.LEATHER)
                .pattern(" # ")
                .pattern("LSL")
                .pattern("LSL")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(recipeConsumer, Utils.loc(Items.LEATHER_BOOTS));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.COMBAT, Items.LEATHER_HELMET)
                .define('#', YTechItemTags.KNIVES.tag)
                .define('S', YTechItemTags.LEATHER_STRIPS)
                .define('L', Items.LEATHER)
                .pattern(" # ")
                .pattern("LLL")
                .pattern("LSL")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(recipeConsumer, Utils.loc(Items.LEATHER_HELMET));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.COMBAT, Items.LEATHER_LEGGINGS)
                .define('#', YTechItemTags.KNIVES.tag)
                .define('S', YTechItemTags.LEATHER_STRIPS)
                .define('L', Items.LEATHER)
                .pattern("LLL")
                .pattern("L#L")
                .pattern("LSL")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(recipeConsumer, Utils.loc(Items.LEATHER_LEGGINGS));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.COMBAT, Items.LEATHER_CHESTPLATE)
                .define('#', YTechItemTags.KNIVES.tag)
                .define('S', YTechItemTags.LEATHER_STRIPS)
                .define('L', Items.LEATHER)
                .pattern("L#L")
                .pattern("LSL")
                .pattern("LLL")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(recipeConsumer, Utils.loc(Items.LEATHER_CHESTPLATE));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.COMBAT, Items.BOW)
                .define('#', YTechItemTags.KNIVES.tag)
                .define('S', Items.STICK)
                .define('W', Items.STRING)
                .pattern(" SW")
                .pattern("S#W")
                .pattern(" SW")
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(recipeConsumer, Utils.loc(Items.BOW));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, Items.BOWL)
                .define('#', YTechItemTags.KNIVES.tag)
                .define('P', ItemTags.PLANKS)
                .pattern("P#P")
                .pattern(" P ")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(recipeConsumer, Utils.loc(Items.BOWL));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.DECORATIONS, Items.CRAFTING_TABLE)
                .define('$', ItemTags.AXES)
                .define('F', Items.FLINT)
                .define('P', ItemTags.PLANKS)
                .pattern("$F")
                .pattern("PP")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(recipeConsumer, Utils.loc(Items.CRAFTING_TABLE));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.DECORATIONS, Items.FURNACE)
                .define('C', ItemTags.STONE_CRAFTING_MATERIALS)
                .define('F', Items.CAMPFIRE)
                .define('S', Items.COBBLESTONE_SLAB)
                .define('H', YTechItemTags.HAMMERS.tag)
                .pattern("SHS")
                .pattern("CFC")
                .pattern("CCC")
                .unlockedBy(RecipeProvider.getHasName(Items.COBBLESTONE), has(ItemTags.STONE_CRAFTING_MATERIALS))
                .save(recipeConsumer, Utils.loc(Items.FURNACE));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.DECORATIONS, Items.IRON_BARS)
                .define('C', YTechItemTags.RODS.of(IRON))
                .pattern("CCC")
                .pattern("CCC")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.of(IRON)))
                .save(recipeConsumer, Utils.loc(Items.IRON_BARS));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.REDSTONE, Items.IRON_DOOR)
                .define('C', YTechItemTags.PLATES.of(IRON))
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('B', YTechItemTags.BOLTS.of(IRON))
                .pattern("CCH")
                .pattern("CCB")
                .pattern("CC ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.of(IRON)))
                .save(recipeConsumer, Utils.loc(Items.IRON_DOOR));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.REDSTONE, Items.IRON_TRAPDOOR, 2)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('B', YTechItemTags.BOLTS.of(MaterialType.IRON))
                .define('P', YTechItemTags.PLATES.of(IRON))
                .pattern("HB ")
                .pattern("PPP")
                .pattern("PPP")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.of(IRON)))
                .save(recipeConsumer, Utils.loc(Items.IRON_TRAPDOOR));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.REDSTONE, Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .define('W', YTechItemTags.HAMMERS.tag)
                .define('B', YTechItemTags.BOLTS.of(IRON))
                .define('P', YTechItemTags.PLATES.of(IRON))
                .pattern(" W ")
                .pattern("PPP")
                .pattern("BBB")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.of(IRON)))
                .save(recipeConsumer, Utils.loc(Items.HEAVY_WEIGHTED_PRESSURE_PLATE));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.REDSTONE, Items.CHAIN)
                .define('W', YTechItemTags.HAMMERS.tag)
                .define('I', YTechItemTags.RODS.of(IRON))
                .pattern("IW")
                .pattern("I ")
                .pattern("I ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.of(IRON)))
                .save(recipeConsumer, Utils.loc(Items.CHAIN));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, Items.LANTERN)
                .define('I', YTechItemTags.RODS.tag)
                .define('P', YTechItemTags.PLATES.tag)
                .define('T', Items.TORCH)
                .pattern(" P ")
                .pattern("ITI")
                .pattern(" P ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.of(IRON)))
                .save(recipeConsumer, Utils.loc(Items.LANTERN));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, Items.SOUL_LANTERN)
                .define('I', YTechItemTags.RODS.tag)
                .define('P', YTechItemTags.PLATES.tag)
                .define('T', Items.SOUL_TORCH)
                .pattern(" P ")
                .pattern("ITI")
                .pattern(" P ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.of(IRON)))
                .save(recipeConsumer, Utils.loc(Items.SOUL_LANTERN));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, Items.STONECUTTER)
                .define('I', YTechItemTags.RODS.of(IRON))
                .define('B', YTechItemTags.BOLTS.of(IRON))
                .define('W', YTechItemTags.SAW_BLADES.of(IRON))
                .define('S', Items.STONE)
                .pattern("IWB")
                .pattern("SSS")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.of(IRON)))
                .save(recipeConsumer, Utils.loc(Items.STONECUTTER));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, Items.BARREL)
                .define('P', YTechItemTags.PLATES.of(WOODEN))
                .define('S', ItemTags.PLANKS)
                .define('A', YTechItemTags.AXES.tag)
                .pattern("SPS")
                .pattern("SAS")
                .pattern("SPS")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(recipeConsumer, Utils.loc(Items.BARREL));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.TOOLS, Items.FISHING_ROD)
                .define('B', YTechItemTags.BOLTS.tag)
                .define('S', Items.STICK)
                .define('T', Items.STRING)
                .pattern("  S")
                .pattern(" ST")
                .pattern("S B")
                .unlockedBy(Utils.getHasName(), has(Items.STICK))
                .save(recipeConsumer, Utils.loc(Items.FISHING_ROD));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.TOOLS, Items.LEAD)
                .define('L', YTechItemTags.LEATHER_STRIPS)
                .define('S', Items.STRING)
                .pattern("LL ")
                .pattern("LS ")
                .pattern("  L")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.LEATHER_STRIPS))
                .save(recipeConsumer, Utils.loc(Items.LEAD));
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, Items.SADDLE)
                .define('L', Items.LEATHER)
                .define('S', YTechItemTags.LEATHER_STRIPS)
                .define('I', YTechItemTags.RODS.of(IRON))
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('K', YTechItemTags.KNIVES.tag)
                .pattern("LLL")
                .pattern("LSL")
                .pattern("HIK")
                .unlockedBy(Utils.getHasName(), has(Items.LEATHER))
                .save(recipeConsumer, Utils.loc(Items.SADDLE));
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL)
                .requires(Items.BONE)
                .requires(YTechItemTags.MORTAR_AND_PESTLES.tag)
                .unlockedBy(RecipeProvider.getHasName(Items.BONE), RecipeProvider.has(Items.BONE))
                .save(recipeConsumer, Utils.loc(Items.BONE_MEAL));
        MillingRecipe.Builder.milling(Items.BONE, Items.BONE_MEAL, 2)
                .bonusChance(0.2f)
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(Items.BONE))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(Items.BONE_MEAL).getPath() + "_from_milling"));

        /*
         * MOD RECIPES
         */

        registerBasketRecipe(recipeConsumer);
        registerBreadDoughRecipe(recipeConsumer);
        registerBrickMoldRecipe(recipeConsumer);
        registerClayBucketRecipe(recipeConsumer);
        registerCookedVenisonRecipe(recipeConsumer);
        registerFlourRecipe(recipeConsumer);
        registerGrassTwineRecipe(recipeConsumer);
        registerIronBloomRecipe(recipeConsumer);
        registerLeatherStripsRecipe(recipeConsumer);
        registerRawHideRecipe(recipeConsumer);
        registerUnfiredBrickRecipe(recipeConsumer);
        registerUnfiredClayBucketRecipe(recipeConsumer);

        YTechItems.ARROWS.entries().forEach((entry) -> registerArrowRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.AXES.entries().forEach((entry) -> registerAxeRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.BOLTS.entries().forEach((entry) -> registerBoltRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.BOOTS.entries().forEach((entry) -> registerBootsRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.CHESTPLATES.entries().forEach((entry) -> registerChestplateRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.CRUSHED_MATERIALS.entries().forEach((entry) -> registerCrushedRawMaterialRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.FILES.entries().forEach((entry) -> registerFileRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.HAMMERS.entries().forEach((entry) -> registerHammerRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.HELMETS.entries().forEach((entry) -> registerHelmetRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.HOES.entries().forEach((entry) -> registerHoeRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.INGOTS.entries().forEach((entry) -> registerIngotRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        registerFlintKnifeRecipe(recipeConsumer);
        YTechItems.LEGGINGS.entries().forEach((entry) -> registerLeggingsRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.MORTAR_AND_PESTLES.entries().forEach((entry) -> registerMortarAndPestleRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.PICKAXES.entries().forEach((entry) -> registerPickaxeRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.PLATES.entries().forEach((entry) -> registerPlateRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.RAW_MATERIALS.entries().forEach((entry) -> registerRawMaterialRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.RODS.entries().forEach((entry) -> registerRodRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.SAWS.entries().forEach((entry) -> registerSawRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.SAW_BLADES.entries().forEach((entry) -> registerSawBladeRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.SHOVELS.entries().forEach((entry) -> registerShovelRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.SPEARS.entries().forEach((entry) -> registerSpearRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.SWORDS.entries().forEach((entry) -> registerSwordRecipe(recipeConsumer, entry.getValue(), entry.getKey()));

        AqueductBlock.registerRecipe(recipeConsumer);
        AqueductFertilizerBlock.registerRecipe(recipeConsumer);
        AqueductHydratorBlock.registerRecipe(recipeConsumer);
        AqueductValveBlock.registerRecipe(recipeConsumer);
        BrickChimneyBlock.registerRecipe(recipeConsumer);
        BronzeAnvilBlock.registerRecipe(recipeConsumer);
        FirePitBlock.registerRecipe(recipeConsumer);
        GrassBedBlock.registerRecipe(recipeConsumer);
        MillstoneBlock.registerRecipe(recipeConsumer);
        PrimitiveAlloySmelterBlock.registerRecipe(recipeConsumer);
        PrimitiveSmelterBlock.registerRecipe(recipeConsumer);
        registerReinforcedBricksRecipe(recipeConsumer);
        ReinforcedBrickChimneyBlock.registerRecipe(recipeConsumer);
        registerTerracottaBricksRecipe(recipeConsumer);
        registerTerracottaBrickSlabRecipe(recipeConsumer);
        registerTerracottaBrickStairsRecipe(recipeConsumer);
        registerThatchBlockRecipe(recipeConsumer);
        registerThatchBlockSlabRecipe(recipeConsumer);
        registerThatchBlockStairsRecipe(recipeConsumer);

        YTechItems.DRYING_RACKS.entries().forEach((entry) -> DryingRackBlock.registerRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.RAW_STORAGE_BLOCKS.entries().forEach((entry) -> registerRawStorageBlockRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.STORAGE_BLOCKS.entries().forEach((entry) -> registerStorageBlockRecipe(recipeConsumer, entry.getValue(), entry.getKey()));
        YTechItems.TANNING_RACKS.entries().forEach((entry) -> TanningRackBlock.registerRecipe(recipeConsumer, entry.getValue(), entry.getKey()));

        alloyingRecipe(recipeConsumer, YTechItemTags.INGOTS.of(COPPER), 9, YTechItemTags.INGOTS.of(TIN), 1, YTechItems.INGOTS.of(BRONZE).get(), 10, Math.max(COPPER.meltingTemp, TIN.meltingTemp), 200);

        smeltingRecipe(recipeConsumer, YTechItemTags.CRUSHED_MATERIALS.of(COPPER), Items.COPPER_INGOT, COPPER.meltingTemp, 200);
        smeltingRecipe(recipeConsumer, YTechItemTags.CRUSHED_MATERIALS.of(GOLD), Items.GOLD_INGOT, GOLD.meltingTemp, 200);
        smeltingRecipe(recipeConsumer, YTechItemTags.CRUSHED_MATERIALS.of(CASSITERITE), YTechItems.INGOTS.of(TIN).get(), CASSITERITE.meltingTemp, 200);
        smeltingRecipe(recipeConsumer, YTechItemTags.CRUSHED_MATERIALS.of(GALENA), YTechItems.INGOTS.of(LEAD).get(), GALENA.meltingTemp, 200);

        hammeringRecipe(recipeConsumer, YTechItemTags.IRON_BLOOMS, Items.IRON_INGOT);

        registerBlockHitRecipe(recipeConsumer, Items.FLINT, Tags.Items.STONE, YTechItems.SHARP_FLINT.get());

        registerDryingRecipe(recipeConsumer, Items.BEEF, YTechItems.DRIED_BEEF);
        registerDryingRecipe(recipeConsumer, Items.CHICKEN, YTechItems.DRIED_CHICKEN);
        registerDryingRecipe(recipeConsumer, Items.COD, YTechItems.DRIED_COD);
        registerDryingRecipe(recipeConsumer, Items.KELP, Items.DRIED_KELP, 1200);
        registerDryingRecipe(recipeConsumer, Items.MUTTON, YTechItems.DRIED_MUTTON);
        registerDryingRecipe(recipeConsumer, Items.PORKCHOP, YTechItems.DRIED_PORKCHOP);
        registerDryingRecipe(recipeConsumer, Items.RABBIT, YTechItems.DRIED_RABBIT);
        registerDryingRecipe(recipeConsumer, Items.SALMON, YTechItems.DRIED_SALMON);
        registerDryingRecipe(recipeConsumer, YTechItemTags.VENISON, YTechItems.DRIED_VENISON);

        crushingRecipe(recipeConsumer, YTechItemTags.ANTLERS, Items.BONE_MEAL, 2, "_from_" + Utils.getPath(YTechItems.ANTLER));
        crushingRecipe(recipeConsumer, YTechItemTags.MAMMOTH_TUSKS, Items.BONE_MEAL, 5, "_from_" + Utils.getPath(YTechItems.MAMMOTH_TUSK));
        crushingRecipe(recipeConsumer, YTechItemTags.RHINO_HORNS, Items.BONE_MEAL, 3, "_from_" + Utils.getPath(YTechItems.RHINO_HORN));

        removeVanillaRecipes(recipeConsumer);
    }

    private void removeVanillaRecipes(@NotNull RecipeOutput recipeConsumer) {
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

    private void removeVanillaRecipe(@NotNull RecipeOutput recipeConsumer, Item item) {
        SpecialRecipeBuilder.special(TippedArrowRecipe::new).save(recipeConsumer, Utils.loc(item).toString());
    }

    private void removeVanillaSmeltingBlastingRecipe(@NotNull RecipeOutput recipeConsumer, Item to, Item from) {
        SpecialRecipeBuilder.special(TippedArrowRecipe::new).save(recipeConsumer,
                Utils.mcLoc(Utils.loc(to).getPath() + "_from_smelting_" + Utils.loc(from).getPath()).toString());
        SpecialRecipeBuilder.special(TippedArrowRecipe::new).save(recipeConsumer,
                Utils.mcLoc(Utils.loc(to).getPath() + "_from_blasting_" + Utils.loc(from).getPath()).toString());
    }

    private void mcSplitBySawRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull Item input, @NotNull Item result) {
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.BUILDING_BLOCKS, result, 2)
                .requires(input)
                .requires(YTechItemTags.SAWS.tag)
                .group(Utils.loc(result).getPath())
                .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcSplitByAxeRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull Item input, @NotNull Item result) {
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.BUILDING_BLOCKS, result)
                .requires(input)
                .requires(YTechItemTags.AXES.tag)
                .group(Utils.loc(result).getPath())
                .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath()));
    }

    private void mcSplitByHammerRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull Item input, @NotNull Item result) {
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.BUILDING_BLOCKS, result)
                .requires(input)
                .requires(YTechItemTags.HAMMERS.tag)
                .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcFenceRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull Item planks, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.DECORATIONS, result, 2)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('W', YTechItemTags.SAWS.tag)
                .define('P', planks)
                .define('S', Items.STICK)
                .pattern("H W")
                .pattern("PSP")
                .pattern("PSP")
                .group("wooden_fence")
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcFenceGateRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull Item planks, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.REDSTONE, result, 2)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('W', YTechItemTags.SAWS.tag)
                .define('B', YTechItemTags.BOLTS.of(MaterialType.WOODEN))
                .define('P', planks)
                .define('S', Items.STICK)
                .pattern("H W")
                .pattern("BPB")
                .pattern("SPS")
                .group("wooden_fence_gate")
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcDoorRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull Item planks, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.REDSTONE, result)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('W', YTechItemTags.SAWS.tag)
                .define('B', YTechItemTags.BOLTS.of(MaterialType.WOODEN))
                .define('P', planks)
                .pattern("PPH")
                .pattern("PPB")
                .pattern("PPW")
                .group("wooden_door")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcTrapdoorRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull Item planks, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.REDSTONE, result, 2)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('W', YTechItemTags.SAWS.tag)
                .define('B', YTechItemTags.BOLTS.of(MaterialType.WOODEN))
                .define('P', planks)
                .pattern("HBW")
                .pattern("PPP")
                .pattern("PPP")
                .group("wooden_trapdoor")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcPressurePlateRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull Item planks, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.REDSTONE, result)
                .define('W', YTechItemTags.SAWS.tag)
                .define('B', YTechItemTags.BOLTS.of(MaterialType.WOODEN))
                .define('P', planks)
                .pattern(" W ")
                .pattern("PPP")
                .pattern("BBB")
                .group("wooden_pressure_plate")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcButtonRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull Item planks, @NotNull Item result) {
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.REDSTONE, result)
                .requires(YTechItemTags.SAWS.tag)
                .requires(planks)
                .group("wooden_button")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcBedRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull Item wool, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.DECORATIONS, result)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('S', YTechItemTags.SAWS.tag)
                .define('W', wool)
                .define('P', ItemTags.WOODEN_SLABS)
                .pattern("H S")
                .pattern("WWW")
                .pattern("PPP")
                .group("bed")
                .unlockedBy(Utils.getHasName(), has(ItemTags.WOOL))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcBoatRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull Item planks, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.TRANSPORTATION, result)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('S', YTechItemTags.SAWS.tag)
                .define('F', YTechItemTags.FILES.tag)
                .define('I', YTechItemTags.RODS.tag)
                .define('P', planks)
                .pattern("HFS")
                .pattern("PIP")
                .pattern("PPP")
                .group("boat")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcHorseArmorRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull Item item, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, result)
                .define('#', YTechItemTags.KNIVES.tag)
                .define('S', YTechItemTags.LEATHER_STRIPS)
                .define('L', item)
                .define('X', Items.SADDLE)
                .pattern("L#L")
                .pattern("LXL")
                .pattern("LSL")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(recipeConsumer, Utils.loc(result));
    }

    private void mcHorseArmorRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull MaterialType material, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, result)
                .define('#', YTechItemTags.HAMMERS.tag)
                .define('S', YTechItemTags.LEATHER_STRIPS)
                .define('L', YTechItemTags.PLATES.of(material))
                .define('X', Items.SADDLE)
                .pattern("L#L")
                .pattern("LXL")
                .pattern("LSL")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.of(material)))
                .save(recipeConsumer, Utils.loc(result));
    }

    @SuppressWarnings("SameParameterValue")
    private void smeltingRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull TagKey<Item> input, @NotNull Item result, int temperature, int smeltingTime) {
        SmeltingRecipe.Builder.smelting(input, temperature, smeltingTime, result)
                .unlockedBy(Utils.getHasName(), has(input))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath() + "_from_smelting"));
    }

    @SuppressWarnings("SameParameterValue")
    private void registerBlockHitRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull Item input, @NotNull TagKey<Item> block, @NotNull Item result) {
        BlockHitRecipe.Builder.blockUse(input, block, result)
                .unlockedBy(Utils.getHasName(), has(input))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath()));
    }

    @SuppressWarnings("SameParameterValue")
    private void mcCookingRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull RecipeCategory category,
                                 @NotNull TagKey<Item> input, @NotNull Item result, float xp, int cookingTime) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), category, result, xp, cookingTime)
                .unlockedBy(Utils.getHasName(), has(input))
                .save(recipeConsumer, Utils.loc(result));
    }

    @SuppressWarnings("SameParameterValue")
    private void alloyingRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull TagKey<Item> input1, int count1,
                                @NotNull TagKey<Item> input2, int count2, @NotNull Item result, int count, int temp, int smeltingTime) {
        AlloyingRecipe.Builder.alloying(input1, count1, input2, count2, temp, smeltingTime, result, count)
                .unlockedBy(Utils.getHasName(), has(input1))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath() + "_from_alloying"));
    }

    @SuppressWarnings("SameParameterValue")
    private void registerDryingRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull Item input, @NotNull Item result, int dryingTime) {
        DryingRecipe.Builder.drying(input, dryingTime, result)
                .unlockedBy(RecipeProvider.getHasName(input), has(input))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath()));
    }

    @SuppressWarnings("SameParameterValue")
    private void hammeringRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull TagKey<Item> input, @NotNull Item result) {
        HammeringRecipe.Builder.hammering(input, result)
                .tool(Ingredient.of(YTechItemTags.HAMMERS.tag))
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(input))
                .save(recipeConsumer, Utils.modLoc(result));
    }

    private void crushingRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull TagKey<Item> input, @NotNull Item result, int count, String suffix) {
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, result, count)
                .requires(input)
                .requires(YTechItemTags.MORTAR_AND_PESTLES.tag)
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(input))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath() + suffix));
        MillingRecipe.Builder.milling(input, result, count + (int)Math.ceil(count / 2.0))
                .bonusChance(0.2f)
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(input))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(result).getPath() + suffix + "_from_milling"));
    }

    private static void registerGrassTwineRecipe(RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, YTechItems.GRASS_TWINE.get())
                .define('#', YTechItemTags.GRASS_FIBERS)
                .pattern("##")
                .pattern("##")
                .unlockedBy(Utils.getHasItem(YTechItems.GRASS_FIBERS), RecipeProvider.has(YTechItemTags.GRASS_FIBERS))
                .save(recipeConsumer, YTechItems.GRASS_TWINE.getId());
    }

    private static void registerBrickMoldRecipe(RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, YTechItems.BRICK_MOLD.get())
                .define('#', YTechItemTags.PLATES.of(MaterialType.WOODEN))
                .define('I', YTechItemTags.BOLTS.of(MaterialType.WOODEN))
                .pattern("I#I")
                .pattern("###")
                .pattern("I#I")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.PLATES.of(MaterialType.WOODEN)))
                .save(recipeConsumer, YTechItems.BRICK_MOLD.getId());
    }

    private static void registerRawHideRecipe(RecipeOutput recipeConsumer) {
        TanningRecipe.Builder.tanning(YTechItemTags.RAW_HIDES, 5, Items.LEATHER)
                .tool(Ingredient.of(YTechItemTags.SHARP_FLINTS))
                .unlockedBy(Utils.getHasItem(YTechItems.RAW_HIDE), RecipeProvider.has(YTechItemTags.RAW_HIDES))
                .save(recipeConsumer, YTechItems.RAW_HIDE.getId());
    }
    private static void registerLeatherStripsRecipe(RecipeOutput recipeConsumer) {
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, YTechItems.LEATHER_STRIPS.get(), 4)
                .requires(Items.LEATHER)
                .requires(YTechItemTags.SHARP_FLINTS)
                .unlockedBy(RecipeProvider.getHasName(Items.LEATHER), RecipeProvider.has(Items.LEATHER))
                .save(recipeConsumer, YTechItems.LEATHER_STRIPS.getId());
    }

    private static void registerIronBloomRecipe(RecipeOutput recipeConsumer) {
        AlloyingRecipe.Builder.alloying(YTechItemTags.CRUSHED_MATERIALS.of(MaterialType.IRON), 1, Items.CHARCOAL, 1, 1250, 200, YTechItems.IRON_BLOOM.get(), 1)
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.CRUSHED_MATERIALS.of(MaterialType.IRON)))
                .save(recipeConsumer, YTechItems.IRON_BLOOM.getId());
    }

    private static void registerUnfiredBrickRecipe(RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, YTechItems.UNFIRED_BRICK.get(), 8)
                .define('#', YTechItemTags.BRICK_MOLDS)
                .define('B', Items.CLAY_BALL)
                .pattern("BBB")
                .pattern("B#B")
                .pattern("BBB")
                .unlockedBy(RecipeProvider.getHasName(Items.CLAY_BALL), RecipeProvider.has(Items.CLAY_BALL))
                .save(recipeConsumer, YTechItems.UNFIRED_BRICK.getId());

    }

    private static void registerDryingRecipe(RecipeOutput recipeConsumer, Item rawMeat, DeferredItem<Item> output) {
        DryingRecipe.Builder.drying(rawMeat, 20 * 60, output.get())
                .unlockedBy(RecipeProvider.getHasName(rawMeat), RecipeProvider.has(rawMeat))
                .save(recipeConsumer, output.getId());
    }

    private static void registerDryingRecipe(RecipeOutput recipeConsumer, TagKey<Item> rawMeat, DeferredItem<Item> output) {
        DryingRecipe.Builder.drying(rawMeat, 20 * 60, output.get())
                .unlockedBy(Utils.getHasItem(rawMeat), RecipeProvider.has(rawMeat))
                .save(recipeConsumer, output.getId());
    }

    private static void registerBasketRecipe(RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, YTechItems.BASKET.get())
                .define('#', YTechItemTags.GRASS_TWINES)
                .pattern(" # ")
                .pattern("###")
                .pattern("###")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.GRASS_TWINES))
                .save(recipeConsumer, YTechItems.BASKET.getId());
    }

    private static void registerFlintKnifeRecipe(RecipeOutput recipeConsumer) {
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.COMBAT, YTechItems.KNIVES.of(FLINT).get())
                .requires(Items.STICK)
                .requires(Items.FLINT)
                .requires(YTechItemTags.LEATHER_STRIPS)
                .unlockedBy(RecipeProvider.getHasName(Items.FLINT), RecipeProvider.has(Items.FLINT))
                .save(recipeConsumer, Utils.modLoc(YTechItems.KNIVES.of(FLINT)));
    }

    private static void registerCookedVenisonRecipe(RecipeOutput recipeConsumer) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(YTechItemTags.VENISON), RecipeCategory.FOOD, YTechItems.COOKED_VENISON.get(), 0.35f, 200)
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.VENISON))
                .save(recipeConsumer, YTechItems.COOKED_VENISON.getId());
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(YTechItemTags.VENISON), RecipeCategory.FOOD, YTechItems.COOKED_VENISON.get(), 0.35f, 600)
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.VENISON))
                .save(recipeConsumer, Utils.modLoc(Utils.getPath(YTechItems.COOKED_VENISON) + "_using_campfire"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(YTechItemTags.VENISON), RecipeCategory.FOOD, YTechItems.COOKED_VENISON.get(), 0.35f, 100)
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.VENISON))
                .save(recipeConsumer, Utils.modLoc(Utils.getPath(YTechItems.COOKED_VENISON) + "_using_smoker"));
    }

    private static void registerFlourRecipe(RecipeOutput recipeConsumer) {
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, YTechItems.FLOUR.get())
                .requires(Tags.Items.CROPS_WHEAT)
                .requires(YTechItemTags.MORTAR_AND_PESTLES.tag)
                .unlockedBy(RecipeProvider.getHasName(Items.WHEAT), RecipeProvider.has(Tags.Items.CROPS_WHEAT))
                .save(recipeConsumer, YTechItems.FLOUR.getId());
        MillingRecipe.Builder.milling(Tags.Items.CROPS_WHEAT, YTechItems.FLOUR.get(), 2)
                .bonusChance(0.5f)
                .unlockedBy(RecipeProvider.getHasName(Items.WHEAT), RecipeProvider.has(Tags.Items.CROPS_WHEAT))
                .save(recipeConsumer, Utils.modLoc(Utils.getPath(YTechItems.FLOUR) + "_from_milling"));
    }

    private static void registerUnfiredClayBucketRecipe(@NotNull RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, YTechItems.UNFIRED_CLAY_BUCKET.get())
                .define('#', Items.CLAY_BALL)
                .pattern("# #")
                .pattern("# #")
                .pattern(" # ")
                .unlockedBy(RecipeProvider.getHasName(Items.CLAY_BALL), RecipeProvider.has(Items.CLAY_BALL))
                .save(recipeConsumer, YTechItems.UNFIRED_CLAY_BUCKET.getId());
    }

    private static void registerClayBucketRecipe(@NotNull RecipeOutput recipeConsumer) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(YTechItemTags.UNFIRED_CLAY_BUCKETS), RecipeCategory.MISC, YTechItems.CLAY_BUCKET.get(), 0.35f, 200)
                .unlockedBy(RecipeProvider.getHasName(Items.CLAY_BALL), RecipeProvider.has(Items.CLAY_BALL))
                .save(recipeConsumer, YTechItems.CLAY_BUCKET.getId());
    }

    private static void registerBreadDoughRecipe(@NotNull RecipeOutput recipeConsumer) {
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, YTechItems.BREAD_DOUGH.get())
                .requires(YTechItemTags.FLOURS)
                .requires(YTechItemTags.FLOURS)
                .requires(YTechItemTags.FLOURS)
                .requires(YTechItemTags.WATER_BUCKETS)
                .unlockedBy(RecipeProvider.getHasName(Items.CLAY_BALL), RecipeProvider.has(Items.CLAY_BALL))
                .save(recipeConsumer, YTechItems.BREAD_DOUGH.getId());
    }

    public static void registerArrowRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.COMBAT, item.get())
                .define('S', Items.STICK)
                .define('F', Items.FEATHER)
                .define('#', YTechItemTags.BOLTS.of(material))
                .pattern("#")
                .pattern("S")
                .pattern("F")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.BOLTS.of(material)))
                .save(recipeConsumer, item.getId());
    }

    private static void registerAxeRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == MaterialType.FLINT) {
            RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.TOOLS, item.get())
                    .requires(Items.STICK)
                    .requires(Items.FLINT)
                    .requires(YTechItemTags.GRASS_TWINES)
                    .unlockedBy(RecipeProvider.getHasName(Items.STICK), RecipeProvider.has(Items.STICK))
                    .save(recipeConsumer, item.getId());
        } else {
            RemainingShapedRecipe.Builder.shaped(RecipeCategory.TOOLS, item.get())
                    .define('S', Items.STICK)
                    .define('#', YTechItemTags.PLATES.of(material))
                    .define('H', YTechItemTags.HAMMERS.tag)
                    .pattern("##H")
                    .pattern("#S ")
                    .pattern(" S ")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.PLATES.of(material)))
                    .save(recipeConsumer, item.getId());
        }
    }

    private static void registerBoltRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == WOODEN) {
            RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, YTechItems.BOLTS.of(WOODEN).get())
                    .requires(Items.STICK)
                    .requires(YTechItemTags.AXES.tag)
                    .group(Utils.getPath(YTechItems.BOLTS.of(WOODEN)))
                    .unlockedBy(RecipeProvider.getHasName(Items.STICK), RecipeProvider.has(Items.STICK))
                    .save(recipeConsumer, Utils.modLoc(Utils.getPath(YTechItems.BOLTS.of(WOODEN)) + "_using_axe"));
            RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, YTechItems.BOLTS.of(WOODEN).get(), 2)
                    .requires(Items.STICK)
                    .requires(YTechItemTags.SAWS.tag)
                    .group(Utils.getPath(YTechItems.BOLTS.of(WOODEN)))
                    .unlockedBy(RecipeProvider.getHasName(Items.STICK), RecipeProvider.has(Items.STICK))
                    .save(recipeConsumer, Utils.modLoc(Utils.getPath(YTechItems.BOLTS.of(WOODEN)) + "_using_saw"));
        } else {
            RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, item.get(), 2)
                    .define('#', YTechItemTags.RODS.of(material))
                    .define('S', YTechItemTags.SAWS.tag)
                    .pattern("# ")
                    .pattern(" S")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.RODS.of(material)))
                    .save(recipeConsumer, item.getId());
        }
    }

    public static void registerBootsRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.COMBAT, item.get())
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('#', YTechItemTags.PLATES.of(material))
                .pattern("#H#")
                .pattern("# #")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.PLATES.of(material)))
                .save(recipeConsumer, item.getId());
    }

    public static void registerChestplateRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.COMBAT, item.get())
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('#', YTechItemTags.PLATES.of(material))
                .pattern("#H#")
                .pattern("###")
                .pattern("###")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.PLATES.of(material)))
                .save(recipeConsumer, item.getId());
    }

    public static void registerCrushedRawMaterialRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, item.get())
                .requires(YTechItemTags.RAW_MATERIALS.of(material))
                .requires(YTechItemTags.MORTAR_AND_PESTLES.tag)
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.RAW_MATERIALS.of(material)))
                .save(recipeConsumer, item.getId());
        MillingRecipe.Builder.milling(YTechItemTags.RAW_MATERIALS.of(material), item.get(), 1)
                .bonusChance(0.5f)
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.RAW_MATERIALS.of(material)))
                .save(recipeConsumer, Utils.modLoc(Utils.getPath(item) + "_from_milling"));
    }

    public static void registerFileRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.TOOLS, item.get())
                .define('#', YTechItemTags.PLATES.of(material))
                .define('S', Items.STICK)
                .pattern("#")
                .pattern("S")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.PLATES.of(material)))
                .save(recipeConsumer, item.getId());
    }

    public static void registerHammerRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == MaterialType.STONE) {
            RemainingShapedRecipe.Builder.shaped(RecipeCategory.TOOLS, item.get())
                    .define('S', Items.STICK)
                    .define('T', YTechItemTags.LEATHER_STRIPS)
                    .define('#', Items.STONE)
                    .pattern(" #T")
                    .pattern(" S#")
                    .pattern("S  ")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.LEATHER_STRIPS))
                    .save(recipeConsumer, item.getId());
        } else {
            RemainingShapedRecipe.Builder.shaped(RecipeCategory.COMBAT, item.get())
                    .define('S', Items.STICK)
                    .define('#', YTechItemTags.STORAGE_BLOCKS.of(material))
                    .pattern(" # ")
                    .pattern(" S#")
                    .pattern("S  ")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.STORAGE_BLOCKS.of(material)))
                    .save(recipeConsumer, item.getId());
        }
    }

    public static void registerHelmetRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.COMBAT, item.get())
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('#', YTechItemTags.PLATES.of(material))
                .pattern("###")
                .pattern("#H#")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.PLATES.of(material)))
                .save(recipeConsumer, item.getId());
    }

    private static void registerHoeRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.TOOLS, item.get())
                .define('S', Items.STICK)
                .define('#', YTechItemTags.PLATES.of(material))
                .define('H', YTechItemTags.HAMMERS.tag)
                .pattern("##H")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.PLATES.of(material)))
                .save(recipeConsumer, item.getId());
    }

    public static void registerIngotRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        if (!VANILLA_METALS.contains(material)) {
            RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, item.get(), 9)
                    .requires(YTechItemTags.STORAGE_BLOCKS.of(material))
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.STORAGE_BLOCKS.of(material)))
                    .save(recipeConsumer, item.getId());
        }
    }

    public static void registerLeggingsRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.COMBAT, item.get())
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('#', YTechItemTags.PLATES.of(material))
                .pattern("###")
                .pattern("#H#")
                .pattern("# #")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.PLATES.of(material)))
                .save(recipeConsumer, item.getId());
    }
    public static void registerMortarAndPestleRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == MaterialType.STONE) {
            RemainingShapedRecipe.Builder.shaped(RecipeCategory.TOOLS, item.get())
                    .define('I', Items.STICK)
                    .define('#', Tags.Items.STONE)
                    .pattern(" I ")
                    .pattern("# #")
                    .pattern(" # ")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(Tags.Items.STONE))
                    .save(recipeConsumer, item.getId());
        } else {
            RemainingShapedRecipe.Builder.shaped(RecipeCategory.TOOLS, item.get())
                    .define('I', YTechItemTags.INGOTS.of(material))
                    .define('#', Tags.Items.STONE)
                    .pattern(" I ")
                    .pattern("# #")
                    .pattern(" # ")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.INGOTS.of(material)))
                    .save(recipeConsumer, item.getId());
        }
    }

    private static void registerPickaxeRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == MaterialType.ANTLER) {
            RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.TOOLS, item.get())
                    .requires(YTechItemTags.ANTLERS)
                    .requires(YTechItemTags.SHARP_FLINTS)
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.SHARP_FLINTS))
                    .save(recipeConsumer, item.getId());
        } else {
            RemainingShapedRecipe.Builder.shaped(RecipeCategory.TOOLS, item.get())
                    .define('S', Items.STICK)
                    .define('P', YTechItemTags.PLATES.of(material))
                    .define('R', YTechItemTags.RODS.of(material))
                    .define('#', YTechItemTags.INGOTS.of(material))
                    .define('H', YTechItemTags.HAMMERS.tag)
                    .pattern("P#R")
                    .pattern(" SH")
                    .pattern(" S ")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.INGOTS.of(material)))
                    .save(recipeConsumer, item.getId());
        }
    }

    private static void registerPlateRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == WOODEN) {
            RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, YTechItems.PLATES.of(WOODEN).get())
                    .requires(ItemTags.WOODEN_SLABS)
                    .requires(YTechItemTags.AXES.tag)
                    .group(Utils.getPath(YTechItems.PLATES.of(WOODEN)))
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(ItemTags.WOODEN_SLABS))
                    .save(recipeConsumer, Utils.modLoc(Utils.getPath(YTechItems.PLATES.of(WOODEN)) + "_using_axe"));
            RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, YTechItems.PLATES.of(WOODEN).get(), 2)
                    .requires(ItemTags.WOODEN_SLABS)
                    .requires(YTechItemTags.SAWS.tag)
                    .group(Utils.getPath(YTechItems.PLATES.of(WOODEN)))
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(ItemTags.WOODEN_SLABS))
                    .save(recipeConsumer, Utils.modLoc(Utils.getPath(YTechItems.PLATES.of(WOODEN)) + "_using_saw"));
        } else {
            RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, item.get())
                    .define('#', YTechItemTags.INGOTS.of(material))
                    .define('H', YTechItemTags.HAMMERS.tag)
                    .pattern("#")
                    .pattern("#")
                    .pattern("H")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.INGOTS.of(material)))
                    .save(recipeConsumer, item.getId());
            HammeringRecipe.Builder.hammering(YTechItemTags.INGOTS.of(material), item.get())
                    .tool(Ingredient.of(YTechItemTags.HAMMERS.tag))
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.INGOTS.of(material)))
                    .save(recipeConsumer, Utils.modLoc(Utils.getPath(item) + "_from_hammering"));
        }
    }

    public static void registerRawMaterialRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        if (!VANILLA_METALS.contains(material)) {
            RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, item.get(), 9)
                    .requires(YTechItemTags.RAW_STORAGE_BLOCKS.of(material))
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.RAW_STORAGE_BLOCKS.of(material)))
                    .save(recipeConsumer, item.getId());
        }
    }

    public static void registerRodRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, item.get())
                .define('#', YTechItemTags.INGOTS.of(material))
                .define('F', YTechItemTags.FILES.tag)
                .pattern("#")
                .pattern("F")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.INGOTS.of(material)))
                .save(recipeConsumer, item.getId());
    }

    public static void registerSawRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.TOOLS, item.get())
                .define('S', Items.STICK)
                .define('#', YTechItemTags.PLATES.of(material))
                .define('H', YTechItemTags.HAMMERS.tag)
                .pattern("S##")
                .pattern("H  ")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.PLATES.of(material)))
                .save(recipeConsumer, item.getId());
    }

    public static void registerSawBladeRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, item.get())
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('P', YTechItemTags.PLATES.of(material))
                .pattern(" P ")
                .pattern("PHP")
                .pattern(" P ")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.PLATES.of(material)))
                .save(recipeConsumer, item.getId());
    }

    private static void registerShovelRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.TOOLS, item.get())
                .define('S', Items.STICK)
                .define('#', YTechItemTags.PLATES.of(material))
                .define('H', YTechItemTags.HAMMERS.tag)
                .pattern("#H")
                .pattern("S ")
                .pattern("S ")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.PLATES.of(material)))
                .save(recipeConsumer, item.getId());
    }

    private static void registerSpearRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == MaterialType.FLINT) {
            RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, item.get())
                    .define('T', YTechItemTags.LEATHER_STRIPS)
                    .define('S', Items.FLINT)
                    .define('#', Items.STICK)
                    .pattern(" TS")
                    .pattern(" #T")
                    .pattern("#  ")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.LEATHER_STRIPS))
                    .save(recipeConsumer, item.getId());
        } else {
            RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, item.get())
                    .define('T', YTechItemTags.LEATHER_STRIPS)
                    .define('S', YTechItemTags.PLATES.of(material))
                    .define('#', Items.STICK)
                    .pattern(" TS")
                    .pattern(" #T")
                    .pattern("#  ")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.LEATHER_STRIPS))
                    .save(recipeConsumer, item.getId());
        }
    }

    private static void registerSwordRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.COMBAT, item.get())
                .define('S', Items.STICK)
                .define('#', YTechItemTags.PLATES.of(material))
                .define('H', YTechItemTags.HAMMERS.tag)
                .pattern("# ")
                .pattern("# ")
                .pattern("SH")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.PLATES.of(material)))
                .save(recipeConsumer, item.getId());
    }

    private static void registerReinforcedBricksRecipe(RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.BUILDING_BLOCKS, YTechBlocks.REINFORCED_BRICKS.get())
                .define('B', Items.BRICKS)
                .define('P', YTechItemTags.PLATES.of(MaterialType.COPPER))
                .define('#', YTechItemTags.BOLTS.of(MaterialType.COPPER))
                .pattern("#P#")
                .pattern("PBP")
                .pattern("#P#")
                .unlockedBy(RecipeProvider.getHasName(Items.BRICKS), RecipeProvider.has(Items.BRICKS))
                .save(recipeConsumer, YTechBlocks.REINFORCED_BRICKS.getId());
    }

    private static void registerTerracottaBricksRecipe(RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.BUILDING_BLOCKS, YTechBlocks.TERRACOTTA_BRICKS.get())
                .define('B', Items.TERRACOTTA)
                .pattern("BB")
                .pattern("BB")
                .unlockedBy(RecipeProvider.getHasName(Items.TERRACOTTA), RecipeProvider.has(Items.TERRACOTTA))
                .save(recipeConsumer, YTechBlocks.TERRACOTTA_BRICKS.getId());
    }

    private static void registerTerracottaBrickSlabRecipe(RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.BUILDING_BLOCKS, YTechBlocks.TERRACOTTA_BRICK_SLAB.get(), 6)
                .define('B', YTechItemTags.TERRACOTTA_BRICKS)
                .pattern("BBB")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.TERRACOTTA_BRICKS))
                .save(recipeConsumer, YTechBlocks.TERRACOTTA_BRICK_SLAB.getId());
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(YTechItemTags.TERRACOTTA_BRICKS), RecipeCategory.BUILDING_BLOCKS, YTechBlocks.TERRACOTTA_BRICK_SLAB.get(), 2)
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.TERRACOTTA_BRICKS))
                .save(recipeConsumer, Utils.modLoc(Utils.getPath(YTechBlocks.TERRACOTTA_BRICK_SLAB) + "_stonecutting"));
    }

    private static void registerTerracottaBrickStairsRecipe(RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.BUILDING_BLOCKS, YTechBlocks.TERRACOTTA_BRICK_STAIRS.get(), 4)
                .define('B', YTechItemTags.TERRACOTTA_BRICKS)
                .pattern("B  ")
                .pattern("BB ")
                .pattern("BBB")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.TERRACOTTA_BRICKS))
                .save(recipeConsumer, YTechBlocks.TERRACOTTA_BRICK_STAIRS.getId());
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(YTechItemTags.TERRACOTTA_BRICKS), RecipeCategory.BUILDING_BLOCKS, YTechBlocks.TERRACOTTA_BRICK_STAIRS.get())
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.TERRACOTTA_BRICKS))
                .save(recipeConsumer, Utils.modLoc(Utils.getPath(YTechBlocks.TERRACOTTA_BRICK_STAIRS) + "_stonecutting"));
    }

    private static void registerThatchBlockRecipe(RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.BUILDING_BLOCKS, YTechBlocks.THATCH.get())
                .define('B', YTechItemTags.GRASS_FIBERS)
                .pattern("BB")
                .pattern("BB")
                .unlockedBy("has_thatch", RecipeProvider.has(YTechItemTags.GRASS_FIBERS))
                .save(recipeConsumer, YTechBlocks.THATCH.getId());
    }

    private static void registerThatchBlockSlabRecipe(RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.BUILDING_BLOCKS, YTechBlocks.THATCH_SLAB.get(), 6)
                .define('B', YTechItemTags.THATCH)
                .pattern("BBB")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.THATCH))
                .save(recipeConsumer, YTechBlocks.THATCH_SLAB.getId());
    }

    private static void registerThatchBlockStairsRecipe(RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.BUILDING_BLOCKS, YTechBlocks.THATCH_STAIRS.get(), 4)
                .define('B', YTechItemTags.THATCH)
                .pattern("B  ")
                .pattern("BB ")
                .pattern("BBB")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.THATCH))
                .save(recipeConsumer, YTechBlocks.THATCH_STAIRS.getId());
    }

    private static void registerRawStorageBlockRecipe(RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        if (!VANILLA_METALS.contains(material)) {
            RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, item.get())
                    .define('#', YTechItemTags.RAW_MATERIALS.of(material))
                    .pattern("###").pattern("###").pattern("###")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.RAW_MATERIALS.of(material)))
                    .save(recipeConsumer, item.getId());
        }
    }

    private static void registerStorageBlockRecipe(RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        if (!VANILLA_METALS.contains(material)) {
            RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, item.get())
                    .define('#', YTechItemTags.INGOTS.of(material))
                    .pattern("###").pattern("###").pattern("###")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.INGOTS.of(material)))
                    .save(recipeConsumer, item.getId());
        }
    }
}
