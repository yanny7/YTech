package com.yanny.ytech.generation;

import com.yanny.ytech.configuration.AdvancementType;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

class YTechAdvancements extends ForgeAdvancementProvider {
    public YTechAdvancements(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, getSubProviders());
    }

    private static List<ForgeAdvancementProvider.AdvancementGenerator> getSubProviders() {
        return List.of(new AdvancementProvider());
    }

    private static class AdvancementProvider implements AdvancementGenerator {
        @Override
        public void generate(@NotNull HolderLookup.Provider registries, @NotNull Consumer<Advancement> saver, @NotNull ExistingFileHelper existingFileHelper) {
            for (AdvancementType type : AdvancementType.values()) {
                type.generate(saver, existingFileHelper);
            }
        }
    }
}
