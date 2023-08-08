package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.registration.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;

class Utils {
    @NotNull
    static ResourceLocation getBlockTexture(@NotNull String base) {
        return new ResourceLocation(YTechMod.MOD_ID, ModelProvider.BLOCK_FOLDER + "/" + base);
    }

    @NotNull
    static Comparator<Map.Entry<MaterialType, Holder.ItemHolder>> itemComparator() {
        return Comparator.comparing(h -> h.getValue().object.id + h.getKey());
    }

    @NotNull
    static Comparator<Map.Entry<MaterialType, Holder.BlockHolder>> blockComparator() {
        return Comparator.comparing(h -> h.getValue().object.id + h.getKey());
    }

    @NotNull
    static Comparator<Map.Entry<MaterialType, Holder.FluidHolder>> fluidComparator() {
        return Comparator.comparing(h -> h.getValue().object.id + h.getKey());
    }
}
