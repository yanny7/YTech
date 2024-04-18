package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.AdvancementType;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumSet;
import java.util.Map;
import java.util.function.Predicate;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechLanguageProvider extends LanguageProvider {
    public YTechLanguageProvider(PackOutput output, String locale) {
        super(output, YTechMod.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        addItem(YTechItems.ANTLER, "Antler");
        addItem(YTechItems.BASKET, "Basket");
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
        addItem(YTechItems.PEBBLE, "Pebble");
        addItem(YTechItems.RAW_HIDE, "Raw Hide");
        addItem(YTechItems.SHARP_FLINT, "Sharp Flint");
        addItem(YTechItems.UNFIRED_BRICK, "Unfired Brick");
        addItem(YTechItems.UNFIRED_CLAY_BUCKET, "Unfired Clay Bucket");
        addItem(YTechItems.VENISON, "Venison");
        addItem(YTechItems.WATER_CLAY_BUCKET, "Water Clay Bucket");

        addBlock(YTechBlocks.BRONZE_ANVIL, "Bronze Anvil");

        addMaterialItem(YTechItems.ARROWS, "Arrow");
        addMaterialItem(YTechItems.AXES, "Axe", YTechLanguageProvider::goldIronFilter);
        addMaterialItem(YTechItems.BOLTS, "Bolt");
        addMaterialItem(YTechItems.BOOTS, "Boots", YTechLanguageProvider::goldIronFilter);
        addMaterialItem(YTechItems.CHESTPLATES, "Chestplate", YTechLanguageProvider::goldIronFilter);
        addMaterialItem(YTechItems.CRUSHED_MATERIALS, "Crushed");
        addMaterialItem(YTechItems.FILES, "File");
        addMaterialItem(YTechItems.HAMMERS, "Hammer");
        addMaterialItem(YTechItems.HELMETS, "Helmet", YTechLanguageProvider::goldIronFilter);
        addMaterialItem(YTechItems.HOES, "Hoe", YTechLanguageProvider::goldIronFilter);
        addMaterialItem(YTechItems.INGOTS, "Ingot", YTechLanguageProvider::vanillaMaterialsFilter);
        addMaterialItem(YTechItems.KNIVES, "Knife");
        addMaterialItem(YTechItems.LEGGINGS, "Leggings", YTechLanguageProvider::goldIronFilter);
        addMaterialItem(YTechItems.MORTAR_AND_PESTLES, "Mortar and Pestle");
        addMaterialItem(YTechItems.PICKAXES, "Pickaxe", YTechLanguageProvider::goldIronFilter);
        addMaterialItem(YTechItems.PLATES, "Plate");
        addMaterialItem(YTechItems.RAW_MATERIALS, "Raw", YTechLanguageProvider::vanillaMaterialsFilter);
        addMaterialItem(YTechItems.RODS, "Rod");
        addMaterialItem(YTechItems.SAWS, "Saw");
        addMaterialItem(YTechItems.SHOVELS, "Shovel", YTechLanguageProvider::goldIronFilter);
        addMaterialItem(YTechItems.SPEARS, "Spear");
        addMaterialItem(YTechItems.SWORDS, "Sword", YTechLanguageProvider::goldIronFilter);

        GeneralUtils.mapToStream(HOLDER.blocks()).forEach(h -> add(h.block.get(), h.name));
        GeneralUtils.mapToStream(HOLDER.fluids()).forEach(h -> add(h.bucket.get(), h.name));
        HOLDER.simpleBlocks().values().forEach(h -> add(h.block.get(), h.name));
        HOLDER.entities().values().forEach(h -> {
            add(h.getEntityType(), h.name);
            add(h.spawnEgg.get(), h.name + " Spawn Egg");
        });

        for (AdvancementType type : AdvancementType.values()) {
            add(type.titleId(), type.title());
            add(type.descriptionId(), type.description());
        }

        add("gui.ytech.category.drying", "Drying");
        add("gui.ytech.category.tanning", "Tanning");
        add("gui.ytech.category.milling", "Milling");
        add("gui.ytech.category.smelting", "Smelting");
        add("gui.ytech.category.block_hit", "Block Hit");
        add("gui.ytech.category.alloying", "Alloying");
        add("gui.ytech.category.hammering", "Hammering");

        add("text.ytech.info.grass_fibers", "Grass fibers are obtained by breaking grass with Sharp Flint");

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

        add("creativeTab.ytech.title", "YTech");
    }

    private void addMaterialItem(YTechItems.MaterialItem item, String name, Predicate<Map.Entry<MaterialType, RegistryObject<Item>>> filter) {
        item.entries().stream().filter(filter).forEach((entry) -> addItem(entry.getValue(), getName(name, item, entry.getKey())));
    }

    private void addMaterialItem(YTechItems.MaterialItem item, String name) {
        item.entries().forEach((entry) -> addItem(entry.getValue(), getName(name, item, entry.getKey())));
    }

    private static boolean vanillaMaterialsFilter(Map.Entry<MaterialType, RegistryObject<Item>> entry) {
        return !MaterialType.VANILLA_METALS.contains(entry.getKey());
    }

    private static boolean goldIronFilter(Map.Entry<MaterialType, RegistryObject<Item>> entry) {
        return !EnumSet.of(MaterialType.GOLD, MaterialType.IRON).contains(entry.getKey());
    }

    private static String getName(String name, YTechItems.MaterialItem item, MaterialType material) {
        String materialName = material.name;

        if (material == MaterialType.GOLD && item.getGroupLocation() == YTechItems.GroupLocation.SUFFIX) {
            materialName += "en";
        }

        if (item.getGroupLocation() == YTechItems.GroupLocation.SUFFIX) {
            return materialName + " " + name;
        } else {
            return name + " " + materialName;
        }
    }
}
