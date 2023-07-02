package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
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
        Registration.REGISTRATION_HOLDER.ore().forEach((material, stoneMap) -> stoneMap.forEach((stone, registry) -> {
            Block block = registry.get();
            tag(Tags.Blocks.ORES).add(block);
            tag(Registration.ORE_BLOCK_TAGS.get(material)).add(block);

            if (stone.equals(Blocks.STONE)) {
                tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(block);
            }
            if (stone.equals(Blocks.DEEPSLATE)) {
                tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(block);
            }
            if (stone.equals(Blocks.NETHERRACK)) {
                tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK).add(block);
            }
        }));
        Registration.REGISTRATION_HOLDER.rawStorageBlock().forEach((material, registry) -> {
            tag(Tags.Blocks.STORAGE_BLOCKS).add(registry.get());
            tag(Registration.STORAGE_BLOCK_TAGS.get(material)).add(registry.get());
        });
        Registration.REGISTRATION_HOLDER.storageBlock().forEach((material, registry) -> {
            tag(Tags.Blocks.STORAGE_BLOCKS).add(registry.get());
            tag(Registration.STORAGE_BLOCK_TAGS.get(material)).add(registry.get());
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
