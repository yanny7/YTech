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
        HOLDER.machine().forEach((machine, tierMap) -> tierMap.forEach((tier, holder) -> add(holder.item().get(), tier.name() + " " + machine.name())));
        HOLDER.kineticNetwork().forEach((type, materialMap) -> materialMap.forEach((material, holder) -> add(holder.block().get(), material.name() + " " + type.lang)));

        GeneralUtils.flatStream(HOLDER.products()).forEach(h -> {
            switch (h.objectType) {
                case ITEM -> add(((Holder.ItemHolder) h).item.get(), h.name);
                case BLOCK -> add(((Holder.BlockHolder) h).block.get(), h.name);
                case FLUID -> add(((Holder.FluidHolder) h).bucket.get(), h.name);
            }
        });
    }
}
