package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.ConfigLoader;
import com.yanny.ytech.configuration.ObjectType;
import com.yanny.ytech.configuration.ProductType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.RegistryObject;

public class Holder {
    public final String key;
    public final String name;
    public final ObjectType objectType;
    public final ProductType productType;
    public final ConfigLoader.Product product;
    public final ConfigLoader.MaterialHolder materialHolder;

    Holder(ConfigLoader.Product product, ConfigLoader.MaterialHolder materialHolder) {
        this.product = product;
        this.materialHolder = materialHolder;
        key = product.name().getKey(materialHolder.material());
        name = product.name().getLocalized(materialHolder.material());
        objectType = product.type();
        productType = product.id();
    }

    public static class ItemHolder extends Holder {
        public final RegistryObject<Item> item;

        public ItemHolder(ConfigLoader.Product product, ConfigLoader.MaterialHolder materialHolder, RegistryObject<Item> item) {
            super(product, materialHolder);
            this.item = item;
        }
    }

    public static class BlockHolder extends Holder {
        public final RegistryObject<Block> block;

        public BlockHolder(ConfigLoader.Product product, ConfigLoader.MaterialHolder materialHolder, RegistryObject<Block> block) {
            super(product, materialHolder);
            this.block = block;
        }
    }

    public static class FluidHolder extends BlockHolder {
        public final RegistryObject<FluidType> type;
        public final RegistryObject<Fluid> source;
        public final RegistryObject<Fluid> flowing;
        public final RegistryObject<Item> bucket;

        public FluidHolder(ConfigLoader.Product product, ConfigLoader.MaterialHolder materialHolder, RegistryObject<Block> block,
                           RegistryObject<FluidType> type, RegistryObject<Fluid> source, RegistryObject<Fluid> flowing, RegistryObject<Item> bucket) {
            super(product, materialHolder, block);
            this.type = type;
            this.source = source;
            this.flowing = flowing;
            this.bucket = bucket;
        }
    }
}
