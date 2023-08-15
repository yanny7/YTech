package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechBlockTags extends BlockTagsProvider {
    public YTechBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        HOLDER.simpleBlocks().values().forEach((holder) -> holder.object.registerTag(holder, this));
        GeneralUtils.sortedStreamMap(HOLDER.blocks(), Utils.blockComparator()).forEach((entry) -> entry.getValue().object.registerTag(entry.getValue(), this));
    }
}
