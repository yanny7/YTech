package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;

import java.util.*;
import java.util.function.Function;

public class YTechBlockTags {
    public static final TagKey<Block> AQUEDUCTS = create("aqueducts");
    public static final TagKey<Block> AQUEDUCT_FERTILIZERS = create("aqueduct_fertilizers");
    public static final TagKey<Block> AQUEDUCT_HYDRATORS = create("aqueduct_hydrators");
    public static final TagKey<Block> AQUEDUCT_VALVES = create("aqueduct_valves");
    public static final TagKey<Block> BRICK_CHIMNEYS = create("brick_chimneys");
    public static final TagKey<Block> BRONZE_ANVILS = create("bronze_anvils");
    public static final TagKey<Block> FIRE_PITS = create("fire_pits");
    public static final TagKey<Block> GRASS_BEDS = create("grass_beds");
    public static final TagKey<Block> MILLSTONES = create("millstones");
    public static final TagKey<Block> PRIMITIVE_ALLOY_SMELTERS = create("primitive_alloy_smelters");
    public static final TagKey<Block> PRIMITIVE_SMELTERS = create("primitive_smelters");
    public static final TagKey<Block> REINFORCED_BRICKS = create("reinforced_bricks");
    public static final TagKey<Block> REINFORCED_BRICK_CHIMNEYS = create("reinforced_brick_chimneys");
    public static final TagKey<Block> TERRACOTTA_BRICKS = create("terracotta_bricks");
    public static final TagKey<Block> TERRACOTTA_BRICK_SLABS = create("terracotta_brick_slabs");
    public static final TagKey<Block> TERRACOTTA_BRICK_STAIRS = create("terracotta_brick_stairs");
    public static final TagKey<Block> THATCH = create("thatch");
    public static final TagKey<Block> THATCH_SLABS = create("thatch_slabs");
    public static final TagKey<Block> THATCH_STAIRS = create("thatch_stairs");

    public static final MaterialTag DEEPSLATE_ORES = new DeepslateOreMaterialTag();
    public static final MaterialTag DRYING_RACKS = new MaterialTag("drying_racks", YTechBlocks.DRYING_RACKS);
    public static final MaterialTag GRAVEL_DEPOSITS = new MaterialTag("gravel_deposits", YTechBlocks.GRAVEL_DEPOSITS);
    public static final MaterialTag NETHER_ORES = new NetherOreMaterialTag();
    public static final MaterialTag RAW_STORAGE_BLOCKS = new RawStorageBlockMaterialTag();
    public static final MaterialTag SAND_DEPOSITS = new MaterialTag("sand_deposits", YTechBlocks.SAND_DEPOSITS);
    public static final MaterialTag STONE_ORES = new StoneOreMaterialTag();
    public static final MaterialTag STORAGE_BLOCKS = new StorageBlockMaterialTag();
    public static final MaterialTag TANNING_RACKS = new MaterialTag("tanning_racks", YTechBlocks.TANNING_RACKS);

    private static TagKey<Block> create(String name) {
        return BlockTags.create(Utils.modLoc(name));
    }

    public static class MaterialTag {
        protected final String name;
        protected final String namespace;
        public final TagKey<Block> tag;
        protected final Map<MaterialType, TagKey<Block>> tags;
        protected Function<MaterialType, String> materialNameSupplier;

        public MaterialTag(String name, YTechBlocks.MaterialBlock block) {
            this(name, create(name), block);
        }

        public MaterialTag(String name, String namespace, YTechBlocks.MaterialBlock block) {
            this(name, namespace, create(name), block);
        }

        public MaterialTag(String name, TagKey<Block> tag, YTechBlocks.MaterialBlock block) {
            this(name, YTechMod.MOD_ID, tag, block);
        }

        public MaterialTag(String name, String namespace, TagKey<Block> tag, YTechBlocks.MaterialBlock block) {
            this(name, namespace, tag, EnumSet.copyOf(block.materials()));
        }

        public MaterialTag(String name, String namespace, TagKey<Block> tag, EnumSet<MaterialType> materials) {
            this(name, namespace, tag, materials, (type) -> type.key);
        }

        public MaterialTag(String name, String namespace, TagKey<Block> tag, EnumSet<MaterialType> materials, Function<MaterialType, String> materialNameSupplier) {
            this.name = name;
            this.namespace = namespace;
            this.tag = tag;
            this.materialNameSupplier = materialNameSupplier;
            tags = new HashMap<>();
            materials.forEach((type) -> tags.put(type, BlockTags.create(new ResourceLocation(namespace, name + "/" + materialNameSupplier.apply(type)))));
        }

        public TagKey<Block> of(MaterialType material) {
            return Objects.requireNonNull(tags.get(material));
        }

        public Collection<TagKey<Block>> values() {
            return tags.values();
        }

        public String getName() {
            return name;
        }

        public String getNamespace() {
            return namespace;
        }
    }

    private static class DeepslateOreMaterialTag extends MaterialTag {
        public DeepslateOreMaterialTag() {
            super("ores", "forge", Tags.Blocks.ORES, Utils.exclude(EnumSet.copyOf(YTechBlocks.DEEPSLATE_ORES.materials()), MaterialType.VANILLA_METALS));
            tags.put(MaterialType.COPPER, Tags.Blocks.ORES_COPPER);
            tags.put(MaterialType.GOLD, Tags.Blocks.ORES_GOLD);
            tags.put(MaterialType.IRON, Tags.Blocks.ORES_IRON);
        }
    }

    private static class NetherOreMaterialTag extends MaterialTag {
        public NetherOreMaterialTag() {
            super("ores", "forge", Tags.Blocks.ORES, Utils.exclude(EnumSet.copyOf(YTechBlocks.NETHER_ORES.materials()), MaterialType.GOLD));
            tags.put(MaterialType.GOLD, Tags.Blocks.ORES_GOLD);
        }
    }

    private static class RawStorageBlockMaterialTag extends MaterialTag {
        public RawStorageBlockMaterialTag() {
            super("storage_blocks", "forge", Tags.Blocks.STORAGE_BLOCKS, Utils.exclude(EnumSet.copyOf(YTechBlocks.RAW_STORAGE_BLOCKS.materials()), MaterialType.VANILLA_METALS), (type) -> "raw_" + type.key);
            tags.put(MaterialType.COPPER, Tags.Blocks.STORAGE_BLOCKS_RAW_COPPER);
            tags.put(MaterialType.GOLD, Tags.Blocks.STORAGE_BLOCKS_RAW_GOLD);
            tags.put(MaterialType.IRON, Tags.Blocks.STORAGE_BLOCKS_RAW_IRON);
        }
    }

    private static class StoneOreMaterialTag extends MaterialTag {
        public StoneOreMaterialTag() {
            super("ores", "forge", Tags.Blocks.ORES, Utils.exclude(EnumSet.copyOf(YTechBlocks.STONE_ORES.materials()), MaterialType.VANILLA_METALS));
            tags.put(MaterialType.COPPER, Tags.Blocks.ORES_COPPER);
            tags.put(MaterialType.GOLD, Tags.Blocks.ORES_GOLD);
            tags.put(MaterialType.IRON, Tags.Blocks.ORES_IRON);
        }
    }

    private static class StorageBlockMaterialTag extends MaterialTag {
        public StorageBlockMaterialTag() {
            super("storage_blocks", "forge", Tags.Blocks.STORAGE_BLOCKS, Utils.exclude(EnumSet.copyOf(YTechBlocks.STORAGE_BLOCKS.materials()), MaterialType.VANILLA_METALS));
            tags.put(MaterialType.COPPER, Tags.Blocks.STORAGE_BLOCKS_COPPER);
            tags.put(MaterialType.GOLD, Tags.Blocks.STORAGE_BLOCKS_GOLD);
            tags.put(MaterialType.IRON, Tags.Blocks.STORAGE_BLOCKS_IRON);
        }
    }
}
