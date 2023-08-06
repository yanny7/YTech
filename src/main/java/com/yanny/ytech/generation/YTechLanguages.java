package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechLanguages extends LanguageProvider {
    public YTechLanguages(PackOutput output, String locale) {
        super(output, YTechMod.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        GeneralUtils.mapToStream(HOLDER.items()).forEach(h -> add(h.item.get(), h.name));
        GeneralUtils.mapToStream(HOLDER.blocks()).forEach(h -> add(h.block.get(), h.name));
        GeneralUtils.mapToStream(HOLDER.fluids()).forEach(h -> add(h.bucket.get(), h.name));
        GeneralUtils.mapToStream(HOLDER.tools()).forEach(h -> add(h.tool.get(), h.name));
        GeneralUtils.mapToStream(HOLDER.machine()).forEach(h -> add(h.item.get(), h.name));
        GeneralUtils.mapToStream(HOLDER.kineticNetwork()).forEach(h -> add(h.block.get(), h.name));
        HOLDER.simpleItems().values().forEach(h -> add(h.item.get(), h.name));
        HOLDER.simpleTools().values().forEach(h -> add(h.tool.get(), h.name));
    }
}
