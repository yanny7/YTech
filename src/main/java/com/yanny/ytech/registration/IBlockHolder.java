package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.IMenu;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public interface IBlockHolder {
    @NotNull RegistryObject<Block> getBlockRegistry();
    @NotNull IMenu getMenu();
}
