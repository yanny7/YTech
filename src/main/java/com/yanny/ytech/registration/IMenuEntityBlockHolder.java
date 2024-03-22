package com.yanny.ytech.registration;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public interface IMenuEntityBlockHolder {
    <T extends AbstractContainerMenu> MenuType<T> getMenuType();
}
