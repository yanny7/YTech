package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class YTechCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, YTechMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TAB = registerCreativeTab();

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }

    private static RegistryObject<CreativeModeTab> registerCreativeTab() {
        Supplier<ItemStack> iconSupplier = () -> new ItemStack(YTechBlocks.PRIMITIVE_SMELTER.get());
        return CREATIVE_TABS.register(YTechMod.MOD_ID, () -> CreativeModeTab.builder().title(Component.translatable("creativeTab.ytech.title")).icon(iconSupplier).build());
    }
}
