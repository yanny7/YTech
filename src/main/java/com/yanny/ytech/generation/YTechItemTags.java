package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.GenericItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechItemTags extends ItemTagsProvider {
    public YTechItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> tagLookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, tagLookup, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        GeneralUtils.sortedStreamMap(HOLDER.simpleItems(), Map.Entry.comparingByKey()).forEach((entry) -> entry.getValue().object.registerTag(entry.getValue(), this));
        GeneralUtils.sortedStreamMap(HOLDER.simpleBlocks(), Map.Entry.comparingByKey()).forEach((entry) -> entry.getValue().object.registerTag(entry.getValue(), this));
        GeneralUtils.sortedStreamMapOfMap(HOLDER.items(), Utils.itemComparator()).forEach((entry) -> entry.getValue().object.registerTag(entry.getValue(), this));
        GeneralUtils.sortedStreamMapOfMap(HOLDER.blocks(), Utils.blockComparator()).forEach((entry) -> entry.getValue().object.registerTag(entry.getValue(), this));

        Arrays.stream(GenericItemTags.values()).forEach((type) -> type.registerTags(this));
    }
}
