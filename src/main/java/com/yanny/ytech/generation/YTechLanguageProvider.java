package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.*;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechEntityTypes;
import com.yanny.ytech.registration.YTechItemTags;
import com.yanny.ytech.registration.YTechItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.function.TriFunction;

import java.util.EnumSet;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

class YTechLanguageProvider extends LanguageProvider {
    public YTechLanguageProvider(PackOutput output, String locale) {
        super(output, YTechMod.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        addItem(YTechItems.ANTLER, "Antler");
        addItem(YTechItems.BASKET, "Basket");
        addItem(YTechItems.BEESWAX, "Beeswax");
        addItem(YTechItems.BONE_NEEDLE, "Bone Needle");
        addItem(YTechItems.BREAD_DOUGH, "Bread Dough");
        addItem(YTechItems.BRICK_MOLD, "Brick Mold");
        addItem(YTechItems.CLAY_BUCKET, "Clay Bucket");
        addItem(YTechItems.COOKED_VENISON, "Cooked Venison");
        addItem(YTechItems.DRIED_BEEF, "Dried Beef");
        addItem(YTechItems.DRIED_CHICKEN, "Dried Chicken");
        addItem(YTechItems.DRIED_COD, "Dried Cod");
        addItem(YTechItems.DRIED_MUTTON, "Dried Mutton");
        addItem(YTechItems.DRIED_PORKCHOP, "Dried Porkchop");
        addItem(YTechItems.DRIED_RABBIT, "Dried Rabbit");
        addItem(YTechItems.DRIED_SALMON, "Dried Salmon");
        addItem(YTechItems.DRIED_VENISON, "Dried Venison");
        addItem(YTechItems.FLOUR, "Flour");
        addItem(YTechItems.GRASS_FIBERS, "Grass Fibers");
        addItem(YTechItems.GRASS_TWINE, "Grass Twine");
        addItem(YTechItems.IRON_BLOOM, "Iron Bloom");
        addItem(YTechItems.LAVA_CLAY_BUCKET, "Lava Clay Bucket");
        addItem(YTechItems.LEATHER_STRIPS, "Leather Strips");
        addItem(YTechItems.MAMMOTH_TUSK, "Mammoth Tusk");
        addItem(YTechItems.PEBBLE, "Pebble");
        addItem(YTechItems.RAW_HIDE, "Raw Hide");
        addItem(YTechItems.RHINO_HORN, "Rhino Horn");
        addItem(YTechItems.SHARP_FLINT, "Sharp Flint");
        addItem(YTechItems.UNFIRED_AMPHORA, "Unfired Amphora");
        addItem(YTechItems.UNFIRED_BRICK, "Unfired Brick");
        addItem(YTechItems.UNFIRED_CLAY_BUCKET, "Unfired Clay Bucket");
        addItem(YTechItems.UNFIRED_DECORATED_POT, "Unfired Decorated Pot");
        addItem(YTechItems.UNFIRED_FLOWER_POT, "Unfired Flower Pot");
        addItem(YTechItems.UNLIT_TORCH, "Unlit Torch");
        addItem(YTechItems.VENISON, "Venison");
        addItem(YTechItems.WATER_CLAY_BUCKET, "Water Clay Bucket");

        addItem(YTechItems.CHLORITE_BRACELET, "Chlorite Bracelet");
        addItem(YTechItems.LION_MAN, "Lion-Man");
        addItem(YTechItems.SHELL_BEADS, "Shell Beads");
        addItem(YTechItems.VENUS_OF_HOHLE_FELS, "Venus of Hohle Fels");
        addItem(YTechItems.WILD_HORSE, "Wild Horse");

        addItem(YTechItems.AUROCHS_SPAWN_EGG, "Aurochs Spawn Egg");
        addItem(YTechItems.DEER_SPAWN_EGG, "Deer Spawn Egg");
        addItem(YTechItems.FOWL_SPAWN_EGG, "Fowl Spawn Egg");
        addItem(YTechItems.MOUFLON_SPAWN_EGG, "Mouflon Spawn Egg");
        addItem(YTechItems.SABER_TOOTH_TIGER_SPAWN_EGG, "Saber-Toothed Tiger Spawn Egg");
        addItem(YTechItems.TERROR_BIRD_SPAWN_EGG, "Terror Bird Spawn Egg");
        addItem(YTechItems.WILD_BOAR_SPAWN_EGG, "Wild Boar Spawn Egg");
        addItem(YTechItems.WOOLLY_MAMMOTH_SPAWN_EGG, "Woolly Mammoth Spawn Egg");
        addItem(YTechItems.WOOLLY_RHINO_SPAWN_EGG, "Woolly Rhino Spawn Egg");

        addBlock(YTechBlocks.AMPHORA, "Amphora");
        addBlock(YTechBlocks.AQUEDUCT, "Aqueduct");
        addBlock(YTechBlocks.AQUEDUCT_FERTILIZER, "Aqueduct Fertilizer");
        addBlock(YTechBlocks.AQUEDUCT_HYDRATOR, "Aqueduct Hydrator");
        addBlock(YTechBlocks.AQUEDUCT_VALVE, "Aqueduct Valve");
        addBlock(YTechBlocks.BRICK_CHIMNEY, "Brick Chimney");
        addBlock(YTechBlocks.BRONZE_ANVIL, "Bronze Anvil");
        addBlock(YTechBlocks.CRAFTING_WORKSPACE, "Crafting Workspace");
        addBlock(YTechBlocks.FIRE_PIT, "Fire Pit");
        addBlock(YTechBlocks.GRASS_BED, "Grass Bed");
        addBlock(YTechBlocks.MILLSTONE, "Millstone");
        addBlock(YTechBlocks.POTTERS_WHEEL, "Potter's Wheel");
        addBlock(YTechBlocks.PRIMITIVE_ALLOY_SMELTER, "Primitive Alloy Smelter");
        addBlock(YTechBlocks.PRIMITIVE_SMELTER, "Primitive Smelter");
        addBlock(YTechBlocks.REINFORCED_BRICKS, "Reinforced Bricks");
        addBlock(YTechBlocks.REINFORCED_BRICK_CHIMNEY, "Reinforced Brick Chimney");
        addBlock(YTechBlocks.TERRACOTTA_BRICKS, "Terracotta Bricks");
        addBlock(YTechBlocks.TERRACOTTA_BRICK_SLAB, "Terracotta Brick Slab");
        addBlock(YTechBlocks.TERRACOTTA_BRICK_STAIRS, "Terracotta Brick Stairs");
        addBlock(YTechBlocks.THATCH, "Thatch");
        addBlock(YTechBlocks.THATCH_SLAB, "Thatch Slab");
        addBlock(YTechBlocks.THATCH_STAIRS, "Thatch Stairs");
        addBlock(YTechBlocks.TREE_STUMP, "Tree Stump");
        addBlock(YTechBlocks.WOODEN_BOX, "Wooden Box");

        addTypedItem(YTechItems.CLAY_MOLDS, NameHolder.suffix("Clay Mold"), YTechLanguageProvider::getName);
        addTypedItem(YTechItems.PATTERNS, NameHolder.suffix("Pattern"), YTechLanguageProvider::getName);
        addTypedItem(YTechItems.SAND_MOLDS, NameHolder.suffix("Sand Mold"), YTechLanguageProvider::getName);
        addTypedItem(YTechItems.UNFIRED_MOLDS, NameHolder.both("Unfired", "Mold"), YTechLanguageProvider::getName);

        addMultiTypedItem(YTechItems.PARTS, NameHolder.suffix("Part"), YTechLanguageProvider::getName);

        addTypedItem(YTechItems.ARROWS, NameHolder.suffix("Arrow"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.AXES, NameHolder.suffix("Axe"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronWoodLeatherFilter);
        addTypedItem(YTechItems.BOLTS, NameHolder.suffix("Bolt"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.BOOTS, NameHolder.suffix("Boots"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronWoodLeatherFilter);
        addTypedItem(YTechItems.CHESTPLATES, NameHolder.suffix("Chestplate"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronWoodLeatherFilter);
        addTypedItem(YTechItems.CRUSHED_MATERIALS, NameHolder.prefix("Crushed"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.FILES, NameHolder.suffix("File"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.HAMMERS, NameHolder.suffix("Hammer"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.HELMETS, NameHolder.suffix("Helmet"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronWoodLeatherFilter);
        addTypedItem(YTechItems.HOES, NameHolder.suffix("Hoe"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronWoodLeatherFilter);
        addTypedItem(YTechItems.INGOTS, NameHolder.suffix("Ingot"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::vanillaMaterialsFilter);
        addTypedItem(YTechItems.KNIVES, NameHolder.suffix("Knife"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.LEGGINGS, NameHolder.suffix("Leggings"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronWoodLeatherFilter);
        addTypedItem(YTechItems.MORTAR_AND_PESTLES, NameHolder.suffix("Mortar and Pestle"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.PICKAXES, NameHolder.suffix("Pickaxe"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronWoodLeatherFilter);
        addTypedItem(YTechItems.PLATES, NameHolder.suffix("Plate"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.RAW_MATERIALS, NameHolder.prefix("Raw"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::vanillaMaterialsFilter);
        addTypedItem(YTechItems.RODS, NameHolder.suffix("Rod"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.SAWS, NameHolder.suffix("Saw"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.SAW_BLADES, NameHolder.suffix("Saw Blade"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.SHEARS, NameHolder.suffix("Shears"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::ironFilter);
        addTypedItem(YTechItems.SHOVELS, NameHolder.suffix("Shovel"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronWoodLeatherFilter);
        addTypedItem(YTechItems.SPEARS, NameHolder.suffix("Spear"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.SWORDS, NameHolder.suffix("Sword"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronWoodLeatherFilter);

        addMaterialBlock(YTechBlocks.DEEPSLATE_ORES, NameHolder.both("Deepslate", "Ore"), YTechLanguageProvider::vanillaMaterialsFilter);
        addMaterialBlock(YTechBlocks.DRYING_RACKS, NameHolder.suffix("Drying Rack"));
        addMaterialBlock(YTechBlocks.GRAVEL_DEPOSITS, NameHolder.suffix("Gravel Deposit"));
        addMaterialBlock(YTechBlocks.NETHER_ORES, NameHolder.both("Nether", "Ore"), (type) -> type.getKey() != MaterialType.GOLD);
        addStorageBlockLanguage(YTechBlocks.RAW_STORAGE_BLOCKS, "Block of Raw");
        addMaterialBlock(YTechBlocks.SAND_DEPOSITS, NameHolder.suffix("Sand Deposit"));
        addMaterialBlock(YTechBlocks.STONE_ORES, NameHolder.suffix("Ore"), YTechLanguageProvider::vanillaMaterialsFilter);
        addStorageBlockLanguage(YTechBlocks.STORAGE_BLOCKS, "Block of");
        addMaterialBlock(YTechBlocks.TANNING_RACKS, NameHolder.suffix("Tanning Rack"));

        addEntityType(YTechEntityTypes.AUROCHS, "Aurochs");
        addEntityType(YTechEntityTypes.DEER, "Deer");
        addEntityType(YTechEntityTypes.FOWL, "Fowl");
        addEntityType(YTechEntityTypes.MOUFLON, "Mouflon");
        addEntityType(YTechEntityTypes.SABER_TOOTH_TIGER, "Saber-Toothed Tiger");
        addEntityType(YTechEntityTypes.TERROR_BIRD, "Terror Bird");
        addEntityType(YTechEntityTypes.WILD_BOAR, "Wild Boar");
        addEntityType(YTechEntityTypes.WOOLLY_MAMMOTH, "Woolly Mammoth");
        addEntityType(YTechEntityTypes.WOOLLY_RHINO, "Woolly Rhino");
        
        addTag(YTechItemTags.ANTLERS, "Antlers");
        addTag(YTechItemTags.BASKETS, "Baskets");
        addTag(YTechItemTags.BEESWAXES, "Bees Waxes");
        addTag(YTechItemTags.BONE_NEEDLES, "Bone Needles");
        addTag(YTechItemTags.BREAD_DOUGHS, "Bread Doughs");
        addTag(YTechItemTags.BRICK_MOLDS, "Brick Molds");
        addTag(YTechItemTags.CLAY_BUCKETS, "Clay Buckets");
        addTag(YTechItemTags.COOKED_VENISON, "Cooked Venison");
        addTag(YTechItemTags.DRIED_BEEFS, "Dried Beefs");
        addTag(YTechItemTags.DRIED_CHICKENS, "Dried Chicken");
        addTag(YTechItemTags.DRIED_CODS, "Dried Cods");
        addTag(YTechItemTags.DRIED_MUTTONS, "Dried Mutton");
        addTag(YTechItemTags.DRIED_PORKCHOP, "Dried Porkchop");
        addTag(YTechItemTags.DRIED_RABBITS, "Dried Rabbit");
        addTag(YTechItemTags.DRIED_SALMONS, "Dried Salmon");
        addTag(YTechItemTags.DRIED_VENISON, "Dried Venison");
        addTag(YTechItemTags.FLOURS, "Flours");
        addTag(YTechItemTags.GRASS_FIBERS, "Grass Fibers");
        addTag(YTechItemTags.GRASS_TWINES, "Grass Twines");
        addTag(YTechItemTags.IRON_BLOOMS, "Iron Blooms");
        addTag(YTechItemTags.LAVA_BUCKETS, "Lava Buckets");
        addTag(YTechItemTags.LEATHER_STRIPS, "Leather Strips");
        addTag(YTechItemTags.MAMMOTH_TUSKS, "Mammoth Tusks");
        addTag(YTechItemTags.PEBBLES, "Pebbles");
        addTag(YTechItemTags.RAW_HIDES, "Raw Hides");
        addTag(YTechItemTags.RHINO_HORNS, "Rhino Horns");
        addTag(YTechItemTags.SHARP_FLINTS, "Sharp Flints");
        addTag(YTechItemTags.UNFIRED_BRICKS, "Unfired Bricks");
        addTag(YTechItemTags.UNFIRED_CLAY_BUCKETS, "Unfired Clay Buckets");
        addTag(YTechItemTags.UNFIRED_DECORATED_POTS, "Unfired Decorated Pots");
        addTag(YTechItemTags.UNFIRED_FLOWER_POTS, "Unfired Flower Pots");
        addTag(YTechItemTags.UNLIT_TORCH, "Unlit Torch");
        addTag(YTechItemTags.VENISON, "Venison");
        addTag(YTechItemTags.WATER_BUCKETS, "Water Buckets");

        addTag(YTechItemTags.FERTILIZER, "Fertilizers");
        addTag(YTechItemTags.FIRE_SOURCE, "Fire Sources");
        addTag(YTechItemTags.SOUL_FIRE_SOURCE, "Soul Fire Sources");
        
        addTag(YTechItemTags.AUROCHS_FOOD, "Aurochs Foods");
        addTag(YTechItemTags.AUROCHS_TEMP_ITEMS, "Aurochs Temptation Items");
        addTag(YTechItemTags.DEER_TEMP_ITEMS, "Deer Temptation Items");
        addTag(YTechItemTags.FOWL_FOOD, "Fowl Foods");
        addTag(YTechItemTags.FOWL_TEMP_ITEMS, "Fowl Temptation Items");
        addTag(YTechItemTags.MOUFLON_FOOD, "Mouflon Foods");
        addTag(YTechItemTags.MOUFLON_TEMP_ITEMS, "Mouflon Temptation Items");
        addTag(YTechItemTags.SABER_TOOTH_TIGER_TEMP_ITEMS, "Saber-Toothed Tiger Temptation Items");
        addTag(YTechItemTags.TERROR_BIRD_TEMP_ITEMS, "Terror Bird Temptation Items");
        addTag(YTechItemTags.WILD_BOAR_FOOD, "Wild Boar Foods");
        addTag(YTechItemTags.WILD_BOAR_TEMP_ITEMS, "Wild Boar Temptation Items");
        addTag(YTechItemTags.WOOLLY_MAMMOTH_TEMP_ITEMS, "Woolly Mammoth Temptation Items");
        addTag(YTechItemTags.WOOLLY_RHINO_TEMP_ITEMS, "Woolly Rhino Temptation Items");

        addTag(YTechItemTags.AMPHORAE, "Amphorae");
        addTag(YTechItemTags.AQUEDUCTS, "Aqueducts");
        addTag(YTechItemTags.AQUEDUCT_FERTILIZERS, "Aqueduct Fertilizers");
        addTag(YTechItemTags.AQUEDUCT_HYDRATORS, "Aqueduct Hydrators");
        addTag(YTechItemTags.AQUEDUCT_VALVES, "Aqueduct Valves");
        addTag(YTechItemTags.BRICK_CHIMNEYS, "Brick Chimneys");
        addTag(YTechItemTags.BRONZE_ANVILS, "Bronze Anvils");
        addTag(YTechItemTags.CRAFTING_WORKSPACES, "Crafting Workspaces");
        addTag(YTechItemTags.FIRE_PITS, "Fire Pits");
        addTag(YTechItemTags.GRASS_BEDS, "Grass Beds");
        addTag(YTechItemTags.MILLSTONES, "Millstones");
        addTag(YTechItemTags.POTTERS_WHEELS, "Potters Wheels");
        addTag(YTechItemTags.PRIMITIVE_ALLOY_SMELTERS, "Primitive Alloy Smelters");
        addTag(YTechItemTags.PRIMITIVE_SMELTERS, "Primitive Smelters");
        addTag(YTechItemTags.REINFORCED_BRICKS, "Reinforced Bricks");
        addTag(YTechItemTags.REINFORCED_BRICK_CHIMNEYS, "Reinforced Brick Chimneys");
        addTag(YTechItemTags.TERRACOTTA_BRICKS, "Terracotta Bricks");
        addTag(YTechItemTags.TERRACOTTA_BRICK_SLABS, "Terracotta Brick Slabs");
        addTag(YTechItemTags.TERRACOTTA_BRICK_STAIRS, "Terracotta Brick Stairs");
        addTag(YTechItemTags.THATCH, "Thatch");
        addTag(YTechItemTags.THATCH_SLABS, "Thatch Slabs");
        addTag(YTechItemTags.THATCH_STAIRS, "Thatch Stairs");
        addTag(YTechItemTags.TREE_STUMPS, "Tree Stumps");

        addTag(YTechItemTags.CHLORITE_BRACELETS, "Chlorite Bracelets");
        addTag(YTechItemTags.LION_MANS, "Lion Mans");
        addTag(YTechItemTags.SHELL_BEADS, "Shell Beads");
        addTag(YTechItemTags.VENUS_OF_HOHLE_FELS, "Venus of Hohle Fels");
        addTag(YTechItemTags.WILD_HORSES, "Wild Horses");

        addTag(YTechItemTags.CLAY_MOLDS, NameHolder.suffix("Clay Molds"), YTechLanguageProvider::getName);
        addTag(YTechItemTags.PATTERNS, NameHolder.suffix("Patterns"), YTechLanguageProvider::getName);
        addTag(YTechItemTags.SAND_MOLDS, NameHolder.suffix("Sand Molds"), YTechLanguageProvider::getName);
        addTag(YTechItemTags.UNFIRED_MOLDS, NameHolder.suffix("Unfired Molds"), YTechLanguageProvider::getName);

        addTag(YTechItemTags.MOLDS, NameHolder.suffix("Molds"), YTechLanguageProvider::getName);

        addTag(YTechItemTags.ARROWS, NameHolder.suffix("Arrows"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.AXES, NameHolder.suffix("Axes"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.BOLTS, NameHolder.suffix("Bolts"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.BOOTS, NameHolder.suffix("Boots"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.CHESTPLATES, NameHolder.suffix("Chestplates"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.CRUSHED_MATERIALS, NameHolder.suffix("Crushed Materials"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.FILES, NameHolder.suffix("Files"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.HAMMERS, NameHolder.suffix("Hammers"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.HELMETS, NameHolder.suffix("Helmets"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.HOES, NameHolder.suffix("Hoes"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.INGOTS, NameHolder.suffix("Ingots"), YTechLanguageProvider::getMaterialName, MaterialType.VANILLA_METALS);
        addTag(YTechItemTags.KNIVES, NameHolder.suffix("Knives"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.MORTAR_AND_PESTLES, NameHolder.suffix("Mortar and Pestles"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.LEGGINGS, NameHolder.suffix("Leggings"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.PICKAXES, NameHolder.suffix("Pickaxes"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.PLATES, NameHolder.suffix("Plates"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.RAW_MATERIALS, NameHolder.suffix("Raw Materials"), YTechLanguageProvider::getMaterialName, MaterialType.VANILLA_METALS);
        addTag(YTechItemTags.RODS, NameHolder.suffix("Rods"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.SAWS, NameHolder.suffix("Saws"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.SHEARS, NameHolder.suffix("Shears"), YTechLanguageProvider::getMaterialName, EnumSet.of(MaterialType.IRON));
        addTag(YTechItemTags.SAW_BLADES, NameHolder.suffix("Saw Blades"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.SHOVELS, NameHolder.suffix("Shovels"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.SPEARS, NameHolder.suffix("Spears"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.SWORDS, NameHolder.suffix("Swords"), YTechLanguageProvider::getMaterialName);

        addTag(YTechItemTags.DRYING_RACKS, NameHolder.suffix("Drying Racks"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.GRAVEL_DEPOSITS, NameHolder.suffix("Gravel Deposits"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.SAND_DEPOSITS, NameHolder.suffix("Sand Deposits"), YTechLanguageProvider::getMaterialName);
        addTag(YTechItemTags.TANNING_RACKS, NameHolder.suffix("Tanning Racks"), YTechLanguageProvider::getMaterialName);

        addTag(YTechItemTags.PARTS, NameHolder.suffix("Parts"), YTechLanguageProvider::getName, YTechLanguageProvider::getName);

        for (AdvancementType type : AdvancementType.values()) {
            add(type.titleId(), type.title());
            add(type.descriptionId(), type.description());
        }

        add("effect.ytech.abyss_walker", "Abyss Walker");
        add("effect.ytech.lions_heart", "Lion's Heart");
        add("effect.ytech.lucky_stone", "Lucky Stone");
        add("effect.ytech.venus_touch", "Venus' Touch");
        add("effect.ytech.wild_ride", "Wild Ride");

        add("emi.category.ytech.drying", "Drying");
        add("emi.category.ytech.tanning", "Tanning");
        add("emi.category.ytech.milling", "Milling");
        add("emi.category.ytech.smelting", "Smelting");
        add("emi.category.ytech.block_hit", "Block Hit");
        add("emi.category.ytech.alloying", "Alloying");
        add("emi.category.ytech.hammering", "Hammering");
        add("emi.category.ytech.pottery", "Pottery");
        add("emi.category.ytech.workspace_crafting", "Workspace Crafting");
        add("emi.category.ytech.chopping", "Chopping");

        add("emi.alloying.time", "%s s");
        add("emi.drying.time", "%s s");
        add("emi.smelting.time", "%s s");
        add("emi.alloying.temperature", "%s °C");
        add("emi.smelting.temperature", "%s °C");
        add("emi.chopping.hit_count", "%s time(s)");
        add("emi.tanning.hit_count", "%s time(s)");

        add("sound.ytech.saber_tooth_tiger.ambient", "Saber-Toothed Tiger Roars");
        add("sound.ytech.saber_tooth_tiger.hurt", "Saber-Toothed Tiger Hurts");
        add("sound.ytech.saber_tooth_tiger.death", "Saber-Toothed Tiger Dies");
        add("sound.ytech.terror_bird.ambient", "Terror Bird Clucks");
        add("sound.ytech.terror_bird.hurt", "Terror Bird Hurts");
        add("sound.ytech.terror_bird.death", "Terror Bird Dies");
        add("sound.ytech.woolly_mammoth.ambient", "Woolly Mammoth Trumpets");
        add("sound.ytech.woolly_mammoth.hurt", "Woolly Mammoth Hurts");
        add("sound.ytech.woolly_mammoth.death", "Woolly Mammoth Dies");
        add("sound.ytech.woolly_rhino.ambient", "Woolly Rhino Grunts");
        add("sound.ytech.woolly_rhino.hurt", "Woolly Rhino Hurts");
        add("sound.ytech.woolly_rhino.death", "Woolly Rhino Dies");

        add("text.ytech.info.grass_fibers", "Grass fibers are obtained by breaking grass with Sharp Flint.");
        add("text.ytech.info.missing_curios", "Missing CuriosAPI, item is useless now");

        add("text.ytech.hover.amphora1", "Right-click to insert stackable items");
        add("text.ytech.hover.amphora2", "Right-click with empty hand to extract single item");
        add("text.ytech.hover.amphora3", "Right-click with empty hand while sneaking to extract stack");
        add("text.ytech.hover.grass_fibers", "Obtainable by breaking grass with Sharp Flint (10% chance)");
        add("text.ytech.hover.primitive_smelter", "Increase temperature by adding chimneys");
        add("text.ytech.hover.chimney", "Increases temperature in smelter by %s °C");
        add("text.ytech.hover.aqueduct1", "Can hold %smB of water");
        add("text.ytech.hover.aqueduct2", "Raining generates %smB every %s tick(s), if visible to sky");
        add("text.ytech.hover.aqueduct_valve1", "Provides water from water source to aqueduct network");
        add("text.ytech.hover.aqueduct_valve2", "Generates %smB every %s tick(s)");
        add("text.ytech.hover.aqueduct_hydrator1", "Hydrates soil similar to water source");
        add("text.ytech.hover.aqueduct_hydrator2", "Consumes %smB every %s tick(s)");
        add("text.ytech.hover.aqueduct_fertilizer1", "Hydrates soil similar to water source and applies random bonemeal effect when fertilizer is provided");
        add("text.ytech.hover.aqueduct_fertilizer2", "Consumes %smB every %s tick(s)");
        add("text.ytech.hover.crafting_workbench1", "Right-click to insert item");
        add("text.ytech.hover.crafting_workbench2", "Right-click with Hammer to assemble block");
        add("text.ytech.hover.crafting_workbench3", "Left-click with empty hand to remove item");
        add("text.ytech.hover.drying_rack1", "Works faster in dry biomes and slower in wet biomes");
        add("text.ytech.hover.drying_rack2", "Doesn't work during rain");
        add("text.ytech.hover.millstone", "Use leashed animal on block as power source. Shift-right click with empty hand for disconnecting animal.");
        add("text.ytech.hover.fire_pit1", "Lit by right-click when holding sticks in both hands (40% chance)");
        add("text.ytech.hover.fire_pit2", "Right-click with burnable item to increase level by floor(log10(burnTime))");
        add("text.ytech.hover.fire_pit3", "Each random tick is level decreased by 1");
        add("text.ytech.hover.fire_pit4", "Rain have chance to extinguish fire (50% chance on random tick)");
        add("text.ytech.hover.pebble", "Drops from gravel (10% chance)");
        add("text.ytech.hover.grass_bed", "Doesn't set respawn point");
        add("text.ytech.hover.potters_wheel1", "Right-click with Clay Ball or Clay Block to add clay. Right-click with empty hand to get result");
        add("text.ytech.hover.potters_wheel2", "Right-click while sneaking to get all clay back");
        add("text.ytech.hover.venus_of_hohle_fels", "Representing a fertility goddess. Small chance to apply bonemeal effect on crops around");
        add("text.ytech.hover.lion_man", "Oldest known statue ever discovered. Increases attack damage when worn");
        add("text.ytech.hover.wild_horse", "The earliest sculpture of a horse. Increases speed when worn");
        add("text.ytech.hover.shell_beads", "World’s oldest jewelry. Will extend the time under water when worn");
        add("text.ytech.hover.chlorite_bracelet", "Oldest piece of jewellery of its kind in the world, crafted by an extinct branch of early humans known as Denisovans. Increases luck when worn");
        add("text.ytech.hover.unlit_torch1", "Right-click on fire source to lit a torch");
        add("text.ytech.hover.unlit_torch2", "Right-click while sneaking on machine that can be lit (e.g. Furnace) to lit a torch");

        add("text.ytech.top.drying_rack.progress", "Progress: %s%%");
        add("text.ytech.top.entity.generation", "Generation: %s");
        add("text.ytech.top.fire_pit.progress", "Progress: %s%%");
        add("text.ytech.top.irrigation.fertilizing", "Fertilizing");
        add("text.ytech.top.irrigation.hydrating", "Hydrating");
        add("text.ytech.top.irrigation.production", "Producing: %s mb/s");
        add("text.ytech.top.irrigation.network", "Network: %s/%s");
        add("text.ytech.top.smelter.progress", "Progress: %s%%");
        add("text.ytech.top.smelter.temperature", "Temperature: %s°C");
        add("text.ytech.top.tanning_rack.progress", "Progress: %s%%");
        add("text.ytech.top.tree_stump.progress", "Progress: %s%%");

        add("creativeTab.ytech.title", "YTech");
    }

    private <E extends Enum<E>> void addTypedItem(YTechItems.TypedItem<E> item, NameHolder nameHolder, BiFunction<NameHolder, E, String> nameGetter, Predicate<Map.Entry<E, RegistryObject<Item>>> filter) {
        item.entrySet().stream().filter(filter).forEach((entry) -> addItem(entry.getValue(), nameGetter.apply(nameHolder, entry.getKey())));
    }

    private <E extends Enum<E>> void addTypedItem(YTechItems.TypedItem<E> typedItem, NameHolder nameHolder, BiFunction<NameHolder, E, String> nameGetter) {
        typedItem.forEach((key, item) -> addItem(item, nameGetter.apply(nameHolder, key)));
    }

    private <E extends Enum<E>, F extends Enum<F>> void addMultiTypedItem(YTechItems.MultiTypedItem<E, F> multiTypedItem, NameHolder nameHolder, TriFunction<NameHolder, E, F, String> nameGetter) {
        multiTypedItem.forEach((key1, map) -> map.forEach((key2, item) -> addItem(item, nameGetter.apply(nameHolder, key1, key2))));
    }

    private void addMaterialBlock(YTechBlocks.MaterialBlock block, NameHolder nameHolder, Predicate<Map.Entry<MaterialType, RegistryObject<Block>>> filter) {
        block.entries().stream().filter(filter).forEach((entry) -> addBlock(entry.getValue(), getMaterialName(nameHolder, entry.getKey())));
    }

    private void addMaterialBlock(YTechBlocks.MaterialBlock block, NameHolder nameHolder) {
        block.entries().forEach((entry) -> addBlock(entry.getValue(), getMaterialName(nameHolder, entry.getKey())));
    }

    private void addStorageBlockLanguage(YTechBlocks.MaterialBlock block, String prefix) {
        block.entries().stream().filter(YTechLanguageProvider::vanillaMaterialsFilter).forEach((entry) -> addBlock(entry.getValue(), getMaterialName(NameHolder.prefix(prefix), entry.getKey())));
    }

    private void addTag(TagKey<Item> tag, String name) {
        ResourceLocation location = tag.location();
        add("tag.item." + location.getNamespace().replace("/", ".") + "." + location.getPath().replace("/", "."), name);
    }

    private <E extends Enum<E> & IType> void addTag(YTechItemTags.TypedTag<E> tag, NameHolder nameHolder, BiFunction<NameHolder, E, String> nameGetter) {
        addTag(tag.tag, nameHolder.prefix() != null ? nameHolder.prefix() : nameHolder.suffix());
        tag.forEach((type, t) -> addTag(t, nameGetter.apply(nameHolder, type)));
    }

    private <E extends Enum<E> & IType> void addTag(YTechItemTags.TypedTag<E> tag, NameHolder nameHolder, BiFunction<NameHolder, E, String> nameGetter, EnumSet<E> exclude) {
        addTag(tag.tag, nameHolder.prefix() != null ? nameHolder.prefix() : nameHolder.suffix());
        tag.entrySet().stream().filter((e) -> !exclude.contains(e.getKey())).forEach((e) -> addTag(e.getValue(), nameGetter.apply(nameHolder, e.getKey())));
    }

    private <E extends Enum<E> & IType, F extends Enum<F> & IType> void addTag(YTechItemTags.MultiTypedTag<E, F> tag, NameHolder nameHolder, BiFunction<NameHolder, F, String> subNameGetter, TriFunction<NameHolder, E, F, String> nameGetter) {
        String name = nameHolder.prefix() != null ? nameHolder.prefix() : nameHolder.suffix();

        addTag(tag.tag, name);
        tag.forEach((type1, map) -> map.forEach((type2, t) -> addTag(tag.get(type1, type2), nameGetter.apply(nameHolder, type1, type2))));
        tag.entrySubSet().forEach((e) -> addTag(e.getValue(), subNameGetter.apply(nameHolder, e.getKey())));
    }

    private static <T> boolean vanillaMaterialsFilter(Map.Entry<MaterialType, RegistryObject<T>> entry) {
        return !MaterialType.VANILLA_METALS.contains(entry.getKey());
    }

    private static boolean goldIronWoodLeatherFilter(Map.Entry<MaterialType, RegistryObject<Item>> entry) {
        return !EnumSet.of(MaterialType.GOLD, MaterialType.IRON, MaterialType.WOODEN, MaterialType.LEATHER).contains(entry.getKey());
    }

    private static boolean ironFilter(Map.Entry<MaterialType, RegistryObject<Item>> entry) {
        return !EnumSet.of(MaterialType.IRON).contains(entry.getKey());
    }

    private static String getMaterialName(NameHolder nameHolder, MaterialType material) {
        String key = nameHolder.prefix() != null ? nameHolder.prefix() + " " : "";

        if (material.name.equals("Gold") && nameHolder.prefix() == null) {
            key += "Golden";
        } else {
            key += material.name;
        }

        key += nameHolder.suffix() != null ? " " + nameHolder.suffix() : "";
        return key;
    }

    private static String getName(NameHolder nameHolder, PartType partType) {
        String key = nameHolder.prefix() != null ? nameHolder.prefix() + " " : "";
        key += partType.name;
        key += nameHolder.suffix() != null ? " " + nameHolder.suffix() : "";
        return key;
    }

    private static String getName(NameHolder nameHolder, MaterialType material, PartType partType) {
        String key = nameHolder.prefix() != null ? nameHolder.prefix() + " " : "";

        if (material.name.equals("Gold") && nameHolder.prefix() == null) {
            key += "Golden";
        } else {
            key += material.name;
        }

        key += " ";
        key += partType.name;
        key += nameHolder.suffix() != null ? " " + nameHolder.suffix() : "";
        return key;
    }
}
