package com.yanny.ytech.generation;

import com.yanny.ytech.registration.Registration;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

class YTechLootTables extends LootTableProvider {
    public YTechLootTables(PackOutput packOutput) {
        super(packOutput, Collections.emptySet(), getSubProviders());
    }

    public static List<SubProviderEntry> getSubProviders() {
        return List.of(
                new LootTableProvider.SubProviderEntry(YTechBlockLootSub::new, LootContextParamSets.BLOCK)
        );
    }

    static class YTechBlockLootSub extends BlockLootSubProvider {
        protected YTechBlockLootSub() {
            super(new HashSet<>(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            Registration.REGISTRATION_HOLDER.ore().forEach((material, stoneMap) -> stoneMap.forEach((stone, registry) -> add(registry.get(), (block) -> createOreDrop(block, Registration.REGISTRATION_HOLDER.rawMaterial().get(material).get()))));
            Registration.REGISTRATION_HOLDER.rawStorageBlock().forEach(((material, registry) -> dropSelf(registry.get())));
            Registration.REGISTRATION_HOLDER.storageBlock().forEach(((material, registry) -> dropSelf(registry.get())));
        }

        @NotNull
        @Override
        protected Iterable<Block> getKnownBlocks() {
            Stream<Block> stream = Stream.of(
                    Registration.REGISTRATION_HOLDER.ore().values().stream().flatMap((map) -> map.values().stream().flatMap(RegistryObject::stream)),
                    Registration.REGISTRATION_HOLDER.rawStorageBlock().values().stream().flatMap(RegistryObject::stream),
                    Registration.REGISTRATION_HOLDER.storageBlock().values().stream().flatMap(RegistryObject::stream)
            ).flatMap(i -> i);
            return stream.toList();
        }
    }
}
