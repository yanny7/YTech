package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.registration.YTechBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

class YTechBiomeTagsProvider extends BiomeTagsProvider {
    public YTechBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        tag(YTechBiomeTags.DEER_BIOMES)
                .add(Biomes.FOREST)
                .add(Biomes.WINDSWEPT_FOREST)
                .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                .add(Biomes.BIRCH_FOREST)
                .add(Biomes.DARK_FOREST)
                .add(Biomes.OLD_GROWTH_PINE_TAIGA)
                .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                .add(Biomes.WOODED_BADLANDS)
                .add(Biomes.FLOWER_FOREST)
                .add(Biomes.CHERRY_GROVE)
                .add(Biomes.GROVE)
                .add(Biomes.SNOWY_TAIGA)
                .add(Biomes.MEADOW)
                .add(Biomes.SNOWY_SLOPES)
                .add(Biomes.WINDSWEPT_HILLS)
                .add(Biomes.TAIGA)
                .add(Biomes.SPARSE_JUNGLE)
                .add(Biomes.SWAMP)
                .add(Biomes.PLAINS)
                .add(Biomes.SUNFLOWER_PLAINS)
                .add(Biomes.SNOWY_PLAINS)
                .add(Biomes.ICE_SPIKES)
                .add(Biomes.SAVANNA);
    }
}
