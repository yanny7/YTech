package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.YTechTier;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import java.util.List;

public class Registration {
    static {
        TierSortingRegistry.registerTier(YTechTier.COPPER, Utils.mcLoc("copper"), List.of(Tiers.STONE), List.of(Tiers.IRON));
        TierSortingRegistry.registerTier(YTechTier.TIN, Utils.mcLoc("tin"), List.of(Tiers.STONE), List.of(Tiers.IRON));
        TierSortingRegistry.registerTier(YTechTier.LEAD, Utils.mcLoc("lead"), List.of(Tiers.STONE), List.of(Tiers.IRON));
        TierSortingRegistry.registerTier(YTechTier.BRONZE, Utils.mcLoc("bronze"), List.of(Tiers.STONE), List.of(Tiers.IRON));
    }

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
