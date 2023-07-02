package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

class YTechItemTags extends ItemTagsProvider {
    public YTechItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> tagLookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, tagLookup, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        Registration.REGISTERED_ORE_ITEMS.forEach((material, stoneMap) -> stoneMap.forEach((item, registry) -> {
            tag(Tags.Items.ORES).add(item);
            tag(Registration.ORE_BLOCK_ITEM_TAGS.get(material)).add(item);

            if (item.equals(Items.STONE)) {
                tag(Tags.Items.ORES_IN_GROUND_STONE).add(item);
            }
            if (item.equals(Items.DEEPSLATE)) {
                tag(Tags.Items.ORES_IN_GROUND_DEEPSLATE).add(item);
            }
            if (item.equals(Items.NETHERRACK)) {
                tag(Tags.Items.ORES_IN_GROUND_NETHERRACK).add(item);
            }
        }));
    }
}
