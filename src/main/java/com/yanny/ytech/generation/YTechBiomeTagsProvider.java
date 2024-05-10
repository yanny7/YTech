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
        tag(YTechBiomeTags.AUROCHS_BIOMES)
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
                .add(Biomes.SAVANNA)
                .add(Biomes.SAVANNA_PLATEAU);
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
                .add(Biomes.ICE_SPIKES);
        tag(YTechBiomeTags.FOWL_BIOMES)
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
                .add(Biomes.SAVANNA)
                .add(Biomes.SAVANNA_PLATEAU);
        tag(YTechBiomeTags.MOUFLON_BIOMES)
                .add(Biomes.TAIGA)
                .add(Biomes.WINDSWEPT_HILLS)
                .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
                .add(Biomes.WINDSWEPT_FOREST)
                .add(Biomes.WINDSWEPT_SAVANNA)
                .add(Biomes.BADLANDS)
                .add(Biomes.ERODED_BADLANDS)
                .add(Biomes.WOODED_BADLANDS)
                .add(Biomes.FROZEN_PEAKS)
                .add(Biomes.JAGGED_PEAKS)
                .add(Biomes.STONY_PEAKS)
                .add(Biomes.STONY_SHORE)
                .add(Biomes.SNOWY_PLAINS);
        tag(YTechBiomeTags.SABER_TOOTH_TIGER_BIOMES)
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
                .add(Biomes.SAVANNA)
                .add(Biomes.SAVANNA_PLATEAU);
        tag(YTechBiomeTags.WILD_BOAR_BIOMES)
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
                .add(Biomes.SAVANNA)
                .add(Biomes.SAVANNA_PLATEAU);
        tag(YTechBiomeTags.WOOLLY_MAMMOTH_BIOMES)
                .add(Biomes.WINDSWEPT_FOREST)
                .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                .add(Biomes.OLD_GROWTH_PINE_TAIGA)
                .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                .add(Biomes.WOODED_BADLANDS)
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
                .add(Biomes.ICE_SPIKES);
        tag(YTechBiomeTags.WOOLLY_RHINO_BIOMES)
                .add(Biomes.WINDSWEPT_FOREST)
                .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                .add(Biomes.OLD_GROWTH_PINE_TAIGA)
                .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                .add(Biomes.WOODED_BADLANDS)
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
                .add(Biomes.SNOWY_BEACH)
                .add(Biomes.FROZEN_RIVER);
    }
}
