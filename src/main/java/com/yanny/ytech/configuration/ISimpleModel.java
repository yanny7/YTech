package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface ISimpleModel<T extends Holder, U extends DataProvider> extends IModel<T, U> {
    @NotNull Set<Integer> getTintIndices();
    @NotNull ResourceLocation[] getTextures();
}