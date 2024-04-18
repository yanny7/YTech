package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.registration.YTechBlockTags;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechBlockTagsProvider extends BlockTagsProvider {
    public YTechBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        tag(YTechBlockTags.BRONZE_ANVIL).add(YTechBlocks.BRONZE_ANVIL.get());
        tag(BlockTags.ANVIL).add(YTechBlocks.BRONZE_ANVIL.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                YTechBlocks.BRONZE_ANVIL.get()
        );
        tag(BlockTags.NEEDS_STONE_TOOL).add(
                YTechBlocks.BRONZE_ANVIL.get()
        );

        GeneralUtils.sortedStreamMap(HOLDER.simpleBlocks(), Map.Entry.comparingByKey()).forEach((entry) -> entry.getValue().object.registerTag(entry.getValue(), this));
        GeneralUtils.sortedStreamMapOfMap(HOLDER.blocks(), Utils.blockComparator()).forEach((entry) -> entry.getValue().object.registerTag(entry.getValue(), this));
    }
}
