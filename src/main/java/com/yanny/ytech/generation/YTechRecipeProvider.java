package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.PartType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.*;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItemTags;
import com.yanny.ytech.registration.YTechItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.TippedArrowRecipe;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static com.yanny.ytech.configuration.MaterialType.*;

class YTechRecipeProvider extends RecipeProvider {
    public YTechRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    @Override
    protected void buildRecipes() {
        /*
         * MODIFIED VANILLA RECIPES
         */

        mcSplitBySawRecipe(Items.ACACIA_PLANKS, Items.ACACIA_SLAB);
        mcSplitBySawRecipe(Items.BIRCH_PLANKS, Items.BIRCH_SLAB);
        mcSplitBySawRecipe(Items.CHERRY_PLANKS, Items.CHERRY_SLAB);
        mcSplitBySawRecipe(Items.JUNGLE_PLANKS, Items.JUNGLE_SLAB);
        mcSplitBySawRecipe(Items.OAK_PLANKS, Items.OAK_SLAB);
        mcSplitBySawRecipe(Items.DARK_OAK_PLANKS, Items.DARK_OAK_SLAB);
        mcSplitBySawRecipe(Items.MANGROVE_PLANKS, Items.MANGROVE_SLAB);
        mcSplitBySawRecipe(Items.SPRUCE_PLANKS, Items.SPRUCE_SLAB);
        mcSplitBySawRecipe(Items.CRIMSON_PLANKS, Items.CRIMSON_SLAB);
        mcSplitBySawRecipe(Items.BAMBOO_PLANKS, Items.BAMBOO_SLAB);
        mcSplitBySawRecipe(Items.WARPED_PLANKS, Items.WARPED_SLAB);

        mcSplitBySawRecipe(Items.ACACIA_LOG, Items.ACACIA_PLANKS);
        mcSplitBySawRecipe(Items.BIRCH_LOG, Items.BIRCH_PLANKS);
        mcSplitBySawRecipe(Items.CHERRY_LOG, Items.CHERRY_PLANKS);
        mcSplitBySawRecipe(Items.JUNGLE_LOG, Items.JUNGLE_PLANKS);
        mcSplitBySawRecipe(Items.OAK_LOG, Items.OAK_PLANKS);
        mcSplitBySawRecipe(Items.DARK_OAK_LOG, Items.DARK_OAK_PLANKS);
        mcSplitBySawRecipe(Items.MANGROVE_LOG, Items.MANGROVE_PLANKS);
        mcSplitBySawRecipe(Items.SPRUCE_LOG, Items.SPRUCE_PLANKS);
        mcSplitBySawRecipe(Items.CRIMSON_STEM, Items.CRIMSON_PLANKS);
        mcSplitBySawRecipe(Items.BAMBOO_BLOCK, Items.BAMBOO_PLANKS);
        mcSplitBySawRecipe(Items.WARPED_STEM, Items.WARPED_PLANKS);

        mcSplitByAxeRecipe(Items.ACACIA_PLANKS, Items.ACACIA_SLAB);
        mcSplitByAxeRecipe(Items.BIRCH_PLANKS, Items.BIRCH_SLAB);
        mcSplitByAxeRecipe(Items.CHERRY_PLANKS, Items.CHERRY_SLAB);
        mcSplitByAxeRecipe(Items.JUNGLE_PLANKS, Items.JUNGLE_SLAB);
        mcSplitByAxeRecipe(Items.OAK_PLANKS, Items.OAK_SLAB);
        mcSplitByAxeRecipe(Items.DARK_OAK_PLANKS, Items.DARK_OAK_SLAB);
        mcSplitByAxeRecipe(Items.MANGROVE_PLANKS, Items.MANGROVE_SLAB);
        mcSplitByAxeRecipe(Items.SPRUCE_PLANKS, Items.SPRUCE_SLAB);
        mcSplitByAxeRecipe(Items.CRIMSON_PLANKS, Items.CRIMSON_SLAB);
        mcSplitByAxeRecipe(Items.BAMBOO_PLANKS, Items.BAMBOO_SLAB);
        mcSplitByAxeRecipe(Items.WARPED_PLANKS, Items.WARPED_SLAB);

        mcSplitByAxeRecipe(Items.ACACIA_LOG, Items.ACACIA_PLANKS);
        mcSplitByAxeRecipe(Items.BIRCH_LOG, Items.BIRCH_PLANKS);
        mcSplitByAxeRecipe(Items.CHERRY_LOG, Items.CHERRY_PLANKS);
        mcSplitByAxeRecipe(Items.JUNGLE_LOG, Items.JUNGLE_PLANKS);
        mcSplitByAxeRecipe(Items.OAK_LOG, Items.OAK_PLANKS);
        mcSplitByAxeRecipe(Items.DARK_OAK_LOG, Items.DARK_OAK_PLANKS);
        mcSplitByAxeRecipe(Items.MANGROVE_LOG, Items.MANGROVE_PLANKS);
        mcSplitByAxeRecipe(Items.SPRUCE_LOG, Items.SPRUCE_PLANKS);
        mcSplitByAxeRecipe(Items.CRIMSON_STEM, Items.CRIMSON_PLANKS);
        mcSplitByAxeRecipe(Items.BAMBOO_BLOCK, Items.BAMBOO_PLANKS);
        mcSplitByAxeRecipe(Items.WARPED_STEM, Items.WARPED_PLANKS);

        mcFenceRecipe(Items.ACACIA_PLANKS, Items.ACACIA_FENCE);
        mcFenceRecipe(Items.BIRCH_PLANKS, Items.BIRCH_FENCE);
        mcFenceRecipe(Items.CHERRY_PLANKS, Items.CHERRY_FENCE);
        mcFenceRecipe(Items.JUNGLE_PLANKS, Items.JUNGLE_FENCE);
        mcFenceRecipe(Items.OAK_PLANKS, Items.OAK_FENCE);
        mcFenceRecipe(Items.DARK_OAK_PLANKS, Items.DARK_OAK_FENCE);
        mcFenceRecipe(Items.MANGROVE_PLANKS, Items.MANGROVE_FENCE);
        mcFenceRecipe(Items.SPRUCE_PLANKS, Items.SPRUCE_FENCE);
        mcFenceRecipe(Items.CRIMSON_PLANKS, Items.CRIMSON_FENCE);
        mcFenceRecipe(Items.BAMBOO_PLANKS, Items.BAMBOO_FENCE);
        mcFenceRecipe(Items.WARPED_PLANKS, Items.WARPED_FENCE);

        mcFenceGateRecipe(Items.ACACIA_PLANKS, Items.ACACIA_FENCE_GATE);
        mcFenceGateRecipe(Items.BIRCH_PLANKS, Items.BIRCH_FENCE_GATE);
        mcFenceGateRecipe(Items.CHERRY_PLANKS, Items.CHERRY_FENCE_GATE);
        mcFenceGateRecipe(Items.JUNGLE_PLANKS, Items.JUNGLE_FENCE_GATE);
        mcFenceGateRecipe(Items.OAK_PLANKS, Items.OAK_FENCE_GATE);
        mcFenceGateRecipe(Items.DARK_OAK_PLANKS, Items.DARK_OAK_FENCE_GATE);
        mcFenceGateRecipe(Items.MANGROVE_PLANKS, Items.MANGROVE_FENCE_GATE);
        mcFenceGateRecipe(Items.SPRUCE_PLANKS, Items.SPRUCE_FENCE_GATE);
        mcFenceGateRecipe(Items.CRIMSON_PLANKS, Items.CRIMSON_FENCE_GATE);
        mcFenceGateRecipe(Items.BAMBOO_PLANKS, Items.BAMBOO_FENCE_GATE);
        mcFenceGateRecipe(Items.WARPED_PLANKS, Items.WARPED_FENCE_GATE);

        mcDoorRecipe(Items.ACACIA_PLANKS, Items.ACACIA_DOOR);
        mcDoorRecipe(Items.BIRCH_PLANKS, Items.BIRCH_DOOR);
        mcDoorRecipe(Items.CHERRY_PLANKS, Items.CHERRY_DOOR);
        mcDoorRecipe(Items.JUNGLE_PLANKS, Items.JUNGLE_DOOR);
        mcDoorRecipe(Items.OAK_PLANKS, Items.OAK_DOOR);
        mcDoorRecipe(Items.DARK_OAK_PLANKS, Items.DARK_OAK_DOOR);
        mcDoorRecipe(Items.MANGROVE_PLANKS, Items.MANGROVE_DOOR);
        mcDoorRecipe(Items.SPRUCE_PLANKS, Items.SPRUCE_DOOR);
        mcDoorRecipe(Items.CRIMSON_PLANKS, Items.CRIMSON_DOOR);
        mcDoorRecipe(Items.BAMBOO_PLANKS, Items.BAMBOO_DOOR);
        mcDoorRecipe(Items.WARPED_PLANKS, Items.WARPED_DOOR);

        mcTrapdoorRecipe(Items.ACACIA_PLANKS, Items.ACACIA_TRAPDOOR);
        mcTrapdoorRecipe(Items.BIRCH_PLANKS, Items.BIRCH_TRAPDOOR);
        mcTrapdoorRecipe(Items.CHERRY_PLANKS, Items.CHERRY_TRAPDOOR);
        mcTrapdoorRecipe(Items.JUNGLE_PLANKS, Items.JUNGLE_TRAPDOOR);
        mcTrapdoorRecipe(Items.OAK_PLANKS, Items.OAK_TRAPDOOR);
        mcTrapdoorRecipe(Items.DARK_OAK_PLANKS, Items.DARK_OAK_TRAPDOOR);
        mcTrapdoorRecipe(Items.MANGROVE_PLANKS, Items.MANGROVE_TRAPDOOR);
        mcTrapdoorRecipe(Items.SPRUCE_PLANKS, Items.SPRUCE_TRAPDOOR);
        mcTrapdoorRecipe(Items.CRIMSON_PLANKS, Items.CRIMSON_TRAPDOOR);
        mcTrapdoorRecipe(Items.BAMBOO_PLANKS, Items.BAMBOO_TRAPDOOR);
        mcTrapdoorRecipe(Items.WARPED_PLANKS, Items.WARPED_TRAPDOOR);

        mcPressurePlateRecipe(Items.ACACIA_PLANKS, Items.ACACIA_PRESSURE_PLATE);
        mcPressurePlateRecipe(Items.BIRCH_PLANKS, Items.BIRCH_PRESSURE_PLATE);
        mcPressurePlateRecipe(Items.CHERRY_PLANKS, Items.CHERRY_PRESSURE_PLATE);
        mcPressurePlateRecipe(Items.JUNGLE_PLANKS, Items.JUNGLE_PRESSURE_PLATE);
        mcPressurePlateRecipe(Items.OAK_PLANKS, Items.OAK_PRESSURE_PLATE);
        mcPressurePlateRecipe(Items.DARK_OAK_PLANKS, Items.DARK_OAK_PRESSURE_PLATE);
        mcPressurePlateRecipe(Items.MANGROVE_PLANKS, Items.MANGROVE_PRESSURE_PLATE);
        mcPressurePlateRecipe(Items.SPRUCE_PLANKS, Items.SPRUCE_PRESSURE_PLATE);
        mcPressurePlateRecipe(Items.CRIMSON_PLANKS, Items.CRIMSON_PRESSURE_PLATE);
        mcPressurePlateRecipe(Items.BAMBOO_PLANKS, Items.BAMBOO_PRESSURE_PLATE);
        mcPressurePlateRecipe(Items.WARPED_PLANKS, Items.WARPED_PRESSURE_PLATE);

        mcButtonRecipe(Items.ACACIA_PLANKS, Items.ACACIA_BUTTON);
        mcButtonRecipe(Items.BIRCH_PLANKS, Items.BIRCH_BUTTON);
        mcButtonRecipe(Items.CHERRY_PLANKS, Items.CHERRY_BUTTON);
        mcButtonRecipe(Items.JUNGLE_PLANKS, Items.JUNGLE_BUTTON);
        mcButtonRecipe(Items.OAK_PLANKS, Items.OAK_BUTTON);
        mcButtonRecipe(Items.DARK_OAK_PLANKS, Items.DARK_OAK_BUTTON);
        mcButtonRecipe(Items.MANGROVE_PLANKS, Items.MANGROVE_BUTTON);
        mcButtonRecipe(Items.SPRUCE_PLANKS, Items.SPRUCE_BUTTON);
        mcButtonRecipe(Items.CRIMSON_PLANKS, Items.CRIMSON_BUTTON);
        mcButtonRecipe(Items.BAMBOO_PLANKS, Items.BAMBOO_BUTTON);
        mcButtonRecipe(Items.WARPED_PLANKS, Items.WARPED_BUTTON);

        mcStairsRecipe(Items.ACACIA_PLANKS, Items.ACACIA_SLAB, Items.ACACIA_STAIRS);
        mcStairsRecipe(Items.BIRCH_PLANKS, Items.BIRCH_SLAB, Items.BIRCH_STAIRS);
        mcStairsRecipe(Items.CHERRY_PLANKS, Items.CHERRY_SLAB, Items.CHERRY_STAIRS);
        mcStairsRecipe(Items.JUNGLE_PLANKS, Items.JUNGLE_SLAB, Items.JUNGLE_STAIRS);
        mcStairsRecipe(Items.OAK_PLANKS, Items.OAK_SLAB, Items.OAK_STAIRS);
        mcStairsRecipe(Items.DARK_OAK_PLANKS, Items.DARK_OAK_SLAB, Items.DARK_OAK_STAIRS);
        mcStairsRecipe(Items.MANGROVE_PLANKS, Items.MANGROVE_SLAB, Items.MANGROVE_STAIRS);
        mcStairsRecipe(Items.SPRUCE_PLANKS, Items.SPRUCE_SLAB, Items.SPRUCE_STAIRS);
        mcStairsRecipe(Items.CRIMSON_PLANKS, Items.CRIMSON_SLAB, Items.CRIMSON_STAIRS);
        mcStairsRecipe(Items.BAMBOO_PLANKS, Items.BAMBOO_SLAB, Items.BAMBOO_STAIRS);
        mcStairsRecipe(Items.WARPED_PLANKS, Items.WARPED_SLAB, Items.WARPED_STAIRS);

        mcBedRecipe(Items.BLACK_WOOL, Items.BLACK_BED);
        mcBedRecipe(Items.BLUE_WOOL, Items.BLUE_BED);
        mcBedRecipe(Items.BROWN_WOOL, Items.BROWN_BED);
        mcBedRecipe(Items.WHITE_WOOL, Items.WHITE_BED);
        mcBedRecipe(Items.CYAN_WOOL, Items.CYAN_BED);
        mcBedRecipe(Items.GRAY_WOOL, Items.GRAY_BED);
        mcBedRecipe(Items.GREEN_WOOL, Items.GREEN_BED);
        mcBedRecipe(Items.LIME_WOOL, Items.LIME_BED);
        mcBedRecipe(Items.MAGENTA_WOOL, Items.MAGENTA_BED);
        mcBedRecipe(Items.ORANGE_WOOL, Items.ORANGE_BED);
        mcBedRecipe(Items.PINK_WOOL, Items.PINK_BED);
        mcBedRecipe(Items.PURPLE_WOOL, Items.PURPLE_BED);
        mcBedRecipe(Items.RED_WOOL, Items.RED_BED);
        mcBedRecipe(Items.YELLOW_WOOL, Items.YELLOW_BED);
        mcBedRecipe(Items.LIGHT_BLUE_WOOL, Items.LIGHT_BLUE_BED);
        mcBedRecipe(Items.LIGHT_GRAY_WOOL, Items.LIGHT_GRAY_BED);

        mcBoatRecipe(Items.ACACIA_PLANKS, Items.ACACIA_BOAT);
        mcBoatRecipe(Items.BIRCH_PLANKS, Items.BIRCH_BOAT);
        mcBoatRecipe(Items.CHERRY_PLANKS, Items.CHERRY_BOAT);
        mcBoatRecipe(Items.JUNGLE_PLANKS, Items.JUNGLE_BOAT);
        mcBoatRecipe(Items.OAK_PLANKS, Items.OAK_BOAT);
        mcBoatRecipe(Items.DARK_OAK_PLANKS, Items.DARK_OAK_BOAT);
        mcBoatRecipe(Items.MANGROVE_PLANKS, Items.MANGROVE_BOAT);
        mcBoatRecipe(Items.SPRUCE_PLANKS, Items.SPRUCE_BOAT);
        mcBoatRecipe(Items.BAMBOO_PLANKS, Items.BAMBOO_RAFT);

        mcSplitByHammerRecipe(Items.ANDESITE, Items.ANDESITE_SLAB);
        mcSplitByHammerRecipe(Items.COBBLESTONE, Items.COBBLESTONE_SLAB);
        mcSplitByHammerRecipe(Items.DIORITE, Items.DIORITE_SLAB);
        mcSplitByHammerRecipe(Items.GRANITE, Items.GRANITE_SLAB);
        mcSplitByHammerRecipe(Items.SMOOTH_STONE, Items.SMOOTH_STONE_SLAB);
        mcSplitByHammerRecipe(Items.STONE, Items.STONE_SLAB);

        mcHorseArmorRecipe(Items.LEATHER, Items.LEATHER_HORSE_ARMOR);
        mcHorseArmorRecipe(GOLD, Items.GOLDEN_HORSE_ARMOR);
        mcHorseArmorRecipe(IRON, Items.IRON_HORSE_ARMOR);

        mcCookingRecipe(RecipeCategory.FOOD, YTechItemTags.BREAD_DOUGHS, Items.BREAD, 0.1f, 200);
        mcCookingRecipe(RecipeCategory.MISC, YTechItemTags.UNFIRED_BRICKS, Items.BRICK, 0.3f, 200);

        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, Items.WOODEN_SHOVEL)
                .define('#', YTechItemTags.PLATES.get(WOODEN))
                .define('S', Items.STICK)
                .pattern("#")
                .pattern("S")
                .pattern("S")
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(output, key(Items.WOODEN_SHOVEL));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.DECORATIONS, Items.CHEST)
                .define('#', YTechItemTags.PLATES.get(WOODEN))
                .define('B', YTechItemTags.BOLTS.get(WOODEN))
                .define('S', YTechItemTags.SAWS.tag)
                .pattern("B#B")
                .pattern("#S#")
                .pattern("B#B")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(WOODEN)))
                .save(output, key(Items.CHEST));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, Items.LEATHER_BOOTS)
                .define('#', YTechItemTags.BONE_NEEDLES)
                .define('S', YTechItemTags.LEATHER_STRIPS)
                .define('L', Items.LEATHER)
                .pattern(" # ")
                .pattern("LSL")
                .pattern("LSL")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(output, key(Items.LEATHER_BOOTS));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, Items.LEATHER_HELMET)
                .define('#', YTechItemTags.BONE_NEEDLES)
                .define('S', YTechItemTags.LEATHER_STRIPS)
                .define('L', Items.LEATHER)
                .pattern(" # ")
                .pattern("LLL")
                .pattern("LSL")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(output, key(Items.LEATHER_HELMET));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, Items.LEATHER_LEGGINGS)
                .define('#', YTechItemTags.BONE_NEEDLES)
                .define('S', YTechItemTags.LEATHER_STRIPS)
                .define('L', Items.LEATHER)
                .pattern("LLL")
                .pattern("L#L")
                .pattern("LSL")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(output, key(Items.LEATHER_LEGGINGS));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, Items.LEATHER_CHESTPLATE)
                .define('#', YTechItemTags.BONE_NEEDLES)
                .define('S', YTechItemTags.LEATHER_STRIPS)
                .define('L', Items.LEATHER)
                .pattern("L#L")
                .pattern("LSL")
                .pattern("LLL")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(output, key(Items.LEATHER_CHESTPLATE));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, Items.BOW)
                .define('#', YTechItemTags.KNIVES.tag)
                .define('S', Items.STICK)
                .define('W', Items.STRING)
                .pattern(" SW")
                .pattern("S#W")
                .pattern(" SW")
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(output, key(Items.BOW));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, Items.BOWL)
                .define('#', YTechItemTags.KNIVES.tag)
                .define('P', ItemTags.PLANKS)
                .pattern("P#P")
                .pattern(" P ")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(Items.BOWL));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.DECORATIONS, Items.CRAFTING_TABLE)
                .define('$', ItemTags.AXES)
                .define('F', Items.FLINT)
                .define('P', ItemTags.PLANKS)
                .pattern("$F")
                .pattern("PP")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(Items.CRAFTING_TABLE));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.DECORATIONS, Items.FURNACE)
                .define('C', ItemTags.STONE_CRAFTING_MATERIALS)
                .define('F', Items.CAMPFIRE)
                .define('S', Items.COBBLESTONE_SLAB)
                .define('H', YTechItemTags.HAMMERS.tag)
                .pattern("SHS")
                .pattern("CFC")
                .pattern("CCC")
                .unlockedBy(RecipeProvider.getHasName(Items.COBBLESTONE), has(ItemTags.STONE_CRAFTING_MATERIALS))
                .save(output, key(Items.FURNACE));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.DECORATIONS, Items.IRON_BARS)
                .define('C', YTechItemTags.RODS.get(IRON))
                .pattern("CCC")
                .pattern("CCC")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(IRON)))
                .save(output, key(Items.IRON_BARS));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.REDSTONE, Items.IRON_DOOR)
                .define('C', YTechItemTags.PLATES.get(IRON))
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('B', YTechItemTags.BOLTS.get(IRON))
                .pattern("CCH")
                .pattern("CCB")
                .pattern("CC ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(IRON)))
                .save(output, key(Items.IRON_DOOR));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.REDSTONE, Items.IRON_TRAPDOOR, 2)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('B', YTechItemTags.BOLTS.get(IRON))
                .define('P', YTechItemTags.PLATES.get(IRON))
                .pattern("HB ")
                .pattern("PPP")
                .pattern("PPP")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(IRON)))
                .save(output, key(Items.IRON_TRAPDOOR));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.REDSTONE, Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .define('W', YTechItemTags.HAMMERS.tag)
                .define('B', YTechItemTags.BOLTS.get(IRON))
                .define('P', YTechItemTags.PLATES.get(IRON))
                .pattern(" W ")
                .pattern("PPP")
                .pattern("BBB")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(IRON)))
                .save(output, key(Items.HEAVY_WEIGHTED_PRESSURE_PLATE));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.REDSTONE, Items.LIGHT_WEIGHTED_PRESSURE_PLATE)
                .define('W', YTechItemTags.HAMMERS.tag)
                .define('B', YTechItemTags.BOLTS.get(GOLD))
                .define('P', YTechItemTags.PLATES.get(GOLD))
                .pattern(" W ")
                .pattern("PPP")
                .pattern("BBB")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(GOLD)))
                .save(output, key(Items.LIGHT_WEIGHTED_PRESSURE_PLATE));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.REDSTONE, Items.CHAIN)
                .define('W', YTechItemTags.HAMMERS.tag)
                .define('I', YTechItemTags.RODS.get(IRON))
                .pattern("IW")
                .pattern("I ")
                .pattern("I ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(IRON)))
                .save(output, key(Items.CHAIN));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, Items.LANTERN)
                .define('I', YTechItemTags.RODS.tag)
                .define('P', YTechItemTags.PLATES.tag)
                .define('T', Items.TORCH)
                .pattern(" P ")
                .pattern("ITI")
                .pattern(" P ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(IRON)))
                .save(output, key(Items.LANTERN));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, Items.SOUL_LANTERN)
                .define('I', YTechItemTags.RODS.tag)
                .define('P', YTechItemTags.PLATES.tag)
                .define('T', Items.SOUL_TORCH)
                .pattern(" P ")
                .pattern("ITI")
                .pattern(" P ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(IRON)))
                .save(output, key(Items.SOUL_LANTERN));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, Items.STONECUTTER)
                .define('I', YTechItemTags.RODS.get(IRON))
                .define('B', YTechItemTags.BOLTS.get(IRON))
                .define('W', YTechItemTags.SAW_BLADES.get(IRON))
                .define('S', Items.STONE)
                .pattern("IWB")
                .pattern("SSS")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(IRON)))
                .save(output, key(Items.STONECUTTER));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, Items.BARREL)
                .define('P', YTechItemTags.PLATES.get(WOODEN))
                .define('S', ItemTags.PLANKS)
                .define('A', YTechItemTags.AXES.tag)
                .pattern("SPS")
                .pattern("SAS")
                .pattern("SPS")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(Items.BARREL));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, Items.FISHING_ROD)
                .define('B', YTechItemTags.BOLTS.tag)
                .define('S', Items.STICK)
                .define('T', Items.STRING)
                .pattern("  S")
                .pattern(" ST")
                .pattern("S B")
                .unlockedBy(Utils.getHasName(), has(Items.STICK))
                .save(output, key(Items.FISHING_ROD));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, Items.LEAD)
                .define('L', YTechItemTags.LEATHER_STRIPS)
                .define('S', Items.STRING)
                .pattern("LL ")
                .pattern("LS ")
                .pattern("  L")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.LEATHER_STRIPS))
                .save(output, key(Items.LEAD));
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, Items.SADDLE)
                .define('L', Items.LEATHER)
                .define('S', YTechItemTags.LEATHER_STRIPS)
                .define('I', YTechItemTags.RODS.get(IRON))
                .define('H', YTechItemTags.BONE_NEEDLES)
                .define('K', YTechItemTags.KNIVES.tag)
                .pattern("LLL")
                .pattern("LSL")
                .pattern("HIK")
                .unlockedBy(Utils.getHasName(), has(Items.LEATHER))
                .save(output, key(Items.SADDLE));
        RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.MISC, Items.BONE_MEAL)
                .requires(Items.BONE)
                .requires(YTechItemTags.MORTAR_AND_PESTLES.tag)
                .unlockedBy(RecipeProvider.getHasName(Items.BONE), has(Items.BONE))
                .save(output, key(Items.BONE_MEAL));
        MillingRecipe.Builder.milling(Items.BONE, Items.BONE_MEAL, 2)
                .bonusChance(0.2f)
                .unlockedBy(Utils.getHasName(), has(Items.BONE))
                .save(output, key(Items.BONE_MEAL, "milling"));
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, Items.COOKIE, 8)
                .define('#', YTechItemTags.FLOURS)
                .define('X', Items.COCOA_BEANS)
                .pattern("#X#")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.FLOURS))
                .save(output, key(Items.COOKIE));
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, Items.CAKE)
                .define('A', YTechItemTags.FLOURS)
                .define('B', Items.SUGAR)
                .define('C', Items.MILK_BUCKET)
                .define('D', Items.EGG)
                .pattern("CCC")
                .pattern("BDB")
                .pattern("AAA")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.FLOURS))
                .save(output, key(Items.CAKE));
        ShapedRecipeBuilder.shaped(items, RecipeCategory.REDSTONE, Items.TRIPWIRE_HOOK)
                .define('I', YTechItemTags.RODS.get(IRON))
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('S', Items.STICK)
                .define('L', ItemTags.LOGS)
                .pattern("IH")
                .pattern("S ")
                .pattern("L ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(IRON)))
                .save(output, key(Items.TRIPWIRE_HOOK));
        ShapedRecipeBuilder.shaped(items, RecipeCategory.COMBAT, Items.CROSSBOW)
                .define('#', Items.STICK)
                .define('$', Items.TRIPWIRE_HOOK)
                .define('&', YTechItemTags.RODS.get(IRON))
                .define('~', YTechItemTags.LEATHER_STRIPS)
                .define('S', ItemTags.WOODEN_SLABS)
                .define('F', YTechItemTags.KNIVES.tag)
                .define('H', YTechItemTags.HAMMERS.tag)
                .pattern("#&#")
                .pattern("~$~")
                .pattern("FSH")
                .unlockedBy(Utils.getHasName(), has(Items.TRIPWIRE_HOOK))
                .save(output, key(Items.CROSSBOW));
        ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, Items.FLETCHING_TABLE)
                .define('F', Items.FLINT)
                .define('K', YTechItemTags.KNIVES.tag)
                .define('S', ItemTags.PLANKS)
                .pattern("KF")
                .pattern("SS")
                .pattern("SS")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(Items.FLETCHING_TABLE));
        ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, Items.CARTOGRAPHY_TABLE)
                .define('P', Items.PAPER)
                .define('L', Items.LEATHER)
                .define('S', ItemTags.PLANKS)
                .pattern("PL")
                .pattern("SS")
                .pattern("SS")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(Items.CARTOGRAPHY_TABLE));
        ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, Items.SMITHING_TABLE)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('F', YTechItemTags.FILES.tag)
                .define('B', YTechItemTags.STORAGE_BLOCKS.get(IRON))
                .define('S', ItemTags.PLANKS)
                .pattern("HF")
                .pattern("BB")
                .pattern("SS")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(Items.SMITHING_TABLE));
        ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, Items.GRINDSTONE)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('F', YTechItemTags.FILES.tag)
                .define('#', Items.STICK)
                .define('-', Items.STONE_SLAB)
                .define('S', ItemTags.PLANKS)
                .pattern("H F")
                .pattern("#-#")
                .pattern("S S")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(Items.GRINDSTONE));
        ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, Items.LOOM)
                .define('~', Items.STRING)
                .define('B', YTechItemTags.BOLTS.tag)
                .define('S', ItemTags.PLANKS)
                .pattern("~~")
                .pattern("BB")
                .pattern("SS")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(Items.LOOM));
        ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, Items.SMOKER)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('W', YTechItemTags.SAWS.tag)
                .define('S', ItemTags.LOGS)
                .define('F', Items.FURNACE)
                .pattern("HSW")
                .pattern("SFS")
                .pattern(" S ")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(Items.SMOKER));
        ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, Items.CAMPFIRE)
                .define('S', Items.STICK)
                .define('L', ItemTags.LOGS)
                .define('T', Items.TORCH)
                .pattern(" S ")
                .pattern("STS")
                .pattern("LLL")
                .unlockedBy(RecipeProvider.getHasName(Items.TORCH), has(Items.TORCH))
                .save(output, key(Items.CAMPFIRE));
        ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, Items.SOUL_CAMPFIRE)
                .define('S', Items.STICK)
                .define('L', ItemTags.LOGS)
                .define('T', Items.SOUL_TORCH)
                .pattern(" S ")
                .pattern("STS")
                .pattern("LLL")
                .unlockedBy(RecipeProvider.getHasName(Items.SOUL_TORCH), has(Items.SOUL_TORCH))
                .save(output, key(Items.SOUL_CAMPFIRE));

        /*
         * MOD RECIPES
         */

        registerBasketRecipe();
        registerBeeswaxRecipe();
        registerBoneNeedleRecipe();
        registerBreadDoughRecipe();
        registerBrickMoldRecipe();
        registerCookedVenisonRecipe();
        registerFlourRecipe();
        registerGrassTwineRecipe();
        registerIronBloomRecipe();
        registerLeatherStripsRecipe();
        registerRawHideRecipe();
        registerUnfiredBrickRecipe();
        registerUnlitTorchRecipe();

        registerVenusOfHohleFelsRecipe();
        registerLionManRecipe();
        registerWildHorseRecipe();
        registerShellBeadsRecipe();
        registerChloriteBraceletRecipe();

        YTechItems.MOLDS.forEach((part, item) -> smeltingRecipe(YTechItemTags.UNFIRED_MOLDS.get(part), item.get(), 1000, 200));
        YTechItems.PATTERNS.forEach((type, item) -> registerPatternRecipe(item, type));
        YTechItems.SAND_MOLDS.forEach((type, item) -> registerSandMoldRecipe(item, type));
        YTechItems.UNFIRED_MOLDS.forEach((part, item) -> registerUnfiredMoldRecipe(item, part));

        YTechItems.PARTS.forEach((material, map) -> map.forEach((part, item) -> {
            smeltingRecipe(YTechItemTags.INGOTS.get(material), part.ingotCount, YTechItemTags.MOLDS.get(part), item.get(), material.meltingTemp, 200 * part.ingotCount, "mold");
            smeltingRecipe(YTechItemTags.INGOTS.get(material), part.ingotCount, YTechItemTags.SAND_MOLDS.get(part), item.get(), material.meltingTemp, 200 * part.ingotCount, "sand_mold");
        }));

        YTechItems.ARROWS.forEach((material, item) -> registerArrowRecipe(item, material));
        YTechItems.AXES.forEach((material, item) -> registerAxeRecipe(item, material));
        YTechItems.BOLTS.forEach((material, item) -> registerBoltRecipe(item, material));
        YTechItems.BOOTS.forEach((material, item) -> registerBootsRecipe(item, material));
        YTechItems.CHESTPLATES.forEach((material, item) -> registerChestplateRecipe(item, material));
        YTechItems.CRUSHED_MATERIALS.forEach((material, item) -> registerCrushedRawMaterialRecipe(item, material));
        YTechItems.FILES.forEach((material, item) -> registerFileRecipe(item, material));
        YTechItems.HAMMERS.forEach((material, item) -> registerHammerRecipe(item, material));
        YTechItems.HELMETS.forEach((material, item) -> registerHelmetRecipe(item, material));
        YTechItems.HOES.forEach((material, item) -> registerHoeRecipe(item, material));
        YTechItems.INGOTS.forEach((material, item) -> registerIngotRecipe(item, material));
        YTechItems.KNIVES.forEach((material, item) -> registerKnifeRecipe(item, material));
        YTechItems.LEGGINGS.forEach((material, item) -> registerLeggingsRecipe(item, material));
        YTechItems.MORTAR_AND_PESTLES.forEach((material, item) -> registerMortarAndPestleRecipe(item, material));
        YTechItems.PICKAXES.forEach((material, item) -> registerPickaxeRecipe(item, material));
        YTechItems.PLATES.forEach((material, item) -> registerPlateRecipe(item, material));
        YTechItems.RAW_MATERIALS.forEach((material, item) -> registerRawMaterialRecipe(item, material));
        YTechItems.RODS.forEach((material, item) -> registerRodRecipe(item, material));
        YTechItems.SAWS.forEach((material, item) -> registerSawRecipe(item, material));
        YTechItems.SAW_BLADES.forEach((material, item) -> registerSawBladeRecipe(item, material));
        YTechItems.SHOVELS.forEach((material, item) -> registerShovelRecipe(item, material));
        YTechItems.SPEARS.forEach((material, item) -> registerSpearRecipe(item, material));
        YTechItems.SWORDS.forEach((key, item) -> registerSwordRecipe(item, key));

        registerAqueductRecipe();
        registerFertilizerRecipe();
        registerHydratorRecipe();
        registerValveRecipe();
        registerBrickChimneyRecipe();
        registerBronzeAnvilRecipe();
        registerFirePitRecipe();
        registerGrassBedRecipe();
        registerMillstoneRecipe();
        registerPottersWheelRecipe();
        registerPrimitiveAlloySmelterRecipe();
        registerPrimitiveSmelterRecipe();
        registerReinforcedBricksRecipe();
        registerReinforcedBrickChimneyRecipe();
        registerTerracottaBricksRecipe();
        registerTerracottaBrickSlabRecipe();
        registerTerracottaBrickStairsRecipe();
        registerThatchBlockRecipe();
        registerThatchBlockSlabRecipe();
        registerThatchBlockStairsRecipe();

        YTechItems.DRYING_RACKS.forEach((material, item) -> registerDryingRackRecipe(item, material));
        YTechItems.RAW_STORAGE_BLOCKS.forEach((material, item) -> registerRawStorageBlockRecipe(item, material));
        YTechItems.STORAGE_BLOCKS.forEach((material, item) -> registerStorageBlockRecipe(item, material));
        YTechItems.TANNING_RACKS.forEach((material, item) -> registerTanningRackRecipe(item, material));

        alloyingRecipe(YTechItemTags.INGOTS.get(COPPER), 9, YTechItemTags.INGOTS.get(TIN), 1, YTechItems.INGOTS.get(BRONZE).get(), 10, Math.max(COPPER.meltingTemp, TIN.meltingTemp), 200);

        smeltingRecipe(YTechItemTags.CRUSHED_MATERIALS.get(COPPER), 1, YTechItemTags.MOLDS.get(PartType.INGOT), Items.COPPER_INGOT, COPPER.meltingTemp, 200, "smelting");
        smeltingRecipe(YTechItemTags.CRUSHED_MATERIALS.get(GOLD), 1, YTechItemTags.MOLDS.get(PartType.INGOT), Items.GOLD_INGOT, GOLD.meltingTemp, 200, "smelting");
        smeltingRecipe(YTechItemTags.CRUSHED_MATERIALS.get(CASSITERITE), 1, YTechItemTags.MOLDS.get(PartType.INGOT), YTechItems.INGOTS.get(TIN).get(), CASSITERITE.meltingTemp, 200, "smelting");
        smeltingRecipe(YTechItemTags.CRUSHED_MATERIALS.get(GALENA), 1, YTechItemTags.MOLDS.get(PartType.INGOT), YTechItems.INGOTS.get(LEAD).get(), GALENA.meltingTemp, 200, "smelting");

        smeltingRecipe(YTechItemTags.UNFIRED_CLAY_BUCKETS, YTechItems.CLAY_BUCKET.get(), 1000, 200);
        smeltingRecipe(YTechItemTags.UNFIRED_DECORATED_POTS, Items.DECORATED_POT, 1000, 200);
        smeltingRecipe(YTechItemTags.UNFIRED_FLOWER_POTS, Items.FLOWER_POT, 1000, 200);

        hammeringRecipe(YTechItemTags.IRON_BLOOMS, Items.IRON_INGOT);

        registerBlockHitRecipe(Items.FLINT, Tags.Items.STONES, YTechItems.SHARP_FLINT.get());
        registerBlockHitRecipe(YTechItems.UNLIT_TORCH.get(), YTechItemTags.FIRE_SOURCE, Items.TORCH);
        registerBlockHitRecipe(YTechItems.UNLIT_TORCH.get(), YTechItemTags.SOUL_FIRE_SOURCE, Items.SOUL_TORCH);

        registerDryingRecipe(Items.BEEF, YTechItems.DRIED_BEEF);
        registerDryingRecipe(Items.CHICKEN, YTechItems.DRIED_CHICKEN);
        registerDryingRecipe(Items.COD, YTechItems.DRIED_COD);
        registerDryingRecipe(Items.KELP, Items.DRIED_KELP, 1200);
        registerDryingRecipe(Items.MUTTON, YTechItems.DRIED_MUTTON);
        registerDryingRecipe(Items.PORKCHOP, YTechItems.DRIED_PORKCHOP);
        registerDryingRecipe(Items.RABBIT, YTechItems.DRIED_RABBIT);
        registerDryingRecipe(Items.SALMON, YTechItems.DRIED_SALMON);
        registerDryingRecipe(YTechItemTags.VENISON, YTechItems.DRIED_VENISON);

        crushingRecipe(YTechItemTags.ANTLERS, Items.BONE_MEAL, 2, Utils.getPath(YTechItems.ANTLER));
        crushingRecipe(YTechItemTags.MAMMOTH_TUSKS, Items.BONE_MEAL, 5, Utils.getPath(YTechItems.MAMMOTH_TUSK));
        crushingRecipe(YTechItemTags.RHINO_HORNS, Items.BONE_MEAL, 3, Utils.getPath(YTechItems.RHINO_HORN));

        potteryRecipe(2, YTechItems.UNFIRED_FLOWER_POT);
        potteryRecipe(3, YTechItems.UNFIRED_CLAY_BUCKET);
        potteryRecipe(4, YTechItems.UNFIRED_DECORATED_POT);

        removeVanillaRecipes();
    }

    private void removeVanillaRecipes() {
        removeVanillaRecipe(Items.WOODEN_AXE);
        removeVanillaRecipe(Items.WOODEN_HOE);
        removeVanillaRecipe(Items.WOODEN_PICKAXE);
        removeVanillaRecipe(Items.WOODEN_SWORD);

        removeVanillaRecipe(Items.STONE_AXE);
        removeVanillaRecipe(Items.STONE_PICKAXE);
        removeVanillaRecipe(Items.STONE_SHOVEL);
        removeVanillaRecipe(Items.STONE_SWORD);

        removeVanillaSmeltingBlastingRecipe(Items.COPPER_INGOT, Items.RAW_COPPER);
        removeVanillaSmeltingBlastingRecipe(Items.COPPER_INGOT, Items.COPPER_ORE);
        removeVanillaSmeltingBlastingRecipe(Items.COPPER_INGOT, Items.DEEPSLATE_COPPER_ORE);

        removeVanillaSmeltingBlastingRecipe(Items.GOLD_INGOT, Items.RAW_GOLD);
        removeVanillaSmeltingBlastingRecipe(Items.GOLD_INGOT, Items.GOLD_ORE);
        removeVanillaSmeltingBlastingRecipe(Items.GOLD_INGOT, Items.DEEPSLATE_GOLD_ORE);
        removeVanillaSmeltingBlastingRecipe(Items.GOLD_INGOT, Items.NETHER_GOLD_ORE);

        removeVanillaSmeltingBlastingRecipe(Items.IRON_INGOT, Items.RAW_IRON);
        removeVanillaSmeltingBlastingRecipe(Items.IRON_INGOT, Items.IRON_ORE);
        removeVanillaSmeltingBlastingRecipe(Items.IRON_INGOT, Items.DEEPSLATE_IRON_ORE);

        removeVanillaRecipe(Items.FLOWER_POT);
        removeVanillaRecipe(Items.TORCH);
        removeVanillaRecipe(Items.SOUL_TORCH);
    }

    private void removeVanillaRecipe(Item item) {
        SpecialRecipeBuilder.special(TippedArrowRecipe::new).save(output, Utils.loc(item).toString());
    }

    private void removeVanillaSmeltingBlastingRecipe(Item to, Item from) {
        SpecialRecipeBuilder.special(TippedArrowRecipe::new).save(output,
                Utils.mcLoc(Utils.loc(to).getPath() + "_from_smelting_" + Utils.loc(from).getPath()).toString());
        SpecialRecipeBuilder.special(TippedArrowRecipe::new).save(output,
                Utils.mcLoc(Utils.loc(to).getPath() + "_from_blasting_" + Utils.loc(from).getPath()).toString());
    }
    
    private void mcSplitBySawRecipe(@NotNull Item input, @NotNull Item result) {
        RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, result, 2)
                .requires(input)
                .requires(YTechItemTags.SAWS.tag)
                .group(Utils.loc(result).getPath())
                .unlockedBy(RecipeProvider.getHasName(input), has(input))
                .save(output, key(result));
    }

    private void mcSplitByAxeRecipe(@NotNull Item input, @NotNull Item result) {
        RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, result)
                .requires(input)
                .requires(YTechItemTags.AXES.tag)
                .group(Utils.loc(result).getPath())
                .unlockedBy(RecipeProvider.getHasName(input), has(input))
                .save(output, key(Utils.modLoc(result)));
    }

    private void mcSplitByHammerRecipe(@NotNull Item input, @NotNull Item result) {
        RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, result)
                .requires(input)
                .requires(YTechItemTags.HAMMERS.tag)
                .unlockedBy(RecipeProvider.getHasName(input), has(input))
                .save(output,key(result));
    }

    private void mcFenceRecipe(@NotNull Item planks, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.DECORATIONS, result, 2)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('W', YTechItemTags.SAWS.tag)
                .define('P', planks)
                .define('S', Items.STICK)
                .pattern("H W")
                .pattern("PSP")
                .pattern("PSP")
                .group("wooden_fence")
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(output, key(result));
    }

    private void mcFenceGateRecipe(@NotNull Item planks, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.REDSTONE, result, 2)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('W', YTechItemTags.SAWS.tag)
                .define('B', YTechItemTags.BOLTS.get(MaterialType.WOODEN))
                .define('P', planks)
                .define('S', Items.STICK)
                .pattern("H W")
                .pattern("BPB")
                .pattern("SPS")
                .group("wooden_fence_gate")
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(output, key(result));
    }

    private void mcDoorRecipe(@NotNull Item planks, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.REDSTONE, result)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('W', YTechItemTags.SAWS.tag)
                .define('B', YTechItemTags.BOLTS.get(MaterialType.WOODEN))
                .define('P', planks)
                .pattern("PPH")
                .pattern("PPB")
                .pattern("PPW")
                .group("wooden_door")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(result));
    }

    private void mcTrapdoorRecipe(@NotNull Item planks, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.REDSTONE, result, 2)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('W', YTechItemTags.SAWS.tag)
                .define('B', YTechItemTags.BOLTS.get(MaterialType.WOODEN))
                .define('P', planks)
                .pattern("HBW")
                .pattern("PPP")
                .pattern("PPP")
                .group("wooden_trapdoor")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(result));
    }

    private void mcPressurePlateRecipe(@NotNull Item planks, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.REDSTONE, result)
                .define('W', YTechItemTags.SAWS.tag)
                .define('B', YTechItemTags.BOLTS.get(MaterialType.WOODEN))
                .define('P', planks)
                .pattern(" W ")
                .pattern("PPP")
                .pattern("BBB")
                .group("wooden_pressure_plate")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(result));
    }

    private void mcButtonRecipe(@NotNull Item planks, @NotNull Item result) {
        RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.REDSTONE, result)
                .requires(YTechItemTags.SAWS.tag)
                .requires(planks)
                .group("wooden_button")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(result));
    }

    private void mcStairsRecipe(@NotNull Item planks, @NotNull Item slab, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.BUILDING_BLOCKS, result, 2)
                .define('W', YTechItemTags.SAWS.tag)
                .define('B', YTechItemTags.BOLTS.get(MaterialType.WOODEN))
                .define('P', planks)
                .define('S', slab)
                .pattern("PW")
                .pattern("BB")
                .pattern("SS")
                .group("wooden_stairs")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(result));
    }

    private void mcBedRecipe(@NotNull Item wool, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.DECORATIONS, result)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('S', YTechItemTags.SAWS.tag)
                .define('W', wool)
                .define('P', ItemTags.WOODEN_SLABS)
                .pattern("H S")
                .pattern("WWW")
                .pattern("PPP")
                .group("bed")
                .unlockedBy(Utils.getHasName(), has(ItemTags.WOOL))
                .save(output, key(result));
    }

    private void mcBoatRecipe(@NotNull Item planks, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TRANSPORTATION, result)
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
                .save(output, key(result));
    }

    private void mcHorseArmorRecipe(@NotNull Item item, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, result)
                .define('#', YTechItemTags.KNIVES.tag)
                .define('S', YTechItemTags.LEATHER_STRIPS)
                .define('L', item)
                .define('X', Items.SADDLE)
                .pattern("L#L")
                .pattern("LXL")
                .pattern("LSL")
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(output, key(result));
    }

    private void mcHorseArmorRecipe(@NotNull MaterialType material, @NotNull Item result) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, result)
                .define('#', YTechItemTags.HAMMERS.tag)
                .define('S', YTechItemTags.LEATHER_STRIPS)
                .define('L', YTechItemTags.PLATES.get(material))
                .define('X', Items.SADDLE)
                .pattern("L#L")
                .pattern("LXL")
                .pattern("LSL")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(material)))
                .save(output, key(result));
    }

    @SuppressWarnings("SameParameterValue")
    private void smeltingRecipe(@NotNull TagKey<Item> input, @NotNull Item result, int temperature, int smeltingTime) {
        SmeltingRecipe.Builder.smelting(items, input, temperature, smeltingTime, result)
                .unlockedBy(Utils.getHasName(), has(input))
                .save(output, key(result, "smelting"));
    }

    private void smeltingRecipe(@NotNull TagKey<Item> input, int inputCount, TagKey<Item> mold, @NotNull Item result, int temperature, int smeltingTime, String from) {
        SmeltingRecipe.Builder.smelting(items, input, inputCount, mold, temperature, smeltingTime, result)
                .unlockedBy(Utils.getHasName(), has(input))
                .save(output, key(result, from));
    }

    @SuppressWarnings("SameParameterValue")
    private void registerBlockHitRecipe(@NotNull Item input, @NotNull TagKey<Item> block, @NotNull Item result) {
        BlockHitRecipe.Builder.blockUse(items, input, block, result)
                .unlockedBy(Utils.getHasName(), has(input))
                .save(output, key(Utils.modLoc(result)));
    }

    @SuppressWarnings("SameParameterValue")
    private void mcCookingRecipe(@NotNull RecipeCategory category,
                                 @NotNull TagKey<Item> input, @NotNull Item result, float xp, int cookingTime) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(items.getOrThrow(input)), category, result, xp, cookingTime)
                .unlockedBy(Utils.getHasName(), has(input))
                .save(output, key(result));
    }

    @SuppressWarnings("SameParameterValue")
    private void alloyingRecipe(@NotNull TagKey<Item> input1, int count1,
                                @NotNull TagKey<Item> input2, int count2, @NotNull Item result, int count, int temp, int smeltingTime) {
        AlloyingRecipe.Builder.alloying(items, input1, count1, input2, count2, temp, smeltingTime, result, count)
                .unlockedBy(Utils.getHasName(), has(input1))
                .save(output, key(result, "alloying"));
    }

    @SuppressWarnings("SameParameterValue")
    private void registerDryingRecipe(@NotNull Item input, @NotNull Item result, int dryingTime) {
        DryingRecipe.Builder.drying(input, dryingTime, result)
                .unlockedBy(RecipeProvider.getHasName(input), has(input))
                .save(output, key(result));
    }

    @SuppressWarnings("SameParameterValue")
    private void hammeringRecipe(@NotNull TagKey<Item> input, @NotNull Item result) {
        HammeringRecipe.Builder.hammering(items, input, YTechItemTags.HAMMERS.tag, result)
                .unlockedBy(Utils.getHasName(), has(input))
                .save(output, key(result));
    }

    private void crushingRecipe(@NotNull TagKey<Item> input, @NotNull Item result, int count, String suffix) {
        RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.MISC, result, count)
                .requires(input)
                .requires(YTechItemTags.MORTAR_AND_PESTLES.tag)
                .unlockedBy(Utils.getHasName(), has(input))
                .save(output, key(result, suffix));
        MillingRecipe.Builder.milling(items, input, result, count + (int)Math.ceil(count / 2.0))
                .bonusChance(0.2f)
                .unlockedBy(Utils.getHasName(), has(input))
                .save(output, key(result, suffix + "_from_milling"));
    }

    private void potteryRecipe(int count, DeferredItem<Item> result) {
        PotteryRecipe.Builder.pottery(count, result.get())
                .unlockedBy(Utils.getHasName(), has(Items.CLAY_BALL))
                .save(output, key(result));
    }

    private void registerGrassTwineRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechItems.GRASS_TWINE.get())
                .define('#', YTechItemTags.GRASS_FIBERS)
                .pattern("##")
                .pattern("##")
                .unlockedBy(Utils.getHasItem(YTechItems.GRASS_FIBERS), has(YTechItemTags.GRASS_FIBERS))
                .save(output, key(YTechItems.GRASS_TWINE));
    }

    private void registerBrickMoldRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechItems.BRICK_MOLD.get())
                .define('#', YTechItemTags.PLATES.get(MaterialType.WOODEN))
                .define('I', YTechItemTags.BOLTS.get(MaterialType.WOODEN))
                .pattern("I#I")
                .pattern("###")
                .pattern("I#I")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(MaterialType.WOODEN)))
                .save(output, key(YTechItems.BRICK_MOLD));
    }

    private void registerRawHideRecipe() {
        TanningRecipe.Builder.tanning(items, YTechItemTags.RAW_HIDES, YTechItemTags.SHARP_FLINTS, 5, Items.LEATHER)
                .unlockedBy(Utils.getHasItem(YTechItems.RAW_HIDE), has(YTechItemTags.RAW_HIDES))
                .save(output, key(YTechItems.RAW_HIDE));
    }
    private void registerLeatherStripsRecipe() {
        RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.MISC, YTechItems.LEATHER_STRIPS.get(), 4)
                .requires(Items.LEATHER)
                .requires(YTechItemTags.SHARP_FLINTS)
                .unlockedBy(RecipeProvider.getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(output, key(YTechItems.LEATHER_STRIPS));
    }

    private void registerIronBloomRecipe() {
        AlloyingRecipe.Builder.alloying(items, YTechItemTags.CRUSHED_MATERIALS.get(MaterialType.IRON), 1, Items.CHARCOAL, 1, 1250, 200, YTechItems.IRON_BLOOM.get(), 1)
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.CRUSHED_MATERIALS.get(MaterialType.IRON)))
                .save(output, key(YTechItems.IRON_BLOOM));
    }

    private void registerUnfiredBrickRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechItems.UNFIRED_BRICK.get(), 8)
                .define('#', YTechItemTags.BRICK_MOLDS)
                .define('B', Items.CLAY_BALL)
                .pattern("BBB")
                .pattern("B#B")
                .pattern("BBB")
                .unlockedBy(RecipeProvider.getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
                .save(output, key(YTechItems.UNFIRED_BRICK));

    }

    private void registerUnlitTorchRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechItems.UNLIT_TORCH.get(), 4)
                .define('#', ItemTags.COALS)
                .define('B', Items.STICK)
                .pattern("#")
                .pattern("B")
                .unlockedBy(Utils.getHasName(), has(ItemTags.COALS))
                .save(output, key(YTechItems.UNLIT_TORCH));
    }

    private void registerVenusOfHohleFelsRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechItems.VENUS_OF_HOHLE_FELS.get())
                .define('T', YTechItemTags.MAMMOTH_TUSKS)
                .define('#', YTechItemTags.SHARP_FLINTS)
                .pattern("T#")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.MAMMOTH_TUSKS))
                .save(output, key(YTechItems.VENUS_OF_HOHLE_FELS));
    }

    private void registerLionManRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechItems.LION_MAN.get())
                .define('T', YTechItemTags.MAMMOTH_TUSKS)
                .define('#', YTechItemTags.SHARP_FLINTS)
                .pattern("T ")
                .pattern(" #")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.MAMMOTH_TUSKS))
                .save(output, key(YTechItems.LION_MAN));
    }

    private void registerWildHorseRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechItems.WILD_HORSE.get())
                .define('T', YTechItemTags.MAMMOTH_TUSKS)
                .define('#', YTechItemTags.SHARP_FLINTS)
                .pattern("T")
                .pattern("#")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.MAMMOTH_TUSKS))
                .save(output, key(YTechItems.WILD_HORSE));
    }

    private void registerShellBeadsRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechItems.SHELL_BEADS.get())
                .define('S', Items.NAUTILUS_SHELL)
                .define('L', YTechItemTags.LEATHER_STRIPS)
                .define('F', YTechItemTags.SHARP_FLINTS)
                .pattern("FL ")
                .pattern("SSS")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.LEATHER_STRIPS))
                .save(output, key(YTechItems.SHELL_BEADS));
    }

    private void registerChloriteBraceletRecipe() {
        RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.MISC, YTechItems.CHLORITE_BRACELET.get())
                .requires(YTechItemTags.PEBBLES)
                .requires(YTechItemTags.SHARP_FLINTS)
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.PEBBLES))
                .save(output, key(YTechItems.CHLORITE_BRACELET));
    }

    private void registerDryingRecipe(Item rawMeat, DeferredItem<Item> result) {
        DryingRecipe.Builder.drying(rawMeat, 20 * 60, result.get())
                .unlockedBy(RecipeProvider.getHasName(rawMeat), has(rawMeat))
                .save(output, key(result.getId()));
    }

    private void registerDryingRecipe(TagKey<Item> rawMeat, DeferredItem<Item> result) {
        DryingRecipe.Builder.drying(items, rawMeat, 20 * 60, result.get())
                .unlockedBy(Utils.getHasItem(rawMeat), has(rawMeat))
                .save(output, key(result.getId()));
    }

    private void registerBasketRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechItems.BASKET.get())
                .define('#', YTechItemTags.GRASS_TWINES)
                .define('B', YTechItemTags.BONE_NEEDLES)
                .pattern(" # ")
                .pattern("#B#")
                .pattern("###")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.GRASS_TWINES))
                .save(output, key(YTechItems.BASKET));
    }

    private void registerBeeswaxRecipe() {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.HONEYCOMB), RecipeCategory.MISC, YTechItems.BEESWAX.get(), 0.5f, 100)
                .unlockedBy(RecipeProvider.getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
                .save(output, key(YTechItems.BEESWAX));
    }

    private void registerBoneNeedleRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechItems.BONE_NEEDLE.get())
                .define('T', YTechItemTags.BONE)
                .define('#', YTechItemTags.SHARP_FLINTS)
                .pattern("#T")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.BONE))
                .save(output, key(YTechItems.BONE_NEEDLE));
    }

    private void registerKnifeRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == FLINT) {
            RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.COMBAT, item.get())
                    .requires(Items.STICK)
                    .requires(Items.FLINT)
                    .requires(YTechItemTags.LEATHER_STRIPS)
                    .unlockedBy(RecipeProvider.getHasName(Items.FLINT), has(Items.FLINT))
                    .save(output, key(item));
        } else {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, item.get())
                    .define('S', Items.STICK)
                    .define('P', YTechItemTags.PLATES.get(material))
                    .define('F', YTechItemTags.FILES.tag)
                    .pattern("FP")
                    .pattern("S ")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                    .save(output, key(item));
        }
    }

    private void registerCookedVenisonRecipe() {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(items.getOrThrow(YTechItemTags.VENISON)), RecipeCategory.FOOD, YTechItems.COOKED_VENISON.get(), 0.35f, 200)
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.VENISON))
                .save(output, key(YTechItems.COOKED_VENISON));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(items.getOrThrow(YTechItemTags.VENISON)), RecipeCategory.FOOD, YTechItems.COOKED_VENISON.get(), 0.35f, 600)
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.VENISON))
                .save(output, key(YTechItems.COOKED_VENISON, "campfire"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(items.getOrThrow(YTechItemTags.VENISON)), RecipeCategory.FOOD, YTechItems.COOKED_VENISON.get(), 0.35f, 100)
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.VENISON))
                .save(output, key(YTechItems.COOKED_VENISON, "smoker"));
    }

    private void registerFlourRecipe() {
        RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.MISC, YTechItems.FLOUR.get())
                .requires(Tags.Items.CROPS_WHEAT)
                .requires(YTechItemTags.MORTAR_AND_PESTLES.tag)
                .unlockedBy(RecipeProvider.getHasName(Items.WHEAT), has(Tags.Items.CROPS_WHEAT))
                .save(output, key(YTechItems.FLOUR));
        MillingRecipe.Builder.milling(items, Tags.Items.CROPS_WHEAT, YTechItems.FLOUR.get(), 2)
                .bonusChance(0.5f)
                .unlockedBy(RecipeProvider.getHasName(Items.WHEAT), has(Tags.Items.CROPS_WHEAT))
                .save(output, key(YTechItems.FLOUR, "milling"));
    }

    private void registerBreadDoughRecipe() {
        RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.MISC, YTechItems.BREAD_DOUGH.get())
                .requires(YTechItemTags.FLOURS)
                .requires(YTechItemTags.FLOURS)
                .requires(YTechItemTags.FLOURS)
                .requires(YTechItemTags.WATER_BUCKETS)
                .unlockedBy(RecipeProvider.getHasName(Items.WHEAT), has(Tags.Items.CROPS_WHEAT))
                .save(output, key(YTechItems.BREAD_DOUGH));
    }

    public void registerPatternRecipe(@NotNull DeferredItem<Item> item, PartType partType) {
        switch (partType) {
            case AXE_HEAD -> RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get())
                    .define('P', YTechItemTags.BEESWAXES)
                    .define('#', YTechItemTags.KNIVES.tag)
                    .pattern("#")
                    .pattern("P")
                    .unlockedBy(RecipeProvider.getHasName(Items.HONEYCOMB), has(Items.HONEYCOMB))
                    .save(output, key(item));
            case HAMMER_HEAD -> RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get())
                    .define('P', YTechItemTags.BEESWAXES)
                    .define('#', YTechItemTags.KNIVES.tag)
                    .pattern(" #")
                    .pattern("P ")
                    .unlockedBy(RecipeProvider.getHasName(Items.HONEYCOMB), has(Items.HONEYCOMB))
                    .save(output, key(item));
            case PICKAXE_HEAD -> RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get())
                    .define('P', YTechItemTags.BEESWAXES)
                    .define('#', YTechItemTags.KNIVES.tag)
                    .pattern("P#")
                    .unlockedBy(RecipeProvider.getHasName(Items.HONEYCOMB), has(Items.HONEYCOMB))
                    .save(output, key(item));
            case SWORD_BLADE -> RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get())
                    .define('P', YTechItemTags.BEESWAXES)
                    .define('#', YTechItemTags.KNIVES.tag)
                    .pattern("P ")
                    .pattern(" #")
                    .unlockedBy(RecipeProvider.getHasName(Items.HONEYCOMB), has(Items.HONEYCOMB))
                    .save(output, key(item));
            case INGOT -> RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get())
                    .define('P', YTechItemTags.BEESWAXES)
                    .define('#', YTechItemTags.KNIVES.tag)
                    .pattern("P")
                    .pattern("#")
                    .unlockedBy(RecipeProvider.getHasName(Items.HONEYCOMB), has(Items.HONEYCOMB))
                    .save(output, key(item));
            default -> throw new IllegalArgumentException("Missing recipe");
        }
    }

    public void registerSandMoldRecipe(@NotNull DeferredItem<Item> item, PartType partType) {
        if (partType == PartType.INGOT) {
            RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.MISC, item.get())
                    .requires(ItemTags.SAND)
                    .requires(YTechItemTags.INGOTS.tag)
                    .unlockedBy(Utils.getHasName(), has(ItemTags.SAND))
                    .save(output, key(item));
        } else {
            RemainingPartShapelessRecipe.Builder.shapeless(items, RecipeCategory.MISC, item.get())
                    .requires(ItemTags.SAND)
                    .requires(YTechItemTags.PARTS.getSubType(partType))
                    .unlockedBy(Utils.getHasName(), has(ItemTags.SAND))
                    .save(output, key(item));
        }
    }

    public void registerUnfiredMoldRecipe(@NotNull DeferredItem<Item> item, PartType partType) {
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, item.get())
                .requires(Items.CLAY)
                .requires(YTechItemTags.PATTERNS.get(partType))
                .unlockedBy(getHasName(Items.CLAY), has(Items.CLAY))
                .save(output, key(item));
    }

    public void registerArrowRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, item.get())
                .define('S', Items.STICK)
                .define('F', Items.FEATHER)
                .define('#', YTechItemTags.BOLTS.get(material))
                .pattern("#")
                .pattern("S")
                .pattern("F")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.BOLTS.get(material)))
                .save(output, key(item));
    }

    private void registerAxeRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        switch (material) {
            case FLINT -> RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.TOOLS, item.get())
                    .requires(Items.STICK)
                    .requires(Items.FLINT)
                    .requires(YTechItemTags.GRASS_TWINES)
                    .unlockedBy(RecipeProvider.getHasName(Items.STICK), has(Items.STICK))
                    .save(output, key(item));
            case IRON -> RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, item.get())
                    .define('S', Items.STICK)
                    .define('#', YTechItemTags.PLATES.get(material))
                    .define('H', YTechItemTags.HAMMERS.tag)
                    .pattern("##H")
                    .pattern("#S ")
                    .pattern(" S ")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                    .save(output, key(item));
            default -> RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.TOOLS, item.get())
                    .requires(Items.STICK)
                    .requires(YTechItemTags.PARTS.get(material, PartType.AXE_HEAD))
                    .requires(YTechItemTags.HAMMERS.tag)
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                    .save(output, key(item));
        }
    }

    private void registerBoltRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == WOODEN) {
            RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.MISC, YTechItems.BOLTS.get(WOODEN).get())
                    .requires(Items.STICK)
                    .requires(YTechItemTags.AXES.tag)
                    .group(Utils.getPath(YTechItems.BOLTS.get(WOODEN)))
                    .unlockedBy(RecipeProvider.getHasName(Items.STICK), has(Items.STICK))
                    .save(output, key(YTechItems.BOLTS.get(WOODEN), "axe"));
            RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.MISC, YTechItems.BOLTS.get(WOODEN).get(), 2)
                    .requires(Items.STICK)
                    .requires(YTechItemTags.SAWS.tag)
                    .group(Utils.getPath(YTechItems.BOLTS.get(WOODEN)))
                    .unlockedBy(RecipeProvider.getHasName(Items.STICK), has(Items.STICK))
                    .save(output, key(YTechItems.BOLTS.get(WOODEN), "saw"));
        } else {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get(), 2)
                    .define('#', YTechItemTags.RODS.get(material))
                    .define('S', YTechItemTags.SAWS.tag)
                    .pattern("# ")
                    .pattern(" S")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.RODS.get(material)))
                    .save(output, key(item));
        }
    }

    public void registerBootsRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, item.get())
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('#', YTechItemTags.PLATES.get(material))
                .pattern("#H#")
                .pattern("# #")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                .save(output, key(item));
    }

    public void registerChestplateRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, item.get())
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('#', YTechItemTags.PLATES.get(material))
                .pattern("#H#")
                .pattern("###")
                .pattern("###")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                .save(output, key(item));
    }

    public void registerCrushedRawMaterialRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.MISC, item.get())
                .requires(YTechItemTags.RAW_MATERIALS.get(material))
                .requires(YTechItemTags.MORTAR_AND_PESTLES.tag)
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.RAW_MATERIALS.get(material)))
                .save(output, key(item));
        MillingRecipe.Builder.milling(items, YTechItemTags.RAW_MATERIALS.get(material), item.get(), 1)
                .bonusChance(0.5f)
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.RAW_MATERIALS.get(material)))
                .save(output, key(item, "milling"));
    }

    public void registerFileRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, item.get())
                .define('#', YTechItemTags.PLATES.get(material))
                .define('S', Items.STICK)
                .pattern("#")
                .pattern("S")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                .save(output, key(item));
    }

    public void registerHammerRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        switch (material) {
            case STONE -> RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, item.get())
                    .define('S', Items.STICK)
                    .define('T', YTechItemTags.LEATHER_STRIPS)
                    .define('#', YTechItemTags.PEBBLES)
                    .pattern(" #T")
                    .pattern(" S#")
                    .pattern("S  ")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.LEATHER_STRIPS))
                    .save(output, key(item));
            case IRON -> RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, item.get())
                    .define('S', Items.STICK)
                    .define('#', YTechItemTags.STORAGE_BLOCKS.get(material))
                    .pattern(" # ")
                    .pattern(" S#")
                    .pattern("S  ")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.STORAGE_BLOCKS.get(material)))
                    .save(output, key(item));
            default -> RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.COMBAT, item.get())
                    .requires(Items.STICK)
                    .requires(YTechItemTags.PARTS.get(material, PartType.HAMMER_HEAD))
                    .requires(YTechItemTags.HAMMERS.tag)
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(material)))
                    .save(output, key(item));
        }
    }

    public void registerHelmetRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, item.get())
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('#', YTechItemTags.PLATES.get(material))
                .pattern("###")
                .pattern("#H#")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                .save(output, key(item));
    }

    private void registerHoeRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, item.get())
                .define('S', Items.STICK)
                .define('#', YTechItemTags.PLATES.get(material))
                .define('H', YTechItemTags.HAMMERS.tag)
                .pattern("##H")
                .pattern(" S ")
                .pattern(" S ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                .save(output, key(item));
    }

    public void registerIngotRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        if (!VANILLA_METALS.contains(material)) {
            RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.MISC, item.get(), 9)
                    .requires(YTechItemTags.STORAGE_BLOCKS.get(material))
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.STORAGE_BLOCKS.get(material)))
                    .save(output, key(item));
        }
    }

    public void registerLeggingsRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, item.get())
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('#', YTechItemTags.PLATES.get(material))
                .pattern("###")
                .pattern("#H#")
                .pattern("# #")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                .save(output, key(item));
    }
    public void registerMortarAndPestleRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == MaterialType.STONE) {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, item.get())
                    .define('I', Items.STICK)
                    .define('#', Tags.Items.STONES)
                    .pattern(" I ")
                    .pattern("# #")
                    .pattern(" # ")
                    .unlockedBy(Utils.getHasName(), has(Tags.Items.STONES))
                    .save(output, key(item));
        } else {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, item.get())
                    .define('I', YTechItemTags.INGOTS.get(material))
                    .define('#', Tags.Items.STONES)
                    .pattern(" I ")
                    .pattern("# #")
                    .pattern(" # ")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(material)))
                    .save(output, key(item));
        }
    }

    private void registerPickaxeRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        switch (material) {
            case ANTLER -> RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.TOOLS, item.get())
                    .requires(YTechItemTags.ANTLERS)
                    .requires(YTechItemTags.SHARP_FLINTS)
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.SHARP_FLINTS))
                    .save(output, key(item));
            case IRON -> RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, item.get())
                    .define('S', Items.STICK)
                    .define('P', YTechItemTags.PLATES.get(material))
                    .define('R', YTechItemTags.RODS.get(material))
                    .define('#', YTechItemTags.INGOTS.get(material))
                    .define('H', YTechItemTags.HAMMERS.tag)
                    .pattern("P#R")
                    .pattern(" SH")
                    .pattern(" S ")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(material)))
                    .save(output, key(item));
            default -> RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.TOOLS, item.get())
                    .requires(Items.STICK)
                    .requires(YTechItemTags.PARTS.get(material, PartType.PICKAXE_HEAD))
                    .requires(YTechItemTags.HAMMERS.tag)
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(material)))
                    .save(output, key(item));
        }
    }

    private void registerPlateRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == WOODEN) {
            RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.MISC, YTechItems.PLATES.get(WOODEN).get())
                    .requires(ItemTags.WOODEN_SLABS)
                    .requires(YTechItemTags.AXES.tag)
                    .group(Utils.getPath(YTechItems.PLATES.get(WOODEN)))
                    .unlockedBy(Utils.getHasName(), has(ItemTags.WOODEN_SLABS))
                    .save(output, key(YTechItems.PLATES.get(WOODEN), "_using_axe"));
            RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.MISC, YTechItems.PLATES.get(WOODEN).get(), 2)
                    .requires(ItemTags.WOODEN_SLABS)
                    .requires(YTechItemTags.SAWS.tag)
                    .group(Utils.getPath(YTechItems.PLATES.get(WOODEN)))
                    .unlockedBy(Utils.getHasName(), has(ItemTags.WOODEN_SLABS))
                    .save(output, key(YTechItems.PLATES.get(WOODEN), "_using_saw"));
        } else {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get())
                    .define('#', YTechItemTags.INGOTS.get(material))
                    .define('H', YTechItemTags.HAMMERS.tag)
                    .pattern("#")
                    .pattern("#")
                    .pattern("H")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(material)))
                    .save(output, key(item));
            HammeringRecipe.Builder.hammering(items, YTechItemTags.INGOTS.get(material), YTechItemTags.HAMMERS.tag, item.get())
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(material)))
                    .save(output, key(item, "hammering"));
        }
    }

    public void registerRawMaterialRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        if (!VANILLA_METALS.contains(material)) {
            RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.MISC, item.get(), 9)
                    .requires(YTechItemTags.RAW_STORAGE_BLOCKS.get(material))
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.RAW_STORAGE_BLOCKS.get(material)))
                    .save(output, key(item));
        }
    }

    public void registerRodRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get())
                .define('#', YTechItemTags.INGOTS.get(material))
                .define('F', YTechItemTags.FILES.tag)
                .pattern("#")
                .pattern("F")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(material)))
                .save(output, key(item));
    }

    public void registerSawRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, item.get())
                .define('S', Items.STICK)
                .define('#', YTechItemTags.PLATES.get(material))
                .define('H', YTechItemTags.HAMMERS.tag)
                .pattern("S##")
                .pattern("H  ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                .save(output, key(item));
    }

    public void registerSawBladeRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get())
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('P', YTechItemTags.PLATES.get(material))
                .pattern(" P ")
                .pattern("PHP")
                .pattern(" P ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                .save(output, key(item));
    }

    private void registerShovelRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, item.get())
                .define('S', Items.STICK)
                .define('#', YTechItemTags.PLATES.get(material))
                .define('H', YTechItemTags.HAMMERS.tag)
                .pattern("#H")
                .pattern("S ")
                .pattern("S ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                .save(output, key(item));
    }

    private void registerSpearRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == MaterialType.FLINT) {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get())
                    .define('T', YTechItemTags.LEATHER_STRIPS)
                    .define('S', Items.FLINT)
                    .define('#', Items.STICK)
                    .pattern(" TS")
                    .pattern(" #T")
                    .pattern("#  ")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.LEATHER_STRIPS))
                    .save(output, key(item));
        } else {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get())
                    .define('T', YTechItemTags.LEATHER_STRIPS)
                    .define('S', YTechItemTags.PLATES.get(material))
                    .define('#', Items.STICK)
                    .pattern(" TS")
                    .pattern(" #T")
                    .pattern("#  ")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.LEATHER_STRIPS))
                    .save(output, key(item));
        }
    }

    private void registerSwordRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == IRON) {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, item.get())
                    .define('S', Items.STICK)
                    .define('#', YTechItemTags.PLATES.get(material))
                    .define('H', YTechItemTags.HAMMERS.tag)
                    .pattern("# ")
                    .pattern("# ")
                    .pattern("SH")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                    .save(output, key(item));
        } else {
            RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.COMBAT, item.get())
                    .requires(Items.STICK)
                    .requires(YTechItemTags.PARTS.get(material, PartType.SWORD_BLADE))
                    .requires(YTechItemTags.HAMMERS.tag)
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                    .save(output, key(item));
        }
    }

    private void registerAqueductRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechBlocks.AQUEDUCT.get())
                .define('#', YTechItemTags.TERRACOTTA_BRICKS)
                .pattern("# #")
                .pattern("# #")
                .pattern("###")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.TERRACOTTA_BRICKS))
                .save(output, key(YTechBlocks.AQUEDUCT));
    }

    private void registerFertilizerRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechBlocks.AQUEDUCT_FERTILIZER.get())
                .define('#', YTechItemTags.AQUEDUCT_HYDRATORS)
                .define('R', YTechItemTags.RODS.get(MaterialType.BRONZE))
                .define('S', YTechItemTags.PLATES.get(MaterialType.BRONZE))
                .define('B', YTechItemTags.BOLTS.get(MaterialType.BRONZE))
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('F', YTechItemTags.FILES.tag)
                .define('C', Tags.Items.CHESTS)
                .pattern("HCF")
                .pattern("S#S")
                .pattern("RBR")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.TERRACOTTA_BRICKS))
                .save(output, key(YTechBlocks.AQUEDUCT_FERTILIZER));
    }

    public void registerHydratorRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechBlocks.AQUEDUCT_HYDRATOR.get())
                .define('#', YTechItemTags.TERRACOTTA_BRICKS)
                .define('R', YTechItemTags.RODS.get(MaterialType.COPPER))
                .define('S', YTechItemTags.PLATES.get(MaterialType.COPPER))
                .define('H', YTechItemTags.HAMMERS.tag)
                .pattern("#R#")
                .pattern("SHS")
                .pattern("#R#")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.TERRACOTTA_BRICKS))
                .save(output, key(YTechBlocks.AQUEDUCT_HYDRATOR));
    }

    public void registerValveRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechBlocks.AQUEDUCT_VALVE.get())
                .define('#', YTechItemTags.TERRACOTTA_BRICKS)
                .define('R', YTechItemTags.RODS.tag)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('S', YTechItemTags.SAWS.tag)
                .pattern("###")
                .pattern("HRS")
                .pattern("###")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.TERRACOTTA_BRICKS))
                .save(output, key(YTechBlocks.AQUEDUCT_VALVE));
    }

    public void registerBrickChimneyRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechBlocks.BRICK_CHIMNEY.get())
                .define('B', Items.BRICKS)
                .pattern(" B ")
                .pattern("B B")
                .pattern(" B ")
                .unlockedBy(RecipeProvider.getHasName(Items.BRICKS), has(Items.BRICKS))
                .save(output, key(YTechBlocks.BRICK_CHIMNEY));
    }

    public void registerBronzeAnvilRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechBlocks.BRONZE_ANVIL.get())
                .define('B', YTechItemTags.STORAGE_BLOCKS.get(MaterialType.BRONZE))
                .define('I', YTechItemTags.INGOTS.get(MaterialType.BRONZE))
                .pattern("BBB")
                .pattern(" I ")
                .pattern("III")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(MaterialType.BRONZE)))
                .save(output, key(YTechBlocks.BRONZE_ANVIL));
    }

    public void registerFirePitRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechBlocks.FIRE_PIT.get())
                .define('S', Items.STICK)
                .define('P', YTechItemTags.PEBBLES)
                .pattern("SS")
                .pattern("PP")
                .unlockedBy("has_pebble", has(YTechItemTags.PEBBLES))
                .save(output, key(YTechBlocks.FIRE_PIT));
    }

    public void registerGrassBedRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechBlocks.GRASS_BED.get())
                .define('G', YTechItemTags.THATCH_SLABS)
                .define('S', YTechItemTags.GRASS_FIBERS)
                .pattern("SSS")
                .pattern("GGG")
                .pattern("GGG")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.THATCH_SLABS))
                .save(output, key(YTechBlocks.GRASS_BED));
    }

    public void registerMillstoneRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechBlocks.MILLSTONE.get())
                .define('W', ItemTags.LOGS)
                .define('S', Items.SMOOTH_STONE_SLAB)
                .define('F', YTechItemTags.SHARP_FLINTS)
                .pattern("WF")
                .pattern("S ")
                .pattern("S ")
                .unlockedBy("has_logs", has(ItemTags.LOGS))
                .save(output, key(YTechBlocks.MILLSTONE));
    }

    public void registerPottersWheelRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechBlocks.POTTERS_WHEEL.get())
                .define('W', ItemTags.LOGS)
                .define('S', ItemTags.WOODEN_SLABS)
                .define('A', YTechItemTags.AXES.tag)
                .define('B', YTechItemTags.SAWS.tag)
                .pattern("ASB")
                .pattern(" W ")
                .pattern("SSS")
                .unlockedBy("has_logs", has(ItemTags.LOGS))
                .save(output, key(YTechBlocks.POTTERS_WHEEL));
    }

    public void registerPrimitiveAlloySmelterRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechBlocks.PRIMITIVE_ALLOY_SMELTER.get())
                .define('B', Items.BRICKS)
                .define('S', YTechItemTags.PRIMITIVE_SMELTERS)
                .pattern("BBB")
                .pattern("SBS")
                .pattern("BBB")
                .unlockedBy(RecipeProvider.getHasName(Items.BRICKS), has(Items.BRICKS))
                .save(output, key(YTechBlocks.PRIMITIVE_ALLOY_SMELTER));
    }

    public void registerPrimitiveSmelterRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechBlocks.PRIMITIVE_SMELTER.get())
                .define('#', Items.FURNACE)
                .define('B', Items.BRICKS)
                .pattern("BBB")
                .pattern("B#B")
                .pattern("BBB")
                .unlockedBy(RecipeProvider.getHasName(Items.BRICKS), has(Items.BRICKS))
                .save(output, key(YTechBlocks.PRIMITIVE_SMELTER));
    }

    private void registerReinforcedBricksRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.BUILDING_BLOCKS, YTechBlocks.REINFORCED_BRICKS.get())
                .define('B', Items.BRICKS)
                .define('P', YTechItemTags.PLATES.get(MaterialType.COPPER))
                .define('#', YTechItemTags.BOLTS.get(MaterialType.COPPER))
                .pattern("#P#")
                .pattern("PBP")
                .pattern("#P#")
                .unlockedBy(RecipeProvider.getHasName(Items.BRICKS), has(Items.BRICKS))
                .save(output, key(YTechBlocks.REINFORCED_BRICKS));
    }

    public void registerReinforcedBrickChimneyRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechBlocks.REINFORCED_BRICK_CHIMNEY.get())
                .define('#', YTechItemTags.REINFORCED_BRICKS)
                .pattern(" # ")
                .pattern("# #")
                .pattern(" # ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.REINFORCED_BRICKS))
                .save(output, key(YTechBlocks.REINFORCED_BRICK_CHIMNEY));
    }

    private void registerTerracottaBricksRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.BUILDING_BLOCKS, YTechBlocks.TERRACOTTA_BRICKS.get())
                .define('B', Items.TERRACOTTA)
                .pattern("BB")
                .pattern("BB")
                .unlockedBy(RecipeProvider.getHasName(Items.TERRACOTTA), has(Items.TERRACOTTA))
                .save(output, key(YTechBlocks.TERRACOTTA_BRICKS));
    }

    private void registerTerracottaBrickSlabRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.BUILDING_BLOCKS, YTechBlocks.TERRACOTTA_BRICK_SLAB.get(), 6)
                .define('B', YTechItemTags.TERRACOTTA_BRICKS)
                .pattern("BBB")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.TERRACOTTA_BRICKS))
                .save(output, key(YTechBlocks.TERRACOTTA_BRICK_SLAB));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(items.getOrThrow(YTechItemTags.TERRACOTTA_BRICKS)), RecipeCategory.BUILDING_BLOCKS, YTechBlocks.TERRACOTTA_BRICK_SLAB.get(), 2)
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.TERRACOTTA_BRICKS))
                .save(output, key(YTechBlocks.TERRACOTTA_BRICK_SLAB, "stonecutting"));
    }

    private void registerTerracottaBrickStairsRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.BUILDING_BLOCKS, YTechBlocks.TERRACOTTA_BRICK_STAIRS.get(), 4)
                .define('B', YTechItemTags.TERRACOTTA_BRICKS)
                .pattern("B  ")
                .pattern("BB ")
                .pattern("BBB")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.TERRACOTTA_BRICKS))
                .save(output, key(YTechBlocks.TERRACOTTA_BRICK_STAIRS));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(items.getOrThrow(YTechItemTags.TERRACOTTA_BRICKS)), RecipeCategory.BUILDING_BLOCKS, YTechBlocks.TERRACOTTA_BRICK_STAIRS.get())
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.TERRACOTTA_BRICKS))
                .save(output, key(YTechBlocks.TERRACOTTA_BRICK_STAIRS, "stonecutting"));
    }

    private void registerThatchBlockRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.BUILDING_BLOCKS, YTechBlocks.THATCH.get())
                .define('B', YTechItemTags.GRASS_FIBERS)
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .unlockedBy("has_thatch", has(YTechItemTags.GRASS_FIBERS))
                .save(output, key(YTechBlocks.THATCH));
    }

    private void registerThatchBlockSlabRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.BUILDING_BLOCKS, YTechBlocks.THATCH_SLAB.get(), 6)
                .define('B', YTechItemTags.THATCH)
                .pattern("BBB")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.THATCH))
                .save(output, key(YTechBlocks.THATCH_SLAB));
    }

    private void registerThatchBlockStairsRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.BUILDING_BLOCKS, YTechBlocks.THATCH_STAIRS.get(), 4)
                .define('B', YTechItemTags.THATCH)
                .pattern("B  ")
                .pattern("BB ")
                .pattern("BBB")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.THATCH))
                .save(output, key(YTechBlocks.THATCH_STAIRS));
    }

    public void registerDryingRackRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get())
                    .define('W', Utils.getLogFromMaterial(material))
                    .define('S', Items.STICK)
                    .define('T', YTechItemTags.GRASS_TWINES)
                    .define('F', YTechItemTags.AXES.tag)
                    .define('B', YTechItemTags.BOLTS.get(MaterialType.WOODEN))
                    .pattern("TST")
                    .pattern("BFB")
                    .pattern("W W")
                    .group(YTechBlocks.DRYING_RACKS.getGroup() + "_" + material.group)
                    .unlockedBy("has_logs", has(ItemTags.LOGS))
                    .save(output, key(item));
    }

    private void registerRawStorageBlockRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        if (!VANILLA_METALS.contains(material)) {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get())
                    .define('#', YTechItemTags.RAW_MATERIALS.get(material))
                    .pattern("###").pattern("###").pattern("###")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.RAW_MATERIALS.get(material)))
                    .save(output, key(item));
        }
    }

    private void registerStorageBlockRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        if (!VANILLA_METALS.contains(material)) {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get())
                    .define('#', YTechItemTags.INGOTS.get(material))
                    .pattern("###").pattern("###").pattern("###")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(material)))
                    .save(output, key(item));
        }
    }

    public void registerTanningRackRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get())
                    .define('W', Utils.getLogFromMaterial(material))
                    .define('S', Items.STICK)
                    .define('T', YTechItemTags.GRASS_TWINES)
                    .define('F', YTechItemTags.AXES.tag)
                    .define('B', YTechItemTags.BOLTS.get(MaterialType.WOODEN))
                    .pattern("TST")
                    .pattern("BFB")
                    .pattern("WSW")
                    .group(YTechBlocks.TANNING_RACKS.getGroup() + "_" + material.group)
                    .unlockedBy("has_logs", has(ItemTags.LOGS))
                    .save(output, key(item));
    }

    private ResourceKey<Recipe<?>> key(@NotNull Item item) {
        return key(Utils.loc(item));
    }

    private ResourceKey<Recipe<?>> key(@NotNull DeferredItem<Item> item) {
        return key(Utils.loc(item));
    }

    private ResourceKey<Recipe<?>> key(@NotNull DeferredBlock<Block> block) {
        return key(Utils.loc(block));
    }

    private ResourceKey<Recipe<?>> key(@NotNull Item item, String from) {
        return key(Utils.modLoc(Utils.loc(item).getPath() + "_from_" + from));
    }

    private ResourceKey<Recipe<?>> key(@NotNull DeferredItem<Item> item, String from) {
        return key(Utils.modLoc(Utils.loc(item.get()).getPath() + "_from_" + from));
    }

    private ResourceKey<Recipe<?>> key(@NotNull DeferredBlock<Block> block, String from) {
        return key(Utils.modLoc(Utils.loc(block.get()).getPath() + "_from_" + from));
    }

    private ResourceKey<Recipe<?>> key(@NotNull ResourceLocation resource) {
        return ResourceKey.create(Registries.RECIPE, resource);
    }

    static class Runner extends RecipeProvider.Runner {
        protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
            super(packOutput, completableFuture);
        }

        @NotNull
        @Override
        protected RecipeProvider createRecipeProvider(@NotNull HolderLookup.Provider provider, @NotNull RecipeOutput recipeOutput) {
            return new YTechRecipeProvider(provider, recipeOutput);
        }

        @NotNull
        @Override
        public String getName() {
            return YTechMod.MOD_ID;
        }
    }
}
