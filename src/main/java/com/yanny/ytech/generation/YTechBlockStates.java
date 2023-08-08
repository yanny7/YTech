package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.registration.KineticNetworkHolder;
import com.yanny.ytech.registration.MachineHolder;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.yanny.ytech.registration.Registration.HOLDER;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;
import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

class YTechBlockStates extends BlockStateProvider {
    public YTechBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, YTechMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        GeneralUtils.mapToStream(HOLDER.blocks()).forEach((m) -> m.object.registerModel(m, this));
        GeneralUtils.mapToStream(HOLDER.kineticNetwork()).forEach(this::registerKinetic);
        GeneralUtils.mapToStream(HOLDER.machine()).forEach(this::registerMachine);
        HOLDER.simpleBlocks().values().forEach((h) -> h.object.registerModel(h, this));
    }

    private void registerMachine(MachineHolder holder) {
        ResourceLocation casing = Utils.getBlockTexture("casing/" + holder.tier.key);
        ResourceLocation face = Utils.getBlockTexture("machine/" + holder.key);
        ResourceLocation facePowered = Utils.getBlockTexture("machine/" + holder.key + "_powered");
        BlockModelBuilder model = models().orientableWithBottom(holder.key, casing, face, casing, casing);
        BlockModelBuilder modelPowered = models().orientableWithBottom(holder.key + "_powered", casing, facePowered, casing, casing);

        model.element().allFaces((direction, faceBuilder) -> {
            switch (direction) {
                case UP -> faceBuilder.texture("#top").uvs(0, 0, 8, 16).cullface(direction);
                case DOWN -> faceBuilder.texture("#bottom").uvs(0, 0, 8, 16).cullface(direction);
                case SOUTH, WEST, EAST -> faceBuilder.texture("#side").uvs(8, 0, 16, 16).cullface(direction);
                case NORTH -> faceBuilder.texture("#front").cullface(direction);
            }
        });
        modelPowered.element().allFaces((direction, faceBuilder) -> {
            switch (direction) {
                case UP -> faceBuilder.texture("#top").uvs(0, 0, 8, 16).cullface(direction);
                case DOWN -> faceBuilder.texture("#bottom").uvs(0, 0, 8, 16).cullface(direction);
                case SOUTH, WEST, EAST -> faceBuilder.texture("#side").uvs(8, 0, 16, 16).cullface(direction);
                case NORTH -> faceBuilder.texture("#front").cullface(direction);
            }
        });
        getVariantBuilder(holder.block.get())
                .partialState().with(HORIZONTAL_FACING, Direction.NORTH).with(POWERED, false).setModels(ConfiguredModel.builder().modelFile(model).build())
                .partialState().with(HORIZONTAL_FACING, Direction.EAST).with(POWERED, false).setModels(ConfiguredModel.builder().modelFile(model).rotationY(90).build())
                .partialState().with(HORIZONTAL_FACING, Direction.SOUTH).with(POWERED, false).setModels(ConfiguredModel.builder().modelFile(model).rotationY(180).build())
                .partialState().with(HORIZONTAL_FACING, Direction.WEST).with(POWERED, false).setModels(ConfiguredModel.builder().modelFile(model).rotationY(270).build())
                .partialState().with(HORIZONTAL_FACING, Direction.NORTH).with(POWERED, true).setModels(ConfiguredModel.builder().modelFile(modelPowered).build())
                .partialState().with(HORIZONTAL_FACING, Direction.EAST).with(POWERED, true).setModels(ConfiguredModel.builder().modelFile(modelPowered).rotationY(90).build())
                .partialState().with(HORIZONTAL_FACING, Direction.SOUTH).with(POWERED, true).setModels(ConfiguredModel.builder().modelFile(modelPowered).rotationY(180).build())
                .partialState().with(HORIZONTAL_FACING, Direction.WEST).with(POWERED, true).setModels(ConfiguredModel.builder().modelFile(modelPowered).rotationY(270).build())
        ;
        // create item model from block
        itemModels().getBuilder(holder.key).parent(model);
    }

    private void registerKinetic(KineticNetworkHolder holder) {
        switch (holder.blockType) {
            case SHAFT -> registerShaft(holder, mcLoc("block/stripped_oak_log"));
            case WATER_WHEEL -> registerWaterWheel(holder, mcLoc("block/stripped_oak_log"));
        }
    }

    private void registerShaft(KineticNetworkHolder holder, ResourceLocation texture) {
        ModelFile modelFile = models().getBuilder(holder.key)
                .parent(models().getExistingFile(mcLoc(BLOCK_FOLDER + "/block")))
                .element().allFaces((direction, faceBuilder) -> faceBuilder.texture("#all")
                .cullface(direction)).from(0, 6, 6).to(16, 10, 10).end()
                .texture("particle", texture)
                .texture("all", texture);
        getVariantBuilder(holder.block.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(modelFile).build());
        itemModels().getBuilder(holder.key).parent(modelFile);
    }

    private void registerWaterWheel(KineticNetworkHolder holder, ResourceLocation texture) {
        ModelFile modelFile = models().getBuilder(holder.key)
                .parent(models().getExistingFile(mcLoc(BLOCK_FOLDER + "/block")))
                .element().allFaces(this::buildHorizontalPlank).from(0, 8, -8).to(16, 9, 8).end()
                .element().allFaces(this::buildVerticalPlank).from(1, 3.85786f, -2).to(15, 12.14214f, -1).end()
                .element().allFaces(this::buildHorizontalPlank).from(0, 8, -8).to(16, 9, 8).rotation().axis(Direction.Axis.X).origin(8, 8, 8).angle(45).end().end()
                .element().allFaces(this::buildVerticalPlank).from(1, 3.85786f, -2).to(15, 12.14214f, -1).rotation().axis(Direction.Axis.X).origin(8, 8, 8).angle(45).end().end()
                .texture("particle", texture)
                .texture("all", texture);
        MultiPartBlockStateBuilder builder = getMultipartBuilder(holder.block.get());

        builder.part().modelFile(modelFile).uvLock(false).addModel();
        builder.part().modelFile(modelFile).rotationX(90).uvLock(false).addModel();
        builder.part().modelFile(modelFile).rotationX(180).uvLock(false).addModel();
        builder.part().modelFile(modelFile).rotationX(270).uvLock(false).addModel();
        itemModels().getBuilder(holder.key).parent(modelFile);
    }

    private void buildHorizontalPlank(Direction direction, BlockModelBuilder.ElementBuilder.FaceBuilder faceBuilder) {
        (switch (direction) {
            case UP -> faceBuilder.uvs(16, 16, 0, 0);
            case DOWN -> faceBuilder.uvs(16, 0, 0, 16);
            case EAST -> faceBuilder.uvs(10, 0, 11, 16).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90);
            case WEST -> faceBuilder.uvs(10, 0, 11, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90);
            case NORTH -> faceBuilder.uvs(0, 7, 16, 8);
            case SOUTH -> faceBuilder.uvs(0, 8, 16, 9);
        }).cullface(direction).texture("#all");
    }

    private void buildVerticalPlank(Direction direction, BlockModelBuilder.ElementBuilder.FaceBuilder faceBuilder) {
        (switch (direction) {
            case UP, DOWN -> faceBuilder.uvs(0, 0, 14, 1);
            case EAST, WEST -> faceBuilder.uvs(3, 0, 4, 7);
            case NORTH, SOUTH -> faceBuilder.uvs(0, 0, 14, 7);
        }).cullface(direction).texture("#all");
    }
}
