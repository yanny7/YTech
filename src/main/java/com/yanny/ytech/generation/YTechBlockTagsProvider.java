package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.registration.YTechBlockTags;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechBlockTagsProvider extends BlockTagsProvider {
    public YTechBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        tag(YTechBlockTags.AQUEDUCTS).add(YTechBlocks.AQUEDUCT.get());
        tag(YTechBlockTags.AQUEDUCT_FERTILIZERS).add(YTechBlocks.AQUEDUCT_FERTILIZER.get());
        tag(YTechBlockTags.AQUEDUCT_HYDRATORS).add(YTechBlocks.AQUEDUCT_HYDRATOR.get());
        tag(YTechBlockTags.AQUEDUCT_VALVES).add(YTechBlocks.AQUEDUCT_VALVE.get());
        tag(YTechBlockTags.BRICK_CHIMNEYS).add(YTechBlocks.BRICK_CHIMNEY.get());
        tag(YTechBlockTags.BRONZE_ANVILS).add(YTechBlocks.BRONZE_ANVIL.get());
        tag(YTechBlockTags.FIRE_PITS).add(YTechBlocks.FIRE_PIT.get());
        tag(YTechBlockTags.GRASS_BEDS).add(YTechBlocks.GRASS_BED.get());
        tag(YTechBlockTags.MILLSTONES).add(YTechBlocks.MILLSTONE.get());
        tag(YTechBlockTags.PRIMITIVE_ALLOY_SMELTERS).add(YTechBlocks.PRIMITIVE_ALLOY_SMELTER.get());
        tag(YTechBlockTags.PRIMITIVE_SMELTERS).add(YTechBlocks.PRIMITIVE_SMELTER.get());
        tag(YTechBlockTags.REINFORCED_BRICKS).add(YTechBlocks.REINFORCED_BRICKS.get());
        tag(YTechBlockTags.REINFORCED_BRICK_CHIMNEYS).add(YTechBlocks.REINFORCED_BRICK_CHIMNEY.get());
        tag(YTechBlockTags.TERRACOTTA_BRICKS).add(YTechBlocks.TERRACOTTA_BRICKS.get());
        tag(YTechBlockTags.TERRACOTTA_BRICK_SLABS).add(YTechBlocks.TERRACOTTA_BRICK_SLAB.get());
        tag(YTechBlockTags.TERRACOTTA_BRICK_STAIRS).add(YTechBlocks.TERRACOTTA_BRICK_STAIRS.get());
        tag(YTechBlockTags.THATCH).add(YTechBlocks.THATCH.get());
        tag(YTechBlockTags.THATCH_SLABS).add(YTechBlocks.THATCH_SLAB.get());
        tag(YTechBlockTags.THATCH_STAIRS).add(YTechBlocks.THATCH_STAIRS.get());

        tag(BlockTags.ANVIL).add(YTechBlocks.BRONZE_ANVIL.get());
        tag(BlockTags.BEDS).add(YTechBlocks.GRASS_BED.get());
        tag(BlockTags.SLABS).add(YTechBlocks.TERRACOTTA_BRICK_SLAB.get(), YTechBlocks.THATCH_SLAB.get());
        tag(BlockTags.STAIRS).add(YTechBlocks.TERRACOTTA_BRICK_STAIRS.get(), YTechBlocks.THATCH_STAIRS.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                YTechBlocks.AQUEDUCT.get(),
                YTechBlocks.AQUEDUCT_FERTILIZER.get(),
                YTechBlocks.AQUEDUCT_HYDRATOR.get(),
                YTechBlocks.AQUEDUCT_VALVE.get(),
                YTechBlocks.BRICK_CHIMNEY.get(),
                YTechBlocks.BRONZE_ANVIL.get(),
                YTechBlocks.MILLSTONE.get(),
                YTechBlocks.PRIMITIVE_ALLOY_SMELTER.get(),
                YTechBlocks.PRIMITIVE_SMELTER.get(),
                YTechBlocks.REINFORCED_BRICKS.get(),
                YTechBlocks.REINFORCED_BRICK_CHIMNEY.get(),
                YTechBlocks.TERRACOTTA_BRICKS.get(),
                YTechBlocks.TERRACOTTA_BRICK_SLAB.get(),
                YTechBlocks.TERRACOTTA_BRICK_STAIRS.get()
        );
        tag(BlockTags.NEEDS_STONE_TOOL).add(
                YTechBlocks.AQUEDUCT.get(),
                YTechBlocks.AQUEDUCT_FERTILIZER.get(),
                YTechBlocks.AQUEDUCT_HYDRATOR.get(),
                YTechBlocks.AQUEDUCT_VALVE.get(),
                YTechBlocks.BRICK_CHIMNEY.get(),
                YTechBlocks.BRONZE_ANVIL.get(),
                YTechBlocks.MILLSTONE.get(),
                YTechBlocks.PRIMITIVE_ALLOY_SMELTER.get(),
                YTechBlocks.PRIMITIVE_SMELTER.get(),
                YTechBlocks.REINFORCED_BRICKS.get(),
                YTechBlocks.REINFORCED_BRICK_CHIMNEY.get(),
                YTechBlocks.TERRACOTTA_BRICKS.get(),
                YTechBlocks.TERRACOTTA_BRICK_SLAB.get(),
                YTechBlocks.TERRACOTTA_BRICK_STAIRS.get()
        );

        GeneralUtils.sortedStreamMapOfMap(HOLDER.blocks(), Utils.blockComparator()).forEach((entry) -> entry.getValue().object.registerTag(entry.getValue(), this));
    }
}
