package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

public interface IBlockTag<T extends Holder> {
    void registerTag(@NotNull T holder, @NotNull BlockTagsProvider provider);
}
