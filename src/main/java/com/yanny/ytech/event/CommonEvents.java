package com.yanny.ytech.event;

import com.yanny.ytech.YTechMod;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonEvents {
    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        YTechMod.ROTARY_PROPAGATOR.onLevelLoad(event.getLevel());
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        YTechMod.ROTARY_PROPAGATOR.onLevelUnload(event.getLevel());
    }
}
