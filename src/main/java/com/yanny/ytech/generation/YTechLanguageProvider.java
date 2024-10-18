package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.AdvancementType;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.NameHolder;
import com.yanny.ytech.configuration.PartType;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechEntityTypes;
import com.yanny.ytech.registration.YTechItems;
import net.minecraft.data.PackOutput;
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

        addBlock(YTechBlocks.AQUEDUCT, "Aqueduct");
        addBlock(YTechBlocks.AQUEDUCT_FERTILIZER, "Aqueduct Fertilizer");
        addBlock(YTechBlocks.AQUEDUCT_HYDRATOR, "Aqueduct Hydrator");
        addBlock(YTechBlocks.AQUEDUCT_VALVE, "Aqueduct Valve");
        addBlock(YTechBlocks.BRICK_CHIMNEY, "Brick Chimney");
        addBlock(YTechBlocks.BRONZE_ANVIL, "Bronze Anvil");
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

        addTypedItem(YTechItems.MOLDS, NameHolder.suffix("Mold"), YTechLanguageProvider::getName);
        addTypedItem(YTechItems.PATTERNS, NameHolder.suffix("Pattern"), YTechLanguageProvider::getName);
        addTypedItem(YTechItems.SAND_MOLDS, NameHolder.suffix("Sand Mold"), YTechLanguageProvider::getName);
        addTypedItem(YTechItems.UNFIRED_MOLDS, NameHolder.both("Unfired", "Mold"), YTechLanguageProvider::getName);

        addMultiTypedItem(YTechItems.PARTS, NameHolder.suffix("Part"), YTechLanguageProvider::getName);

        addTypedItem(YTechItems.ARROWS, NameHolder.suffix("Arrow"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.AXES, NameHolder.suffix("Axe"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronFilter);
        addTypedItem(YTechItems.BOLTS, NameHolder.suffix("Bolt"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.BOOTS, NameHolder.suffix("Boots"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronFilter);
        addTypedItem(YTechItems.CHESTPLATES, NameHolder.suffix("Chestplate"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronFilter);
        addTypedItem(YTechItems.CRUSHED_MATERIALS, NameHolder.prefix("Crushed"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.FILES, NameHolder.suffix("File"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.HAMMERS, NameHolder.suffix("Hammer"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.HELMETS, NameHolder.suffix("Helmet"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronFilter);
        addTypedItem(YTechItems.HOES, NameHolder.suffix("Hoe"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronFilter);
        addTypedItem(YTechItems.INGOTS, NameHolder.suffix("Ingot"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::vanillaMaterialsFilter);
        addTypedItem(YTechItems.KNIVES, NameHolder.suffix("Knife"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.LEGGINGS, NameHolder.suffix("Leggings"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronFilter);
        addTypedItem(YTechItems.MORTAR_AND_PESTLES, NameHolder.suffix("Mortar and Pestle"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.PICKAXES, NameHolder.suffix("Pickaxe"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronFilter);
        addTypedItem(YTechItems.PLATES, NameHolder.suffix("Plate"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.RAW_MATERIALS, NameHolder.prefix("Raw"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::vanillaMaterialsFilter);
        addTypedItem(YTechItems.RODS, NameHolder.suffix("Rod"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.SAWS, NameHolder.suffix("Saw"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.SAW_BLADES, NameHolder.suffix("Saw Blade"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.SHOVELS, NameHolder.suffix("Shovel"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronFilter);
        addTypedItem(YTechItems.SPEARS, NameHolder.suffix("Spear"), YTechLanguageProvider::getMaterialName);
        addTypedItem(YTechItems.SWORDS, NameHolder.suffix("Sword"), YTechLanguageProvider::getMaterialName, YTechLanguageProvider::goldIronFilter);

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

        for (AdvancementType type : AdvancementType.values()) {
            add(type.titleId(), type.title());
            add(type.descriptionId(), type.description());
        }

        add("effect.ytech.abyss_walker", "Abyss Walker");
        add("effect.ytech.lions_heart", "Lion's Heart");
        add("effect.ytech.lucky_stone", "Lucky Stone");
        add("effect.ytech.venus_touch", "Venus' Touch");
        add("effect.ytech.wild_ride", "Wild Ride");

        add("gui.ytech.category.drying", "Drying");
        add("gui.ytech.category.tanning", "Tanning");
        add("gui.ytech.category.milling", "Milling");
        add("gui.ytech.category.smelting", "Smelting");
        add("gui.ytech.category.block_hit", "Block Hit");
        add("gui.ytech.category.alloying", "Alloying");
        add("gui.ytech.category.hammering", "Hammering");
        add("gui.ytech.category.pottery", "Pottery");

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

        add("text.ytech.top.drying_rack.progress", "Remaining: %ss");
        add("text.ytech.top.entity.generation", "Generation: %s");
        add("text.ytech.top.fire_pit.progress", "Progress: %s%%");
        add("text.ytech.top.irrigation.fertilizing", "Fertilizing");
        add("text.ytech.top.irrigation.hydrating", "Hydrating");
        add("text.ytech.top.irrigation.production", "Producing: %s mb/s");
        add("text.ytech.top.irrigation.network", "Network: %s/%s");
        add("text.ytech.top.smelter.progress", "Progress: %s%%");
        add("text.ytech.top.smelter.temperature", "Temperature: %s°C");
        add("text.ytech.top.tanning_rack.progress", "Hit left: %s times");

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

    private static <T> boolean vanillaMaterialsFilter(Map.Entry<MaterialType, RegistryObject<T>> entry) {
        return !MaterialType.VANILLA_METALS.contains(entry.getKey());
    }

    private static boolean goldIronFilter(Map.Entry<MaterialType, RegistryObject<Item>> entry) {
        return !EnumSet.of(MaterialType.GOLD, MaterialType.IRON).contains(entry.getKey());
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
