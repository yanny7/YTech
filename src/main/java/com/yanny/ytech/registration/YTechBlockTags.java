package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.Utils;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

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

    private static TagKey<Block> create(String name) {
        return BlockTags.create(Utils.modLoc(name));
    }
}
