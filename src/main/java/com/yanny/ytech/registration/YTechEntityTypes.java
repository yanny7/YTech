package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.SpearType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class YTechEntityTypes {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, YTechMod.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<AurochsEntity>> AUROCHS = register("aurochs", EntityType.Builder.of(AurochsEntity::new, MobCategory.CREATURE).sized(0.95F, 1.65F).clientTrackingRange(10));
    public static final DeferredHolder<EntityType<?>, EntityType<SpearEntity>> BRONZE_SPEAR = register("bronze_spear", EntityType.Builder.<SpearEntity>of((type, level) -> new SpearEntity(type, level, SpearType.BRONZE), MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
    public static final DeferredHolder<EntityType<?>, EntityType<SpearEntity>> COPPER_SPEAR = register("copper_spear", EntityType.Builder.<SpearEntity>of((type, level) -> new SpearEntity(type, level, SpearType.COPPER), MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
    public static final DeferredHolder<EntityType<?>, EntityType<DeerEntity>> DEER = register("deer", EntityType.Builder.of(DeerEntity::new, MobCategory.CREATURE).sized(0.95F, 1.95F).clientTrackingRange(10));
    public static final DeferredHolder<EntityType<?>, EntityType<SpearEntity>> FLINT_SPEAR = register("flint_spear", EntityType.Builder.<SpearEntity>of((type, level) -> new SpearEntity(type, level, SpearType.FLINT), MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
    public static final DeferredHolder<EntityType<?>, EntityType<FowlEntity>> FOWL = register("fowl", EntityType.Builder.of(FowlEntity::new, MobCategory.CREATURE).sized(0.5F, 0.9F).clientTrackingRange(10));
    public static final DeferredHolder<EntityType<?>, EntityType<GoAroundEntity>> GO_AROUND = register("go_around", EntityType.Builder.<GoAroundEntity>of(GoAroundEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).updateInterval(10));
    public static final DeferredHolder<EntityType<?>, EntityType<SpearEntity>> IRON_SPEAR = register("iron_spear", EntityType.Builder.<SpearEntity>of((type, level) -> new SpearEntity(type, level, SpearType.IRON), MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
    public static final DeferredHolder<EntityType<?>, EntityType<MouflonEntity>> MOUFLON = register("mouflon", EntityType.Builder.of(MouflonEntity::new, MobCategory.CREATURE).sized(0.9F, 1.3F).clientTrackingRange(10));
    public static final DeferredHolder<EntityType<?>, EntityType<PebbleEntity>> PEBBLE = register("pebble", EntityType.Builder.<PebbleEntity>of(PebbleEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(20));
    public static final DeferredHolder<EntityType<?>, EntityType<SaberToothTigerEntity>> SABER_TOOTH_TIGER = register("saber_tooth_tiger", EntityType.Builder.of(SaberToothTigerEntity::new, MobCategory.CREATURE).sized(0.95F, 1.2F).clientTrackingRange(10));
    public static final DeferredHolder<EntityType<?>, EntityType<TerrorBirdEntity>> TERROR_BIRD = register("terror_bird", EntityType.Builder.of(TerrorBirdEntity::new, MobCategory.CREATURE).sized(0.95F, 1.75F).clientTrackingRange(10));
    public static final DeferredHolder<EntityType<?>, EntityType<WildBoarEntity>> WILD_BOAR = register("wild_boar", EntityType.Builder.of(WildBoarEntity::new, MobCategory.CREATURE).sized(0.95F, 0.95F).clientTrackingRange(10));
    public static final DeferredHolder<EntityType<?>, EntityType<WoollyMammothEntity>> WOOLLY_MAMMOTH = register("woolly_mammoth", EntityType.Builder.of(WoollyMammothEntity::new, MobCategory.CREATURE).sized(1.7F, 1.95F).clientTrackingRange(10));
    public static final DeferredHolder<EntityType<?>, EntityType<WoollyRhinoEntity>> WOOLLY_RHINO = register("woolly_rhino", EntityType.Builder.of(WoollyRhinoEntity::new, MobCategory.CREATURE).sized(0.95F, 1.4F).clientTrackingRange(10));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> resourceKey = ResourceKey.create(Registries.ENTITY_TYPE, Utils.modLoc(name));
        return ENTITIES.register(name, () -> builder.build(resourceKey));
    }
}
