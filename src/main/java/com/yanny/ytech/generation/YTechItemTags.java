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
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
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

        Utils.sortedByMaterial(Registration.REGISTRATION_HOLDER.ore().entrySet()).forEach((oreEntry) -> {
            YTechConfigLoader.Material material = oreEntry.getKey();
            HashMap<Block, RegistryObject<Block>> stoneMap = oreEntry.getValue();
            TagKey<Item> oreTag = Registration.FORGE_ORE_TAGS.get(material).item();

            tag(Tags.Items.ORES).addTag(oreTag);

            Utils.sortedByBlock(stoneMap.entrySet()).forEach((stoneEntry) -> {
                Block stone = stoneEntry.getKey();
                RegistryObject<Block> registry = stoneEntry.getValue();
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
        Utils.sortedByMaterial(Registration.REGISTRATION_HOLDER.storageBlock().entrySet()).forEach((entry) -> {
            YTechConfigLoader.Material material = entry.getKey();
            RegistryObject<Block> registry = entry.getValue();
            TagKey<Item> storageBlockTag = Registration.FORGE_STORAGE_BLOCK_TAGS.get(material).item();

            tag(Tags.Items.STORAGE_BLOCKS).addTag(storageBlockTag);
            tag(storageBlockTag).add(registry.get().asItem());
        });
        Utils.sortedByMaterial(Registration.REGISTRATION_HOLDER.rawStorageBlock().entrySet()).forEach((entry) -> {
            YTechConfigLoader.Material material = entry.getKey();
            RegistryObject<Block> registry = entry.getValue();
            TagKey<Item> storageBlockTag = Registration.FORGE_RAW_STORAGE_BLOCK_TAGS.get(material).item();

            tag(Tags.Items.STORAGE_BLOCKS).addTag(storageBlockTag);
            tag(storageBlockTag).add(registry.get().asItem());
        });
        Utils.sortedByMaterial(Registration.REGISTRATION_HOLDER.rawMaterial().entrySet()).forEach((entry) -> {
            YTechConfigLoader.Material material = entry.getKey();
            RegistryObject<Item> registry = entry.getValue();
            TagKey<Item> rawMaterial = Registration.FORGE_RAW_MATERIAL_TAGS.get(material);

            tag(Tags.Items.RAW_MATERIALS).addTag(rawMaterial);
            tag(rawMaterial).add(registry.get());
        });
        Utils.sortedByMaterial(Registration.REGISTRATION_HOLDER.ingot().entrySet()).forEach((entry) -> {
            YTechConfigLoader.Material material = entry.getKey();
            RegistryObject<Item> registry = entry.getValue();
            TagKey<Item> ingot = Registration.FORGE_INGOT_TAGS.get(material);

            tag(Tags.Items.INGOTS).addTag(ingot);
            tag(ingot).add(registry.get());
        });
        Utils.sortedByMaterial(Registration.REGISTRATION_HOLDER.dust().entrySet()).forEach((entry) -> {
            YTechConfigLoader.Material material = entry.getKey();
            RegistryObject<Item> registry = entry.getValue();
            TagKey<Item> ingot = Registration.FORGE_DUST_TAGS.get(material);

            tag(Tags.Items.DUSTS).addTag(ingot);
            tag(ingot).add(registry.get());
        });
    }
}
