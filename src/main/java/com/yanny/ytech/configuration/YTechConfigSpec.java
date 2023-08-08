package com.yanny.ytech.configuration;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class YTechConfigSpec {
    @NotNull private final ForgeConfigSpec.ConfigValue<Boolean> removeMinecraftRecipes;
    @NotNull private final ForgeConfigSpec.ConfigValue<List<? extends String>> removeMinecraftRecipesList;
    @NotNull private final ForgeConfigSpec.ConfigValue<Boolean> makeBlocksRequireValidTool;
    @NotNull private final ForgeConfigSpec.ConfigValue<List<? extends String>> makeBlocksRequireValidToolList;
    @NotNull private final ForgeConfigSpec.ConfigValue<Boolean> craftSharpFlintByRightClickingOnStone;

    public YTechConfigSpec(@NotNull ForgeConfigSpec.Builder builder) {
        removeMinecraftRecipes = builder.comment("If mod can remove default minecraft recipes")
                .worldRestart().define("removeMinecraftRecipes", true);
        removeMinecraftRecipesList = builder.comment("List of recipes that will be removed")
                .worldRestart().defineListAllowEmpty("removeMinecraftRecipesList", YTechConfigSpec::getToRemoveMinecraftRecipes, YTechConfigSpec::validateResourceLocation);
        makeBlocksRequireValidTool = builder.comment("If mod can change behaviour of specified blocks to require valid tool for harvesting")
                .worldRestart().define("makeBlocksRequireValidTool", true);
        makeBlocksRequireValidToolList = builder.comment("List of blocks that will require valid tool for harvesting")
                .worldRestart().defineListAllowEmpty("makeBlocksRequireValidToolList", YTechConfigSpec::getMinecraftBlocksRequiringValidTool, YTechConfigSpec::validateResourceLocation);
        craftSharpFlintByRightClickingOnStone = builder.comment("Enables crafting Sharp Flint by right-clicking on stone")
                .worldRestart().define("craftSharpFlintByRightClickingOnStone", true);
    }

    public boolean shouldRemoveMinecraftRecipes() {
        return removeMinecraftRecipes.get();
    }

    @NotNull
    public Set<ResourceLocation> getRemoveMinecraftRecipesList() {
        return removeMinecraftRecipesList.get().stream().map(ResourceLocation::new).collect(Collectors.toSet());
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

    @NotNull
    private static List<String> getToRemoveMinecraftRecipes() {
        return Stream.of(
                Items.WOODEN_AXE,
                Items.WOODEN_PICKAXE,
                Items.WOODEN_HOE,
                Items.WOODEN_SHOVEL,
                Items.WOODEN_SWORD
        ).map(value -> Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(value)).toString()).collect(Collectors.toList());
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
        ).map(value -> Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(value)).toString()).collect(Collectors.toList());
    }

    private static boolean validateResourceLocation(@NotNull Object recipe) {
        return recipe instanceof String resourceLocation && ResourceLocation.isValidResourceLocation(resourceLocation);
    }
}
