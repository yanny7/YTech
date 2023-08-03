package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Holder<T, U extends ConfigLoader.BaseObject<T>> {
    @NotNull public final String key;
    @NotNull public final String name;
    @NotNull public final U object;
    @NotNull public final ConfigLoader.MaterialHolder materialHolder;

    Holder(@NotNull U object, @NotNull ConfigLoader.MaterialHolder materialHolder) {
        this.object = Objects.requireNonNull(object, "Product must be non null");
        this.materialHolder = Objects.requireNonNull(materialHolder, "Material must be non null");
        key = Objects.requireNonNull(object.name.getKey(materialHolder.material), "Key must be non null");
        name = Objects.requireNonNull(object.name.getLocalized(materialHolder.material), "Name must be non null");
    }

    public static class ItemHolder extends Holder<ItemObjectType, ConfigLoader.ItemObject> {
        @NotNull public final RegistryObject<Item> item;

        public ItemHolder(@NotNull ConfigLoader.ItemObject product, @NotNull ConfigLoader.MaterialHolder materialHolder, @NotNull RegistryObject<Item> item) {
            super(product, materialHolder);
            this.item = item;
        }
    }

    public static class BlockHolder extends Holder<BlockObjectType, ConfigLoader.BlockObject> {
        @NotNull public final RegistryObject<Block> block;

        public BlockHolder(@NotNull ConfigLoader.BlockObject product, @NotNull ConfigLoader.MaterialHolder materialHolder, @NotNull RegistryObject<Block> block) {
            super(product, materialHolder);
            this.block = block;
        }
    }

    public static class FluidHolder extends Holder<FluidObjectType, ConfigLoader.FluidObject> {
        @NotNull public final RegistryObject<Block> block;
        @NotNull public final RegistryObject<FluidType> type;
        @NotNull public final RegistryObject<Fluid> source;
        @NotNull public final RegistryObject<Fluid> flowing;
        @NotNull public final RegistryObject<Item> bucket;

        public FluidHolder(@NotNull ConfigLoader.FluidObject product, @NotNull ConfigLoader.MaterialHolder materialHolder,
                           @NotNull RegistryObject<Block> block, @NotNull RegistryObject<FluidType> type, @NotNull RegistryObject<Fluid> source,
                           @NotNull RegistryObject<Fluid> flowing, @NotNull RegistryObject<Item> bucket) {
            super(product, materialHolder);
            this.block = block;
            this.type = type;
            this.source = source;
            this.flowing = flowing;
            this.bucket = bucket;
        }
    }

    public static class ToolHolder extends Holder<ToolObjectType, ConfigLoader.ToolObject> {
        @NotNull public final RegistryObject<Item> tool;

        ToolHolder(@NotNull ConfigLoader.ToolObject object, @NotNull ConfigLoader.MaterialHolder materialHolder, @NotNull RegistryObject<Item> tool) {
            super(object, materialHolder);
            this.tool = tool;
        }
    }
}
