package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.ConfigLoader;
import com.yanny.ytech.configuration.ProductType;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechBlockTags extends BlockTagsProvider {
    private final ConfigLoader.Material iron = ConfigLoader.getMaterial("iron");
    private final ConfigLoader.Material copper = ConfigLoader.getMaterial("copper");
    private final ConfigLoader.Material gold = ConfigLoader.getMaterial("gold");

    public YTechBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        GeneralUtils.filteredSortedStream(HOLDER.products(), Utils::onlyBlocks, Utils.sortMapByProductMaterial(), Holder.BlockHolder.class).forEach((holder) -> {
            ConfigLoader.Material material = holder.materialHolder.material();

            switch (holder.productType) {
                case STORAGE_BLOCK -> {
                    TagKey<Block> storageBlockTag = Registration.FORGE_STORAGE_BLOCK_TAGS.get(material).block();
                    Block block = holder.block.get();

                    if (material.equals(iron)) {
                        tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                    } else if (material.equals(copper)) {
                        tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                    } else if (material.equals(gold)) {
                        tag(BlockTags.NEEDS_IRON_TOOL).add(block);
                    } else {
                        tag(BlockTags.NEEDS_IRON_TOOL).add(block);
                    }

                    tag(Tags.Blocks.STORAGE_BLOCKS).addTag(storageBlockTag);
                    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
                    tag(storageBlockTag).add(block);
                }
                case RAW_STORAGE_BLOCK -> {
                    TagKey<Block> storageBlockTag = Registration.FORGE_RAW_STORAGE_BLOCK_TAGS.get(material).block();
                    Block block = holder.block.get();

                    if (material.equals(iron)) {
                        tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                    } else if (material.equals(copper)) {
                        tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                    } else if (material.equals(gold)) {
                        tag(BlockTags.NEEDS_IRON_TOOL).add(block);
                    } else {
                        tag(BlockTags.NEEDS_IRON_TOOL).add(block);
                    }

                    tag(Tags.Blocks.STORAGE_BLOCKS).addTag(storageBlockTag);
                    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
                    tag(storageBlockTag).add(block);
                }
                case STONE_ORE, NETHERRACK_ORE, DEEPSLATE_ORE -> {
                    Block block = holder.block.get();
                    TagKey<Block> oreTag = Registration.FORGE_ORE_TAGS.get(material).block();

                    if (material.equals(iron)) {
                        tag(BlockTags.IRON_ORES).add(block);
                        tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                    } else if (material.equals(copper)) {
                        tag(BlockTags.COPPER_ORES).add(block);
                        tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                    } else if (material.equals(gold)) {
                        tag(BlockTags.GOLD_ORES).add(block);
                        tag(BlockTags.NEEDS_IRON_TOOL).add(block);
                    } else {
                        tag(BlockTags.NEEDS_IRON_TOOL).add(block);
                    }

                    if (holder.productType == ProductType.STONE_ORE) {
                        tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(block);
                    } else if (holder.productType == ProductType.DEEPSLATE_ORE) {
                        tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(block);
                    } else if (holder.productType == ProductType.NETHERRACK_ORE) {
                        tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK).add(block);
                    }

                    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
                    tag(Tags.Blocks.ORES).add(block);
                    tag(oreTag).add(block);
                }
            }
        });
    }
}
