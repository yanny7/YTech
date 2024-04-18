package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;

import java.util.*;

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
    public static final TagKey<Item> PEBBLES = create("pebbles");
    public static final TagKey<Item> RAW_HIDES = create("raw_hides");
    public static final TagKey<Item> SHARP_FLINTS = create("sharp_flints");
    public static final TagKey<Item> UNFIRED_BRICKS = create("unfired_bricks");
    public static final TagKey<Item> UNFIRED_CLAY_BUCKETS = create("unfired_clay_buckets");
    public static final TagKey<Item> VENISON = create("venison");
    public static final TagKey<Item> WATER_BUCKETS = create("water_buckets");

    public static final TagKey<Item> BRONZE_ANVIL = create(YTechBlockTags.BRONZE_ANVIL);

    public static final MaterialTag ARROWS = new MaterialTag("arrows", ItemTags.ARROWS, YTechItems.ARROWS);
    public static final MaterialTag AXES = new MaterialTag("axes", ItemTags.AXES, YTechItems.AXES);
    public static final MaterialTag BOLTS = new MaterialTag("bolts", YTechItems.BOLTS);
    public static final MaterialTag BOOTS = new MaterialTag("boots", Tags.Items.ARMORS_BOOTS, YTechItems.BOOTS);
    public static final MaterialTag CHESTPLATES = new MaterialTag("chestplates", Tags.Items.ARMORS_CHESTPLATES, YTechItems.CHESTPLATES);
    public static final MaterialTag CRUSHED_MATERIALS = new MaterialTag("crushed_materials", "forge", YTechItems.CRUSHED_MATERIALS);
    public static final MaterialTag FILES = new MaterialTag("files", YTechItems.FILES);
    public static final MaterialTag HAMMERS = new MaterialTag("hammers", YTechItems.HAMMERS);
    public static final MaterialTag HELMETS = new MaterialTag("helmets", Tags.Items.ARMORS_HELMETS, YTechItems.HELMETS);
    public static final MaterialTag HOES = new MaterialTag("hoes", ItemTags.HOES, YTechItems.HOES);
    public static final MaterialTag INGOTS = new IngotMaterialTag("ingots", "forge", Tags.Items.INGOTS, YTechItems.INGOTS);
    public static final MaterialTag KNIVES = new MaterialTag("knives", YTechItems.KNIVES);
    public static final MaterialTag MORTAR_AND_PESTLES = new MaterialTag("mortar_and_pestles", YTechItems.MORTAR_AND_PESTLES);
    public static final MaterialTag LEGGINGS = new MaterialTag("leggings", Tags.Items.ARMORS_LEGGINGS, YTechItems.LEGGINGS);
    public static final MaterialTag PICKAXES = new MaterialTag("pickaxes", ItemTags.PICKAXES, YTechItems.PICKAXES);
    public static final MaterialTag PLATES = new MaterialTag("plates", YTechItems.PLATES);
    public static final MaterialTag RAW_MATERIALS = new RawMaterialTag("raw_materials", "forge", Tags.Items.RAW_MATERIALS, YTechItems.RAW_MATERIALS);
    public static final MaterialTag RODS = new MaterialTag("rods", YTechItems.RODS);
    public static final MaterialTag SAWS = new MaterialTag("saws", YTechItems.SAWS);
    public static final MaterialTag SHOVELS = new MaterialTag("shovels", ItemTags.SHOVELS, YTechItems.SHOVELS);
    public static final MaterialTag SPEARS = new MaterialTag("spears", YTechItems.SPEARS);
    public static final MaterialTag SWORDS = new MaterialTag("swords", ItemTags.SWORDS, YTechItems.SWORDS);

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

        public MaterialTag(String name, String namespace, TagKey<Item> tag, EnumSet<MaterialType> item) {
            this.tag = tag;
            tags = new HashMap<>();
            item.forEach((type) -> tags.put(type, ItemTags.create(new ResourceLocation(namespace, name + "/" + type.key))));
        }

        public TagKey<Item> of(MaterialType material) {
            return Objects.requireNonNull(tags.get(material));
        }

        public Collection<TagKey<Item>> values() {
            return tags.values();
        }
    }

    private static class IngotMaterialTag extends MaterialTag {
        public IngotMaterialTag(String name, String namespace, TagKey<Item> tag, YTechItems.MaterialItem item) {
            super(name, namespace, tag, Utils.exclude(EnumSet.copyOf(item.materials()), MaterialType.VANILLA_METALS));
            tags.put(MaterialType.COPPER, Tags.Items.INGOTS_COPPER);
            tags.put(MaterialType.GOLD, Tags.Items.INGOTS_GOLD);
            tags.put(MaterialType.IRON, Tags.Items.INGOTS_IRON);
        }
    }

    private static class RawMaterialTag extends MaterialTag {
        public RawMaterialTag(String name, String namespace, TagKey<Item> tag, YTechItems.MaterialItem item) {
            super(name, namespace, tag, Utils.exclude(EnumSet.copyOf(item.materials()), MaterialType.VANILLA_METALS));
            tags.put(MaterialType.COPPER, Tags.Items.RAW_MATERIALS_COPPER);
            tags.put(MaterialType.GOLD, Tags.Items.RAW_MATERIALS_GOLD);
            tags.put(MaterialType.IRON, Tags.Items.RAW_MATERIALS_IRON);
        }
    }

    private static TagKey<Item> create(String name) {
        return ItemTags.create(Utils.modLoc(name));
    }

    private static TagKey<Item> create(TagKey<Block> block) {
        return ItemTags.create(block.location());
    }
}
