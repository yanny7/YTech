package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface IMaterialModel<T extends Holder, U extends DataProvider> extends IModel<T, U> {
    @NotNull Map<Integer, Integer> getTintColors();
    @NotNull ResourceLocation[] getTextures(@NotNull MaterialType material);
}
