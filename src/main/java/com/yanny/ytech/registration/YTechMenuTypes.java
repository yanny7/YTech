package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.IMenu;
import com.yanny.ytech.configuration.container.AqueductFertilizerMenu;
import com.yanny.ytech.configuration.container.PrimitiveAlloySmelterContainerMenu;
import com.yanny.ytech.configuration.container.PrimitiveSmelterContainerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class YTechMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, YTechMod.MOD_ID);

    public static final RegistryObject<MenuType<AqueductFertilizerMenu>> AQUEDUCT_FERTILIZER = register("aqueduct_fertilizer", ((windowId, inv, pos, itemStackHandler, data) -> new AqueductFertilizerMenu(windowId, inv.player, pos, itemStackHandler, data)));
    public static final RegistryObject<MenuType<PrimitiveAlloySmelterContainerMenu>> PRIMITIVE_ALLOY_SMELTER = register("primitive_alloy_smelter", ((windowId, inv, pos, itemStackHandler, data) -> new PrimitiveAlloySmelterContainerMenu(windowId, inv.player, pos, itemStackHandler, data)));
    public static final RegistryObject<MenuType<PrimitiveSmelterContainerMenu>> PRIMITIVE_SMELTER = register("primitive_smelter", ((windowId, inv, pos, itemStackHandler, data) -> new PrimitiveSmelterContainerMenu(windowId, inv.player, pos, itemStackHandler, data)));

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String name, IMenu<T> menu) {
        return MENU_TYPES.register(name, () -> IForgeMenuType.create((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            return menu.getContainerMenu(windowId, inv, pos);
        }));
    }
}
