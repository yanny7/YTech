package com.yanny.ytech;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.machine.container.YTechContainerMenu;
import com.yanny.ytech.machine.screen.ScreenFactory;
import com.yanny.ytech.network.kinetic.common.KineticBlockType;
import com.yanny.ytech.network.kinetic.renderer.ShaftRenderer;
import com.yanny.ytech.registration.Registration;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = YTechMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusSubscriber {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Registration.REGISTRATION_HOLDER.machine().forEach((machine, tierMap) -> tierMap.forEach((tier, holder) ->
                    MenuScreens.register((MenuType<YTechContainerMenu>) holder.menuType().get(), ScreenFactory::create)));
        });
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        BlockEntityType<? extends BlockEntity> entityType = Registration.REGISTRATION_HOLDER.kineticNetwork().get(KineticBlockType.SHAFT).entityType().get();
        LogUtils.getLogger().warn("EntityType - Renderer {}", entityType);
        event.registerBlockEntityRenderer(entityType, ShaftRenderer::new);
    }
}
