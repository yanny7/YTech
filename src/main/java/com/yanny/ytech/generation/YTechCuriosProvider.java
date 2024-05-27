package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.concurrent.CompletableFuture;

public class YTechCuriosProvider extends CuriosDataProvider {
    public YTechCuriosProvider(PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(YTechMod.MOD_ID, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {
        createSlot("bracelet").size(1).dropRule(ICurio.DropRule.DEFAULT);
        createSlot("necklace").size(1).dropRule(ICurio.DropRule.DEFAULT);
        createSlot("charm").size(1).dropRule(ICurio.DropRule.DEFAULT);
        createEntities("bracelet_entities").addPlayer().addSlots("bracelet");
        createEntities("necklace_entities").addPlayer().addSlots("necklace");
        createEntities("charm_entities").addPlayer().addSlots("charm");
    }
}
