package com.yanny.ytech;

import com.yanny.ytech.machine.container.YTechContainerMenu;
import com.yanny.ytech.machine.screen.ScreenFactory;
import com.yanny.ytech.registration.Registration;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = YTechMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class YTechModClient {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Registration.REGISTRATION_HOLDER.machine().forEach((machine, tierMap) -> tierMap.forEach((tier, holder) ->
                    MenuScreens.register((MenuType<YTechContainerMenu>) holder.menuType().get(), ScreenFactory::create)));
        });
    }
}
