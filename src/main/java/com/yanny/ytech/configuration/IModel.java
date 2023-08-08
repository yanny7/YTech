package com.yanny.ytech.configuration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelProvider;
import org.jetbrains.annotations.NotNull;

public interface IModel<T extends Holder, U extends DataProvider> {
    void registerModel(@NotNull T holder, @NotNull U provider);

    @NotNull
    static ResourceLocation mcLoc(@NotNull String path) {
        return new ResourceLocation(path);
    }

    @NotNull
    static ResourceLocation mcBlockLoc(@NotNull String path) {
        return mcLoc(ModelProvider.BLOCK_FOLDER + "/" + path);
    }

    @NotNull
    static ResourceLocation mcItemLoc(@NotNull String path) {
        return mcLoc(ModelProvider.ITEM_FOLDER + "/" + path);
    }

    @NotNull
    static ResourceLocation modLoc(@NotNull String path) {
        return  new ResourceLocation(YTechMod.MOD_ID, path);
    }

    @NotNull
    static ResourceLocation modBlockLoc(@NotNull String path) {
        return modLoc(ModelProvider.BLOCK_FOLDER + "/" + path);
    }

    @NotNull
    static ResourceLocation modItemLoc(@NotNull String path) {
        return modLoc(ModelProvider.ITEM_FOLDER + "/" + path);
    }
}
