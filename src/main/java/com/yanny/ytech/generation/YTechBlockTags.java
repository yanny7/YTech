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
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

class YTechBlockTags extends BlockTagsProvider {
    private final YTechConfigLoader.Material iron = YTechConfigLoader.getElement("iron");
    private final YTechConfigLoader.Material copper = YTechConfigLoader.getElement("copper");
    private final YTechConfigLoader.Material gold = YTechConfigLoader.getElement("gold");

    public YTechBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {

        Utils.sortedByMaterial(Registration.REGISTRATION_HOLDER.ore().entrySet()).forEach((oreEntry) -> {
            YTechConfigLoader.Material material = oreEntry.getKey();
            HashMap<Block, RegistryObject<Block>> stoneMap = oreEntry.getValue();
            TagKey<Block> oreTag = Registration.FORGE_ORE_TAGS.get(material).block();

            tag(Tags.Blocks.ORES).addTag(oreTag);

            Utils.sortedByBlock(stoneMap.entrySet()).forEach((stoneEntry) -> {
                Block stone = stoneEntry.getKey();
                RegistryObject<Block> registry = stoneEntry.getValue();
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
        Utils.sortedByMaterial(Registration.REGISTRATION_HOLDER.storageBlock().entrySet()).forEach((entry) -> {
            YTechConfigLoader.Material material = entry.getKey();
            RegistryObject<Block> registry = entry.getValue();
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
        Utils.sortedByMaterial(Registration.REGISTRATION_HOLDER.rawStorageBlock().entrySet()).forEach((entry) -> {
            YTechConfigLoader.Material material = entry.getKey();
            RegistryObject<Block> registry = entry.getValue();
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
        });
    }
}
