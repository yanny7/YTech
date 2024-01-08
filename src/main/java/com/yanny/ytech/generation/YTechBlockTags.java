package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechBlockTags extends BlockTagsProvider {
    public YTechBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        GeneralUtils.sortedStreamMap(HOLDER.simpleBlocks(), Map.Entry.comparingByKey()).forEach((entry) -> entry.getValue().object.registerTag(entry.getValue(), this));
        GeneralUtils.sortedStreamMapOfMap(HOLDER.blocks(), Utils.blockComparator()).forEach((entry) -> entry.getValue().object.registerTag(entry.getValue(), this));
    }
}
