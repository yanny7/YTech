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
        Registration.REGISTRATION_HOLDER.ore().forEach((material, stoneMap) -> stoneMap.forEach((stone, registry) -> add(registry.get(), I18n.get(stone.getDescriptionId()) + " " + material.name() + " Ore")));
        Registration.REGISTRATION_HOLDER.rawStorageBlock().forEach((material, registry) -> add(registry.get(), "Raw " + material.name() + " Block"));
        Registration.REGISTRATION_HOLDER.storageBlock().forEach((material, registry) -> add(registry.get(), "Block of " + material.name()));

        Registration.REGISTRATION_HOLDER.rawMaterial().forEach((material, registry) -> add(registry.get(), "Raw " + material.name()));
        Registration.REGISTRATION_HOLDER.ingot().forEach((material, registry) -> add(registry.get(), material.name() + " Ingot"));
        Registration.REGISTRATION_HOLDER.fluid().forEach(((material, holder) -> add(holder.bucket().get(), material.name() + " Bucket")));
    }
}
