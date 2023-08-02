package com.yanny.ytech.configuration;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class YTechConfigSpec {
    private final ForgeConfigSpec.ConfigValue<Boolean> removeMinecraftRecipes;
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> removeMinecraftRecipesList;

    public YTechConfigSpec(ForgeConfigSpec.Builder builder) {
        removeMinecraftRecipes = builder.comment("If mod can remove default minecraft recipes")
                .worldRestart().define("removeMinecraftRecipes", true);
        removeMinecraftRecipesList = builder.comment("List of recipes that will be removed")
                .worldRestart().defineListAllowEmpty("removeMinecraftRecipesList", YTechConfigSpec::getMinecraftRecipes, YTechConfigSpec::validateMinecraftRecipe);
    }

    public boolean shouldRemoveMinecraftRecipes() {
        return removeMinecraftRecipes.get();
    }

    public Set<ResourceLocation> getRemoveMinecraftRecipesList() {
        return removeMinecraftRecipesList.get().stream().map(ResourceLocation::new).collect(Collectors.toSet());
    }

    private static List<String> getMinecraftRecipes() {
        return Stream.of(
                Items.WOODEN_AXE,
                Items.WOODEN_PICKAXE,
                Items.WOODEN_HOE,
                Items.WOODEN_SHOVEL,
                Items.WOODEN_SWORD
        ).map(value -> Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(value)).toString()).collect(Collectors.toList());
    }

    private static boolean validateMinecraftRecipe(Object recipe) {
        return recipe instanceof String resourceLocation && ResourceLocation.isValidResourceLocation(resourceLocation);
    }
}
