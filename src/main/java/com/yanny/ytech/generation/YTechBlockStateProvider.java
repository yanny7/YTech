package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block.*;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import static com.yanny.ytech.registration.Registration.HOLDER;

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
        registerSlabBlockState(this, YTechBlocks.TERRACOTTA_BRICK_SLAB, Utils.getId(YTechBlocks.TERRACOTTA_BRICKS));
        registerStairsBlockState(this, YTechBlocks.TERRACOTTA_BRICK_STAIRS, Utils.getId(YTechBlocks.TERRACOTTA_BRICKS));
        registerSimpleBlockState(this, YTechBlocks.THATCH);
        registerSlabBlockState(this, YTechBlocks.THATCH_SLAB, Utils.getId(YTechBlocks.THATCH));
        registerStairsBlockState(this, YTechBlocks.THATCH_STAIRS, Utils.getId(YTechBlocks.THATCH));

        GeneralUtils.mapToStream(HOLDER.blocks()).forEach((m) -> m.object.registerModel(m, this));
    }

    private static void reinforcedBricksBlockState(@NotNull BlockStateProvider provider) {
        String name = Utils.getId(YTechBlocks.REINFORCED_BRICKS);
        BlockModelBuilder model = provider.models().cubeBottomTop(name, Utils.modBlockLoc("reinforced_bricks"), Utils.blockLoc(Blocks.BRICKS), Utils.blockLoc(Blocks.BRICKS));

        provider.getVariantBuilder(YTechBlocks.REINFORCED_BRICKS.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(name).parent(model);
    }

    private static void registerSimpleBlockState(@NotNull BlockStateProvider provider, RegistryObject<Block> block) {
        String name = Utils.getId(block);
        BlockModelBuilder model = provider.models().cubeAll(name, Utils.modBlockLoc(name));

        provider.getVariantBuilder(block.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(name).parent(model);
    }

    private static void registerSlabBlockState(BlockStateProvider provider, RegistryObject<Block> block, String texture) {
        String name = Utils.getId(block);
        ModelFile bottom = provider.models().slab(name, Utils.modBlockLoc(texture), Utils.modBlockLoc(texture), Utils.modBlockLoc(texture));
        ModelFile top = provider.models().slabTop(name + "_top", Utils.modBlockLoc(texture), Utils.modBlockLoc(texture), Utils.modBlockLoc(texture));
        ModelFile doubleSlab = provider.models().cubeAll(name + "_double", Utils.modBlockLoc(texture));

        provider.getVariantBuilder(block.get())
                .partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).addModels(new ConfiguredModel(bottom))
                .partialState().with(SlabBlock.TYPE, SlabType.TOP).addModels(new ConfiguredModel(top))
                .partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).addModels(new ConfiguredModel(doubleSlab));
        provider.itemModels().getBuilder(name).parent(bottom);
    }

    private static void registerStairsBlockState(BlockStateProvider provider, RegistryObject<Block> block, String texture) {
        String name = Utils.getId(block);
        ModelFile stairs = provider.models().stairs(name, Utils.modBlockLoc(texture), Utils.modBlockLoc(texture), Utils.modBlockLoc(texture));
        ModelFile stairsInner = provider.models().stairsInner(name + "_inner", Utils.modBlockLoc(texture), Utils.modBlockLoc(texture), Utils.modBlockLoc(texture));
        ModelFile stairsOuter = provider.models().stairsOuter(name + "_outer", Utils.modBlockLoc(texture), Utils.modBlockLoc(texture), Utils.modBlockLoc(texture));

        provider.stairsBlock((StairBlock)block.get(), stairs, stairsInner, stairsOuter);
        provider.itemModels().getBuilder(name).parent(stairs);
    }
}
