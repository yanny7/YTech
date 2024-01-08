package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.IMenu;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface IBlockHolder {
    @NotNull Supplier<Block> getBlockRegistry();
    @NotNull IMenu getMenu();
}
