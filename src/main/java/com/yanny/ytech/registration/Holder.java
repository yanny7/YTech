package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.ObjectType;
import com.yanny.ytech.configuration.ProductType;
import com.yanny.ytech.configuration.YTechConfigLoader;
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
    public final YTechConfigLoader.Product product;
    public final YTechConfigLoader.Material material;

    Holder(YTechConfigLoader.Product product, YTechConfigLoader.Material material) {
        this.product = product;
        this.material = material;
        key = product.name().getKey(material);
        name = product.name().getLocalized(material);
        objectType = product.type();
        productType = product.id();
    }

    public static class ItemHolder extends Holder {
        public final RegistryObject<Item> item;

        public ItemHolder(YTechConfigLoader.Product product, YTechConfigLoader.Material material, RegistryObject<Item> item) {
            super(product, material);
            this.item = item;
        }
    }

    public static class BlockHolder extends Holder {
        public final RegistryObject<Block> block;

        public BlockHolder(YTechConfigLoader.Product product, YTechConfigLoader.Material material, RegistryObject<Block> block) {
            super(product, material);
            this.block = block;
        }
    }

    public static class FluidHolder extends BlockHolder {
        public final RegistryObject<FluidType> type;
        public final RegistryObject<Fluid> source;
        public final RegistryObject<Fluid> flowing;
        public final RegistryObject<Item> bucket;

        public FluidHolder(YTechConfigLoader.Product product, YTechConfigLoader.Material material, RegistryObject<Block> block,
                           RegistryObject<FluidType> type, RegistryObject<Fluid> source, RegistryObject<Fluid> flowing, RegistryObject<Item> bucket) {
            super(product, material, block);
            this.type = type;
            this.source = source;
            this.flowing = flowing;
            this.bucket = bucket;
        }
    }
}
