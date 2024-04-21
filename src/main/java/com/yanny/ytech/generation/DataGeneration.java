package com.yanny.ytech.generation;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

public class DataGeneration {
    public static void generate(@NotNull GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        YTechBlockTagsProvider blockTags = new YTechBlockTagsProvider(packOutput, event.getLookupProvider(), event.getExistingFileHelper());

        generator.addProvider(event.includeClient(), new YTechBlockStateProvider(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new YTechItemModelsProvider(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new YTechLanguageProvider(packOutput, "en_us"));
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new YTechItemTagsProvider(packOutput, event.getLookupProvider(), blockTags.contentsGetter(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new YTechBiomeTagsProvider(packOutput, event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new YTechRecipeProvider(packOutput));
        generator.addProvider(event.includeServer(), new YTechLootTableProvider(packOutput));
        generator.addProvider(event.includeServer(), new YTechGlobalLootModifierProvider(packOutput));
        generator.addProvider(event.includeServer(), new YTechDatapackProvider(packOutput, event.getLookupProvider()));
        generator.addProvider(event.includeServer(), new YTechAdvancementProvider(packOutput, event.getLookupProvider(), event.getExistingFileHelper()));
    }
}