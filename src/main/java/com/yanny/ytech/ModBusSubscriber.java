package com.yanny.ytech;

import com.yanny.ytech.compatibility.TopCompatibility;
import com.yanny.ytech.configuration.HolderType;
import com.yanny.ytech.configuration.renderer.DryingRackRenderer;
import com.yanny.ytech.configuration.renderer.KineticRenderer;
import com.yanny.ytech.registration.Holder;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.yanny.ytech.registration.Registration.HOLDER;

@Mod.EventBusSubscriber(modid = YTechMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusSubscriber {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        TopCompatibility.register();
        event.enqueueWork(() -> {
            GeneralUtils.mapToStream(HOLDER.blocks()).forEach((blockHolder) -> {
                if (blockHolder.object.type == HolderType.MENU_BLOCK && blockHolder instanceof Holder.MenuEntityBlockHolder holder) {
                    MenuScreens.register(holder.menuType.get(), holder.object::getScreen);
                }
            });
            HOLDER.simpleBlocks().values().forEach((blockHolder) -> {
                if (blockHolder.object.type == HolderType.MENU_BLOCK && blockHolder instanceof Holder.MenuEntitySimpleBlockHolder holder) {
                    MenuScreens.register(holder.menuType.get(), holder.object::getScreen);
                }
            });
        });
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        HOLDER.blocks().forEach((blockType, map) -> map.forEach((material, blockHolder) -> {
            if (blockHolder instanceof Holder.EntityBlockHolder holder) {
                switch (blockType) {
                    case SHAFT, WATER_WHEEL -> event.registerBlockEntityRenderer(holder.entityType.get(), KineticRenderer::new);
                    case DRYING_RACK -> event.registerBlockEntityRenderer(holder.entityType.get(), DryingRackRenderer::new);
                }
            }
        }));
    }
}
