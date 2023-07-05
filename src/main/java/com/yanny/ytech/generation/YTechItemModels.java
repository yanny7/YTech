package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.registration.Registration;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

class YTechItemModels extends ItemModelProvider {
    private static final ResourceLocation RAW_METAL = Utils.getItemTexture("raw_metal");
    private static final ResourceLocation INGOT = Utils.getItemTexture("ingot");
    private static final ResourceLocation DUST = Utils.getItemTexture("dust");
    private static final ResourceLocation BUCKET = Utils.getBaseItemTexture(Items.BUCKET);
    private static final ResourceLocation BUCKET_OVERLAY = Utils.getItemTexture("bucket_overlay");

    public YTechItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Registration.REGISTRATION_HOLDER.rawMaterial().forEach((material, registry) -> registerTintedItem(registry, RAW_METAL));
        Registration.REGISTRATION_HOLDER.ingot().forEach((material, registry) -> registerTintedItem(registry, INGOT));
        Registration.REGISTRATION_HOLDER.dust().forEach((material, registry) -> registerTintedItem(registry, DUST));
        Registration.REGISTRATION_HOLDER.fluid().forEach(((material, holder) -> registerBucketItem(holder.bucket())));
    }

    private void registerTintedItem(RegistryObject<Item> registry, ResourceLocation texture) {
        String path = registry.getId().getPath();
        getBuilder(path).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", texture);
    }

    private void registerBucketItem(RegistryObject<Item> registry) {
        String path = registry.getId().getPath();
        getBuilder(path).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", BUCKET).texture("layer1", BUCKET_OVERLAY);
    }
}
