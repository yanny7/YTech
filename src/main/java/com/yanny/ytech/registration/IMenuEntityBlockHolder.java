package com.yanny.ytech.registration;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public interface IMenuEntityBlockHolder {
    Supplier<MenuType<? extends AbstractContainerMenu>> getMenuRegistry();
}
