package com.yanny.ytech.generation;

import com.yanny.ytech.configuration.YTechAdvancementType;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

class YTechAdvancementProvider extends AdvancementProvider {
    public YTechAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, getSubProviders());
    }

    private static List<AdvancementProvider.AdvancementGenerator> getSubProviders() {
        return List.of(new CustomAdvancementProvider());
    }

    private static class CustomAdvancementProvider implements AdvancementGenerator {
        @Override
        public void generate(@NotNull HolderLookup.Provider registries, @NotNull Consumer<AdvancementHolder> saver, @NotNull ExistingFileHelper existingFileHelper) {
            for (YTechAdvancementType type : YTechAdvancementType.values()) {
                type.generate(registries, saver, existingFileHelper);
            }
        }
    }
}
