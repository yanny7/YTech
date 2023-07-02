package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.registration.Registration;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

class YTechLanguages extends LanguageProvider {
    public YTechLanguages(PackOutput output, String locale) {
        super(output, YTechMod.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        Registration.REGISTERED_ORE_BLOCKS.forEach((material, stoneMap) -> stoneMap.forEach((block, registry) -> add(registry.get(), I18n.get(block.getDescriptionId()) + " " + material.name() + " Ore")));
        Registration.REGISTERED_RAW_STORAGE_BLOCKS.forEach((material, registry) -> add(registry.get(), "Raw " + material.name() + " Block"));
        Registration.REGISTERED_STORAGE_BLOCKS.forEach((material, registry) -> add(registry.get(), "Block of " + material.name()));

        Registration.REGISTERED_RAW_METAL_ITEMS.forEach((material, registry) -> add(registry.get(), "Raw " + material.name()));
    }
}
