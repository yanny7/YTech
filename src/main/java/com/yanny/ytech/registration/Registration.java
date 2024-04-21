package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.YTechTier;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.common.TierSortingRegistry;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Registration {
    static {
        for (int i = 0; i < MaterialType.TIERS.size(); i++) {
            Tier tier = MaterialType.TIERS.get(i);

            if (tier instanceof YTechTier techTier) {
                List<Object> after = i > 0 ? List.of(getTierObject(MaterialType.TIERS.get(i - 1))) : List.of();
                List<Object> before = i + 1 < MaterialType.TIERS.size() ? List.of(getTierObject(MaterialType.TIERS.get(i + 1))) : List.of();

                TierSortingRegistry.registerTier(tier, techTier.getId(), after, before);
            }
        }
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

    private static Object getTierObject(@NotNull Tier tier) {
        return tier instanceof YTechTier techTier ? techTier.getId() : tier;
    }
}
