package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialBlockType;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class YTechWorldGen extends DatapackBuiltinEntriesProvider {
    private static final ResourceKey<ConfiguredFeature<?, ?>> CASSITERITE_ORE_FEATURE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Utils.modLoc("cassiterite_ore"));
    private static final ResourceKey<PlacedFeature> CASSITERITE_ORE_UPPER = ResourceKey.create(Registries.PLACED_FEATURE, Utils.modLoc("cassiterite_ore_upper"));
    private static final ResourceKey<PlacedFeature> CASSITERITE_ORE_LOWER = ResourceKey.create(Registries.PLACED_FEATURE, Utils.modLoc("cassiterite_ore_lower"));

    private static final TagMatchTest STONE_ORE_REPLACEABLE = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);

    public YTechWorldGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, getRegistrySet(), Set.of(YTechMod.MOD_ID));
    }

    private static RegistrySetBuilder getRegistrySet() {
        return new RegistrySetBuilder()
                .add(Registries.CONFIGURED_FEATURE, bootstrap -> {
                    bootstrap.register(
                            CASSITERITE_ORE_FEATURE,
                            new ConfiguredFeature<>(
                                    Feature.ORE,
                                    new OreConfiguration(
                                            List.of(OreConfiguration.target(STONE_ORE_REPLACEABLE, Registration.block(MaterialBlockType.STONE_ORE, MaterialType.CASSITERITE).defaultBlockState())),
                                            16
                                    )
                            )
                    );
                })
                .add(Registries.PLACED_FEATURE, bootstrap -> {
                    HolderGetter<ConfiguredFeature<?, ?>> configured = bootstrap.lookup(Registries.CONFIGURED_FEATURE);

                    bootstrap.register(
                            CASSITERITE_ORE_UPPER,
                            new PlacedFeature(
                                    configured.getOrThrow(CASSITERITE_ORE_FEATURE),
                                    List.of(RarityFilter.onAverageOnceEvery(6),
                                            InSquarePlacement.spread(),
                                            HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128)),
                                            BiomeFilter.biome()
                                    )
                            )
                    );
                    bootstrap.register(
                            CASSITERITE_ORE_LOWER,
                            new PlacedFeature(
                                    configured.getOrThrow(CASSITERITE_ORE_FEATURE),
                                    List.of(CountPlacement.of(2),
                                            InSquarePlacement.spread(),
                                            HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(60)),
                                            BiomeFilter.biome()
                                    )
                            )
                    );
                })
                .add(ForgeRegistries.Keys.BIOME_MODIFIERS, bootstrap -> {
                    final HolderGetter<Biome> biomeReg = bootstrap.lookup(Registries.BIOME);
                    final HolderGetter<PlacedFeature> featureReg = bootstrap.lookup(Registries.PLACED_FEATURE);
                    HolderSet<Biome> biomeHolderSet = biomeReg.getOrThrow(BiomeTags.IS_OVERWORLD);
                    HolderSet<PlacedFeature> placedFeatureHolderSet = HolderSet.direct(featureReg.getOrThrow(CASSITERITE_ORE_UPPER), featureReg.getOrThrow(CASSITERITE_ORE_LOWER));

                    bootstrap.register(ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, Utils.modLoc("overworld_ore_generation")),
                            new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                                    biomeHolderSet,
                                    placedFeatureHolderSet,
                                    GenerationStep.Decoration.UNDERGROUND_ORES
                            ));
                });
    }
}
