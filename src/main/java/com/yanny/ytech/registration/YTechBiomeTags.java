package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.Utils;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class YTechBiomeTags {
    public static final TagKey<Biome> DEER_BIOMES = create("deer_native_biome");

    private static TagKey<Biome> create(String name) {
        return TagKey.create(Registries.BIOME, Utils.modLoc(name));
    }
}
