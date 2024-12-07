package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.IMenu;
import com.yanny.ytech.configuration.container.AqueductFertilizerMenu;
import com.yanny.ytech.configuration.container.PrimitiveAlloySmelterContainerMenu;
import com.yanny.ytech.configuration.container.PrimitiveSmelterContainerMenu;
import com.yanny.ytech.configuration.container.StrainerContainerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class YTechMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, YTechMod.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<AqueductFertilizerMenu>> AQUEDUCT_FERTILIZER = register("aqueduct_fertilizer", ((windowId, inv, pos, itemStackHandler, data) -> new AqueductFertilizerMenu(windowId, inv.player, pos, itemStackHandler, data)));
    public static final DeferredHolder<MenuType<?>, MenuType<PrimitiveAlloySmelterContainerMenu>> PRIMITIVE_ALLOY_SMELTER = register("primitive_alloy_smelter", ((windowId, inv, pos, itemStackHandler, data) -> new PrimitiveAlloySmelterContainerMenu(windowId, inv.player, pos, itemStackHandler, data)));
    public static final DeferredHolder<MenuType<?>, MenuType<PrimitiveSmelterContainerMenu>> PRIMITIVE_SMELTER = register("primitive_smelter", ((windowId, inv, pos, itemStackHandler, data) -> new PrimitiveSmelterContainerMenu(windowId, inv.player, pos, itemStackHandler, data)));
    public static final DeferredHolder<MenuType<?>, MenuType<StrainerContainerMenu>> STRAINER = register("strainer", ((windowId, inv, pos, itemStackHandler, data) -> new StrainerContainerMenu(windowId, inv.player, pos, itemStackHandler, data)));

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String name, IMenu<T> menu) {
        return MENU_TYPES.register(name, () -> IMenuTypeExtension.create((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            return menu.getContainerMenu(windowId, inv, pos);
        }));
    }
}
