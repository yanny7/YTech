package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.ConfigLoader;
import com.yanny.ytech.registration.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

class Utils {
    @NotNull
    static ResourceLocation getBaseBlockTexture(@NotNull Block base) {
        return new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(base)).getPath());
    }
    @NotNull

    static ResourceLocation getBlockTexture(@NotNull String base) {
        return new ResourceLocation(YTechMod.MOD_ID, ModelProvider.BLOCK_FOLDER + "/" + base);
    }

    @NotNull
    static Comparator<Map.Entry<ConfigLoader.Material, Holder.ItemHolder>> itemComparator() {
        return Comparator.comparing(h -> h.getValue().object.id.name() + h.getKey().id());
    }

    @NotNull
    static Comparator<Map.Entry<ConfigLoader.Material, Holder.BlockHolder>> blockComparator() {
        return Comparator.comparing(h -> h.getValue().object.id.name() + h.getKey().id());
    }

    @NotNull
    static Comparator<Map.Entry<ConfigLoader.Material, Holder.FluidHolder>> fluidComparator() {
        return Comparator.comparing(h -> h.getValue().object.id.name() + h.getKey().id());
    }
}
