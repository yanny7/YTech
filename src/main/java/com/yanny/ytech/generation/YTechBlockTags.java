package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialBlockType;
import com.yanny.ytech.configuration.MaterialType;
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
    public YTechBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        GeneralUtils.sortedStreamMap(HOLDER.blocks(), Utils.blockComparator()).forEach((entry) -> {
            Holder.BlockHolder holder = entry.getValue();
            MaterialType material = holder.material;

            switch (holder.object) {
                case STORAGE_BLOCK -> {
                    TagKey<Block> storageBlockTag = Registration.FORGE_STORAGE_BLOCK_TAGS.get(material).block();
                    Block block = holder.block.get();

                    if (material.equals(MaterialType.IRON)) {
                        tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                    } else if (material.equals(MaterialType.COPPER)) {
                        tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                    } else if (material.equals(MaterialType.GOLD)) {
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

                    if (material.equals(MaterialType.IRON)) {
                        tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                    } else if (material.equals(MaterialType.COPPER)) {
                        tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                    } else if (material.equals(MaterialType.GOLD)) {
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

                    if (material.equals(MaterialType.IRON)) {
                        tag(BlockTags.IRON_ORES).add(block);
                        tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                    } else if (material.equals(MaterialType.COPPER)) {
                        tag(BlockTags.COPPER_ORES).add(block);
                        tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                    } else if (material.equals(MaterialType.GOLD)) {
                        tag(BlockTags.GOLD_ORES).add(block);
                        tag(BlockTags.NEEDS_IRON_TOOL).add(block);
                    } else {
                        tag(BlockTags.NEEDS_IRON_TOOL).add(block);
                    }

                    if (holder.object == MaterialBlockType.STONE_ORE) {
                        tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(block);
                    } else if (holder.object == MaterialBlockType.DEEPSLATE_ORE) {
                        tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(block);
                    } else {
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
