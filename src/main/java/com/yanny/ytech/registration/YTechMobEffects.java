package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.mob_effect.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class YTechMobEffects {
    private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, YTechMod.MOD_ID);

    public static final DeferredHolder<MobEffect, AbyssWalkerMobEffect> ABYSS_WALKER = MOB_EFFECTS.register("abyss_walker", AbyssWalkerMobEffect::new);
    public static final DeferredHolder<MobEffect, LionHeartMobEffect> LION_HEART = MOB_EFFECTS.register("lions_heart", LionHeartMobEffect::new);
    public static final DeferredHolder<MobEffect, LuckyStoneMobEffect> LUCKY_STONE = MOB_EFFECTS.register("lucky_stone", LuckyStoneMobEffect::new);
    public static final DeferredHolder<MobEffect, VenusTouchMobEffect> VENUS_TOUCH = MOB_EFFECTS.register("venus_touch", VenusTouchMobEffect::new);
    public static final DeferredHolder<MobEffect, WildRideMobEffect> WILD_RIDE = MOB_EFFECTS.register("wild_ride", WildRideMobEffect::new);

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
