package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.registration.FluidHolder;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

class YTechFluidTags extends FluidTagsProvider {
    public YTechFluidTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, completableFuture, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        Utils.sortedByMaterial(Registration.REGISTRATION_HOLDER.fluid().entrySet()).forEach(((entry) -> {
            YTechConfigLoader.Material material = entry.getKey();
            FluidHolder holder = entry.getValue();
            tag(Registration.FORGE_FLUID_TAGS.get(material)).add(holder.source().get()).add(holder.flowing().get());
        }));
    }
}
