package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechItemModels extends ItemModelProvider {
    public YTechItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        GeneralUtils.mapToStream(HOLDER.items()).forEach((item) -> item.object.registerModel(item, this));
        GeneralUtils.mapToStream(HOLDER.fluids()).forEach((fluid) -> fluid.object.registerModel(fluid, this));
        HOLDER.simpleItems().values().forEach((item) -> item.object.registerModel(item, this));
    }
}
