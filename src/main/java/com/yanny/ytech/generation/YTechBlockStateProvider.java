package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block.*;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

class YTechBlockStateProvider extends BlockStateProvider {
    public YTechBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, YTechMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        AqueductBlock.registerModel(this);
        AqueductFertilizerBlock.registerModel(this);
        AqueductHydratorBlock.registerModel(this);
        AqueductValveBlock.registerModel(this);
        BrickChimneyBlock.registerModel(this);
        BronzeAnvilBlock.registerModel(this);
        FirePitBlock.registerModel(this);
        GrassBedBlock.registerModel(this);
        MillstoneBlock.registerModel(this);
        PrimitiveAlloySmelterBlock.registerModel(this);
        PrimitiveSmelterBlock.registerModel(this);
        reinforcedBricksBlockState(this);
        ReinforcedBrickChimneyBlock.registerModel(this);
        registerSimpleBlockState(this, YTechBlocks.TERRACOTTA_BRICKS);
        registerSlabBlockState(this, YTechBlocks.TERRACOTTA_BRICK_SLAB, Utils.getPath(YTechBlocks.TERRACOTTA_BRICKS));
        registerStairsBlockState(this, YTechBlocks.TERRACOTTA_BRICK_STAIRS, Utils.getPath(YTechBlocks.TERRACOTTA_BRICKS));
        registerSimpleBlockState(this, YTechBlocks.THATCH);
        registerSlabBlockState(this, YTechBlocks.THATCH_SLAB, Utils.getPath(YTechBlocks.THATCH));
        registerStairsBlockState(this, YTechBlocks.THATCH_STAIRS, Utils.getPath(YTechBlocks.THATCH));

        registerMaterialBlockState(this, "deepslate_ore", YTechBlocks.DEEPSLATE_ORES, MaterialType.VANILLA_METALS);
        YTechBlocks.DRYING_RACKS.entries().forEach((entry) -> DryingRackBlock.registerModel(this, entry.getValue(), entry.getKey()));
        registerMaterialBlockState(this, "gravel_deposit", YTechBlocks.GRAVEL_DEPOSITS);
        registerMaterialBlockState(this, "nether_ore", YTechBlocks.NETHER_ORES, EnumSet.of(MaterialType.GOLD));
        registerMaterialBlockState(this, "raw_storage", YTechBlocks.RAW_STORAGE_BLOCKS, MaterialType.VANILLA_METALS);
        registerMaterialBlockState(this, "sand_deposit", YTechBlocks.SAND_DEPOSITS);
        registerMaterialBlockState(this, "ore", YTechBlocks.STONE_ORES, MaterialType.VANILLA_METALS);
        registerMaterialBlockState(this, "storage", YTechBlocks.STORAGE_BLOCKS, MaterialType.VANILLA_METALS);
        YTechBlocks.TANNING_RACKS.entries().forEach((entry) -> TanningRackBlock.registerModel(this, entry.getValue(), entry.getKey()));
    }

    private static void reinforcedBricksBlockState(@NotNull BlockStateProvider provider) {
        String name = Utils.getPath(YTechBlocks.REINFORCED_BRICKS);
        BlockModelBuilder model = provider.models().cubeBottomTop(name, Utils.modBlockLoc("reinforced_bricks"), Utils.blockLoc(Blocks.BRICKS), Utils.blockLoc(Blocks.BRICKS));

        provider.getVariantBuilder(YTechBlocks.REINFORCED_BRICKS.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(name).parent(model);
    }

    private static void registerSimpleBlockState(@NotNull BlockStateProvider provider, DeferredBlock<Block> block) {
        String name = Utils.getPath(block);
        BlockModelBuilder model = provider.models().cubeAll(name, Utils.modBlockLoc(name));

        provider.getVariantBuilder(block.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(name).parent(model);
    }

    private static void registerSlabBlockState(BlockStateProvider provider, DeferredBlock<Block> block, String texture) {
        String name = Utils.getPath(block);
        ModelFile bottom = provider.models().slab(name, Utils.modBlockLoc(texture), Utils.modBlockLoc(texture), Utils.modBlockLoc(texture));
        ModelFile top = provider.models().slabTop(name + "_top", Utils.modBlockLoc(texture), Utils.modBlockLoc(texture), Utils.modBlockLoc(texture));
        ModelFile doubleSlab = provider.models().cubeAll(name + "_double", Utils.modBlockLoc(texture));

        provider.getVariantBuilder(block.get())
                .partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).addModels(new ConfiguredModel(bottom))
                .partialState().with(SlabBlock.TYPE, SlabType.TOP).addModels(new ConfiguredModel(top))
                .partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).addModels(new ConfiguredModel(doubleSlab));
        provider.itemModels().getBuilder(name).parent(bottom);
    }

    private static void registerStairsBlockState(BlockStateProvider provider, DeferredBlock<Block> block, String texture) {
        String name = Utils.getPath(block);
        ModelFile stairs = provider.models().stairs(name, Utils.modBlockLoc(texture), Utils.modBlockLoc(texture), Utils.modBlockLoc(texture));
        ModelFile stairsInner = provider.models().stairsInner(name + "_inner", Utils.modBlockLoc(texture), Utils.modBlockLoc(texture), Utils.modBlockLoc(texture));
        ModelFile stairsOuter = provider.models().stairsOuter(name + "_outer", Utils.modBlockLoc(texture), Utils.modBlockLoc(texture), Utils.modBlockLoc(texture));

        provider.stairsBlock((StairBlock)block.get(), stairs, stairsInner, stairsOuter);
        provider.itemModels().getBuilder(name).parent(stairs);
    }

    private static void registerMaterialBlockState(@NotNull BlockStateProvider provider, String group, YTechBlocks.MaterialBlock materialBlock) {
        materialBlock.entries().forEach((entry) -> {
            DeferredBlock<Block> block = entry.getValue();
            BlockModelBuilder model = provider.models().cubeAll(Utils.getPath(block), Utils.modBlockLoc(group + "/" + entry.getKey().key));

            model.element().allFaces((direction, faceBuilder) -> faceBuilder.texture("#all").cullface(direction).tintindex(0));
            provider.getVariantBuilder(block.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
            provider.itemModels().getBuilder(Utils.getPath(block)).parent(model);
        });
    }

    private static void registerMaterialBlockState(@NotNull BlockStateProvider provider, String group, YTechBlocks.MaterialBlock materialBlock, EnumSet<MaterialType> exclude) {
        materialBlock.entries().forEach((entry) -> {
            if (!exclude.contains(entry.getKey())) {
                DeferredBlock<Block> block = entry.getValue();
                BlockModelBuilder model = provider.models().cubeAll(Utils.getPath(block), Utils.modBlockLoc(group + "/" + entry.getKey().key));

                model.element().allFaces((direction, faceBuilder) -> faceBuilder.texture("#all").cullface(direction).tintindex(0));
                provider.getVariantBuilder(block.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
                provider.itemModels().getBuilder(Utils.getPath(block)).parent(model);
            }
        });
    }
}
