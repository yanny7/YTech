package com.yanny.ytech.configuration;

import com.google.gson.JsonPrimitive;
import com.yanny.ytech.configuration.entity.DeerEntity;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.YTechItems;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public enum AnimalEntityType {
    DEER("deer", "Deer",
            TagKey.create(Registries.BIOME, Utils.modLoc("deer_native_biome")),
            (builder) -> builder.sized(0.9F, 1.6F).clientTrackingRange(10),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.WORLD_SURFACE,
            DeerEntity::new,
            () -> DeerEntity.createAttributes().build(),
            holder -> AnimalEntityType.spawnEggItem(holder, 0x664825, 0xE09C53),
            AnimalEntityType::registerSpawnEggModel,
            AnimalEntityType::registerDeerDrops,
            AnimalEntityType::registerDeerBiomeTag,
            AnimalEntityType::spawnAnimalPredicate)
    ;

    @NotNull public final String key;
    @NotNull public final String name;
    @NotNull public final TagKey<Biome> biomeTag;
    @NotNull public final Consumer<EntityType.Builder<Animal>> entityTypeBuilder;
    @NotNull public final SpawnPlacements.Type spawnPlacement;
    @NotNull public final Heightmap.Types heightMapType;
    @NotNull private final AnimalEntityGetter entityGetter;
    @NotNull private final Supplier<AttributeSupplier> attributeGetter;
    @NotNull private final Function<Holder.AnimalEntityHolder, Item> spawnEggGetter;
    @NotNull private final BiConsumer<Holder.AnimalEntityHolder, ItemModelProvider> modelGetter;
    @NotNull private final BiConsumer<Holder.AnimalEntityHolder, EntityLootSubProvider> lootGetter;
    @NotNull private final BiConsumer<Holder.AnimalEntityHolder, BiomeTagsProvider> biomeTagsGetter;
    @NotNull public final SpawnPlacements.SpawnPredicate<Animal> spawnPredicate;

    AnimalEntityType(@NotNull String key, @NotNull String name, @NotNull TagKey<Biome> biomeTag, @NotNull Consumer<EntityType.Builder<Animal>> entityTypeBuilder,
                     @NotNull SpawnPlacements.Type spawnPlacement, @NotNull Heightmap.Types heightMapType, @NotNull AnimalEntityGetter entityGetter,
                     @NotNull Supplier<AttributeSupplier> attributeGetter, @NotNull Function<Holder.AnimalEntityHolder, Item> spawnEggGetter,
                     @NotNull BiConsumer<Holder.AnimalEntityHolder, ItemModelProvider> modelGetter,
                     @NotNull BiConsumer<Holder.AnimalEntityHolder, EntityLootSubProvider> lootGetter,
                     @NotNull BiConsumer<Holder.AnimalEntityHolder, BiomeTagsProvider> biomeTagsGetter,
                     @NotNull SpawnPlacements.SpawnPredicate<Animal> spawnPredicate) {
        this.key = key;
        this.name = name;
        this.biomeTag = biomeTag;
        this.entityTypeBuilder = entityTypeBuilder;
        this.spawnPlacement = spawnPlacement;
        this.heightMapType = heightMapType;
        this.entityGetter = entityGetter;
        this.attributeGetter = attributeGetter;
        this.spawnEggGetter = spawnEggGetter;
        this.modelGetter = modelGetter;
        this.lootGetter = lootGetter;
        this.biomeTagsGetter = biomeTagsGetter;
        this.spawnPredicate = spawnPredicate;
    }

    public void registerModel(@NotNull Holder.AnimalEntityHolder holder, @NotNull ItemModelProvider provider) {
        modelGetter.accept(holder, provider);
    }

    public void registerLoot(@NotNull Holder.AnimalEntityHolder holder, @NotNull EntityLootSubProvider provider) {
        lootGetter.accept(holder, provider);
    }

    public void registerTag(@NotNull Holder.AnimalEntityHolder holder, @NotNull BiomeTagsProvider provider) {
        biomeTagsGetter.accept(holder, provider);
    }

    public Animal getEntity(@NotNull Holder.AnimalEntityHolder holder, @NotNull EntityType<Animal> entityType, @NotNull Level level) {
        return entityGetter.get(holder, entityType, level);
    }

    public Item getSpawnEgg(@NotNull Holder.AnimalEntityHolder holder) {
        return spawnEggGetter.apply(holder);
    }

    public AttributeSupplier getAttributes() {
        return attributeGetter.get();
    }

    private static Item spawnEggItem(@NotNull Holder.AnimalEntityHolder holder, int background, int highlight) {
        return new ForgeSpawnEggItem(holder::getEntityType, background, highlight, new Item.Properties());
    }

    private static void registerSpawnEggModel(@NotNull Holder.AnimalEntityHolder holder, @NotNull ItemModelProvider provider) {
        provider.getBuilder(holder.key + "_spawn_egg").parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
    }

    private static boolean spawnAnimalPredicate(@NotNull EntityType<Animal> entityType, @NotNull ServerLevelAccessor level,
                                          @NotNull MobSpawnType spawnType, @NotNull BlockPos pos, @NotNull RandomSource random) {
        return Animal.checkAnimalSpawnRules(entityType, level, spawnType, pos, random);
    }

    private static void registerDeerDrops(Holder.AnimalEntityHolder holder, EntityLootSubProvider provider) {
        EntityPredicate.Builder entityOnFire = EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true).build());

        provider.add(holder.getEntityType(), LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        LootItem.lootTableItem(YTechItems.RAW_HIDE.get())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        LootItem.lootTableItem(YTechItems.VENISON.get())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                                                .apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, entityOnFire)))
                                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(
                                        LootItem.lootTableItem(YTechItems.ANTLER.get())
                                                .when(
                                                        LootItemEntityPropertyCondition.hasProperties(
                                                                LootContext.EntityTarget.THIS,
                                                                EntityPredicate.Builder.entity().nbt(NbtPredicate.fromJson(new JsonPrimitive(DeerEntity.hasAntlersStr())))
                                                        )
                                                )
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                )
                )
        );
    }

    private static void registerDeerBiomeTag(Holder.AnimalEntityHolder holder, BiomeTagsProvider provider) {
        provider.tag(holder.object.biomeTag)
                .add(Biomes.FOREST)
                .add(Biomes.WINDSWEPT_FOREST)
                .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                .add(Biomes.BIRCH_FOREST)
                .add(Biomes.DARK_FOREST)
                .add(Biomes.OLD_GROWTH_PINE_TAIGA)
                .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                .add(Biomes.WOODED_BADLANDS)
                .add(Biomes.FLOWER_FOREST)
                .add(Biomes.CHERRY_GROVE)
                .add(Biomes.GROVE)
                .add(Biomes.SNOWY_TAIGA)
                .add(Biomes.MEADOW)
                .add(Biomes.SNOWY_SLOPES)
                .add(Biomes.WINDSWEPT_HILLS)
                .add(Biomes.TAIGA)
                .add(Biomes.SPARSE_JUNGLE)
                .add(Biomes.SWAMP)
                .add(Biomes.PLAINS)
                .add(Biomes.SUNFLOWER_PLAINS)
                .add(Biomes.SNOWY_PLAINS)
                .add(Biomes.ICE_SPIKES)
                .add(Biomes.SAVANNA)
        ;
    }

    interface AnimalEntityGetter {
        Animal get(@NotNull Holder.AnimalEntityHolder holder, @NotNull EntityType<Animal> entityType, @NotNull Level level);
    }
}
