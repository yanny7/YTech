package com.yanny.ytech.configuration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.registration.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Utils {
    @NotNull
    public static ResourceLocation forgeLoc(@NotNull String path) {
        return new ResourceLocation("forge", path);
    }

    @NotNull
    public static ResourceLocation mcLoc(@NotNull String path) {
        return new ResourceLocation(path);
    }

    @NotNull
    public static ResourceLocation mcBlockLoc(@NotNull String path) {
        return mcLoc(ModelProvider.BLOCK_FOLDER + "/" + path);
    }

    @NotNull
    public static ResourceLocation mcItemLoc(@NotNull String path) {
        return mcLoc(ModelProvider.ITEM_FOLDER + "/" + path);
    }

    @NotNull
    public static ResourceLocation modLoc(@NotNull String path) {
        return  new ResourceLocation(YTechMod.MOD_ID, path);
    }

    @NotNull
    public static ResourceLocation modBlockLoc(@NotNull String path) {
        return modLoc(ModelProvider.BLOCK_FOLDER + "/" + path);
    }

    @NotNull
    public static ResourceLocation modItemLoc(@NotNull String path) {
        return modLoc(ModelProvider.ITEM_FOLDER + "/" + path);
    }

    @NotNull
    public static String getHasItem(Holder holder) {
        return "has_" + holder.key;
    }

    @NotNull
    public static String getHasItem() {
        return "has_item";
    }

    @NotNull
    public static Block getLogFromMaterial(MaterialType material) {
        if (MaterialType.ALL_WOOD.contains(material)) {
            return Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(material.key + "_log")));
        } else {
            throw new IllegalStateException("Not wood type provided!");
        }
    }
}
