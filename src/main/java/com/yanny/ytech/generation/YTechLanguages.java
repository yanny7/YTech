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
        HOLDER.entities().values().forEach(h -> {
            add(h.entityType.get(), h.name);
            add(h.spawnEgg.get(), h.name + " Spawn Egg");
        });

        add("gui.ytech.category.drying", "Drying");
        add("gui.ytech.category.tanning", "Tanning");
        add("gui.ytech.category.milling", "Milling");
        add("gui.ytech.category.smelting", "Smelting");
        add("gui.ytech.category.block_hit", "Block Hit");
        add("gui.ytech.category.alloying", "Alloying");
        add("gui.ytech.category.hammering", "Hammering");
    }
}
