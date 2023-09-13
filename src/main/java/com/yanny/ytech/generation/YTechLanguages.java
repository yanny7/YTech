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
        HOLDER.simpleItems().values().forEach(h -> add(h.item.get(), h.name));
        HOLDER.simpleBlocks().values().forEach(h -> add(h.block.get(), h.name));

        add("gui.jei.category.drying", "Drying Recipe");
        add("gui.jei.category.tanning", "Tanning Recipe");
        add("gui.jei.category.milling", "Milling Recipe");
        add("gui.jei.category.smelting", "Smelting Recipe");
        add("gui.jei.category.block_hit", "Block Hit Recipe");
    }
}
