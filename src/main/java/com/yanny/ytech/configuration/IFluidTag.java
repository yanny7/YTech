package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraft.data.tags.FluidTagsProvider;
import org.jetbrains.annotations.NotNull;

public interface IFluidTag<T extends Holder> {
    void registerTag(@NotNull T holder, @NotNull FluidTagsProvider provider);
}
