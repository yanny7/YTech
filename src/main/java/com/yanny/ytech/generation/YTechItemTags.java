package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialBlockType;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechItemTags extends ItemTagsProvider {
    public YTechItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> tagLookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, tagLookup, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        GeneralUtils.sortedStreamMap(HOLDER.items(), Utils.itemComparator()).forEach(entry -> {
            MaterialType material = entry.getKey();
            Holder.ItemHolder holder = entry.getValue();

            switch (holder.object) {
                case INGOT -> {
                    TagKey<Item> ingot = Registration.FORGE_INGOT_TAGS.get(material);

                    tag(Tags.Items.INGOTS).addTag(ingot);
                    tag(ingot).add(holder.item.get());
                }
                case DUST -> {
                    TagKey<Item> ingot = Registration.FORGE_DUST_TAGS.get(material);

                    tag(Tags.Items.DUSTS).addTag(ingot);
                    tag(ingot).add(holder.item.get());
                }
                case RAW_MATERIAL -> {
                    TagKey<Item> rawMaterial = Registration.FORGE_RAW_MATERIAL_TAGS.get(material);

                    tag(Tags.Items.RAW_MATERIALS).addTag(rawMaterial);
                    tag(rawMaterial).add(holder.item.get());
                }
            }
        });
        GeneralUtils.sortedStreamMap(HOLDER.blocks(), Utils.blockComparator()).forEach(entry -> {
            MaterialType material = entry.getKey();
            Holder.BlockHolder holder = entry.getValue();

            switch (holder.object) {
                case STORAGE_BLOCK -> {
                    TagKey<Item> storageBlockTag = Registration.FORGE_STORAGE_BLOCK_TAGS.get(material).item();

                    tag(Tags.Items.STORAGE_BLOCKS).addTag(storageBlockTag);
                    tag(storageBlockTag).add(holder.block.get().asItem());
                }
                case RAW_STORAGE_BLOCK -> {
                    TagKey<Item> storageBlockTag = Registration.FORGE_RAW_STORAGE_BLOCK_TAGS.get(material).item();

                    tag(Tags.Items.STORAGE_BLOCKS).addTag(storageBlockTag);
                    tag(storageBlockTag).add(holder.block.get().asItem());
                }
                case STONE_ORE, DEEPSLATE_ORE, NETHERRACK_ORE -> {
                    Item item = holder.block.get().asItem();
                    TagKey<Item> oreTag = Registration.FORGE_ORE_TAGS.get(material).item();

                    if (holder.material.equals(MaterialType.IRON)) {
                        tag(ItemTags.IRON_ORES).add(item);
                    } else if (material.equals(MaterialType.COPPER)) {
                        tag(ItemTags.COPPER_ORES).add(item);
                    } else if (material.equals(MaterialType.GOLD)) {
                        tag(ItemTags.GOLD_ORES).add(item);
                    }

                    if (holder.object == MaterialBlockType.STONE_ORE) {
                        tag(Tags.Items.ORES_IN_GROUND_STONE).add(item);
                    } else if (holder.object == MaterialBlockType.DEEPSLATE_ORE) {
                        tag(Tags.Items.ORES_IN_GROUND_DEEPSLATE).add(item);
                    } else {
                        tag(Tags.Items.ORES_IN_GROUND_NETHERRACK).add(item);
                    }
                    tag(Tags.Items.ORES).add(item);
                    tag(oreTag).add(item);
                }
            }
        });
    }
}
