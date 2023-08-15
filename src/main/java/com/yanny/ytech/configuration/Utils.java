package com.yanny.ytech.configuration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.registration.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelProvider;
import org.jetbrains.annotations.NotNull;

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

    public static String getHasName(Holder holder) {
        return "has_" + holder.key;
    }
}
