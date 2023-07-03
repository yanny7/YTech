package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

class YTechItemTags extends ItemTagsProvider {
    public YTechItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> tagLookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, tagLookup, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        YTechConfigLoader.Material iron = YTechMod.CONFIGURATION.getElement("iron");
        YTechConfigLoader.Material copper = YTechMod.CONFIGURATION.getElement("copper");
        YTechConfigLoader.Material gold = YTechMod.CONFIGURATION.getElement("gold");

        Registration.REGISTRATION_HOLDER.ore().forEach((material, stoneMap) -> {
            TagKey<Item> oreTag = Registration.FORGE_ORE_TAGS.get(material).item();

            tag(Tags.Items.ORES).addTag(oreTag);

            stoneMap.forEach((stone, registry) -> {
                Item item = registry.get().asItem();

                if (material.equals(iron)) {
                    tag(ItemTags.IRON_ORES).add(item);
                } else if (material.equals(copper)) {
                    tag(ItemTags.COPPER_ORES).add(item);
                } else if (material.equals(gold)) {
                    tag(ItemTags.GOLD_ORES).add(item);
                }

                if (stone.equals(Blocks.STONE)) {
                    tag(Tags.Items.ORES_IN_GROUND_STONE).add(item);
                } else if (stone.equals(Blocks.DEEPSLATE)) {
                    tag(Tags.Items.ORES_IN_GROUND_DEEPSLATE).add(item);
                } else if (stone.equals(Blocks.NETHERRACK)) {
                    tag(Tags.Items.ORES_IN_GROUND_NETHERRACK).add(item);
                }

                tag(oreTag).add(item);
            });
        });
        Registration.REGISTRATION_HOLDER.storageBlock().forEach((material, registry) -> {
            TagKey<Item> storageBlockTag = Registration.FORGE_STORAGE_BLOCK_TAGS.get(material).item();

            tag(Tags.Items.STORAGE_BLOCKS).addTag(storageBlockTag);
            tag(storageBlockTag).add(registry.get().asItem());
        });
        Registration.REGISTRATION_HOLDER.rawStorageBlock().forEach((material, registry) -> {
            TagKey<Item> storageBlockTag = Registration.FORGE_RAW_STORAGE_BLOCK_TAGS.get(material).item();

            tag(Tags.Items.STORAGE_BLOCKS).addTag(storageBlockTag);
            tag(storageBlockTag).add(registry.get().asItem());
        });
        Registration.REGISTRATION_HOLDER.rawMaterial().forEach((material, registry) -> {
            TagKey<Item> rawMaterial = Registration.FORGE_RAW_MATERIAL_TAGS.get(material);

            tag(Tags.Items.RAW_MATERIALS).addTag(rawMaterial);
            tag(rawMaterial).add(registry.get());
        });
    }
}
