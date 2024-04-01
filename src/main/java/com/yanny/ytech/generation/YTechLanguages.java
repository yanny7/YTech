package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.AdvancementType;
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
            add(h.getEntityType(), h.name);
            add(h.spawnEgg.get(), h.name + " Spawn Egg");
        });

        for (AdvancementType type : AdvancementType.values()) {
            add(type.titleId(), type.title());
            add(type.descriptionId(), type.description());
        }

        add("gui.ytech.category.drying", "Drying");
        add("gui.ytech.category.tanning", "Tanning");
        add("gui.ytech.category.milling", "Milling");
        add("gui.ytech.category.smelting", "Smelting");
        add("gui.ytech.category.block_hit", "Block Hit");
        add("gui.ytech.category.alloying", "Alloying");
        add("gui.ytech.category.hammering", "Hammering");

        add("text.ytech.info.grass_fibers", "Grass fibers are obtained by breaking grass with Sharp Flint");

        add("text.ytech.hover.grass_fibers", "Obtainable by breaking grass with");
        add("text.ytech.hover.primitive_smelter", "Increase temperature by adding chimneys");
        add("text.ytech.hover.chimney", "Increases temperature in smelter by %s °C");
        add("text.ytech.hover.aqueduct1", "Can hold %smB of water");
        add("text.ytech.hover.aqueduct2", "Raining generates %smB every %s tick(s), if visible to sky");
        add("text.ytech.hover.aqueduct_valve1", "Provides water from water source to aqueduct network");
        add("text.ytech.hover.aqueduct_valve2", "Generates %smB every %s tick(s)");
        add("text.ytech.hover.aqueduct_hydrator1", "Hydrates soil similar to water source");
        add("text.ytech.hover.aqueduct_hydrator2", "Consumes %smB every %s tick(s)");
        add("text.ytech.hover.aqueduct_fertilizer1", "Hydrates soil similar to water source and applies random bonemeal effect when fertilizer is provided");
        add("text.ytech.hover.aqueduct_fertilizer2", "Consumes %smB every %s tick(s)");
        add("text.ytech.hover.drying_rack1", "Works faster in dry biomes and slower in wet biomes");
        add("text.ytech.hover.drying_rack2", "Doesn't work during rain");
        add("text.ytech.hover.millstone", "Use leashed animal on block as power source. Shift-right click with empty hand for disconnecting animal.");
        add("text.ytech.hover.fire_pit1", "Lit by right-click when holding sticks in both hands (40% chance)");
        add("text.ytech.hover.fire_pit2", "Right-click with burnable item to increase level by floor(log10(burnTime))");
        add("text.ytech.hover.fire_pit3", "Each random tick is level decreased by 1");
        add("text.ytech.hover.fire_pit4", "Rain have chance to extinguish fire (50% chance on random tick)");

        add("creativeTab.ytech.title", "YTech");
    }
}
