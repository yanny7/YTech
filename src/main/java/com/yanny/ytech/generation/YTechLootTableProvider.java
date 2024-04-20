package com.yanny.ytech.generation;

import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.block.GrassBedBlock;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItems;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechLootTableProvider extends LootTableProvider {
    public YTechLootTableProvider(PackOutput packOutput) {
        super(packOutput, Collections.emptySet(), getSubProviders());
    }

    private static List<SubProviderEntry> getSubProviders() {
        return List.of(
                new LootTableProvider.SubProviderEntry(YTechBlockLootSub::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(YTechEntityLootSub::new, LootContextParamSets.ENTITY)
        );
    }

    private static class YTechBlockLootSub extends BlockLootSubProvider {
        protected YTechBlockLootSub() {
            super(new HashSet<>(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            dropSelf(YTechBlocks.AQUEDUCT);
            dropSelf(YTechBlocks.AQUEDUCT_FERTILIZER);
            dropSelf(YTechBlocks.AQUEDUCT_HYDRATOR);
            dropSelf(YTechBlocks.AQUEDUCT_VALVE);
            dropSelf(YTechBlocks.BRICK_CHIMNEY);
            dropSelf(YTechBlocks.BRONZE_ANVIL);
            dropSelf(YTechBlocks.FIRE_PIT);
            GrassBedBlock.registerLootTable(this);
            dropSelf(YTechBlocks.MILLSTONE);
            dropSelf(YTechBlocks.PRIMITIVE_ALLOY_SMELTER);
            dropSelf(YTechBlocks.PRIMITIVE_SMELTER);
            dropSelf(YTechBlocks.REINFORCED_BRICKS);
            dropSelf(YTechBlocks.REINFORCED_BRICK_CHIMNEY);
            dropSelf(YTechBlocks.TERRACOTTA_BRICKS);
            registerSlabLootTable(YTechBlocks.TERRACOTTA_BRICK_SLAB);
            dropSelf(YTechBlocks.TERRACOTTA_BRICK_STAIRS);
            dropSelf(YTechBlocks.THATCH);
            registerSlabLootTable(YTechBlocks.THATCH_SLAB);
            dropSelf(YTechBlocks.THATCH_STAIRS);

            registerMaterialLootTable(YTechBlocks.DEEPSLATE_ORES, this::oreLoot, MaterialType.VANILLA_METALS);
            registerMaterialLootTable(YTechBlocks.DRYING_RACKS, this::dropSelf);
            registerMaterialLootTable(YTechBlocks.GRAVEL_DEPOSITS, (block, material) -> depositLootProvider(block, material, Items.GRAVEL));
            registerMaterialLootTable(YTechBlocks.NETHER_ORES, this::oreLoot, EnumSet.of(MaterialType.GOLD));
            registerMaterialLootTable(YTechBlocks.RAW_STORAGE_BLOCKS, this::dropSelf, MaterialType.VANILLA_METALS);
            registerMaterialLootTable(YTechBlocks.SAND_DEPOSITS, (block, material) -> depositLootProvider(block, material, Items.SAND));
            registerMaterialLootTable(YTechBlocks.STONE_ORES, this::oreLoot, MaterialType.VANILLA_METALS);
            registerMaterialLootTable(YTechBlocks.STORAGE_BLOCKS, this::dropSelf, MaterialType.VANILLA_METALS);
            registerMaterialLootTable(YTechBlocks.TANNING_RACKS, this::dropSelf);
        }

        @NotNull
        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Stream.of(
                    Stream.of(
                            YTechBlocks.AQUEDUCT,
                            YTechBlocks.AQUEDUCT_FERTILIZER,
                            YTechBlocks.AQUEDUCT_HYDRATOR,
                            YTechBlocks.AQUEDUCT_VALVE,
                            YTechBlocks.BRICK_CHIMNEY,
                            YTechBlocks.BRONZE_ANVIL,
                            YTechBlocks.FIRE_PIT,
                            YTechBlocks.GRASS_BED,
                            YTechBlocks.MILLSTONE,
                            YTechBlocks.PRIMITIVE_ALLOY_SMELTER,
                            YTechBlocks.PRIMITIVE_SMELTER,
                            YTechBlocks.REINFORCED_BRICKS,
                            YTechBlocks.REINFORCED_BRICK_CHIMNEY,
                            YTechBlocks.TERRACOTTA_BRICKS,
                            YTechBlocks.TERRACOTTA_BRICK_SLAB,
                            YTechBlocks.TERRACOTTA_BRICK_STAIRS,
                            YTechBlocks.THATCH,
                            YTechBlocks.THATCH_SLAB,
                            YTechBlocks.THATCH_STAIRS
                    ).map(RegistryObject::get),
                    filteredStream(YTechBlocks.DEEPSLATE_ORES, MaterialType.VANILLA_METALS).map(Map.Entry::getValue).map(RegistryObject::get),
                    YTechBlocks.DRYING_RACKS.entries().stream().map(Map.Entry::getValue).map(RegistryObject::get),
                    YTechBlocks.GRAVEL_DEPOSITS.entries().stream().map(Map.Entry::getValue).map(RegistryObject::get),
                    filteredStream(YTechBlocks.NETHER_ORES, EnumSet.of(MaterialType.GOLD)).map(Map.Entry::getValue).map(RegistryObject::get),
                    filteredStream(YTechBlocks.RAW_STORAGE_BLOCKS, MaterialType.VANILLA_METALS).map(Map.Entry::getValue).map(RegistryObject::get),
                    YTechBlocks.SAND_DEPOSITS.entries().stream().map(Map.Entry::getValue).map(RegistryObject::get),
                    filteredStream(YTechBlocks.STONE_ORES, MaterialType.VANILLA_METALS).map(Map.Entry::getValue).map(RegistryObject::get),
                    filteredStream(YTechBlocks.STORAGE_BLOCKS, MaterialType.VANILLA_METALS).map(Map.Entry::getValue).map(RegistryObject::get),
                    YTechBlocks.TANNING_RACKS.entries().stream().map(Map.Entry::getValue).map(RegistryObject::get)
            ).flatMap(i -> i).toList();
        }

        private void dropSelf(RegistryObject<Block> block) {
            dropSelf(block.get());
        }

        private void registerSlabLootTable(RegistryObject<Block> block) {
            add(block.get(), this::createSlabItemTable);
        }

        private void oreLoot(RegistryObject<Block> block, MaterialType material) {
            add(block.get(), b -> createOreDrop(b, YTechItems.RAW_MATERIALS.of(material).get()));
        }

        private void depositLootProvider(RegistryObject<Block> object, MaterialType material, @NotNull Item baseItem) {
            LootItemCondition.Builder hasSilkTouch = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(
                    new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));

            add(
                    object.get(),
                    (block) -> LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1.0F))
                                            .when(hasSilkTouch)
                                            .add(LootItem.lootTableItem(block))
                            )
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1.0F))
                                            .when(hasSilkTouch.invert())
                                            .add(LootItem.lootTableItem(baseItem))
                            )
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1.0F))
                                            .when(hasSilkTouch.invert())
                                            .add(
                                                    LootItem.lootTableItem(YTechItems.CRUSHED_MATERIALS.of(material).get())
                                                            .when(LootItemRandomChanceCondition.randomChance(0.25F))
                                                            .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 2))
                                            )
                            )
            );
        }
    }

    private static class YTechEntityLootSub extends EntityLootSubProvider {
        protected YTechEntityLootSub() {
            super(FeatureFlagSet.of(FeatureFlags.VANILLA), FeatureFlagSet.of());
        }

        @Override
        public void generate() {
            HOLDER.entities().values().forEach(h -> h.object.registerLoot(h, this));
        }
    }

    private static Stream<Map.Entry<MaterialType, RegistryObject<Block>>> filteredStream(YTechBlocks.MaterialBlock block, EnumSet<MaterialType> exclude) {
        return block.entries().stream().filter((entry) -> !exclude.contains(entry.getKey()));
    }

    private static void registerMaterialLootTable(YTechBlocks.MaterialBlock block, Consumer<RegistryObject<Block>> loot, EnumSet<MaterialType> exclude) {
        filteredStream(block, exclude).map(Map.Entry::getValue).forEach(loot);
    }

    private static void registerMaterialLootTable(YTechBlocks.MaterialBlock block, BiConsumer<RegistryObject<Block>, MaterialType> loot, EnumSet<MaterialType> exclude) {
        filteredStream(block, exclude).forEach((entry) -> loot.accept(entry.getValue(), entry.getKey()));
    }

    private static void registerMaterialLootTable(YTechBlocks.MaterialBlock block, BiConsumer<RegistryObject<Block>, MaterialType> loot) {
        block.entries().forEach((entry) -> loot.accept(entry.getValue(), entry.getKey()));
    }

    private static void registerMaterialLootTable(YTechBlocks.MaterialBlock block, Consumer<RegistryObject<Block>> loot) {
        block.entries().forEach((entry) -> loot.accept(entry.getValue()));
    }
}
