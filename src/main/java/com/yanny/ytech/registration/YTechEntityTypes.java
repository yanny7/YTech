package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.SpearType;
import com.yanny.ytech.configuration.entity.GoAroundEntity;
import com.yanny.ytech.configuration.entity.PebbleEntity;
import com.yanny.ytech.configuration.entity.SpearEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class YTechEntityTypes {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, YTechMod.MOD_ID);

    public static final RegistryObject<EntityType<SpearEntity>> BRONZE_SPEAR = ENTITIES.register("bronze_spear", () -> EntityType.Builder.<SpearEntity>of((type, level) -> new SpearEntity(type, level, SpearType.BRONZE), MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("bronze_spear"));
    public static final RegistryObject<EntityType<SpearEntity>> COPPER_SPEAR = ENTITIES.register("copper_spear", () -> EntityType.Builder.<SpearEntity>of((type, level) -> new SpearEntity(type, level, SpearType.COPPER), MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("copper_spear"));
    public static final RegistryObject<EntityType<SpearEntity>> FLINT_SPEAR = ENTITIES.register("flint_spear", () -> EntityType.Builder.<SpearEntity>of((type, level) -> new SpearEntity(type, level, SpearType.FLINT), MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("flint_spear"));
    public static final RegistryObject<EntityType<GoAroundEntity>> GO_AROUND = ENTITIES.register("go_around", () -> EntityType.Builder.<GoAroundEntity>of(GoAroundEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).updateInterval(10).build("go_around"));
    public static final RegistryObject<EntityType<SpearEntity>> IRON_SPEAR = ENTITIES.register("iron_spear", () -> EntityType.Builder.<SpearEntity>of((type, level) -> new SpearEntity(type, level, SpearType.IRON), MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("iron_spear"));
    public static final RegistryObject<EntityType<PebbleEntity>> PEBBLE = ENTITIES.register("pebble", () -> EntityType.Builder.<PebbleEntity>of(PebbleEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(20).build("pebble"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
