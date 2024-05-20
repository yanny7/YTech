package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

import java.util.*;
import java.util.function.Function;

public class YTechItemTags {
    public static final TagKey<Item> ANTLERS = create("antlers");
    public static final TagKey<Item> BASKETS = create("baskets");
    public static final TagKey<Item> BREAD_DOUGHS = create("bread_doughs");
    public static final TagKey<Item> BRICK_MOLDS = create("brick_molds");
    public static final TagKey<Item> CLAY_BUCKETS = create("clay_buckets");
    public static final TagKey<Item> COOKED_VENISON = create("cooked_venison");
    public static final TagKey<Item> DRIED_BEEFS = create("dried_beefs");
    public static final TagKey<Item> DRIED_CHICKENS = create("dried_chickens");
    public static final TagKey<Item> DRIED_CODS = create("dried_cods");
    public static final TagKey<Item> DRIED_MUTTONS = create("dried_muttons");
    public static final TagKey<Item> DRIED_PORKCHOP = create("dried_pork_chops");
    public static final TagKey<Item> DRIED_RABBITS = create("dried_rabbits");
    public static final TagKey<Item> DRIED_SALMONS = create("dried_salmons");
    public static final TagKey<Item> DRIED_VENISON = create("dried_venison");
    public static final TagKey<Item> FLOURS = create("flours");
    public static final TagKey<Item> GRASS_FIBERS = create("grass_fibers");
    public static final TagKey<Item> GRASS_TWINES = create("grass_twines");
    public static final TagKey<Item> IRON_BLOOMS = create("iron_blooms");
    public static final TagKey<Item> LAVA_BUCKETS = create("lava_buckets");
    public static final TagKey<Item> LEATHER_STRIPS = create("leather_strips");
    public static final TagKey<Item> MAMMOTH_TUSKS = create("mammoth_tusks");
    public static final TagKey<Item> PEBBLES = create("pebbles");
    public static final TagKey<Item> RAW_HIDES = create("raw_hides");
    public static final TagKey<Item> RHINO_HORNS = create("rhino_horns");
    public static final TagKey<Item> SHARP_FLINTS = create("sharp_flints");
    public static final TagKey<Item> UNFIRED_BRICKS = create("unfired_bricks");
    public static final TagKey<Item> UNFIRED_CLAY_BUCKETS = create("unfired_clay_buckets");
    public static final TagKey<Item> UNFIRED_DECORATED_POTS = create("unfired_decorated_pots");
    public static final TagKey<Item> UNFIRED_FLOWER_POTS = create("unfired_flower_pots");
    public static final TagKey<Item> VENISON = create("venison");
    public static final TagKey<Item> WATER_BUCKETS = create("water_buckets");

    public static final TagKey<Item> FERTILIZER = create("fertilizer");
    public static final TagKey<Item> DEER_FOOD = create("deer_food");

    public static final TagKey<Item> AUROCHS_FOOD = create("aurochs_food");
    public static final TagKey<Item> AUROCHS_TEMP_ITEMS = create("aurochs_temp_items");
    public static final TagKey<Item> DEER_TEMP_ITEMS = create("deer_temp_items");
    public static final TagKey<Item> FOWL_FOOD = create("fowl_food");
    public static final TagKey<Item> FOWL_TEMP_ITEMS = create("fowl_temp_items");
    public static final TagKey<Item> MOUFLON_FOOD = create("mouflon_food");
    public static final TagKey<Item> MOUFLON_TEMP_ITEMS = create("mouflon_temp_items");
    public static final TagKey<Item> SABER_TOOTH_TIGER_TEMP_ITEMS = create("saber_tooth_tiger_temp_items");
    public static final TagKey<Item> TERROR_BIRD_TEMP_ITEMS = create("terror_bird_temp_items");
    public static final TagKey<Item> WILD_BOAR_FOOD = create("wild_boar_food");
    public static final TagKey<Item> WILD_BOAR_TEMP_ITEMS = create("wild_boar_temp_items");
    public static final TagKey<Item> WOOLLY_MAMMOTH_TEMP_ITEMS = create("woolly_mammoth_temp_items");
    public static final TagKey<Item> WOOLLY_RHINO_TEMP_ITEMS = create("woolly_rhino_temp_items");

    public static final TagKey<Item> AQUEDUCTS = create(YTechBlockTags.AQUEDUCTS);
    public static final TagKey<Item> AQUEDUCT_FERTILIZERS = create(YTechBlockTags.AQUEDUCT_FERTILIZERS);
    public static final TagKey<Item> AQUEDUCT_HYDRATORS = create(YTechBlockTags.AQUEDUCT_HYDRATORS);
    public static final TagKey<Item> AQUEDUCT_VALVES = create(YTechBlockTags.AQUEDUCT_VALVES);
    public static final TagKey<Item> BRICK_CHIMNEYS = create(YTechBlockTags.BRICK_CHIMNEYS);
    public static final TagKey<Item> BRONZE_ANVILS = create(YTechBlockTags.BRONZE_ANVILS);
    public static final TagKey<Item> FIRE_PITS = create(YTechBlockTags.FIRE_PITS);
    public static final TagKey<Item> GRASS_BEDS = create(YTechBlockTags.GRASS_BEDS);
    public static final TagKey<Item> MILLSTONES = create(YTechBlockTags.MILLSTONES);
    public static final TagKey<Item> POTTERS_WHEELS = create(YTechBlockTags.POTTERS_WHEELS);
    public static final TagKey<Item> PRIMITIVE_ALLOY_SMELTERS = create(YTechBlockTags.PRIMITIVE_ALLOY_SMELTERS);
    public static final TagKey<Item> PRIMITIVE_SMELTERS = create(YTechBlockTags.PRIMITIVE_SMELTERS);
    public static final TagKey<Item> REINFORCED_BRICKS = create(YTechBlockTags.REINFORCED_BRICKS);
    public static final TagKey<Item> REINFORCED_BRICK_CHIMNEYS = create(YTechBlockTags.REINFORCED_BRICK_CHIMNEYS);
    public static final TagKey<Item> TERRACOTTA_BRICKS = create(YTechBlockTags.TERRACOTTA_BRICKS);
    public static final TagKey<Item> TERRACOTTA_BRICK_SLABS = create(YTechBlockTags.TERRACOTTA_BRICK_SLABS);
    public static final TagKey<Item> TERRACOTTA_BRICK_STAIRS = create(YTechBlockTags.TERRACOTTA_BRICK_STAIRS);
    public static final TagKey<Item> THATCH = create(YTechBlockTags.THATCH);
    public static final TagKey<Item> THATCH_SLABS = create(YTechBlockTags.THATCH_SLABS);
    public static final TagKey<Item> THATCH_STAIRS = create(YTechBlockTags.THATCH_STAIRS);

    public static final MaterialTag ARROWS = new MaterialTag("arrows", ItemTags.ARROWS, YTechItems.ARROWS);
    public static final MaterialTag AXES = new MaterialTag("axes", ItemTags.AXES, YTechItems.AXES);
    public static final MaterialTag BOLTS = new MaterialTag("bolts", YTechItems.BOLTS);
    public static final MaterialTag BOOTS = new MaterialTag("boots", ItemTags.FOOT_ARMOR, YTechItems.BOOTS);
    public static final MaterialTag CHESTPLATES = new MaterialTag("chestplates", ItemTags.CHEST_ARMOR, YTechItems.CHESTPLATES);
    public static final MaterialTag CRUSHED_MATERIALS = new MaterialTag("crushed_materials", "c", YTechItems.CRUSHED_MATERIALS);
    public static final MaterialTag FILES = new MaterialTag("files", YTechItems.FILES);
    public static final MaterialTag HAMMERS = new MaterialTag("hammers", YTechItems.HAMMERS);
    public static final MaterialTag HELMETS = new MaterialTag("helmets", ItemTags.HEAD_ARMOR, YTechItems.HELMETS);
    public static final MaterialTag HOES = new MaterialTag("hoes", ItemTags.HOES, YTechItems.HOES);
    public static final MaterialTag INGOTS = new IngotMaterialTag();
    public static final MaterialTag KNIVES = new MaterialTag("knives", YTechItems.KNIVES);
    public static final MaterialTag MORTAR_AND_PESTLES = new MaterialTag("mortar_and_pestles", YTechItems.MORTAR_AND_PESTLES);
    public static final MaterialTag LEGGINGS = new MaterialTag("leggings", ItemTags.LEG_ARMOR, YTechItems.LEGGINGS);
    public static final MaterialTag PICKAXES = new MaterialTag("pickaxes", ItemTags.PICKAXES, YTechItems.PICKAXES);
    public static final MaterialTag PLATES = new MaterialTag("plates", YTechItems.PLATES);
    public static final MaterialTag RAW_MATERIALS = new RawMaterialTag();
    public static final MaterialTag RODS = new MaterialTag("rods", YTechItems.RODS);
    public static final MaterialTag SAWS = new MaterialTag("saws", YTechItems.SAWS);
    public static final MaterialTag SAW_BLADES = new MaterialTag("saw_blades", YTechItems.SAW_BLADES);
    public static final MaterialTag SHOVELS = new MaterialTag("shovels", ItemTags.SHOVELS, YTechItems.SHOVELS);
    public static final MaterialTag SPEARS = new MaterialTag("spears", YTechItems.SPEARS);
    public static final MaterialTag SWORDS = new MaterialTag("swords", ItemTags.SWORDS, YTechItems.SWORDS);

    public static final MaterialTag DEEPSLATE_ORES = new DeepslateOreMaterialTag();
    public static final MaterialTag DRYING_RACKS = new MaterialTag(YTechBlockTags.DRYING_RACKS);
    public static final MaterialTag GRAVEL_DEPOSITS = new MaterialTag(YTechBlockTags.GRAVEL_DEPOSITS);
    public static final MaterialTag NETHER_ORES = new NetherOreMaterialTag();
    public static final MaterialTag RAW_STORAGE_BLOCKS = new RawStorageBlockMaterialTag();
    public static final MaterialTag SAND_DEPOSITS = new MaterialTag(YTechBlockTags.SAND_DEPOSITS);
    public static final MaterialTag STONE_ORES = new StoneOreMaterialTag();
    public static final MaterialTag STORAGE_BLOCKS = new StorageBlockMaterialTag();
    public static final MaterialTag TANNING_RACKS = new MaterialTag(YTechBlockTags.TANNING_RACKS);

    private static TagKey<Item> create(String name) {
        return ItemTags.create(Utils.modLoc(name));
    }

    private static TagKey<Item> create(TagKey<Block> block) {
        return ItemTags.create(block.location());
    }

    public static class MaterialTag {
        public final TagKey<Item> tag;
        protected final Map<MaterialType, TagKey<Item>> tags;

        public MaterialTag(String name, YTechItems.MaterialItem item) {
            this(name, create(name), item);
        }

        public MaterialTag(String name, String namespace, YTechItems.MaterialItem item) {
            this(name, namespace, create(name), item);
        }

        public MaterialTag(String name, TagKey<Item> tag, YTechItems.MaterialItem item) {
            this(name, YTechMod.MOD_ID, tag, item);
        }

        public MaterialTag(String name, String namespace, TagKey<Item> tag, YTechItems.MaterialItem item) {
            this(name, namespace, tag, EnumSet.copyOf(item.materials()));
        }

        public MaterialTag(YTechBlockTags.MaterialTag materialTag) {
            this(materialTag.getName(), materialTag.getNamespace(), create(materialTag.tag), EnumSet.copyOf(materialTag.tags.keySet()), materialTag.materialNameSupplier);
        }

        public MaterialTag(YTechBlockTags.MaterialTag materialTag, EnumSet<MaterialType> materials) {
            this(materialTag.getName(), materialTag.getNamespace(), create(materialTag.tag), materials, materialTag.materialNameSupplier);
        }

        public MaterialTag(String name, String namespace, TagKey<Item> tag, EnumSet<MaterialType> materials) {
            this(name, namespace, tag, materials, (type) -> type.key);
        }

        public MaterialTag(String name, String namespace, TagKey<Item> tag, EnumSet<MaterialType> materials, Function<MaterialType, String> materialNameSupplier) {
            this.tag = tag;
            tags = new HashMap<>();
            materials.forEach((type) -> tags.put(type, ItemTags.create(new ResourceLocation(namespace, name + "/" + materialNameSupplier.apply(type)))));
        }

        public TagKey<Item> of(MaterialType material) {
            return Objects.requireNonNull(tags.get(material), material.key);
        }

        public Collection<TagKey<Item>> values() {
            return tags.values();
        }
    }

    private static class IngotMaterialTag extends MaterialTag {
        public IngotMaterialTag() {
            super("ingots", "c", Tags.Items.INGOTS, YTechItems.INGOTS);
            tags.put(MaterialType.COPPER, Tags.Items.INGOTS_COPPER);
            tags.put(MaterialType.GOLD, Tags.Items.INGOTS_GOLD);
            tags.put(MaterialType.IRON, Tags.Items.INGOTS_IRON);
        }
    }

    private static class RawMaterialTag extends MaterialTag {
        public RawMaterialTag() {
            super("raw_materials", "c", Tags.Items.RAW_MATERIALS, YTechItems.RAW_MATERIALS);
            tags.put(MaterialType.COPPER, Tags.Items.RAW_MATERIALS_COPPER);
            tags.put(MaterialType.GOLD, Tags.Items.RAW_MATERIALS_GOLD);
            tags.put(MaterialType.IRON, Tags.Items.RAW_MATERIALS_IRON);
        }
    }

    private static class DeepslateOreMaterialTag extends MaterialTag {
        public DeepslateOreMaterialTag() {
            super(YTechBlockTags.DEEPSLATE_ORES, Utils.exclude(EnumSet.copyOf(YTechBlocks.DEEPSLATE_ORES.materials()), MaterialType.VANILLA_METALS));
            tags.put(MaterialType.COPPER, Tags.Items.ORES_COPPER);
            tags.put(MaterialType.GOLD, Tags.Items.ORES_GOLD);
            tags.put(MaterialType.IRON, Tags.Items.ORES_IRON);
        }
    }

    private static class NetherOreMaterialTag extends MaterialTag {
        public NetherOreMaterialTag() {
            super(YTechBlockTags.NETHER_ORES, Utils.exclude(EnumSet.copyOf(YTechBlocks.NETHER_ORES.materials()), MaterialType.GOLD));
            tags.put(MaterialType.GOLD, Tags.Items.ORES_GOLD);
        }
    }

    private static class RawStorageBlockMaterialTag extends MaterialTag {
        public RawStorageBlockMaterialTag() {
            super(YTechBlockTags.RAW_STORAGE_BLOCKS, Utils.exclude(EnumSet.copyOf(YTechBlocks.RAW_STORAGE_BLOCKS.materials()), MaterialType.VANILLA_METALS));
            tags.put(MaterialType.COPPER, Tags.Items.STORAGE_BLOCKS_RAW_COPPER);
            tags.put(MaterialType.GOLD, Tags.Items.STORAGE_BLOCKS_RAW_GOLD);
            tags.put(MaterialType.IRON, Tags.Items.STORAGE_BLOCKS_RAW_IRON);
        }
    }

    private static class StoneOreMaterialTag extends MaterialTag {
        public StoneOreMaterialTag() {
            super(YTechBlockTags.STONE_ORES, Utils.exclude(EnumSet.copyOf(YTechBlocks.STONE_ORES.materials()), MaterialType.VANILLA_METALS));
            tags.put(MaterialType.COPPER, Tags.Items.ORES_COPPER);
            tags.put(MaterialType.GOLD, Tags.Items.ORES_GOLD);
            tags.put(MaterialType.IRON, Tags.Items.ORES_IRON);
        }
    }

    private static class StorageBlockMaterialTag extends MaterialTag {
        public StorageBlockMaterialTag() {
            super(YTechBlockTags.STORAGE_BLOCKS, Utils.exclude(EnumSet.copyOf(YTechBlocks.STORAGE_BLOCKS.materials()), MaterialType.VANILLA_METALS));
            tags.put(MaterialType.COPPER, Tags.Items.STORAGE_BLOCKS_COPPER);
            tags.put(MaterialType.GOLD, Tags.Items.STORAGE_BLOCKS_GOLD);
            tags.put(MaterialType.IRON, Tags.Items.STORAGE_BLOCKS_IRON);
        }
    }
}
