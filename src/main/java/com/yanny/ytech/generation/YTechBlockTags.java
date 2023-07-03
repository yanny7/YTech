package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

class YTechBlockTags extends BlockTagsProvider {
    public YTechBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        YTechConfigLoader.Material iron = YTechMod.CONFIGURATION.getElement("iron");
        YTechConfigLoader.Material copper = YTechMod.CONFIGURATION.getElement("copper");
        YTechConfigLoader.Material gold = YTechMod.CONFIGURATION.getElement("gold");

        Registration.REGISTRATION_HOLDER.ore().forEach((material, stoneMap) -> {
            TagKey<Block> oreTag = Registration.FORGE_ORE_TAGS.get(material).block();

            tag(Tags.Blocks.ORES).addTag(oreTag);

            stoneMap.forEach((stone, registry) -> {
                Block block = registry.get();

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

                if (stone.equals(Blocks.STONE)) {
                    tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(block);
                } else if (stone.equals(Blocks.DEEPSLATE)) {
                    tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(block);
                } else if (stone.equals(Blocks.NETHERRACK)) {
                    tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK).add(block);
                }

                tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
                tag(oreTag).add(block);
            });
        });
        Registration.REGISTRATION_HOLDER.storageBlock().forEach((material, registry) -> {
            TagKey<Block> storageBlockTag = Registration.FORGE_STORAGE_BLOCK_TAGS.get(material).block();
            Block block = registry.get();

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
        });
        Registration.REGISTRATION_HOLDER.rawStorageBlock().forEach(((material, registry) -> {
            TagKey<Block> storageBlockTag = Registration.FORGE_RAW_STORAGE_BLOCK_TAGS.get(material).block();
            Block block = registry.get();

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
        }));
    }
}
