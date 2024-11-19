package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class YTechSoundEvents {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, YTechMod.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> SABER_TOOTH_TIGER_AMBIENT = register("entity.saber_tooth_tiger.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> SABER_TOOTH_TIGER_HURT = register("entity.saber_tooth_tiger.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> SABER_TOOTH_TIGER_DEATH = register("entity.saber_tooth_tiger.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> TERROR_BIRD_AMBIENT = register("entity.terror_bird.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> TERROR_BIRD_HURT = register("entity.terror_bird.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> TERROR_BIRD_DEATH = register("entity.terror_bird.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOOLLY_MAMMOTH_AMBIENT = register("entity.woolly_mammoth.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOOLLY_MAMMOTH_HURT = register("entity.woolly_mammoth.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOOLLY_MAMMOTH_DEATH = register("entity.woolly_mammoth.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOOLLY_RHINO_AMBIENT = register("entity.woolly_rhino.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOOLLY_RHINO_HURT = register("entity.woolly_rhino.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOOLLY_RHINO_DEATH = register("entity.woolly_rhino.death");

    public static final DeferredHolder<SoundEvent, SoundEvent> BRONZE_ANVIL_USE = register("block.bronze_anvil.use");
    public static final DeferredHolder<SoundEvent, SoundEvent> TANNING_RACK_USE = register("block.tanning_rack.use");
    public static final DeferredHolder<SoundEvent, SoundEvent> TREE_STUMP_USE = register("block.tree_stump.use");
    public static final DeferredHolder<SoundEvent, SoundEvent> WELL_PULLEY_USE = register("block.well_pulley.use");

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Utils.modLoc(name)));
    }
}
