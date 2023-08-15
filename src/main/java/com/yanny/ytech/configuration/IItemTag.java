package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraft.data.tags.ItemTagsProvider;
import org.jetbrains.annotations.NotNull;

public interface IItemTag<T extends Holder> {
    void registerTag(@NotNull T holder, @NotNull ItemTagsProvider provider);
}
