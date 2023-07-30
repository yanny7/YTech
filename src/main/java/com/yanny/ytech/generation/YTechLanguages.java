package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechLanguages extends LanguageProvider {
    public YTechLanguages(PackOutput output, String locale) {
        super(output, YTechMod.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        GeneralUtils.mapToStream(HOLDER.machine()).forEach(h -> add(h.item.get(), h.name));
        GeneralUtils.mapToStream(HOLDER.kineticNetwork()).forEach(h -> add(h.block.get(), h.name));
        GeneralUtils.mapToStream(HOLDER.products()).forEach(h -> {
            switch (h.objectType) {
                case ITEM -> add(((Holder.ItemHolder) h).item.get(), h.name);
                case BLOCK -> add(((Holder.BlockHolder) h).block.get(), h.name);
                case FLUID -> add(((Holder.FluidHolder) h).bucket.get(), h.name);
            }
        });
    }
}
