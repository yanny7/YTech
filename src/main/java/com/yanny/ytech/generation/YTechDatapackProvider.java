package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.registration.YTechBiomeTags;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechEntityTypes;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.yanny.ytech.configuration.MaterialType.*;

public class YTechDatapackProvider extends DatapackBuiltinEntriesProvider {
    private static final ResourceKey<ConfiguredFeature<?, ?>> CASSITERITE_ORE_FEATURE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Utils.modLoc("cassiterite_ore"));
    private static final ResourceKey<ConfiguredFeature<?, ?>> GALENA_ORE_FEATURE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Utils.modLoc("galena_ore"));
    private static final ResourceKey<ConfiguredFeature<?, ?>> GOLD_SAND_DEPOSIT_FEATURE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Utils.modLoc("gold_sand_deposit"));
    private static final ResourceKey<ConfiguredFeature<?, ?>> GOLD_GRAVEL_DEPOSIT_FEATURE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Utils.modLoc("gold_gravel_deposit"));
    private static final ResourceKey<ConfiguredFeature<?, ?>> CASSITERITE_SAND_DEPOSIT_FEATURE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Utils.modLoc("cassiterite_sand_deposit"));
    private static final ResourceKey<ConfiguredFeature<?, ?>> CASSITERITE_GRAVEL_DEPOSIT_FEATURE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Utils.modLoc("cassiterite_gravel_deposit"));

    private static final ResourceKey<PlacedFeature> CASSITERITE_ORE_UPPER = ResourceKey.create(Registries.PLACED_FEATURE, Utils.modLoc("cassiterite_ore_upper"));
    private static final ResourceKey<PlacedFeature> GALENA_ORE_UPPER = ResourceKey.create(Registries.PLACED_FEATURE, Utils.modLoc("galena_ore_upper"));
    private static final ResourceKey<PlacedFeature> CASSITERITE_ORE_LOWER = ResourceKey.create(Registries.PLACED_FEATURE, Utils.modLoc("cassiterite_ore_lower"));
    private static final ResourceKey<PlacedFeature> GALENA_ORE_LOWER = ResourceKey.create(Registries.PLACED_FEATURE, Utils.modLoc("galena_ore_lower"));
    private static final ResourceKey<PlacedFeature> GOLD_SAND_DEPOSIT = ResourceKey.create(Registries.PLACED_FEATURE, Utils.modLoc("gold_sand_deposit"));
    private static final ResourceKey<PlacedFeature> GOLD_GRAVEL_DEPOSIT = ResourceKey.create(Registries.PLACED_FEATURE, Utils.modLoc("gold_gravel_deposit"));
    private static final ResourceKey<PlacedFeature> CASSITERITE_SAND_DEPOSIT = ResourceKey.create(Registries.PLACED_FEATURE, Utils.modLoc("cassiterite_sand_deposit"));
    private static final ResourceKey<PlacedFeature> CASSITERITE_GRAVEL_DEPOSIT = ResourceKey.create(Registries.PLACED_FEATURE, Utils.modLoc("cassiterite_gravel_deposit"));

    private static final TagMatchTest STONE_ORE_REPLACEABLE = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);

    public YTechDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, getRegistrySet(), Set.of(YTechMod.MOD_ID));
    }

    private static ConfiguredFeature<OreConfiguration, Feature<OreConfiguration>> oreConfiguration(@NotNull Block block, int size) {
        return new ConfiguredFeature<>(
                Feature.ORE,
                new OreConfiguration(
                        List.of(OreConfiguration.target(STONE_ORE_REPLACEABLE, block.defaultBlockState())),
                        size
                )
        );
    }

    private static RegistrySetBuilder getRegistrySet() {
        return new RegistrySetBuilder()
                .add(Registries.CONFIGURED_FEATURE, bootstrap -> {
                    bootstrap.register(CASSITERITE_ORE_FEATURE, oreConfiguration(YTechBlocks.STONE_ORES.of(CASSITERITE).get(), 16));
                    bootstrap.register(GALENA_ORE_FEATURE, oreConfiguration(YTechBlocks.STONE_ORES.of(GALENA).get(), 8));
                    bootstrap.register(GOLD_SAND_DEPOSIT_FEATURE, sandDiskConfiguration(YTechBlocks.SAND_DEPOSITS.of(GOLD).get(), 1, 3, 2));
                    bootstrap.register(GOLD_GRAVEL_DEPOSIT_FEATURE, gravelDiskConfiguration(YTechBlocks.GRAVEL_DEPOSITS.of(GOLD).get(), 1, 2, 2));
                    bootstrap.register(CASSITERITE_SAND_DEPOSIT_FEATURE, sandDiskConfiguration(YTechBlocks.SAND_DEPOSITS.of(CASSITERITE).get(), 2, 5, 2));
                    bootstrap.register(CASSITERITE_GRAVEL_DEPOSIT_FEATURE, gravelDiskConfiguration(YTechBlocks.GRAVEL_DEPOSITS.of(CASSITERITE).get(), 2, 4, 2));
                })
                .add(Registries.PLACED_FEATURE, bootstrap -> {
                    HolderGetter<ConfiguredFeature<?, ?>> configured = bootstrap.lookup(Registries.CONFIGURED_FEATURE);
                    bootstrap.register(CASSITERITE_ORE_UPPER, rareOrePlacement(configured, CASSITERITE_ORE_FEATURE, 6, 64, 128));
                    bootstrap.register(CASSITERITE_ORE_LOWER, orePlacement(configured, CASSITERITE_ORE_FEATURE, 2, 0, 60));
                    bootstrap.register(GALENA_ORE_UPPER, rareOrePlacement(configured, GALENA_ORE_FEATURE, 4, 64, 128));
                    bootstrap.register(GALENA_ORE_LOWER, orePlacement(configured, GALENA_ORE_FEATURE, 1, 0, 60));
                    bootstrap.register(GOLD_SAND_DEPOSIT, rareDepositPlacement(configured, GOLD_SAND_DEPOSIT_FEATURE, 3));
                    bootstrap.register(GOLD_GRAVEL_DEPOSIT, rareDepositPlacement(configured, GOLD_GRAVEL_DEPOSIT_FEATURE, 4));
                    bootstrap.register(CASSITERITE_SAND_DEPOSIT, rareDepositPlacement(configured, CASSITERITE_SAND_DEPOSIT_FEATURE, 2));
                    bootstrap.register(CASSITERITE_GRAVEL_DEPOSIT, rareDepositPlacement(configured, CASSITERITE_GRAVEL_DEPOSIT_FEATURE, 3));
                })
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, bootstrap -> {
                    final HolderGetter<Biome> biomeReg = bootstrap.lookup(Registries.BIOME);
                    final HolderGetter<PlacedFeature> featureReg = bootstrap.lookup(Registries.PLACED_FEATURE);

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

                    bootstrap.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Utils.modLoc("overworld_ore_generation")),
                            new BiomeModifiers.AddFeaturesBiomeModifier(
                                    biomeReg.getOrThrow(BiomeTags.IS_OVERWORLD),
                                    HolderSet.direct(
                                            featureReg.getOrThrow(CASSITERITE_ORE_UPPER),
                                            featureReg.getOrThrow(CASSITERITE_ORE_LOWER),
                                            featureReg.getOrThrow(GALENA_ORE_UPPER),
                                            featureReg.getOrThrow(GALENA_ORE_LOWER)
                                    ),
                                    GenerationStep.Decoration.UNDERGROUND_ORES
                            )
                    );
                    bootstrap.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Utils.modLoc("river_ore_generation")),
                            new BiomeModifiers.AddFeaturesBiomeModifier(
                                    biomeReg.getOrThrow(BiomeTags.IS_RIVER),
                                    HolderSet.direct(
                                            featureReg.getOrThrow(GOLD_SAND_DEPOSIT),
                                            featureReg.getOrThrow(GOLD_GRAVEL_DEPOSIT),
                                            featureReg.getOrThrow(CASSITERITE_SAND_DEPOSIT),
                                            featureReg.getOrThrow(CASSITERITE_GRAVEL_DEPOSIT)
                                    ),
                                    GenerationStep.Decoration.UNDERGROUND_ORES
                            )
                    );
                });
    }

    private static ConfiguredFeature<DiskConfiguration, Feature<DiskConfiguration>> sandDiskConfiguration(@NotNull Block block, int from, int to, int halfHeight) {
        return new ConfiguredFeature<>(
                Feature.DISK,
                new DiskConfiguration(
                        new RuleBasedBlockStateProvider(
                                BlockStateProvider.simple(block),
                                List.of(new RuleBasedBlockStateProvider.Rule(
                                        BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), Blocks.AIR),
                                        BlockStateProvider.simple(Blocks.SANDSTONE)
                                ))
                        ),
                        BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.GRASS_BLOCK)),
                        UniformInt.of(from, to),
                        halfHeight
                )
        );
    }

    private static ConfiguredFeature<DiskConfiguration, Feature<DiskConfiguration>> gravelDiskConfiguration(@NotNull Block block, int from, int to, int halfHeight) {
        return new ConfiguredFeature<>(
                Feature.DISK,
                new DiskConfiguration(
                        RuleBasedBlockStateProvider.simple(block),
                        BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.GRASS_BLOCK)),
                        UniformInt.of(from, to),
                        halfHeight
                )
        );
    }

    private static PlacedFeature rareOrePlacement(HolderGetter<ConfiguredFeature<?, ?>> configured, ResourceKey<ConfiguredFeature<?, ?>> feature, int chance, int from, int to) {
        return new PlacedFeature(
                configured.getOrThrow(feature),
                List.of(
                        RarityFilter.onAverageOnceEvery(chance),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(from), VerticalAnchor.absolute(to)),
                        BiomeFilter.biome()
                )
        );
    }

    private static PlacedFeature orePlacement(HolderGetter<ConfiguredFeature<?, ?>> configured, ResourceKey<ConfiguredFeature<?, ?>> feature, int count, int from, int to) {
        return new PlacedFeature(
                configured.getOrThrow(feature),
                List.of(
                        CountPlacement.of(count),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(from), VerticalAnchor.absolute(to)),
                        BiomeFilter.biome()
                )
        );
    }

    private static PlacedFeature rareDepositPlacement(HolderGetter<ConfiguredFeature<?, ?>> configured, ResourceKey<ConfiguredFeature<?, ?>> feature, int chance) {
        return new PlacedFeature(
                configured.getOrThrow(feature),
                List.of(
                        RarityFilter.onAverageOnceEvery(chance),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_TOP_SOLID,
                        BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.WATER)),
                        BiomeFilter.biome()
                )
        );
    }

    private static PlacedFeature depositPlacement(HolderGetter<ConfiguredFeature<?, ?>> configured, ResourceKey<ConfiguredFeature<?, ?>> feature, int count) {
        return new PlacedFeature(
                configured.getOrThrow(feature),
                List.of(
                        CountPlacement.of(count),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_TOP_SOLID,
                        BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.WATER)),
                        BiomeFilter.biome()
                )
        );
    }
}
