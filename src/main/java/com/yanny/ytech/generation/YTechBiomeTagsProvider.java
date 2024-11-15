package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.registration.YTechBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

class YTechBiomeTagsProvider extends BiomeTagsProvider {
    public YTechBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        tag(YTechBiomeTags.AUROCHS_BIOMES)
                .addTag(BiomeTags.IS_TAIGA)
                .addTag(BiomeTags.IS_SAVANNA)
                .addTag(BiomeTags.IS_FOREST)
                .addTag(Tags.Biomes.IS_PLAINS)
                .addTag(BiomeTags.IS_BADLANDS);
        tag(YTechBiomeTags.DEER_BIOMES)
                .addTag(BiomeTags.IS_TAIGA)
                .addTag(BiomeTags.IS_SAVANNA)
                .addTag(BiomeTags.IS_FOREST)
                .addTag(Tags.Biomes.IS_PLAINS)
                .addTag(BiomeTags.IS_BADLANDS);
        tag(YTechBiomeTags.FOWL_BIOMES)
                .addTag(BiomeTags.IS_TAIGA)
                .addTag(BiomeTags.IS_SAVANNA)
                .addTag(BiomeTags.IS_FOREST)
                .addTag(Tags.Biomes.IS_PLAINS)
                .addTag(BiomeTags.IS_BADLANDS);
        tag(YTechBiomeTags.MOUFLON_BIOMES)
                .addTag(BiomeTags.IS_TAIGA)
                .addTag(BiomeTags.IS_FOREST)
                .addTag(BiomeTags.IS_HILL)
                .addTag(BiomeTags.IS_MOUNTAIN)
                .addTag(Tags.Biomes.IS_PLAINS)
                .addTag(BiomeTags.IS_BADLANDS);
        tag(YTechBiomeTags.SABER_TOOTH_TIGER_BIOMES)
                .addTag(BiomeTags.IS_TAIGA)
                .addTag(BiomeTags.IS_SAVANNA)
                .addTag(BiomeTags.IS_BADLANDS);
        tag(YTechBiomeTags.TERROR_BIRD_BIOMES)
                .addTag(BiomeTags.IS_JUNGLE)
                .addTag(BiomeTags.IS_SAVANNA)
                .addTag(Tags.Biomes.IS_PLAINS)
                .addTag(BiomeTags.IS_FOREST);
        tag(YTechBiomeTags.WILD_BOAR_BIOMES)
                .addTag(BiomeTags.IS_TAIGA)
                .addTag(BiomeTags.IS_SAVANNA)
                .addTag(Tags.Biomes.IS_PLAINS)
                .addTag(BiomeTags.IS_FOREST);
        tag(YTechBiomeTags.WOOLLY_MAMMOTH_BIOMES)
                .addTag(BiomeTags.IS_TAIGA)
                .addTag(BiomeTags.IS_SAVANNA)
                .addTag(BiomeTags.IS_HILL)
                .addTag(BiomeTags.IS_BADLANDS);
        tag(YTechBiomeTags.WOOLLY_RHINO_BIOMES)
                .addTag(BiomeTags.IS_TAIGA)
                .addTag(BiomeTags.IS_BEACH)
                .addTag(BiomeTags.IS_SAVANNA);

        tag(YTechBiomeTags.FAST_DRYING_BIOMES).addTag(Tags.Biomes.IS_DRY);
        tag(YTechBiomeTags.SLOW_DRYING_BIOMES).addTag(Tags.Biomes.IS_WET);
    }
}
