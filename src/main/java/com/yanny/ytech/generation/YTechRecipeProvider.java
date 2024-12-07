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

        mcSplitByChoppingRecipe(Items.ACACIA_LOG, Items.ACACIA_PLANKS);
        mcSplitByChoppingRecipe(Items.BIRCH_LOG, Items.BIRCH_PLANKS);
        mcSplitByChoppingRecipe(Items.CHERRY_LOG, Items.CHERRY_PLANKS);
        mcSplitByChoppingRecipe(Items.JUNGLE_LOG, Items.JUNGLE_PLANKS);
        mcSplitByChoppingRecipe(Items.OAK_LOG, Items.OAK_PLANKS);
        mcSplitByChoppingRecipe(Items.DARK_OAK_LOG, Items.DARK_OAK_PLANKS);
        mcSplitByChoppingRecipe(Items.MANGROVE_LOG, Items.MANGROVE_PLANKS);
        mcSplitByChoppingRecipe(Items.SPRUCE_LOG, Items.SPRUCE_PLANKS);
        mcSplitByChoppingRecipe(Items.CRIMSON_STEM, Items.CRIMSON_PLANKS);
        mcSplitByChoppingRecipe(Items.BAMBOO_BLOCK, Items.BAMBOO_PLANKS);
        mcSplitByChoppingRecipe(Items.WARPED_STEM, Items.WARPED_PLANKS);


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

        mcBowRecipe();
        mcBowlRecipe();
        mcIronBarsRecipe();
        mcIronDoorRecipe();
        mcIronTrapdoorRecipe();
        mcHeavyWeightedPressurePlateRecipe();
        mcLightWeightedPressurePlateRecipe();
        mcChainRecipe();
        mcLanternRecipe();
        mcSoulLanternRecipe();
        mcFishingRodRecipe();
        mcLeadRecipe();
        mcSaddleRecipe();
        mcBoneMealRecipe();
        mcCookieRecipe();
        mcCakeRecipe();
        mcTripwireHookRecipe();
        mcCrossbowRecipe();
        mcCampfireRecipe();
        mcSoulCampfireRecipe();
        mcMudRecipe();

        /*
         * MOD RECIPES
         */

        registerBasketRecipe();
        registerBeeswaxRecipe();
        registerBoneNeedleRecipe();
        registerBreadDoughRecipe();
        registerBrickMoldRecipe();
        registerCookedVenisonRecipe();
        registerDiviningRodRecipe();
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

        YTechItems.CLAY_MOLDS.forEach((part, item) -> smeltingRecipe(YTechItemTags.UNFIRED_MOLDS.get(part), item.get(), 1000, 200));
        YTechItems.PATTERNS.forEach((type, item) -> registerPatternRecipe(item, type));
        YTechItems.SAND_MOLDS.forEach((type, item) -> registerSandMoldRecipe(item, type));
        YTechItems.UNFIRED_MOLDS.forEach((part, item) -> registerUnfiredMoldRecipe(item, part));

        YTechItems.PARTS.forEach((material, map) -> map.forEach((part, item) -> smeltingRecipe(YTechItemTags.INGOTS.get(material), part.ingotCount, YTechItemTags.MOLDS.get(part), item.get(), material.meltingTemp, 200 * part.ingotCount, "mold")));

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
        YTechItems.MESHES.forEach((material, item) -> registerMeshRecipe(item, material));
        YTechItems.MORTAR_AND_PESTLES.forEach((material, item) -> registerMortarAndPestleRecipe(item, material));
        YTechItems.PICKAXES.forEach((material, item) -> registerPickaxeRecipe(item, material));
        YTechItems.PLATES.forEach((material, item) -> registerPlateRecipe(item, material));
        YTechItems.RAW_MATERIALS.forEach((material, item) -> registerRawMaterialRecipe(item, material));
        YTechItems.RODS.forEach((material, item) -> registerRodRecipe(item, material));
        YTechItems.SAWS.forEach((material, item) -> registerSawRecipe(item, material));
        YTechItems.SAW_BLADES.forEach((material, item) -> registerSawBladeRecipe(item, material));
        YTechItems.SHEARS.forEach((material, item) -> registerShearsRecipe(item, material));
        YTechItems.SHOVELS.forEach((material, item) -> registerShovelRecipe(item, material));
        YTechItems.SPEARS.forEach((material, item) -> registerSpearRecipe(item, material));
        YTechItems.SWORDS.forEach((key, item) -> registerSwordRecipe(item, key));

        registerFertilizerRecipe();
        registerHydratorRecipe();
        registerValveRecipe();
        registerBrickChimneyRecipe();
        registerBronzeAnvilRecipe();
        registerCraftingWorkspaceRecipe();
        registerFirePitRecipe();
        registerGrassBedRecipe();
        registerMillstoneRecipe();
        registerPottersWheelRecipe();
        registerPrimitiveAlloySmelterRecipe();
        registerPrimitiveSmelterRecipe();
        registerReinforcedBricksRecipe();
        registerReinforcedBrickChimneyRecipe();
        registerStrainerRecipe();
        registerTerracottaBricksRecipe();
        registerTerracottaBrickSlabRecipe();
        registerTerracottaBrickStairsRecipe();
        registerThatchBlockRecipe();
        registerThatchBlockSlabRecipe();
        registerThatchBlockStairsRecipe();
        registerTreeStumpRecipe();

        YTechItems.AQUEDUCTS.forEach((material, item) -> registerAqueductRecipe(item, material));
        YTechItems.DRYING_RACKS.forEach((material, item) -> registerDryingRackRecipe(item, material));
        YTechItems.RAW_STORAGE_BLOCKS.forEach((material, item) -> registerRawStorageBlockRecipe(item, material));
        YTechItems.STORAGE_BLOCKS.forEach((material, item) -> registerStorageBlockRecipe(item, material));
        YTechItems.TANNING_RACKS.forEach((material, item) -> registerTanningRackRecipe(item, material));

        alloyingRecipe(YTechItemTags.INGOTS.get(COPPER), 9, YTechItemTags.INGOTS.get(TIN), 1, YTechItems.INGOTS.get(BRONZE).get(), 10, Math.max(COPPER.meltingTemp, TIN.meltingTemp), 200);
        alloyingRecipe(YTechItemTags.CRUSHED_MATERIALS.get(GALENA), 1, ItemTags.SMELTS_TO_GLASS, 1, Items.GLASS, 1, 800, 200);

        smeltingRecipe(YTechItemTags.CRUSHED_MATERIALS.get(COPPER), 1, YTechItemTags.MOLDS.get(PartType.INGOT), Items.COPPER_INGOT, COPPER.meltingTemp, 200, "smelting");
        smeltingRecipe(YTechItemTags.CRUSHED_MATERIALS.get(GOLD), 1, YTechItemTags.MOLDS.get(PartType.INGOT), Items.GOLD_INGOT, GOLD.meltingTemp, 200, "smelting");
        smeltingRecipe(YTechItemTags.CRUSHED_MATERIALS.get(CASSITERITE), 1, YTechItemTags.MOLDS.get(PartType.INGOT), YTechItems.INGOTS.get(TIN).get(), CASSITERITE.meltingTemp, 200, "smelting");
        smeltingRecipe(YTechItemTags.CRUSHED_MATERIALS.get(GALENA), 1, YTechItemTags.MOLDS.get(PartType.INGOT), YTechItems.INGOTS.get(LEAD).get(), GALENA.meltingTemp, 200, "smelting");

        smeltingRecipe(YTechItemTags.UNFIRED_CLAY_BUCKETS, YTechItems.CLAY_BUCKET.get(), 1000, 200);
        smeltingRecipe(YTechItemTags.UNFIRED_DECORATED_POTS, Items.DECORATED_POT, 1000, 200);
        smeltingRecipe(YTechItemTags.UNFIRED_FLOWER_POTS, Items.FLOWER_POT, 1000, 200);
        smeltingRecipe(YTechItemTags.UNFIRED_AMPHORAE, YTechItems.AMPHORA.get(), 1000, 200);

        smeltingRecipe(Tags.Items.COBBLESTONES_NORMAL, Items.STONE, 1300, 200);

        hammeringRecipe(YTechItemTags.IRON_BLOOMS, 4, Items.IRON_INGOT);

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
        potteryRecipe(5, YTechItems.UNFIRED_AMPHORA);

        wcChestRecipe();
        wcFurnaceRecipe();
        wcCraftingTableRecipe();
        wcStonecutterRecipe();
        wcBarrelRecipe();
        wcFletchingTableRecipe();
        wcCartographyTableRecipe();
        wcSmithingTableRecipe();
        wcGrindstoneRecipe();
        wcLoomRecipe();
        wcSmokerRecipe();
        wcComposterRecipe();
        wcWoodenBoxRecipe();
        wcToolRackRecipe();
        wcWellPulleyRecipe();

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

        removeVanillaRecipe(Items.GLASS);
        removeVanillaRecipe(Items.STONE);
        removeVanillaSmeltingBlastingRecipe(Items.STONE, Items.COBBLESTONE);

        removeVanillaRecipe(Items.FLOWER_POT);
        removeVanillaRecipe(Items.TORCH);
        removeVanillaRecipe(Items.SOUL_TORCH);
        removeVanillaRecipe(Items.CRAFTING_TABLE);
        removeVanillaRecipe(Items.FURNACE);
        removeVanillaRecipe(Items.SMOKER);
        removeVanillaRecipe(Items.FLETCHING_TABLE);
        removeVanillaRecipe(Items.CARTOGRAPHY_TABLE);
        removeVanillaRecipe(Items.STONECUTTER);
        removeVanillaRecipe(Items.SMITHING_TABLE);
        removeVanillaRecipe(Items.GRINDSTONE);
        removeVanillaRecipe(Items.LOOM);
        removeVanillaRecipe(Items.CHEST);
        removeVanillaRecipe(Items.BARREL);
        removeVanillaRecipe(Items.COMPOSTER);

        SpecialRecipeBuilder.special(TippedArrowRecipe::new).save(output, Utils.mcLoc("decorated_pot_simple").toString());
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
        RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, result, 3)
                .requires(input)
                .requires(YTechItemTags.SAWS.tag)
                .group(Utils.loc(result).getPath())
                .unlockedBy(RecipeProvider.getHasName(input), has(input))
                .save(output, key(result));
    }

    private void mcSplitByChoppingRecipe(@NotNull Item input, @NotNull Item result) {
        ChoppingRecipe.Builder.chopping(items, input, YTechItemTags.AXES.tag, 3, result, 2)
                .group(Utils.loc(result).getPath())
                .unlockedBy(RecipeProvider.getHasName(input), has(input))
                .save(output, key(result, "chopping"));
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
                .requires(YTechItemTags.BOLTS.get(WOODEN))
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

    private void mcSoulCampfireRecipe() {
        ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, Items.SOUL_CAMPFIRE)
                .define('S', Items.STICK)
                .define('L', ItemTags.LOGS)
                .define('T', Items.SOUL_TORCH)
                .pattern(" S ")
                .pattern("STS")
                .pattern("LLL")
                .unlockedBy(RecipeProvider.getHasName(Items.SOUL_TORCH), has(Items.SOUL_TORCH))
                .save(output, key(Items.SOUL_CAMPFIRE));
    }

    private void mcMudRecipe() {
        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, Items.MUD, 3)
                .requires(Items.DIRT, 3)
                .requires(YTechItemTags.WATER_BUCKETS)
                .unlockedBy(RecipeProvider.getHasName(Items.DIRT), has(Items.DIRT))
                .save(output, key(Items.MUD, true));
    }

    private void mcCampfireRecipe() {
        ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, Items.CAMPFIRE)
                .define('S', Items.STICK)
                .define('L', ItemTags.LOGS)
                .define('T', Items.TORCH)
                .pattern(" S ")
                .pattern("STS")
                .pattern("LLL")
                .unlockedBy(RecipeProvider.getHasName(Items.TORCH), has(Items.TORCH))
                .save(output, key(Items.CAMPFIRE));
    }

    private void mcCrossbowRecipe() {
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
    }

    private void mcTripwireHookRecipe() {
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
    }

    private void mcCakeRecipe() {
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
    }

    private void mcCookieRecipe() {
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, Items.COOKIE, 8)
                .define('#', YTechItemTags.FLOURS)
                .define('X', Items.COCOA_BEANS)
                .pattern("#X#")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.FLOURS))
                .save(output, key(Items.COOKIE));
    }

    private void mcBoneMealRecipe() {
        RemainingShapelessRecipe.Builder.shapeless(items, RecipeCategory.MISC, Items.BONE_MEAL)
                .requires(Items.BONE)
                .requires(YTechItemTags.MORTAR_AND_PESTLES.tag)
                .unlockedBy(RecipeProvider.getHasName(Items.BONE), has(Items.BONE))
                .save(output, key(Items.BONE_MEAL));
        MillingRecipe.Builder.milling(Items.BONE, Items.BONE_MEAL, 2)
                .bonusChance(0.2f)
                .unlockedBy(Utils.getHasName(), has(Items.BONE))
                .save(output, key(Items.BONE_MEAL, "milling"));
    }

    private void mcSaddleRecipe() {
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
    }

    private void mcLeadRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, Items.LEAD)
                .define('L', YTechItemTags.LEATHER_STRIPS)
                .define('S', Tags.Items.STRINGS)
                .pattern("LL ")
                .pattern("LS ")
                .pattern("  L")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.LEATHER_STRIPS))
                .save(output, key(Items.LEAD));
    }

    private void mcFishingRodRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, Items.FISHING_ROD)
                .define('B', YTechItemTags.BOLTS.tag)
                .define('S', Items.STICK)
                .define('T', Tags.Items.STRINGS)
                .pattern("  S")
                .pattern(" ST")
                .pattern("S B")
                .unlockedBy(Utils.getHasName(), has(Items.STICK))
                .save(output, key(Items.FISHING_ROD));
    }

    private void mcSoulLanternRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, Items.SOUL_LANTERN)
                .define('I', YTechItemTags.RODS.tag)
                .define('P', YTechItemTags.PLATES.tag)
                .define('T', Items.SOUL_TORCH)
                .pattern(" P ")
                .pattern("ITI")
                .pattern(" P ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(IRON)))
                .save(output, key(Items.SOUL_LANTERN));
    }

    private void mcLanternRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, Items.LANTERN)
                .define('I', YTechItemTags.RODS.tag)
                .define('P', YTechItemTags.PLATES.tag)
                .define('T', Items.TORCH)
                .pattern(" P ")
                .pattern("ITI")
                .pattern(" P ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(IRON)))
                .save(output, key(Items.LANTERN));
    }

    private void mcChainRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.REDSTONE, Items.CHAIN)
                .define('W', YTechItemTags.HAMMERS.tag)
                .define('I', YTechItemTags.RODS.get(IRON))
                .pattern("IW")
                .pattern("I ")
                .pattern("I ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(IRON)))
                .save(output, key(Items.CHAIN));
    }

    private void mcLightWeightedPressurePlateRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.REDSTONE, Items.LIGHT_WEIGHTED_PRESSURE_PLATE)
                .define('W', YTechItemTags.HAMMERS.tag)
                .define('B', YTechItemTags.BOLTS.get(GOLD))
                .define('P', YTechItemTags.PLATES.get(GOLD))
                .pattern(" W ")
                .pattern("PPP")
                .pattern("BBB")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(GOLD)))
                .save(output, key(Items.LIGHT_WEIGHTED_PRESSURE_PLATE));
    }

    private void mcHeavyWeightedPressurePlateRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.REDSTONE, Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .define('W', YTechItemTags.HAMMERS.tag)
                .define('B', YTechItemTags.BOLTS.get(IRON))
                .define('P', YTechItemTags.PLATES.get(IRON))
                .pattern(" W ")
                .pattern("PPP")
                .pattern("BBB")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(IRON)))
                .save(output, key(Items.HEAVY_WEIGHTED_PRESSURE_PLATE));
    }

    private void mcIronTrapdoorRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.REDSTONE, Items.IRON_TRAPDOOR, 2)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('B', YTechItemTags.BOLTS.get(IRON))
                .define('P', YTechItemTags.PLATES.get(IRON))
                .pattern("HB ")
                .pattern("PPP")
                .pattern("PPP")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(IRON)))
                .save(output, key(Items.IRON_TRAPDOOR));
    }

    private void mcIronDoorRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.REDSTONE, Items.IRON_DOOR)
                .define('C', YTechItemTags.PLATES.get(IRON))
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('B', YTechItemTags.BOLTS.get(IRON))
                .pattern("CCH")
                .pattern("CCB")
                .pattern("CC ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(IRON)))
                .save(output, key(Items.IRON_DOOR));
    }

    private void mcIronBarsRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.DECORATIONS, Items.IRON_BARS)
                .define('C', YTechItemTags.RODS.get(IRON))
                .pattern("CCC")
                .pattern("CCC")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(IRON)))
                .save(output, key(Items.IRON_BARS));
    }

    private void mcBowlRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, Items.BOWL)
                .define('#', YTechItemTags.KNIVES.tag)
                .define('P', ItemTags.PLANKS)
                .pattern("P#P")
                .pattern(" P ")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(Items.BOWL));
    }

    private void mcBowRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, Items.BOW)
                .define('#', YTechItemTags.KNIVES.tag)
                .define('S', Items.STICK)
                .define('W', Tags.Items.STRINGS)
                .pattern(" SW")
                .pattern("S#W")
                .pattern(" SW")
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(output, key(Items.BOW));
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
    private void hammeringRecipe(@NotNull TagKey<Item> input, int hitCount, @NotNull Item result) {
        HammeringRecipe.Builder.hammering(items, input, YTechItemTags.HAMMERS.tag, hitCount, result)
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
                .define('T', Tags.Items.BONES)
                .define('#', YTechItemTags.SHARP_FLINTS)
                .pattern("#T")
                .unlockedBy(Utils.getHasName(), has(Tags.Items.BONES))
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

    private void registerDiviningRodRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, YTechItems.DIVINING_ROD.get())
                .define('S', Items.STICK)
                .define('T', YTechItemTags.GRASS_TWINES)
                .define('F', YTechItemTags.SHARP_FLINTS)
                .pattern("SS")
                .pattern("TF")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.SHARP_FLINTS))
                .save(output, key(YTechItems.DIVINING_ROD));
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
        if (material == LEATHER) {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, item.get())
                    .define('#', YTechItemTags.BONE_NEEDLES)
                    .define('S', YTechItemTags.LEATHER_STRIPS)
                    .define('L', Items.LEATHER)
                    .pattern(" # ")
                    .pattern("LSL")
                    .pattern("LSL")
                    .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                    .save(output, key(item));
        } else {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, item.get())
                    .define('H', YTechItemTags.HAMMERS.tag)
                    .define('#', YTechItemTags.PLATES.get(material))
                    .pattern("#H#")
                    .pattern("# #")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                    .save(output, key(item));
        }
    }

    public void registerChestplateRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == LEATHER) {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, item.get())
                    .define('#', YTechItemTags.BONE_NEEDLES)
                    .define('S', YTechItemTags.LEATHER_STRIPS)
                    .define('L', Items.LEATHER)
                    .pattern("L#L")
                    .pattern("LSL")
                    .pattern("LLL")
                    .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                    .save(output, key(item));
        } else {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, item.get())
                    .define('H', YTechItemTags.HAMMERS.tag)
                    .define('#', YTechItemTags.PLATES.get(material))
                    .pattern("#H#")
                    .pattern("###")
                    .pattern("###")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                    .save(output, key(item));
        }
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
        if (material == LEATHER) {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, item.get())
                    .define('#', YTechItemTags.BONE_NEEDLES)
                    .define('S', YTechItemTags.LEATHER_STRIPS)
                    .define('L', Items.LEATHER)
                    .pattern(" # ")
                    .pattern("LLL")
                    .pattern("LSL")
                    .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                    .save(output, key(item));
        } else {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, item.get())
                    .define('H', YTechItemTags.HAMMERS.tag)
                    .define('#', YTechItemTags.PLATES.get(material))
                    .pattern("###")
                    .pattern("#H#")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                    .save(output, key(item));
        }
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
        if (material == LEATHER) {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, item.get())
                    .define('#', YTechItemTags.BONE_NEEDLES)
                    .define('S', YTechItemTags.LEATHER_STRIPS)
                    .define('L', Items.LEATHER)
                    .pattern("LLL")
                    .pattern("L#L")
                    .pattern("LSL")
                    .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                    .save(output, key(item));
        } else {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.COMBAT, item.get())
                    .define('H', YTechItemTags.HAMMERS.tag)
                    .define('#', YTechItemTags.PLATES.get(material))
                    .pattern("###")
                    .pattern("#H#")
                    .pattern("# #")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                    .save(output, key(item));
        }
    }

    public void registerMeshRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == TWINE) {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get())
                    .define('T', YTechItemTags.GRASS_TWINES)
                    .pattern("TTT")
                    .pattern("TTT")
                    .pattern("TTT")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.GRASS_TWINES))
                    .save(output, key(item));
        } else {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get())
                    .define('R', YTechItemTags.RODS.get(material))
                    .pattern("RRR")
                    .pattern("RRR")
                    .pattern("RRR")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.RODS.get(material)))
                    .save(output, key(item));
        }
    }

    public void registerMortarAndPestleRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == MaterialType.STONE) {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, item.get())
                    .define('P', YTechItemTags.PEBBLES)
                    .define('#', Tags.Items.COBBLESTONES)
                    .pattern(" P ")
                    .pattern("# #")
                    .pattern(" # ")
                    .unlockedBy(Utils.getHasName(), has(Tags.Items.STONES))
                    .save(output, key(item));
        } else {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, item.get())
                    .define('P', YTechItemTags.PEBBLES)
                    .define('#', YTechItemTags.PLATES.get(material))
                    .pattern(" P ")
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
            HammeringRecipe.Builder.hammering(items, YTechItemTags.INGOTS.get(material), YTechItemTags.HAMMERS.tag, 2, item.get())
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

    private void registerShearsRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, item.get())
                .define('#', YTechItemTags.PLATES.get(material))
                .define('R', YTechItemTags.RODS.get(material))
                .define('B', YTechItemTags.BOLTS.get(material))
                .define('F', YTechItemTags.FILES.tag)
                .define('L', YTechItemTags.LEATHER_STRIPS)
                .pattern(" #F")
                .pattern("RB#")
                .pattern("LR ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.PLATES.get(material)))
                .save(output, key(item));
    }

    private void registerShovelRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == WOODEN) {
            RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.TOOLS, item.get())
                    .define('#', YTechItemTags.PLATES.get(WOODEN))
                    .define('S', Items.STICK)
                    .pattern("#")
                    .pattern("S")
                    .pattern("S")
                    .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                    .save(output, key(item));
        } else {
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
    }

    private void registerSpearRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        if (material == FLINT) {
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

    private void registerAqueductRecipe(@NotNull DeferredItem<Item> item, MaterialType material) {
        switch (material) {
            case MUDBRICK -> RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get(), 2)
                    .define('#', Items.MUD_BRICKS)
                    .pattern("# #")
                    .pattern("# #")
                    .pattern("###")
                    .unlockedBy(Utils.getHasName(), has(Items.MUD_BRICKS))
                    .save(output, key(item));
            case TERRACOTTA -> RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get(), 2)
                    .define('#', YTechItemTags.TERRACOTTA_BRICKS)
                    .define('H', YTechItemTags.HAMMERS.tag)
                    .pattern("# #")
                    .pattern("#H#")
                    .pattern("###")
                    .unlockedBy(Utils.getHasName(), has(YTechItemTags.TERRACOTTA_BRICKS))
                    .save(output, key(item));
            case STONE -> RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.MISC, item.get(), 2)
                    .define('#', Items.STONE_BRICKS)
                    .define('H', YTechItemTags.HAMMERS.tag)
                    .pattern("# #")
                    .pattern("#H#")
                    .pattern("###")
                    .unlockedBy(Utils.getHasName(), has(Items.STONE_BRICKS))
                    .save(output, key(item));
            default -> throw new IllegalStateException();
        }
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
                .define('R', YTechItemTags.RODS.get(COPPER))
                .define('S', YTechItemTags.PLATES.get(COPPER))
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
        WorkspaceCraftingRecipe.Builder.recipe(items, YTechBlocks.BRONZE_ANVIL.get())
                .define('B', YTechItemTags.STORAGE_BLOCKS.get(MaterialType.BRONZE))
                .bottomPattern(" B ")
                .bottomPattern("BBB")
                .bottomPattern(" B ")
                .middlePattern("   ")
                .middlePattern(" B ")
                .middlePattern("   ")
                .topPattern("BBB")
                .topPattern("BBB")
                .topPattern("BBB")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(MaterialType.BRONZE)))
                .save(output, key(YTechBlocks.BRONZE_ANVIL));
    }

    public void registerCraftingWorkspaceRecipe() {
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, YTechBlocks.CRAFTING_WORKSPACE.get())
                .define('T', YTechItemTags.GRASS_TWINES)
                .define('S', Items.STICK)
                .pattern("TT")
                .pattern("SS")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.GRASS_TWINES))
                .save(output, key(YTechBlocks.CRAFTING_WORKSPACE));
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
        WorkspaceCraftingRecipe.Builder.recipe(items, YTechBlocks.MILLSTONE.get())
            .define('W', ItemTags.LOGS)
            .define('S', Items.SMOOTH_STONE)
            .bottomPattern("SSS")
            .bottomPattern("SWS")
            .bottomPattern("SSS")
            .middlePattern("SSS")
            .middlePattern("SWS")
            .middlePattern("SSS")
            .topPattern("   ")
            .topPattern(" W ")
            .topPattern("   ")
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
        WorkspaceCraftingRecipe.Builder.recipe(items, YTechBlocks.PRIMITIVE_ALLOY_SMELTER.get())
                .define('A', Items.FURNACE)
                .define('C', Items.BRICKS)
                .bottomPattern("CCC")
                .bottomPattern("CCC")
                .bottomPattern("CCC")
                .middlePattern("CCC")
                .middlePattern("AAA")
                .middlePattern("C C")
                .topPattern("CCC")
                .topPattern("C C")
                .topPattern("CCC")
                .unlockedBy(RecipeProvider.getHasName(Items.BRICKS), has(Items.BRICKS))
                .save(output, key(YTechBlocks.PRIMITIVE_ALLOY_SMELTER));
    }

    public void registerPrimitiveSmelterRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, YTechBlocks.PRIMITIVE_SMELTER.get())
                .define('A', Items.FURNACE)
                .define('C', Items.BRICKS)
                .bottomPattern("CCC")
                .bottomPattern("CCC")
                .bottomPattern("CCC")
                .middlePattern("CCC")
                .middlePattern("CAC")
                .middlePattern("C C")
                .topPattern("CCC")
                .topPattern("C C")
                .topPattern("CCC")
                .unlockedBy(RecipeProvider.getHasName(Items.BRICKS), has(Items.BRICKS))
                .save(output, key(YTechBlocks.PRIMITIVE_SMELTER));
    }

    private void registerReinforcedBricksRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.BUILDING_BLOCKS, YTechBlocks.REINFORCED_BRICKS.get())
                .define('B', Items.BRICKS)
                .define('P', YTechItemTags.PLATES.get(COPPER))
                .define('#', YTechItemTags.BOLTS.get(COPPER))
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

    private void registerStrainerRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, YTechItems.STRAINER.get())
                .define('L', ItemTags.LOGS)
                .define('W', YTechItemTags.WOODEN_BOXES)
                .bottomPattern("LLL")
                .bottomPattern("LLL")
                .bottomPattern("LLL")
                .middlePattern("LLL")
                .middlePattern("LWL")
                .middlePattern("LLL")
                .topPattern("L L")
                .topPattern("   ")
                .topPattern("L L")
                .unlockedBy(Utils.getHasName(), has(ItemTags.LOGS))
                .save(output, key(YTechBlocks.STRAINER));
    }

    private void registerTerracottaBricksRecipe() {
        RemainingShapedRecipe.Builder.shaped(items, RecipeCategory.BUILDING_BLOCKS, YTechBlocks.TERRACOTTA_BRICKS.get(), 4)
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
        WorkspaceCraftingRecipe.Builder.recipe(items, item.get())
                .define('W', Utils.getLogFromMaterial(material))
                .define('T', YTechItemTags.GRASS_TWINES)
                .bottomPattern("   ")
                .bottomPattern("W W")
                .bottomPattern("   ")
                .middlePattern("   ")
                .middlePattern("W W")
                .middlePattern("   ")
                .topPattern("   ")
                .topPattern("WTW")
                .topPattern("   ")
                .group(YTechBlocks.DRYING_RACKS.getGroup() + "_" + material.group)
                .unlockedBy("has_logs", has(ItemTags.LOGS))
                .save(output, key(item));
    }

    private void registerTreeStumpRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, YTechItems.TREE_STUMP.get())
                .define('C', ItemTags.LOGS)
                .define('S', Tags.Items.COBBLESTONES_NORMAL)
                .bottomPattern("SSS")
                .bottomPattern("SSS")
                .bottomPattern("SSS")
                .middlePattern("CCC")
                .middlePattern("CCC")
                .middlePattern("CCC")
                .topPattern("CCC")
                .topPattern("CCC")
                .topPattern("CCC")
                .unlockedBy(Utils.getHasName(), has(ItemTags.LOGS))
                .save(output, key(YTechItems.TREE_STUMP));
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
        WorkspaceCraftingRecipe.Builder.recipe(items, item.get())
                .define('W', Utils.getLogFromMaterial(material))
                .define('T', YTechItemTags.GRASS_TWINES)
                .bottomPattern("   ")
                .bottomPattern("WTW")
                .bottomPattern("   ")
                .middlePattern("   ")
                .middlePattern("W W")
                .middlePattern("   ")
                .topPattern("   ")
                .topPattern("WTW")
                .topPattern("   ")
                .group(YTechBlocks.TANNING_RACKS.getGroup() + "_" + material.group)
                .unlockedBy("has_logs", has(ItemTags.LOGS))
                .save(output, key(item));
    }

    private ResourceKey<Recipe<?>> key(@NotNull Item item) {
        return key(item, false);
    }

    private ResourceKey<Recipe<?>> key(@NotNull Item item, boolean modLoc) {
        return key(modLoc ? Utils.modLoc(item) : Utils.loc(item));
    }

    private ResourceKey<Recipe<?>> key(@NotNull DeferredItem<? extends Item> item) {
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

    private void wcChestRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, Items.CHEST)
                .define('C', ItemTags.PLANKS)
                .define('R', YTechItemTags.RODS.get(IRON))
                .define('B', ItemTags.WOODEN_BUTTONS)
                .bottomPattern("CCC")
                .bottomPattern("CCC")
                .bottomPattern("CCC")
                .middlePattern("CCC")
                .middlePattern("C C")
                .middlePattern("CBC")
                .topPattern("RRR")
                .topPattern("CCC")
                .topPattern("CCC")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(Items.CHEST, true));
    }

    private void wcFurnaceRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, Items.FURNACE)
                .define('C', Tags.Items.COBBLESTONES_NORMAL)
                .define('A', Items.CAMPFIRE)
                .bottomPattern("CCC")
                .bottomPattern("CCC")
                .bottomPattern("CCC")
                .middlePattern("CCC")
                .middlePattern("CAC")
                .middlePattern("C C")
                .topPattern("CCC")
                .topPattern("CCC")
                .topPattern("CCC")
                .unlockedBy(RecipeProvider.getHasName(Items.COBBLESTONE), has(Tags.Items.COBBLESTONES_NORMAL))
                .save(output, key(Items.FURNACE, true));
    }

    private void wcCraftingTableRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, Items.CRAFTING_TABLE)
                .define('L', Items.LEATHER)
                .define('P', ItemTags.PLANKS)
                .bottomPattern("PPP")
                .bottomPattern("PPP")
                .bottomPattern("PPP")
                .middlePattern("PPP")
                .middlePattern("PPP")
                .middlePattern("PPP")
                .topPattern("   ")
                .topPattern(" L ")
                .topPattern("   ")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(Items.CRAFTING_TABLE, true));
    }

    private void wcStonecutterRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, Items.STONECUTTER)
                .define('L', ItemTags.LOGS)
                .define('P', Items.SMOOTH_STONE)
                .define('S', YTechItemTags.SAW_BLADES.tag)
                .bottomPattern("LPL")
                .bottomPattern("PPP")
                .bottomPattern("LPL")
                .middlePattern("LPL")
                .middlePattern("PPP")
                .middlePattern("LPL")
                .topPattern("   ")
                .topPattern(" S ")
                .topPattern("   ")
                .unlockedBy(Utils.getHasName(), has(Items.SMOOTH_STONE))
                .save(output, key(Items.STONECUTTER, true));
    }

    private void wcBarrelRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, Items.BARREL)
                .define('S', ItemTags.WOODEN_SLABS)
                .define('P', ItemTags.PLANKS)
                .bottomPattern("PPP")
                .bottomPattern("PPP")
                .bottomPattern("PPP")
                .middlePattern("PPP")
                .middlePattern("P P")
                .middlePattern("PPP")
                .topPattern("SSS")
                .topPattern("SSS")
                .topPattern("SSS")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(Items.BARREL, true));
    }

    private void wcFletchingTableRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, Items.FLETCHING_TABLE)
                .define('T', Items.TARGET)
                .define('P', ItemTags.PLANKS)
                .bottomPattern("PPP")
                .bottomPattern("PPP")
                .bottomPattern("PPP")
                .middlePattern("PTP")
                .middlePattern("PPP")
                .middlePattern("PTP")
                .topPattern("PPP")
                .topPattern("PPP")
                .topPattern("PPP")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(Items.FLETCHING_TABLE, true));
    }

    private void wcCartographyTableRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, Items.CARTOGRAPHY_TABLE)
                .define('P', ItemTags.PLANKS)
                .define('M', Items.MAP)
                .bottomPattern("PPP")
                .bottomPattern("PPP")
                .bottomPattern("PPP")
                .middlePattern("PPP")
                .middlePattern("PPP")
                .middlePattern("PPP")
                .topPattern("   ")
                .topPattern(" M ")
                .topPattern("   ")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(Items.CARTOGRAPHY_TABLE, true));
    }

    private void wcSmithingTableRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, Items.SMITHING_TABLE)
                .define('P', ItemTags.PLANKS)
                .define('C', YTechItemTags.STORAGE_BLOCKS.get(IRON))
                .bottomPattern("CPC")
                .bottomPattern("PPP")
                .bottomPattern("CPC")
                .middlePattern("CPC")
                .middlePattern("PPP")
                .middlePattern("CPC")
                .topPattern("CCC")
                .topPattern("CCC")
                .topPattern("CCC")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.INGOTS.get(IRON)))
                .save(output, key(Items.SMITHING_TABLE, true));
    }

    private void wcGrindstoneRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, Items.GRINDSTONE)
                .define('L', ItemTags.LOGS)
                .define('S', Items.SMOOTH_STONE)
                .bottomPattern(" L ")
                .bottomPattern("SSS")
                .bottomPattern(" L ")
                .middlePattern(" L ")
                .middlePattern("SSS")
                .middlePattern(" L ")
                .topPattern("   ")
                .topPattern("SSS")
                .topPattern("   ")
                .unlockedBy(Utils.getHasName(), has(Items.SMOOTH_STONE))
                .save(output, key(Items.GRINDSTONE, true));
    }

    private void wcLoomRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, Items.LOOM)
                .define('P', ItemTags.PLANKS)
                .define('S', Tags.Items.STRINGS)
                .bottomPattern("P P")
                .bottomPattern("   ")
                .bottomPattern("P P")
                .middlePattern("PPP")
                .middlePattern("PPP")
                .middlePattern("PPP")
                .topPattern("PPP")
                .topPattern("SSS")
                .topPattern("SSS")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(Items.LOOM, true));
    }

    private void wcSmokerRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, Items.SMOKER)
                .define('L', ItemTags.LOGS)
                .define('A', Items.CAMPFIRE)
                .define('C', Tags.Items.COBBLESTONES_NORMAL)
                .bottomPattern("LCL")
                .bottomPattern("CCC")
                .bottomPattern("LCL")
                .middlePattern("LCL")
                .middlePattern("CAC")
                .middlePattern("L L")
                .topPattern("LCL")
                .topPattern("C C")
                .topPattern("LCL")
                .unlockedBy(Utils.getHasName(), has(Tags.Items.COBBLESTONES_NORMAL))
                .save(output, key(Items.SMOKER, true));
    }

    private void wcComposterRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, Items.COMPOSTER)
                .define('L', ItemTags.PLANKS)
                .bottomPattern("LLL")
                .bottomPattern("LLL")
                .bottomPattern("LLL")
                .middlePattern("LLL")
                .middlePattern("L L")
                .middlePattern("LLL")
                .topPattern("LLL")
                .topPattern("L L")
                .topPattern("LLL")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(Items.COMPOSTER, true));
    }

    private void wcWoodenBoxRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, YTechItems.WOODEN_BOX.get())
                .define('L', ItemTags.PLANKS)
                .bottomPattern("LLL")
                .bottomPattern("LLL")
                .bottomPattern("LLL")
                .middlePattern("LLL")
                .middlePattern("L L")
                .middlePattern("LLL")
                .topPattern("   ")
                .topPattern("   ")
                .topPattern("   ")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(YTechItems.WOODEN_BOX));
    }

    private void wcToolRackRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, YTechItems.TOOL_RACK.get())
                .define('L', ItemTags.PLANKS)
                .define('B', YTechItemTags.BOLTS.tag)
                .bottomPattern("LLL")
                .bottomPattern("BBB")
                .bottomPattern("   ")
                .middlePattern("LLL")
                .middlePattern("BBB")
                .middlePattern("   ")
                .topPattern("LLL")
                .topPattern("BBB")
                .topPattern("   ")
                .unlockedBy(Utils.getHasName(), has(ItemTags.PLANKS))
                .save(output, key(YTechItems.TOOL_RACK));
    }

    private void wcWellPulleyRecipe() {
        WorkspaceCraftingRecipe.Builder.recipe(items, YTechItems.WELL_PULLEY.get())
                .define('L', ItemTags.LOGS)
                .define('P', ItemTags.PLANKS)
                .define('B', YTechItemTags.TERRACOTTA_BRICKS)
                .define('T', YTechItemTags.GRASS_TWINES)
                .define('V', YTechItemTags.AQUEDUCT_VALVES)
                .bottomPattern("BBB")
                .bottomPattern("BVB")
                .bottomPattern("BBB")
                .middlePattern("   ")
                .middlePattern("LTL")
                .middlePattern("   ")
                .topPattern("   ")
                .topPattern("LPL")
                .topPattern("   ")
                .unlockedBy(Utils.getHasName(), has(YTechItemTags.TERRACOTTA_BRICKS))
                .save(output, key(YTechItems.WELL_PULLEY));
    }
}
