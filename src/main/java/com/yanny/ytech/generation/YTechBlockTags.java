package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.blocks.MaterialBlock;
import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

class YTechBlockTags extends BlockTagsProvider {
    public YTechBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        Registration.REGISTERED_ORE_BLOCKS.forEach((material, stoneMap) -> stoneMap.forEach((block, registry) -> {
            tag(Tags.Blocks.ORES).add(registry.get());
            tag(Registration.ORE_BLOCK_TAGS.get(material)).add(registry.get());

            if (block.equals(Blocks.STONE)) {
                tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(registry.get());
            }
            if (block.equals(Blocks.DEEPSLATE)) {
                tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(registry.get());
            }
            if (block.equals(Blocks.NETHERRACK)) {
                tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK).add(registry.get());
            }
        }));
        Registration.REGISTERED_RAW_STORAGE_BLOCKS.forEach((material, registry) -> {
            MaterialBlock block = (MaterialBlock) registry.get();

            tag(Tags.Blocks.STORAGE_BLOCKS).add(block);
            tag(Registration.STORAGE_BLOCK_TAGS.get(material)).add(block);
        });
        Registration.REGISTERED_STORAGE_BLOCKS.forEach((material, registry) -> {
            MaterialBlock block = (MaterialBlock) registry.get();

            tag(Tags.Blocks.STORAGE_BLOCKS).add(block);
            tag(Registration.STORAGE_BLOCK_TAGS.get(material)).add(block);
        });
        for (YTechConfigLoader.Material material : YTechMod.CONFIGURATION.getElements()) {
            if (Objects.equals(material.id(), "copper")) {
                tag(Tags.Blocks.ORES_COPPER).addTag(Registration.ORE_BLOCK_TAGS.get(material));
                tag(Tags.Blocks.STORAGE_BLOCKS_COPPER).addTag(Registration.STORAGE_BLOCK_TAGS.get(material));
            }
            if (Objects.equals(material.id(), "iron")) {
                tag(Tags.Blocks.ORES_IRON).addTag(Registration.ORE_BLOCK_TAGS.get(material));
                tag(Tags.Blocks.STORAGE_BLOCKS_IRON).addTag(Registration.STORAGE_BLOCK_TAGS.get(material));
            }
            if (Objects.equals(material.id(), "gold")) {
                tag(Tags.Blocks.ORES_GOLD).addTag(Registration.ORE_BLOCK_TAGS.get(material));
                tag(Tags.Blocks.STORAGE_BLOCKS_GOLD).addTag(Registration.STORAGE_BLOCK_TAGS.get(material));
            }
        }
    }
}
