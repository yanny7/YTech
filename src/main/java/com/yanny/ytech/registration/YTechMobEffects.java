package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.mob_effect.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class YTechMobEffects {
    private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, YTechMod.MOD_ID);

    public static final RegistryObject<MobEffect> ABYSS_WALKER = MOB_EFFECTS.register("abyss_walker", AbyssWalkerMobEffect::new);
    public static final RegistryObject<MobEffect> LION_HEART = MOB_EFFECTS.register("lions_heart", LionHeartMobEffect::new);
    public static final RegistryObject<MobEffect> LUCKY_STONE = MOB_EFFECTS.register("lucky_stone", LuckyStoneMobEffect::new);
    public static final RegistryObject<MobEffect> VENUS_TOUCH = MOB_EFFECTS.register("venus_touch", VenusTouchMobEffect::new);
    public static final RegistryObject<MobEffect> WILD_RIDE = MOB_EFFECTS.register("wild_ride", WildRideMobEffect::new);

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
