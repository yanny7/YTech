package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.configuration.ObjectType;
import com.yanny.ytech.configuration.ProductType;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechLootTables extends LootTableProvider {
    public YTechLootTables(PackOutput packOutput) {
        super(packOutput, Collections.emptySet(), getSubProviders());
    }

    public static List<SubProviderEntry> getSubProviders() {
        return List.of(
                new LootTableProvider.SubProviderEntry(YTechBlockLootSub::new, LootContextParamSets.BLOCK)
        );
    }

    private static class YTechBlockLootSub extends BlockLootSubProvider {
        protected YTechBlockLootSub() {
            super(new HashSet<>(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            HOLDER.machine().forEach((machine, tierMap) -> tierMap.forEach((tier, holder) -> dropSelf(holder.block().get())));
            HOLDER.kineticNetwork().forEach((type, materialMap) -> materialMap.forEach((material, holder) -> dropSelf(holder.block().get())));
            GeneralUtils.filteredStream(HOLDER.products(), ObjectType.BLOCK, Holder.BlockHolder.class).forEach(h -> {
                switch (h.productType) {
                    case STONE_ORE, DEEPSLATE_ORE, NETHERRACK_ORE ->
                            add(h.block.get(), (block) -> createOreDrop(block, GeneralUtils.get(HOLDER.products(),
                                    ProductType.RAW_MATERIAL, h.material, Holder.ItemHolder.class).item.get()));
                    default -> dropSelf(h.block.get());
                }
            });
        }

        @NotNull
        @Override
        protected Iterable<Block> getKnownBlocks() {
            Stream<Block> stream = Stream.of(
                    GeneralUtils.filteredStream(HOLDER.products(), ObjectType.BLOCK, Holder.BlockHolder.class).flatMap(e -> e.block.stream()),
                    HOLDER.machine().values().stream().flatMap((map) -> map.values().stream().flatMap((f) -> f.block().stream())),
                    HOLDER.kineticNetwork().values().stream().flatMap((map) -> map.values().stream().flatMap((holder) -> holder.block().stream()))
            ).flatMap(i -> i);
            return stream.toList();
        }
    }
}
