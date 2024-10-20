package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.IType;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.PartType;
import com.yanny.ytech.configuration.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class YTechItemTags {
    public static final TagKey<Item> ANTLERS = create("antlers");
    public static final TagKey<Item> BASKETS = create("baskets");
    public static final TagKey<Item> BEESWAXES = create("beeswaxes");
    public static final TagKey<Item> BONE_NEEDLES = create("bone_needles");
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
    public static final TagKey<Item> UNLIT_TORCH = create("unlit_torch");
    public static final TagKey<Item> VENISON = create("venison");
    public static final TagKey<Item> WATER_BUCKETS = create("water_buckets");

    public static final TagKey<Item> BONE = create("bone");
    public static final TagKey<Item> DEER_FOOD = create("deer_food");
    public static final TagKey<Item> FERTILIZER = create("fertilizer");
    public static final TagKey<Item> FIRE_SOURCE = create("fire_source");
    public static final TagKey<Item> SOUL_FIRE_SOURCE = create("soul_fire_source");

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

    public static final TagKey<Item> CURIOS_BRACELETS = create("curios", "bracelet");
    public static final TagKey<Item> CURIOS_CHARMS = create("curios", "charm");
    public static final TagKey<Item> CURIOS_NECKLACES = create("curios", "necklace");

    public static final TagKey<Item> CHLORITE_BRACELETS = create("chlorite_bracelets");
    public static final TagKey<Item> LION_MANS = create("lion_mans");
    public static final TagKey<Item> SHELL_BEADS = create("shell_beads");
    public static final TagKey<Item> VENUS_OF_HOHLE_FELS = create("venus_of_hohle_fels");
    public static final TagKey<Item> WILD_HORSES = create("wild_horse");

    public static final TypedTag<PartType> MOLDS = new PartTag("molds", YTechItems.MOLDS);
    public static final TypedTag<PartType> PATTERNS = new PartTag("patterns", YTechItems.PATTERNS);
    public static final TypedTag<PartType> SAND_MOLDS = new PartTag("sand_molds", YTechItems.SAND_MOLDS);
    public static final TypedTag<PartType> UNFIRED_MOLDS = new PartTag("unfired_molds", YTechItems.UNFIRED_MOLDS);

    public static final TypedTag<MaterialType> ARROWS = new MaterialTag("arrows", ItemTags.ARROWS, YTechItems.ARROWS);
    public static final TypedTag<MaterialType> AXES = new MaterialTag("axes", ItemTags.AXES, YTechItems.AXES);
    public static final TypedTag<MaterialType> BOLTS = new MaterialTag("bolts", YTechItems.BOLTS);
    public static final TypedTag<MaterialType> BOOTS = new MaterialTag("boots", ItemTags.FOOT_ARMOR, YTechItems.BOOTS);
    public static final TypedTag<MaterialType> CHESTPLATES = new MaterialTag("chestplates", ItemTags.CHEST_ARMOR, YTechItems.CHESTPLATES);
    public static final TypedTag<MaterialType> CRUSHED_MATERIALS = new MaterialTag("crushed_materials", "forge", YTechItems.CRUSHED_MATERIALS);
    public static final TypedTag<MaterialType> FILES = new MaterialTag("files", YTechItems.FILES);
    public static final TypedTag<MaterialType> HAMMERS = new MaterialTag("hammers", YTechItems.HAMMERS);
    public static final TypedTag<MaterialType> HELMETS = new MaterialTag("helmets", ItemTags.HEAD_ARMOR, YTechItems.HELMETS);
    public static final TypedTag<MaterialType> HOES = new MaterialTag("hoes", ItemTags.HOES, YTechItems.HOES);
    public static final TypedTag<MaterialType> INGOTS = new IngotMaterialTag();
    public static final TypedTag<MaterialType> KNIVES = new MaterialTag("knives", YTechItems.KNIVES);
    public static final TypedTag<MaterialType> MORTAR_AND_PESTLES = new MaterialTag("mortar_and_pestles", YTechItems.MORTAR_AND_PESTLES);
    public static final TypedTag<MaterialType> LEGGINGS = new MaterialTag("leggings", ItemTags.LEG_ARMOR, YTechItems.LEGGINGS);
    public static final TypedTag<MaterialType> PICKAXES = new MaterialTag("pickaxes", ItemTags.PICKAXES, YTechItems.PICKAXES);
    public static final TypedTag<MaterialType> PLATES = new MaterialTag("plates", YTechItems.PLATES);
    public static final TypedTag<MaterialType> RAW_MATERIALS = new RawMaterialTag();
    public static final TypedTag<MaterialType> RODS = new MaterialTag("rods", YTechItems.RODS);
    public static final TypedTag<MaterialType> SAWS = new MaterialTag("saws", YTechItems.SAWS);
    public static final TypedTag<MaterialType> SAW_BLADES = new MaterialTag("saw_blades", YTechItems.SAW_BLADES);
    public static final TypedTag<MaterialType> SHOVELS = new MaterialTag("shovels", ItemTags.SHOVELS, YTechItems.SHOVELS);
    public static final TypedTag<MaterialType> SPEARS = new MaterialTag("spears", YTechItems.SPEARS);
    public static final TypedTag<MaterialType> SWORDS = new MaterialTag("swords", ItemTags.SWORDS, YTechItems.SWORDS);

    public static final TypedTag<MaterialType> DEEPSLATE_ORES = new DeepslateOreMaterialTag();
    public static final TypedTag<MaterialType> DRYING_RACKS = new MaterialTag(YTechBlockTags.DRYING_RACKS);
    public static final TypedTag<MaterialType> GRAVEL_DEPOSITS = new MaterialTag(YTechBlockTags.GRAVEL_DEPOSITS);
    public static final TypedTag<MaterialType> NETHER_ORES = new NetherOreMaterialTag();
    public static final TypedTag<MaterialType> RAW_STORAGE_BLOCKS = new RawStorageBlockMaterialTag();
    public static final TypedTag<MaterialType> SAND_DEPOSITS = new MaterialTag(YTechBlockTags.SAND_DEPOSITS);
    public static final TypedTag<MaterialType> STONE_ORES = new StoneOreMaterialTag();
    public static final TypedTag<MaterialType> STORAGE_BLOCKS = new StorageBlockMaterialTag();
    public static final TypedTag<MaterialType> TANNING_RACKS = new MaterialTag(YTechBlockTags.TANNING_RACKS);

    public static final MultiTypedTag<MaterialType, PartType> PARTS = new MaterialPartTag("parts", YTechItems.PARTS);

    private static TagKey<Item> create(String name) {
        return ItemTags.create(Utils.modLoc(name));
    }

    private static TagKey<Item> create(TagKey<Block> block) {
        return ItemTags.create(block.location());
    }

    private static TagKey<Item> create(String namespace, String name) {
        return ItemTags.create(new ResourceLocation(namespace, name));
    }

    public static class TypedTag<E extends Enum<E> & IType> extends AbstractMap<E, TagKey<Item>> {
        public final TagKey<Item> tag;
        protected final Map<E, TagKey<Item>> tags;

        public TypedTag(String name, String namespace, TagKey<Item> tag, EnumSet<E> types, Function<E, String> typeNameSupplier) {
            this.tag = tag;
            tags = new HashMap<>();
            types.forEach((type) -> tags.put(type, ItemTags.create(new ResourceLocation(namespace, name + "/" + typeNameSupplier.apply(type)))));
        }

        @Override
        public @NotNull Set<Entry<E, TagKey<Item>>> entrySet() {
            return tags.entrySet();
        }
    }

    public static class MultiTypedTag<E extends Enum<E> & IType, F extends Enum<F> & IType> extends AbstractMap<E, Map<F, TagKey<Item>>> {
        public final TagKey<Item> tag;
        protected final Map<F, TagKey<Item>> categoryTags = new HashMap<>();
        protected final Map<E, Map<F, TagKey<Item>>> tags = new HashMap<>();

        public MultiTypedTag(TagKey<Item> tag) {
            this.tag = tag;
        }

        public TagKey<Item> get(E type1, F type2) {
            return Objects.requireNonNull(tags.get(type1).get(type2), type1.key() + "_" + type2.key());
        }

        public TagKey<Item> getSubType(F type) {
            return Objects.requireNonNull(categoryTags.get(type), type.key());
        }

        @Override
        public @NotNull Set<Entry<E, Map<F, TagKey<Item>>>> entrySet() {
            return tags.entrySet();
        }
    }

    public static class PartTag extends TypedTag<PartType> {
        public PartTag(String name, YTechItems.TypedItem<PartType> item) {
            super(name, YTechMod.MOD_ID, create(name), EnumSet.copyOf(item.keySet()), (type) -> type.key);
        }
    }

    public static class MaterialTag extends TypedTag<MaterialType> {
        public MaterialTag(String name, YTechItems.TypedItem<MaterialType> item) {
            this(name, create(name), item);
        }

        public MaterialTag(String name, String namespace, YTechItems.TypedItem<MaterialType> item) {
            this(name, namespace, create(name), item);
        }

        public MaterialTag(String name, TagKey<Item> tag, YTechItems.TypedItem<MaterialType> item) {
            this(name, YTechMod.MOD_ID, tag, item);
        }

        public MaterialTag(String name, String namespace, TagKey<Item> tag, YTechItems.TypedItem<MaterialType> item) {
            this(name, namespace, tag, EnumSet.copyOf(item.keySet()));
        }

        public MaterialTag(YTechBlockTags.MaterialTag materialTag) {
            super(materialTag.getName(), materialTag.getNamespace(), create(materialTag.tag), EnumSet.copyOf(materialTag.tags.keySet()), materialTag.materialNameSupplier);
        }

        public MaterialTag(YTechBlockTags.MaterialTag materialTag, EnumSet<MaterialType> materials) {
            super(materialTag.getName(), materialTag.getNamespace(), create(materialTag.tag), materials, materialTag.materialNameSupplier);
        }

        public MaterialTag(String name, String namespace, TagKey<Item> tag, EnumSet<MaterialType> materials) {
            super(name, namespace, tag, materials, (type) -> type.key);
        }
    }

    private static class MaterialPartTag extends MultiTypedTag<MaterialType, PartType> {
        public MaterialPartTag(String name, YTechItems.MultiTypedItem<MaterialType, PartType> multiTypedItem) {
            super(create(name));
            for (PartType part : PartType.ALL_PARTS) {
                for (MaterialType material : multiTypedItem.keySet()) {
                    tags.computeIfAbsent(material, (t) -> new HashMap<>()).put(part, ItemTags.create(Utils.modLoc(name + "/" + part.key + "s/" + material.key)));
                }

                this.categoryTags.put(part, ItemTags.create(Utils.modLoc(name + "/" + part.key + "s")));
            }
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
