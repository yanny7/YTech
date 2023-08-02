package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.ConfigLoader;
import com.yanny.ytech.configuration.ObjectType;
import com.yanny.ytech.registration.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

class Utils {
    static ResourceLocation getBaseBlockTexture(Block base) {
        return new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(base)).getPath());
    }

    static ResourceLocation getBlockTexture(String base) {
        return new ResourceLocation(YTechMod.MOD_ID, ModelProvider.BLOCK_FOLDER + "/" + base);
    }

    static boolean onlyFluids(Map.Entry<ConfigLoader.Material, Holder> h) {
        return h.getValue().objectType == ObjectType.FLUID;
    }

    static boolean onlyBlocks(Map.Entry<ConfigLoader.Material, Holder> h) {
        return h.getValue().objectType == ObjectType.BLOCK;
    }

    static boolean onlyBlocks(Holder h) {
        return h.objectType == ObjectType.BLOCK;
    }

    static Comparator<Map.Entry<ConfigLoader.Material, Holder>> sortMapByProductMaterial() {
        return Comparator.comparing(h -> h.getValue().productType.name() + h.getKey().id());
    }
}
