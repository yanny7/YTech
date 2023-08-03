package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechFluidTags extends FluidTagsProvider {
    public YTechFluidTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, completableFuture, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        GeneralUtils.sortedStreamMap(HOLDER.fluids(), Utils.fluidComparator()).forEach((entry) -> {
            Holder.FluidHolder holder = entry.getValue();
            tag(Registration.FORGE_FLUID_TAGS.get(holder.materialHolder.material)).add(holder.source.get()).add(holder.flowing.get());
        });
    }
}
