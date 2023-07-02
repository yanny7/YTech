package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

class Utils {
    static ResourceLocation getBaseBlockTexture(Block base) {
        return new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(base)).getPath());
    }

    static ResourceLocation getBlockTexture(String base) {
        return new ResourceLocation(YTechMod.MOD_ID, ModelProvider.BLOCK_FOLDER + "/" + base);
    }

    static ResourceLocation getBaseItemTexture(Item base) {
        return new ResourceLocation(ModelProvider.ITEM_FOLDER + "/" + Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(base)).getPath());
    }

    static ResourceLocation getItemTexture(String base) {
        return new ResourceLocation(YTechMod.MOD_ID, ModelProvider.ITEM_FOLDER + "/" + base);
    }
}
