package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.ConfigLoader;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechItemModels extends ItemModelProvider {

    public YTechItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        GeneralUtils.mapToStream(HOLDER.items()).forEach(this::registerItem);
        GeneralUtils.mapToStream(HOLDER.fluids()).forEach(this::registerItem);
    }

    private <T, U extends ConfigLoader.BaseObject<T>> void registerItem(Holder<T, U> holder) {
        ItemModelBuilder builder = getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        ResourceLocation baseTexture = Objects.requireNonNull(holder.materialHolder.model, "Base model texture required").base().texture();

        builder.texture("layer0", baseTexture);

        if (holder.materialHolder.model.overlay() != null) {
            builder.texture("layer1", holder.materialHolder.model.overlay().texture());
        }
    }
}
