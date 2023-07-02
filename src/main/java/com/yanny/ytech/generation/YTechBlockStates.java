package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.registration.Registration;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

class YTechBlockStates extends BlockStateProvider {
    private static final ResourceLocation ORE_OVERLAY = Utils.getBlockTexture("ore_overlay");
    private static final ResourceLocation RAW_STORAGE_BLOCK = Utils.getBlockTexture("raw_storage_block");
    private static final ResourceLocation STORAGE_BLOCK = Utils.getBlockTexture("storage_block");

    public YTechBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, YTechMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        Registration.REGISTRATION_HOLDER.ore().forEach((material, stoneMap) -> stoneMap.forEach(this::registerOre));
        Registration.REGISTRATION_HOLDER.rawStorageBlock().forEach((material, registry) -> registerTintedCube(registry, RAW_STORAGE_BLOCK));
        Registration.REGISTRATION_HOLDER.storageBlock().forEach((material, registry) -> registerTintedCube(registry, STORAGE_BLOCK));
    }

    void registerOre(Block stone, @NotNull RegistryObject<Block> registry) {
        String path = registry.getId().getPath();
        BlockModelBuilder model = models().cubeAll(path, Utils.getBaseBlockTexture(stone));

        model.renderType(mcLoc("cutout"));
        model.texture("overlay", ORE_OVERLAY);
        model.element().allFaces(((direction, faceBuilder) -> faceBuilder.texture("#all").cullface(direction)));
        model.element().allFaces(((direction, faceBuilder) -> faceBuilder.texture("#overlay").tintindex(0).cullface(direction)));
        getVariantBuilder(registry.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        // create item model from block
        itemModels().getBuilder(path).parent(model);
    }

    void registerTintedCube(@NotNull RegistryObject<Block> registry, @NotNull ResourceLocation texture) {
        String path = registry.getId().getPath();
        BlockModelBuilder model = models().cubeAll(path, texture);

        model.element().allFaces((direction, faceBuilder) -> faceBuilder.texture("#all").tintindex(0).cullface(direction));
        getVariantBuilder(registry.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        // create item model from block
        itemModels().getBuilder(path).parent(model);
    }
}
