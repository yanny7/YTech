package com.yanny.ytech.compatibility;

import com.yanny.ytech.registration.YTechItems;
import com.yanny.ytech.registration.YTechMobEffects;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosCapability {
    public static void register() {
        CuriosApi.registerCurio(YTechItems.CHLORITE_BRACELET.get(), new CuriosItem(YTechMobEffects.LUCKY_STONE.get()));
        CuriosApi.registerCurio(YTechItems.LION_MAN.get(), new CuriosItem(YTechMobEffects.LION_HEART.get()));
        CuriosApi.registerCurio(YTechItems.SHELL_BEADS.get(), new CuriosItem(YTechMobEffects.ABYSS_WALKER.get()));
        CuriosApi.registerCurio(YTechItems.VENUS_OF_HOHLE_FELS.get(), new CuriosItem(YTechMobEffects.VENUS_TOUCH.get()));
        CuriosApi.registerCurio(YTechItems.WILD_HORSE.get(), new CuriosItem(YTechMobEffects.WILD_RIDE.get()));
    }
}
