package com.yanny.ytech.configuration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class YTechConfigSpec {
    @NotNull private final ForgeConfigSpec.ConfigValue<Boolean> makeBlocksRequireValidTool;
    @NotNull private final ForgeConfigSpec.ConfigValue<List<? extends String>> makeBlocksRequireValidToolList;
    @NotNull private final ForgeConfigSpec.ConfigValue<Boolean> craftSharpFlintByRightClickingOnStone;
    @NotNull private final ForgeConfigSpec.ConfigValue<Boolean> noDryingDuringRain;
    @NotNull private final ForgeConfigSpec.ConfigValue<List<? extends String>> slowDryingBiomeTags;
    @NotNull private final ForgeConfigSpec.ConfigValue<List<? extends String>> fastDryingBiomeTags;
    @NotNull private final ForgeConfigSpec.ConfigValue<Integer> baseFluidStoragePerBlock;

    public YTechConfigSpec(@NotNull ForgeConfigSpec.Builder builder) {
        builder.push("general");
        makeBlocksRequireValidTool = builder.comment("If mod can change behaviour of specified blocks to require valid tool for harvesting")
                .worldRestart().define("makeBlocksRequireValidTool", true);
        makeBlocksRequireValidToolList = builder.comment("List of blocks that will require valid tool for harvesting")
                .worldRestart().defineListAllowEmpty("makeBlocksRequireValidToolList", YTechConfigSpec::getMinecraftBlocksRequiringValidTool, YTechConfigSpec::validateResourceLocation);
        craftSharpFlintByRightClickingOnStone = builder.comment("Enables crafting Sharp Flint by right-clicking on stone")
                .worldRestart().define("craftSharpFlintByRightClickingOnStone", true);
        builder.pop();
        builder.push("dryingRack");
        noDryingDuringRain = builder.comment("If Drying Rack should stop working during rain")
                .worldRestart().define("noDryingDuringRain", true);
        slowDryingBiomeTags = builder.comment("List of biome tags, where will be drying 2x slower")
                .worldRestart().defineListAllowEmpty("slowDryingBiomeTags", YTechConfigSpec::getSlowDryingBiomeTags, YTechConfigSpec::validateResourceLocation);
        fastDryingBiomeTags = builder.comment("List of biome tags, where will be drying 2x faster")
                .worldRestart().defineListAllowEmpty("fastDryingBiomeTags", YTechConfigSpec::getFastDryingBiomeTags, YTechConfigSpec::validateResourceLocation);
        builder.pop();
        builder.push("irrigation");
        baseFluidStoragePerBlock = builder.comment("Base amount of fluid stored per block")
                .worldRestart().define("baseFluidStoragePerBlock", 500);
        builder.pop();
    }

    public boolean shouldRequireValidTool() {
        return makeBlocksRequireValidTool.get();
    }

    @NotNull
    public Set<ResourceLocation> getBlocksRequiringValidTool() {
        return makeBlocksRequireValidToolList.get().stream().map(ResourceLocation::new).collect(Collectors.toSet());
    }

    public boolean enableCraftingSharpFlint() {
        return craftSharpFlintByRightClickingOnStone.get();
    }

    public boolean noDryingDuringRain() {
        return noDryingDuringRain.get();
    }

    public Set<TagKey<Biome>> getSlowDryingBiomes() {
        return slowDryingBiomeTags.get().stream().map(ResourceLocation::new).map((v) -> TagKey.create(Registries.BIOME, v)).collect(Collectors.toSet());
    }

    public Set<TagKey<Biome>> getFastDryingBiomes() {
        return fastDryingBiomeTags.get().stream().map(ResourceLocation::new).map((v) -> TagKey.create(Registries.BIOME, v)).collect(Collectors.toSet());
    }

    public int getBaseFluidStoragePerBlock() {
        return baseFluidStoragePerBlock.get();
    }

    @NotNull
    private static List<String> getMinecraftBlocksRequiringValidTool() {
        return Stream.of(
                // Logs & woods
                Blocks.ACACIA_LOG,
                Blocks.ACACIA_WOOD,
                Blocks.BIRCH_LOG,
                Blocks.BIRCH_WOOD,
                Blocks.CHERRY_LOG,
                Blocks.CHERRY_WOOD,
                Blocks.JUNGLE_LOG,
                Blocks.JUNGLE_WOOD,
                Blocks.OAK_LOG,
                Blocks.OAK_WOOD,
                Blocks.DARK_OAK_LOG,
                Blocks.DARK_OAK_WOOD,
                Blocks.MANGROVE_LOG,
                Blocks.MANGROVE_WOOD,
                Blocks.SPRUCE_LOG,
                Blocks.SPRUCE_WOOD,
                // stripped logs & woods
                Blocks.STRIPPED_ACACIA_LOG,
                Blocks.STRIPPED_ACACIA_WOOD,
                Blocks.STRIPPED_BIRCH_LOG,
                Blocks.STRIPPED_BIRCH_WOOD,
                Blocks.STRIPPED_CHERRY_LOG,
                Blocks.STRIPPED_CHERRY_WOOD,
                Blocks.STRIPPED_JUNGLE_LOG,
                Blocks.STRIPPED_JUNGLE_WOOD,
                Blocks.STRIPPED_OAK_LOG,
                Blocks.STRIPPED_OAK_WOOD,
                Blocks.STRIPPED_DARK_OAK_LOG,
                Blocks.STRIPPED_DARK_OAK_WOOD,
                Blocks.STRIPPED_MANGROVE_LOG,
                Blocks.STRIPPED_MANGROVE_WOOD,
                Blocks.STRIPPED_SPRUCE_LOG,
                Blocks.STRIPPED_SPRUCE_WOOD,
                // dirt variants
                Blocks.DIRT,
                Blocks.DIRT_PATH,
                Blocks.GRASS_BLOCK,
                Blocks.FARMLAND,
                Blocks.PODZOL,
                Blocks.MYCELIUM,
                Blocks.COARSE_DIRT,
                Blocks.ROOTED_DIRT
        ).map(value -> Utils.loc(value).toString()).collect(Collectors.toList());
    }

    @NotNull
    private static List<String> getFastDryingBiomeTags() {
        return Stream.of(
                Tags.Biomes.IS_DRY
        ).map(value -> Objects.requireNonNull(value.location()).toString()).collect(Collectors.toList());
    }

    @NotNull
    private static List<String> getSlowDryingBiomeTags() {
        return Stream.of(
                Tags.Biomes.IS_WET
        ).map(value -> Objects.requireNonNull(value.location()).toString()).collect(Collectors.toList());
    }

    private static boolean validateResourceLocation(@NotNull Object recipe) {
        return recipe instanceof String resourceLocation && ResourceLocation.isValidResourceLocation(resourceLocation);
    }
}
