package com.yanny.ytech.generation;

import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class YTechDataMapProvider extends DataMapProvider {
    protected YTechDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
        Builder<FurnaceFuel, Item> builder = builder(NeoForgeDataMaps.FURNACE_FUELS);

        builder.add(YTechItemTags.BASKETS, new FurnaceFuel(300), false)
                .add(YTechItemTags.BOLTS.get(MaterialType.WOODEN), new FurnaceFuel(100), false)
                .add(YTechItemTags.GRASS_BEDS, new FurnaceFuel(300), false)
                .add(YTechItemTags.GRASS_FIBERS, new FurnaceFuel(100), false)
                .add(YTechItemTags.GRASS_TWINES, new FurnaceFuel(200), false)
                .add(YTechItemTags.LAVA_BUCKETS, new FurnaceFuel(20000), false)
                .add(YTechItemTags.PLATES.get(MaterialType.WOODEN), new FurnaceFuel(200), false)
                .add(YTechItemTags.THATCH, new FurnaceFuel(200), false)
                .add(YTechItemTags.THATCH_SLABS, new FurnaceFuel(100), false)
                .add(YTechItemTags.THATCH_STAIRS, new FurnaceFuel(200), false);

        YTechItemTags.DRYING_RACKS.values().forEach((tag) -> builder.add(tag, new FurnaceFuel(300), false));
        YTechItemTags.TANNING_RACKS.values().forEach((tag) -> builder.add(tag, new FurnaceFuel(300), false));
    }
}
