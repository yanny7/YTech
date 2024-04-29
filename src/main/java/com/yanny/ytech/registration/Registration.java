package com.yanny.ytech.registration;

import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

public class Registration {
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == YTechCreativeTabs.TAB.getKey()) {
            YTechItems.getRegisteredItems().forEach((object) -> event.accept(object.get()));
        }
    }

    public static void addItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((i, t) -> t == 1 ? 0xF54D0C : 0xFFFFFFFF, YTechItems.LAVA_CLAY_BUCKET.get());
        event.register((i, t) -> t == 1 ? 0x0C4DF5 : 0xFFFFFFFF, YTechItems.WATER_CLAY_BUCKET.get());
    }
}
