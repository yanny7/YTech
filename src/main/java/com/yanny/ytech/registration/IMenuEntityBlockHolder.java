package com.yanny.ytech.registration;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.RegistryObject;

public interface IMenuEntityBlockHolder {
    RegistryObject<MenuType<? extends AbstractContainerMenu>> getMenuRegistry();
}
