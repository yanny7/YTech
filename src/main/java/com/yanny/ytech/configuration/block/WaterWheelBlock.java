package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.IModel;
import com.yanny.ytech.configuration.MaterialBlockType;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.TextureHolder;
import com.yanny.ytech.network.kinetic.block.KineticBlock;
import com.yanny.ytech.network.kinetic.block_entity.WaterWheelBlockEntity;
import com.yanny.ytech.network.kinetic.common.IKineticBlockEntity;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class WaterWheelBlock extends KineticBlock {
    @NotNull private final MaterialType material;
    private final float stressMultiplier;

    public WaterWheelBlock(@NotNull MaterialType material, float stressMultiplier) {
        super(Properties.copy(Blocks.STONE).noOcclusion());
        this.material = material;
        this.stressMultiplier = stressMultiplier;
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @NotNull
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    @NotNull
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        Holder.BlockHolder blockHolder = Registration.HOLDER.blocks().get(MaterialBlockType.WATER_WHEEL).get(material);

        if (blockHolder instanceof Holder.EntityBlockHolder holder) {
            BlockEntityType<? extends BlockEntity> blockEntityType = holder.entityType.get();
            return new WaterWheelBlockEntity(blockEntityType, pos, blockState, stressMultiplier);
        } else {
            throw new IllegalStateException("Invalid holder type!");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block oldBlock, @NotNull BlockPos neighbour, boolean b) {
        super.neighborChanged(state, level, pos, oldBlock, neighbour, b);

        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof IKineticBlockEntity kineticBlockEntity) {
                Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

                if (pos.above().equals(neighbour) || pos.below().equals(neighbour) || pos.relative(direction).equals(neighbour) || pos.relative(direction.getOpposite()).equals(neighbour)) {
                    kineticBlockEntity.onChangedState(state, state);
                }
            }
        }
    }

    public static void registerModel(@NotNull Holder.BlockHolder holder, @NotNull BlockStateProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures(holder.material);
        ModelFile modelFile = provider.models().getBuilder(holder.key)
                .parent(provider.models().getExistingFile(IModel.mcBlockLoc("block")))
                .element().allFaces(WaterWheelBlock::buildHorizontalPlank).from(0, 8, -8).to(16, 9, 8).end()
                .element().allFaces(WaterWheelBlock::buildVerticalPlank).from(1, 3.85786f, -2).to(15, 12.14214f, -1).end()
                .element().allFaces(WaterWheelBlock::buildHorizontalPlank).from(0, 8, -8).to(16, 9, 8).rotation().axis(Direction.Axis.X).origin(8, 8, 8).angle(45).end().end()
                .element().allFaces(WaterWheelBlock::buildVerticalPlank).from(1, 3.85786f, -2).to(15, 12.14214f, -1).rotation().axis(Direction.Axis.X).origin(8, 8, 8).angle(45).end().end()
                .texture("particle", textures[0])
                .texture("all", textures[0]);
        MultiPartBlockStateBuilder builder = provider.getMultipartBuilder(holder.block.get());

        builder.part().modelFile(modelFile).uvLock(false).addModel();
        builder.part().modelFile(modelFile).rotationX(90).uvLock(false).addModel();
        builder.part().modelFile(modelFile).rotationX(180).uvLock(false).addModel();
        builder.part().modelFile(modelFile).rotationX(270).uvLock(false).addModel();
        provider.itemModels().getBuilder(holder.key).parent(modelFile);
    }

    public static void registerRecipe(@NotNull Holder.BlockHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        //TODO
    }

    public static TextureHolder[] getTexture(MaterialType material) {
        return List.of(new TextureHolder(-1, IModel.mcBlockLoc("stripped_" + material.key + "_log"))).toArray(TextureHolder[]::new);
    }

    private static void buildHorizontalPlank(Direction direction, BlockModelBuilder.ElementBuilder.FaceBuilder faceBuilder) {
        (switch (direction) {
            case UP -> faceBuilder.uvs(16, 16, 0, 0);
            case DOWN -> faceBuilder.uvs(16, 0, 0, 16);
            case EAST -> faceBuilder.uvs(10, 0, 11, 16).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90);
            case WEST -> faceBuilder.uvs(10, 0, 11, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90);
            case NORTH -> faceBuilder.uvs(0, 7, 16, 8);
            case SOUTH -> faceBuilder.uvs(0, 8, 16, 9);
        }).cullface(direction).texture("#all");
    }

    private static void buildVerticalPlank(Direction direction, BlockModelBuilder.ElementBuilder.FaceBuilder faceBuilder) {
        (switch (direction) {
            case UP, DOWN -> faceBuilder.uvs(0, 0, 14, 1);
            case EAST, WEST -> faceBuilder.uvs(3, 0, 4, 7);
            case NORTH, SOUTH -> faceBuilder.uvs(0, 0, 14, 7);
        }).cullface(direction).texture("#all");
    }
}
