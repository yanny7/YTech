package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.registration.YTechBiomeTags;
import com.yanny.ytech.registration.YTechEntityTypes;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class YTechDatapackProvider extends DatapackBuiltinEntriesProvider {
    public YTechDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, getRegistrySet(), Set.of(YTechMod.MOD_ID));
    }

    private static RegistrySetBuilder getRegistrySet() {
        return new RegistrySetBuilder().add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, bootstrap -> {
            final HolderGetter<Biome> biomeReg = bootstrap.lookup(Registries.BIOME);

            bootstrap.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Utils.modLoc("overworld_aurochs_spawn")),
                    new BiomeModifiers.AddSpawnsBiomeModifier(
                            biomeReg.getOrThrow(YTechBiomeTags.AUROCHS_BIOMES),
                            List.of(new MobSpawnSettings.SpawnerData(YTechEntityTypes.AUROCHS.get(), 10, 2, 6))
                    )
            );
            bootstrap.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Utils.modLoc("overworld_deer_spawn")),
                    new BiomeModifiers.AddSpawnsBiomeModifier(
                            biomeReg.getOrThrow(YTechBiomeTags.DEER_BIOMES),
                            List.of(new MobSpawnSettings.SpawnerData(YTechEntityTypes.DEER.get(), 15, 4, 8))
                    )
            );
            bootstrap.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Utils.modLoc("overworld_fowl_spawn")),
                    new BiomeModifiers.AddSpawnsBiomeModifier(
                            biomeReg.getOrThrow(YTechBiomeTags.FOWL_BIOMES),
                            List.of(new MobSpawnSettings.SpawnerData(YTechEntityTypes.FOWL.get(), 10, 2, 6))
                    )
            );
            bootstrap.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Utils.modLoc("overworld_mouflon_spawn")),
                    new BiomeModifiers.AddSpawnsBiomeModifier(
                            biomeReg.getOrThrow(YTechBiomeTags.MOUFLON_BIOMES),
                            List.of(new MobSpawnSettings.SpawnerData(YTechEntityTypes.MOUFLON.get(), 10, 2, 6))
                    )
            );
            bootstrap.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Utils.modLoc("overworld_saber_tooth_tiger_spawn")),
                    new BiomeModifiers.AddSpawnsBiomeModifier(
                            biomeReg.getOrThrow(YTechBiomeTags.SABER_TOOTH_TIGER_BIOMES),
                            List.of(new MobSpawnSettings.SpawnerData(YTechEntityTypes.SABER_TOOTH_TIGER.get(), 6, 1, 3))
                    )
            );
            bootstrap.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Utils.modLoc("overworld_terror_bird_spawn")),
                    new BiomeModifiers.AddSpawnsBiomeModifier(
                            biomeReg.getOrThrow(YTechBiomeTags.TERROR_BIRD_BIOMES),
                            List.of(new MobSpawnSettings.SpawnerData(YTechEntityTypes.TERROR_BIRD.get(), 8, 1, 2))
                    )
            );
            bootstrap.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Utils.modLoc("overworld_wild_boar_spawn")),
                    new BiomeModifiers.AddSpawnsBiomeModifier(
                            biomeReg.getOrThrow(YTechBiomeTags.WILD_BOAR_BIOMES),
                            List.of(new MobSpawnSettings.SpawnerData(YTechEntityTypes.WILD_BOAR.get(), 10, 2, 6))
                    )
            );
            bootstrap.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Utils.modLoc("overworld_woolly_mammoth_spawn")),
                    new BiomeModifiers.AddSpawnsBiomeModifier(
                            biomeReg.getOrThrow(YTechBiomeTags.WOOLLY_MAMMOTH_BIOMES),
                            List.of(new MobSpawnSettings.SpawnerData(YTechEntityTypes.WOOLLY_MAMMOTH.get(), 6, 2, 6))
                    )
            );
            bootstrap.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Utils.modLoc("overworld_woolly_rhino_spawn")),
                    new BiomeModifiers.AddSpawnsBiomeModifier(
                            biomeReg.getOrThrow(YTechBiomeTags.WOOLLY_RHINO_BIOMES),
                            List.of(new MobSpawnSettings.SpawnerData(YTechEntityTypes.WOOLLY_RHINO.get(), 6, 2, 6))
                    )
            );
        });
    }
}
