package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.ObjectType;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechItemModels extends ItemModelProvider {

    public YTechItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        GeneralUtils.filteredStream(HOLDER.products(), h -> h.objectType == ObjectType.ITEM || h.objectType == ObjectType.FLUID).forEach(this::registerItem);
    }

    private void registerItem(Holder holder) {
        ItemModelBuilder builder = getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));

        builder.texture("layer0", holder.product.model().base().texture());

        if (holder.product.model().overlay() != null) {
            builder.texture("layer1", holder.product.model().overlay().texture());
        }
    }
}
