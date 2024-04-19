package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.configuration.block.GrassBedBlock;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

            GeneralUtils.mapToStream(HOLDER.blocks()).forEach(h -> h.object.registerLoot(h, this));
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
                    GeneralUtils.mapToStream(HOLDER.blocks()).flatMap(e -> e.block.stream())
            ).flatMap(i -> i).toList();
        }

        private void dropSelf(RegistryObject<Block> block) {
            dropSelf(block.get());
        }

        private void registerSlabLootTable(RegistryObject<Block> block) {
            add(block.get(), this::createSlabItemTable);
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
}
