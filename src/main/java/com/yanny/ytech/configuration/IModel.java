package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraft.data.DataProvider;
import org.jetbrains.annotations.NotNull;

public interface IModel<T extends Holder, U extends DataProvider> {
    void registerModel(@NotNull T holder, @NotNull U provider);
}
