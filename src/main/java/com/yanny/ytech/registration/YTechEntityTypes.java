package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.SpearType;
import com.yanny.ytech.configuration.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class YTechEntityTypes {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, YTechMod.MOD_ID);

    public static final RegistryObject<EntityType<AurochsEntity>> AUROCHS = ENTITIES.register("aurochs", () -> EntityType.Builder.of(AurochsEntity::new, MobCategory.CREATURE).sized(0.95F, 1.65F).clientTrackingRange(10).build("aurochs"));
    public static final RegistryObject<EntityType<SpearEntity>> BRONZE_SPEAR = ENTITIES.register("bronze_spear", () -> EntityType.Builder.<SpearEntity>of((type, level) -> new SpearEntity(type, level, SpearType.BRONZE), MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("bronze_spear"));
    public static final RegistryObject<EntityType<SpearEntity>> COPPER_SPEAR = ENTITIES.register("copper_spear", () -> EntityType.Builder.<SpearEntity>of((type, level) -> new SpearEntity(type, level, SpearType.COPPER), MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("copper_spear"));
    public static final RegistryObject<EntityType<DeerEntity>> DEER = ENTITIES.register("deer", () -> EntityType.Builder.of(DeerEntity::new, MobCategory.CREATURE).sized(0.95F, 1.95F).clientTrackingRange(10).build("deer"));
    public static final RegistryObject<EntityType<SpearEntity>> FLINT_SPEAR = ENTITIES.register("flint_spear", () -> EntityType.Builder.<SpearEntity>of((type, level) -> new SpearEntity(type, level, SpearType.FLINT), MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("flint_spear"));
    public static final RegistryObject<EntityType<FowlEntity>> FOWL = ENTITIES.register("fowl", () -> EntityType.Builder.of(FowlEntity::new, MobCategory.CREATURE).sized(0.5F, 0.9F).clientTrackingRange(10).build("fowl"));
    public static final RegistryObject<EntityType<GoAroundEntity>> GO_AROUND = ENTITIES.register("go_around", () -> EntityType.Builder.<GoAroundEntity>of(GoAroundEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).updateInterval(10).build("go_around"));
    public static final RegistryObject<EntityType<SpearEntity>> IRON_SPEAR = ENTITIES.register("iron_spear", () -> EntityType.Builder.<SpearEntity>of((type, level) -> new SpearEntity(type, level, SpearType.IRON), MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("iron_spear"));
    public static final RegistryObject<EntityType<MouflonEntity>> MOUFLON = ENTITIES.register("mouflon", () -> EntityType.Builder.of(MouflonEntity::new, MobCategory.CREATURE).sized(0.9F, 1.3F).clientTrackingRange(10).build("mouflon"));
    public static final RegistryObject<EntityType<PebbleEntity>> PEBBLE = ENTITIES.register("pebble", () -> EntityType.Builder.<PebbleEntity>of(PebbleEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(20).build("pebble"));
    public static final RegistryObject<EntityType<SaberToothTigerEntity>> SABER_TOOTH_TIGER = ENTITIES.register("saber_tooth_tiger", () -> EntityType.Builder.of(SaberToothTigerEntity::new, MobCategory.CREATURE).sized(0.95F, 1.2F).clientTrackingRange(10).build("saber_tooth_tiger"));
    public static final RegistryObject<EntityType<TerrorBirdEntity>> TERROR_BIRD = ENTITIES.register("terror_bird", () -> EntityType.Builder.of(TerrorBirdEntity::new, MobCategory.CREATURE).sized(0.95F, 1.75F).clientTrackingRange(10).build("terror_bird"));
    public static final RegistryObject<EntityType<WildBoarEntity>> WILD_BOAR = ENTITIES.register("wild_boar", () -> EntityType.Builder.of(WildBoarEntity::new, MobCategory.CREATURE).sized(0.95F, 0.95F).clientTrackingRange(10).build("wild_boar"));
    public static final RegistryObject<EntityType<WoollyMammothEntity>> WOOLLY_MAMMOTH = ENTITIES.register("woolly_mammoth", () -> EntityType.Builder.of(WoollyMammothEntity::new, MobCategory.CREATURE).sized(1.7F, 1.95F).clientTrackingRange(10).build("woolly_mammoth"));
    public static final RegistryObject<EntityType<WoollyRhinoEntity>> WOOLLY_RHINO = ENTITIES.register("woolly_rhino", () -> EntityType.Builder.of(WoollyRhinoEntity::new, MobCategory.CREATURE).sized(0.95F, 1.4F).clientTrackingRange(10).build("woolly_rhino"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
