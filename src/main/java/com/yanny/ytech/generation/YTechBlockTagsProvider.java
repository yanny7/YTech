package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.registration.YTechBlockTags;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
        tag(YTechBlockTags.CRAFTING_WORKSPACES).add(YTechBlocks.CRAFTING_WORKSPACE.get());
        tag(YTechBlockTags.FIRE_PITS).add(YTechBlocks.FIRE_PIT.get());
        tag(YTechBlockTags.GRASS_BEDS).add(YTechBlocks.GRASS_BED.get());
        tag(YTechBlockTags.MILLSTONES).add(YTechBlocks.MILLSTONE.get());
        tag(YTechBlockTags.POTTERS_WHEELS).add(YTechBlocks.POTTERS_WHEEL.get());
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

        tag(YTechBlockTags.AUROCHS_RAID_BLOCKS).add(Blocks.WHEAT);
        tag(YTechBlockTags.DEER_RAID_BLOCKS).add(Blocks.WHEAT);
        tag(YTechBlockTags.FOWL_RAID_BLOCKS).add(Blocks.BEETROOTS);
        tag(YTechBlockTags.MOUFLON_RAID_BLOCKS).add(Blocks.WHEAT, Blocks.BEETROOTS, Blocks.PUMPKIN_STEM, Blocks.ATTACHED_PUMPKIN_STEM, Blocks.MELON_STEM, Blocks.ATTACHED_MELON_STEM);

        tag(YTechBlockTags.FIRE_SOURCE)
                .add(Blocks.FIRE, Blocks.CAMPFIRE, Blocks.LANTERN, Blocks.TORCH, Blocks.WALL_TORCH, Blocks.FURNACE, Blocks.BLAST_FURNACE)
                .addTag(YTechBlockTags.FIRE_PITS)
                .addTag(YTechBlockTags.PRIMITIVE_SMELTERS)
                .addTag(YTechBlockTags.PRIMITIVE_ALLOY_SMELTERS);
        tag(YTechBlockTags.SOUL_FIRE_SOURCE).add(Blocks.SOUL_FIRE, Blocks.SOUL_CAMPFIRE, Blocks.SOUL_LANTERN, Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH);

        materialOreTag(YTechBlocks.DEEPSLATE_ORES, YTechBlockTags.DEEPSLATE_ORES, MaterialType.VANILLA_METALS);
        materialTag(YTechBlocks.DRYING_RACKS, YTechBlockTags.DRYING_RACKS);
        materialTag(YTechBlocks.GRAVEL_DEPOSITS, YTechBlockTags.GRAVEL_DEPOSITS);
        materialOreTag(YTechBlocks.NETHER_ORES, YTechBlockTags.NETHER_ORES, EnumSet.of(MaterialType.GOLD));
        materialTag(YTechBlocks.RAW_STORAGE_BLOCKS, YTechBlockTags.RAW_STORAGE_BLOCKS, MaterialType.VANILLA_METALS);
        materialTag(YTechBlocks.SAND_DEPOSITS, YTechBlockTags.SAND_DEPOSITS);
        materialOreTag(YTechBlocks.STONE_ORES, YTechBlockTags.STONE_ORES, MaterialType.VANILLA_METALS);
        tieredMaterialTag(YTechBlocks.STORAGE_BLOCKS, YTechBlockTags.STORAGE_BLOCKS, MaterialType.VANILLA_METALS);
        materialTag(YTechBlocks.TANNING_RACKS, YTechBlockTags.TANNING_RACKS);

        tag(BlockTags.ANVIL).add(YTechBlocks.BRONZE_ANVIL.get());
        tag(BlockTags.BEDS).add(YTechBlocks.GRASS_BED.get());
        tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(filteredMaterials(YTechBlocks.DEEPSLATE_ORES, MaterialType.VANILLA_METALS));
        tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK).add(filteredMaterials(YTechBlocks.NETHER_ORES, EnumSet.of(MaterialType.GOLD)));
        tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(filteredMaterials(YTechBlocks.STONE_ORES, MaterialType.VANILLA_METALS));
        tag(BlockTags.SLABS).add(YTechBlocks.TERRACOTTA_BRICK_SLAB.get(), YTechBlocks.THATCH_SLAB.get());
        tag(BlockTags.STAIRS).add(YTechBlocks.TERRACOTTA_BRICK_STAIRS.get(), YTechBlocks.THATCH_STAIRS.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(
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
                )
                .add(filteredMaterials(YTechBlocks.DEEPSLATE_ORES, MaterialType.VANILLA_METALS))
                .add(filteredMaterials(YTechBlocks.NETHER_ORES, EnumSet.of(MaterialType.GOLD)))
                .add(filteredMaterials(YTechBlocks.RAW_STORAGE_BLOCKS, MaterialType.VANILLA_METALS))
                .add(filteredMaterials(YTechBlocks.STONE_ORES, MaterialType.VANILLA_METALS))
                .add(filteredMaterials(YTechBlocks.STORAGE_BLOCKS, MaterialType.VANILLA_METALS));

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(sortedMaterials(YTechBlocks.GRAVEL_DEPOSITS))
                .add(sortedMaterials(YTechBlocks.SAND_DEPOSITS));

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(sortedMaterials(YTechBlocks.DRYING_RACKS))
                .add(sortedMaterials(YTechBlocks.TANNING_RACKS))
                .add(YTechBlocks.POTTERS_WHEEL.get());

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
    }

    private void materialTag(YTechBlocks.MaterialBlock materialItem, YTechBlockTags.MaterialTag materialTag) {
        materialItem.entries().stream().sorted(Comparator.comparing(t -> t.getKey().key)).forEach((entry) -> {
            MaterialType material = entry.getKey();

            tag(materialTag.of(material)).add(materialItem.of(material).get());
            tag(materialTag.tag).addTag(materialTag.of(material));
        });
    }

    private void materialTag(YTechBlocks.MaterialBlock materialItem, YTechBlockTags.MaterialTag materialTag, EnumSet<MaterialType> excludeMaterials) {
        materialItem.entries().stream().sorted(Comparator.comparing(t -> t.getKey().key)).forEach((entry) -> {
            MaterialType material = entry.getKey();

            if (!excludeMaterials.contains(material)) {
                tag(materialTag.of(material)).add(materialItem.of(material).get());
                tag(materialTag.tag).addTag(materialTag.of(material));
            }
        });
    }

    private void tieredMaterialTag(YTechBlocks.MaterialBlock materialItem, YTechBlockTags.MaterialTag materialTag, EnumSet<MaterialType> excludeMaterials) {
        materialItem.entries().stream().sorted(Comparator.comparing(t -> t.getKey().key)).forEach((entry) -> {
            MaterialType material = entry.getKey();

            if (!excludeMaterials.contains(material)) {
                tag(materialTag.of(material)).add(materialItem.of(material).get());
                tag(materialTag.tag).addTag(materialTag.of(material));

                if (material.getTier().getTag() != null) {
                    tag(BlockTags.NEEDS_STONE_TOOL).add(entry.getValue().get());
                }
            }
        });
    }

    private void materialOreTag(YTechBlocks.MaterialBlock materialItem, YTechBlockTags.MaterialTag materialTag, EnumSet<MaterialType> excludeMaterials) {
        materialItem.entries().stream().sorted(Comparator.comparing(t -> t.getKey().key)).forEach((entry) -> {
            MaterialType material = entry.getKey();

            if (!excludeMaterials.contains(material)) {
                DeferredBlock<Block> block = materialItem.of(material);

                tag(materialTag.of(material)).add(block.get());
                tag(materialTag.tag).add(block.get());

                tag(BlockTags.NEEDS_STONE_TOOL).add(block.get());

                switch (material) {
                    case IRON -> tag(BlockTags.IRON_ORES).add(block.get());
                    case COPPER -> tag(BlockTags.COPPER_ORES).add(block.get());
                    case GOLD -> tag(BlockTags.GOLD_ORES).add(block.get());
                }
            }
        });
    }

    private static Block[] filteredMaterials(YTechBlocks.MaterialBlock block, EnumSet<MaterialType> exclude) {
        return block.entries().stream().filter((entry) -> !exclude.contains(entry.getKey())).sorted(Comparator.comparing(t -> t.getKey().key)).map(Map.Entry::getValue).map(DeferredBlock::get).toArray(Block[]::new);
    }

    private static Block[] sortedMaterials(YTechBlocks.MaterialBlock block) {
        return block.entries().stream().sorted(Comparator.comparing(t -> t.getKey().key)).map(Map.Entry::getValue).map(DeferredBlock::get).toArray(Block[]::new);
    }
}
