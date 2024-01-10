package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.MaterialBlockType;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.TextureHolder;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.WaterWheelBlockEntity;
import com.yanny.ytech.network.kinetic.IKineticBlockEntity;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WaterWheelBlock extends KineticBlock {
    @NotNull private final MaterialType material;
    private final float stressMultiplier;

    public WaterWheelBlock(@NotNull MaterialType material, float stressMultiplier) {
        super(Properties.ofFullCopy(Blocks.STONE).noOcclusion());
        this.material = material;
        this.stressMultiplier = stressMultiplier;
    }

    @SuppressWarnings("deprecation")
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
            return new WaterWheelBlockEntity(holder.entityType.get(), pos, blockState, stressMultiplier);
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
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 4, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case EAST -> faceBuilder.uvs(1, 0, 3, 4).texture("#1");
                        case SOUTH -> faceBuilder.uvs(0, 0, 4, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case WEST -> faceBuilder.uvs(1, 0, 3, 4).texture("#1");
                        case UP -> faceBuilder.uvs(1, 0, 3, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case DOWN -> faceBuilder.uvs(1, 0, 3, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                    }
                })
                .from(0, 6, 7).to(16, 10, 9).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(1, 0, 3, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case EAST -> faceBuilder.uvs(0, 1, 4, 3).texture("#1");
                        case SOUTH -> faceBuilder.uvs(1, 0, 3, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case WEST -> faceBuilder.uvs(0, 1, 4, 3).texture("#1");
                        case UP -> faceBuilder.uvs(0, 0, 4, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case DOWN -> faceBuilder.uvs(0, 0, 4, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                    }
                })
                .from(0, 7, 6).to(16, 9, 10).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 7, 16, 8).texture("#3");
                        case EAST -> faceBuilder.uvs(10, 0, 11, 16).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 8, 16, 9).texture("#3");
                        case WEST -> faceBuilder.uvs(10, 0, 11, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(16, 16, 0, 0).texture("#3");
                        case DOWN -> faceBuilder.uvs(16, 0, 0, 16).texture("#3");
                    }
                })
                .from(1, 7.5f, -8).to(15, 8.5f, -1).rotation().angle(0).axis(Direction.Axis.Y).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 14, 7).texture("#3");
                        case EAST -> faceBuilder.uvs(3, 0, 4, 7).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 14, 7).texture("#3");
                        case WEST -> faceBuilder.uvs(3, 0, 4, 7).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 14, 1).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 14, 1).texture("#3");
                    }
                })
                .from(1, 4, -1.5f).to(15, 12, -0.5f).rotation().angle(22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case EAST -> faceBuilder.uvs(6, 12, 8, 14).texture("#1");
                        case SOUTH -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case WEST -> faceBuilder.uvs(6, 12, 8, 14).texture("#1");
                        case UP -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case DOWN -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                    }
                })
                .from(0.5f, 7, -2.5f).to(15.5f, 9, -0.5f).rotation().angle(0).axis(Direction.Axis.Y).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(11, 1, 12, 9).texture("#1");
                        case EAST -> faceBuilder.uvs(6, 7, 7, 15).texture("#1");
                        case SOUTH -> faceBuilder.uvs(11, 1, 12, 9).texture("#1");
                        case WEST -> faceBuilder.uvs(6, 7, 7, 15).texture("#1");
                        case UP -> faceBuilder.uvs(11, 1, 12, 2.2f).texture("#1");
                        case DOWN -> faceBuilder.uvs(11, 1, 12, 2.2f).texture("#1");
                    }
                })
                .from(0.75f, 4, -1.6f).to(1.75f, 12, -0.4f).rotation().angle(22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(6, 7, 7, 15).texture("#1");
                        case EAST -> faceBuilder.uvs(11, 1, 12.2f, 9).texture("#1");
                        case SOUTH -> faceBuilder.uvs(6, 7, 7, 15).texture("#1");
                        case WEST -> faceBuilder.uvs(11, 1, 12.2f, 9).texture("#1");
                        case UP -> faceBuilder.uvs(6, 7, 7, 8.2f).texture("#1");
                        case DOWN -> faceBuilder.uvs(6, 7, 7, 8.2f).texture("#1");
                    }
                })
                .from(14.25f, 4, -1.6f).to(15.25f, 12, -0.4f).rotation().angle(22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 1, 1).texture("#missing");
                        case EAST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 1, 1).texture("#missing");
                        case WEST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                    }
                })
                .from(1, 7.5f, -0.5f).to(2, 8.5f, 7).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 1, 1).texture("#missing");
                        case EAST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 1, 1).texture("#missing");
                        case WEST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                    }
                })
                .from(14, 7.5f, -0.5f).to(15, 8.5f, 7).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 7, 16, 8).texture("#3");
                        case EAST -> faceBuilder.uvs(10, 0, 11, 16).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 8, 16, 9).texture("#3");
                        case WEST -> faceBuilder.uvs(10, 0, 11, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(16, 16, 0, 0).texture("#3");
                        case DOWN -> faceBuilder.uvs(16, 0, 0, 16).texture("#3");
                    }
                })
                .from(1, 7.5f, -8).to(15, 8.5f, -1).rotation().angle(45).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 14, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(3, 0, 4, 7).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 14, 1).texture("#3");
                        case WEST -> faceBuilder.uvs(3, 0, 4, 7).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 14, 7).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 14, 7).texture("#3");
                    }
                })
                .from(1, 16.5f, 4).to(15, 17.5f, 12).rotation().angle(-22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case EAST -> faceBuilder.uvs(6, 12, 8, 14).texture("#1");
                        case SOUTH -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case WEST -> faceBuilder.uvs(6, 12, 8, 14).texture("#1");
                        case UP -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case DOWN -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                    }
                })
                .from(0.5f, 7, -2.5f).to(15.5f, 9, -0.5f).rotation().angle(45).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(11, 1, 12, 2.2f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case EAST -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case SOUTH -> faceBuilder.uvs(11, 1, 12, 2.2f).texture("#1");
                        case WEST -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case UP -> faceBuilder.uvs(11, 1, 12, 9).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case DOWN -> faceBuilder.uvs(11, 1, 12, 9).texture("#1");
                    }
                })
                .from(0.75f, 16.4f, 4).to(1.75f, 17.6f, 12).rotation().angle(-22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(6, 7, 7, 8.2f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case EAST -> faceBuilder.uvs(11, 1, 12.2f, 9).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case SOUTH -> faceBuilder.uvs(6, 7, 7, 8.2f).texture("#1");
                        case WEST -> faceBuilder.uvs(11, 1, 12.2f, 9).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case UP -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case DOWN -> faceBuilder.uvs(6, 7, 7, 15).texture("#1");
                    }
                })
                .from(14.25f, 16.4f, 4).to(15.25f, 17.6f, 12).rotation().angle(-22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 1, 1).texture("#missing");
                        case EAST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 1, 1).texture("#missing");
                        case WEST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                    }
                })
                .from(1, 7.5f, -0.5f).to(2, 8.5f, 7).rotation().angle(45).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 1, 1).texture("#missing");
                        case EAST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 1, 1).texture("#missing");
                        case WEST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                    }
                })
                .from(14, 7.5f, -0.5f).to(15, 8.5f, 7).rotation().angle(45).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(16, 0, 0, 16).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(10, 0, 11, 16).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case SOUTH -> faceBuilder.uvs(16, 16, 0, 0).texture("#3");
                        case WEST -> faceBuilder.uvs(10, 0, 11, 16).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case UP -> faceBuilder.uvs(0, 7, 16, 8).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 8, 16, 9).texture("#3");
                    }
                })
                .from(1, 17, 7.5f).to(15, 24, 8.5f).rotation().angle(0).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 14, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(3, 0, 4, 7).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 14, 1).texture("#3");
                        case WEST -> faceBuilder.uvs(3, 0, 4, 7).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 14, 7).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 14, 7).texture("#3");
                    }
                })
                .from(1, 16.5f, 4).to(15, 17.5f, 12).rotation().angle(22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case EAST -> faceBuilder.uvs(6, 12, 8, 14).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case SOUTH -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case WEST -> faceBuilder.uvs(6, 12, 8, 14).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case UP -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case DOWN -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                    }
                })
                .from(0.5f, 16.5f, 7).to(15.5f, 18.5f, 9).rotation().angle(0).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(11, 1, 12, 2.2f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case EAST -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case SOUTH -> faceBuilder.uvs(11, 1, 12, 2.2f).texture("#1");
                        case WEST -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case UP -> faceBuilder.uvs(11, 1, 12, 9).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case DOWN -> faceBuilder.uvs(11, 1, 12, 9).texture("#1");
                    }
                })
                .from(0.75f, 16.4f, 4).to(1.75f, 17.6f, 12).rotation().angle(22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(6, 7, 7, 8.2f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case EAST -> faceBuilder.uvs(11, 1, 12.2f, 9).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case SOUTH -> faceBuilder.uvs(6, 7, 7, 8.2f).texture("#1");
                        case WEST -> faceBuilder.uvs(11, 1, 12.2f, 9).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case UP -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case DOWN -> faceBuilder.uvs(6, 7, 7, 15).texture("#1");
                    }
                })
                .from(14.25f, 16.4f, 4).to(15.25f, 17.6f, 12).rotation().angle(22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case WEST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                        case DOWN -> faceBuilder.uvs(0, 0, 1, 1).texture("#missing");
                    }
                })
                .from(1, 9, 7.5f).to(2, 16.5f, 8.5f).rotation().angle(0).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case WEST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                        case DOWN -> faceBuilder.uvs(0, 0, 1, 1).texture("#missing");
                    }
                })
                .from(14, 9, 7.5f).to(15, 16.5f, 8.5f).rotation().angle(0).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 8, 16, 9).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(10, 0, 11, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 7, 16, 8).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case WEST -> faceBuilder.uvs(10, 0, 11, 16).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(16, 0, 0, 16).texture("#3");
                        case DOWN -> faceBuilder.uvs(16, 16, 0, 0).texture("#3");
                    }
                })
                .from(1, 7.5f, 17).to(15, 8.5f, 24).rotation().angle(-45).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 14, 7).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(3, 0, 4, 7).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 14, 7).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case WEST -> faceBuilder.uvs(3, 0, 4, 7).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 14, 1).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 14, 1).texture("#3");
                    }
                })
                .from(1, 4, 16.5f).to(15, 12, 17.5f).rotation().angle(-22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case EAST -> faceBuilder.uvs(6, 12, 8, 14).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case SOUTH -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case WEST -> faceBuilder.uvs(6, 12, 8, 14).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case UP -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case DOWN -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                    }
                })
                .from(0.5f, 7, 16.5f).to(15.5f, 9, 18.5f).rotation().angle(-45).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(11, 1, 12, 9).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case EAST -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case SOUTH -> faceBuilder.uvs(11, 1, 12, 9).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case WEST -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case UP -> faceBuilder.uvs(11, 1, 12, 2.2f).texture("#1");
                        case DOWN -> faceBuilder.uvs(11, 1, 12, 2.2f).texture("#1");
                    }
                })
                .from(0.75f, 4, 16.4f).to(1.75f, 12, 17.6f).rotation().angle(-22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case EAST -> faceBuilder.uvs(11, 1, 12.2f, 9).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case SOUTH -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case WEST -> faceBuilder.uvs(11, 1, 12.2f, 9).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case UP -> faceBuilder.uvs(6, 7, 7, 8.2f).texture("#1");
                        case DOWN -> faceBuilder.uvs(6, 7, 7, 8.2f).texture("#1");
                    }
                })
                .from(14.25f, 4, 16.4f).to(15.25f, 12, 17.6f).rotation().angle(-22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                        case EAST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                        case WEST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                    }
                })
                .from(1, 7.5f, 9).to(2, 8.5f, 16.5f).rotation().angle(-45).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                        case EAST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                        case WEST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                    }
                })
                .from(14, 7.5f, 9).to(15, 8.5f, 16.5f).rotation().angle(-45).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 8, 16, 9).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(10, 0, 11, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 7, 16, 8).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case WEST -> faceBuilder.uvs(10, 0, 11, 16).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(16, 0, 0, 16).texture("#3");
                        case DOWN -> faceBuilder.uvs(16, 16, 0, 0).texture("#3");
                    }
                })
                .from(1, 7.5f, 17).to(15, 8.5f, 24).rotation().angle(0).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 14, 7).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(3, 0, 4, 7).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 14, 7).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case WEST -> faceBuilder.uvs(3, 0, 4, 7).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 14, 1).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 14, 1).texture("#3");
                    }
                })
                .from(1, 4, 16.5f).to(15, 12, 17.5f).rotation().angle(22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case EAST -> faceBuilder.uvs(6, 12, 8, 14).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case SOUTH -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case WEST -> faceBuilder.uvs(6, 12, 8, 14).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case UP -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case DOWN -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                    }
                })
                .from(0.5f, 7, 16.5f).to(15.5f, 9, 18.5f).rotation().angle(0).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(11, 1, 12, 9).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case EAST -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case SOUTH -> faceBuilder.uvs(11, 1, 12, 9).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case WEST -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case UP -> faceBuilder.uvs(11, 1, 12, 2.2f).texture("#1");
                        case DOWN -> faceBuilder.uvs(11, 1, 12, 2.2f).texture("#1");
                    }
                })
                .from(0.75f, 4, 16.4f).to(1.75f, 12, 17.6f).rotation().angle(22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case EAST -> faceBuilder.uvs(11, 1, 12.2f, 9).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case SOUTH -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case WEST -> faceBuilder.uvs(11, 1, 12.2f, 9).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case UP -> faceBuilder.uvs(6, 7, 7, 8.2f).texture("#1");
                        case DOWN -> faceBuilder.uvs(6, 7, 7, 8.2f).texture("#1");
                    }
                })
                .from(14.25f, 4, 16.4f).to(15.25f, 12, 17.6f).rotation().angle(22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                        case EAST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                        case WEST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                    }
                })
                .from(1, 7.5f, 9).to(2, 8.5f, 16.5f).rotation().angle(0).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                        case EAST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                        case WEST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                    }
                })
                .from(14, 7.5f, 9).to(15, 8.5f, 16.5f).rotation().angle(0).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 8, 16, 9).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(10, 0, 11, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 7, 16, 8).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case WEST -> faceBuilder.uvs(10, 0, 11, 16).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(16, 0, 0, 16).texture("#3");
                        case DOWN -> faceBuilder.uvs(16, 16, 0, 0).texture("#3");
                    }
                })
                .from(1, 7.5f, 17).to(15, 8.5f, 24).rotation().angle(45).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 14, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(3, 0, 4, 7).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 14, 1).texture("#3");
                        case WEST -> faceBuilder.uvs(3, 0, 4, 7).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 14, 7).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 14, 7).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                    }
                })
                .from(1, -1.5f, 4).to(15, -0.5f, 12).rotation().angle(-22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case EAST -> faceBuilder.uvs(6, 12, 8, 14).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case SOUTH -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case WEST -> faceBuilder.uvs(6, 12, 8, 14).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case UP -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case DOWN -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                    }
                })
                .from(0.5f, 7, 16.5f).to(15.5f, 9, 18.5f).rotation().angle(45).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(11, 1, 12, 2.2f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case EAST -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case SOUTH -> faceBuilder.uvs(11, 1, 12, 2.2f).texture("#1");
                        case WEST -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case UP -> faceBuilder.uvs(11, 1, 12, 9).texture("#1");
                        case DOWN -> faceBuilder.uvs(11, 1, 12, 9).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                    }
                })
                .from(0.75f, -1.6f, 4).to(1.75f, -0.4f, 12).rotation().angle(-22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(6, 7, 7, 8.2f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case EAST -> faceBuilder.uvs(11, 1, 12.2f, 9).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case SOUTH -> faceBuilder.uvs(6, 7, 7, 8.2f).texture("#1");
                        case WEST -> faceBuilder.uvs(11, 1, 12.2f, 9).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case UP -> faceBuilder.uvs(6, 7, 7, 15).texture("#1");
                        case DOWN -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                    }
                })
                .from(14.25f, -1.6f, 4).to(15.25f, -0.4f, 12).rotation().angle(-22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                        case EAST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                        case WEST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                    }
                })
                .from(1, 7.5f, 9).to(2, 8.5f, 16.5f).rotation().angle(45).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                        case EAST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                        case WEST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                    }
                })
                .from(14, 7.5f, 9).to(15, 8.5f, 16.5f).rotation().angle(45).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(16, 16, 0, 0).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(10, 0, 11, 16).texture("#3");
                        case SOUTH -> faceBuilder.uvs(16, 0, 0, 16).texture("#3");
                        case WEST -> faceBuilder.uvs(10, 0, 11, 16).texture("#3");
                        case UP -> faceBuilder.uvs(0, 8, 16, 9).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 7, 16, 8).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                    }
                })
                .from(1, -8, 7.5f).to(15, -1, 8.5f).rotation().angle(0).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 14, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(3, 0, 4, 7).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 14, 1).texture("#3");
                        case WEST -> faceBuilder.uvs(3, 0, 4, 7).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 14, 7).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 14, 7).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                    }
                })
                .from(1, -1.5f, 4).to(15, -0.5f, 12).rotation().angle(22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case EAST -> faceBuilder.uvs(6, 12, 8, 14).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case SOUTH -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case WEST -> faceBuilder.uvs(6, 12, 8, 14).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case UP -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case DOWN -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                    }
                })
                .from(0.5f, -2.5f, 7).to(15.5f, -0.5f, 9).rotation().angle(0).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(11, 1, 12, 2.2f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case EAST -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case SOUTH -> faceBuilder.uvs(11, 1, 12, 2.2f).texture("#1");
                        case WEST -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case UP -> faceBuilder.uvs(11, 1, 12, 9).texture("#1");
                        case DOWN -> faceBuilder.uvs(11, 1, 12, 9).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                    }
                })
                .from(0.75f, -1.6f, 4).to(1.75f, -0.4f, 12).rotation().angle(22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(6, 7, 7, 8.2f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                        case EAST -> faceBuilder.uvs(11, 1, 12.2f, 9).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case SOUTH -> faceBuilder.uvs(6, 7, 7, 8.2f).texture("#1");
                        case WEST -> faceBuilder.uvs(11, 1, 12.2f, 9).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case UP -> faceBuilder.uvs(6, 7, 7, 15).texture("#1");
                        case DOWN -> faceBuilder.uvs(6, 7, 7, 15).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#1");
                    }
                })
                .from(14.25f, -1.6f, 4).to(15.25f, -0.4f, 12).rotation().angle(22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case WEST -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 1, 1).texture("#missing");
                        case DOWN -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                    }
                })
                .from(1, -0.5f, 7.5f).to(2, 7, 8.5f).rotation().angle(0).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case WEST -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 1, 1).texture("#missing");
                        case DOWN -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                    }
                })
                .from(14, -0.5f, 7.5f).to(15, 7, 8.5f).rotation().angle(0).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(16, 16, 0, 0).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(10, 0, 11, 16).texture("#3");
                        case SOUTH -> faceBuilder.uvs(16, 0, 0, 16).texture("#3");
                        case WEST -> faceBuilder.uvs(10, 0, 11, 16).texture("#3");
                        case UP -> faceBuilder.uvs(0, 8, 16, 9).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 7, 16, 8).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                    }
                })
                .from(1, -8, 7.5f).to(15, -1, 8.5f).rotation().angle(45).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 14, 7).texture("#3");
                        case EAST -> faceBuilder.uvs(3, 0, 4, 7).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 14, 7).texture("#3");
                        case WEST -> faceBuilder.uvs(3, 0, 4, 7).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 14, 1).texture("#3");
                        case DOWN -> faceBuilder.uvs(0, 0, 14, 1).texture("#3");
                    }
                })
                .from(1, 4, -1.5f).to(15, 12, -0.5f).rotation().angle(-22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case EAST -> faceBuilder.uvs(6, 12, 8, 14).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case SOUTH -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case WEST -> faceBuilder.uvs(6, 12, 8, 14).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                        case UP -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case DOWN -> faceBuilder.uvs(0, 0, 2, 15).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#1");
                    }
                })
                .from(0.5f, -2.5f, 7).to(15.5f, -0.5f, 9).rotation().angle(45).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(11, 1, 12, 9).texture("#1");
                        case EAST -> faceBuilder.uvs(6, 7, 7, 15).texture("#1");
                        case SOUTH -> faceBuilder.uvs(11, 1, 12, 9).texture("#1");
                        case WEST -> faceBuilder.uvs(6, 7, 7, 15).texture("#1");
                        case UP -> faceBuilder.uvs(11, 1, 12, 2.2f).texture("#1");
                        case DOWN -> faceBuilder.uvs(11, 1, 12, 2.2f).texture("#1");
                    }
                })
                .from(0.75f, 4, -1.6f).to(1.75f, 12, -0.4f).rotation().angle(-22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(6, 7, 7, 15).texture("#1");
                        case EAST -> faceBuilder.uvs(11, 1, 12.2f, 9).texture("#1");
                        case SOUTH -> faceBuilder.uvs(6, 7, 7, 15).texture("#1");
                        case WEST -> faceBuilder.uvs(11, 1, 12.2f, 9).texture("#1");
                        case UP -> faceBuilder.uvs(6, 7, 7, 8.2f).texture("#1");
                        case DOWN -> faceBuilder.uvs(6, 7, 7, 8.2f).texture("#1");
                    }
                })
                .from(14.25f, 4, -1.6f).to(15.25f, 12, -0.4f).rotation().angle(-22.5f).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case WEST -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 1, 1).texture("#missing");
                        case DOWN -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                    }
                })
                .from(1, -0.5f, 7.5f).to(2, 7, 8.5f).rotation().angle(45).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case EAST -> faceBuilder.uvs(0, 0, 1, 6.5f).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#3");
                        case SOUTH -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case WEST -> faceBuilder.uvs(0, 0, 1, 6.5f).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 1, 1).texture("#missing");
                        case DOWN -> faceBuilder.uvs(0, 0, 1, 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#missing");
                    }
                })
                .from(14, -0.5f, 7.5f).to(15, 7, 8.5f).rotation().angle(45).axis(Direction.Axis.X).origin(8, 8, 8).end()
                .end()
                .texture("particle", textures[0])
                .texture("1", textures[0])
                .texture("3", textures[1]);
        provider.getVariantBuilder(holder.block.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(modelFile).build());
        provider.itemModels().getBuilder(holder.key).parent(modelFile);
    }

    public static void registerRecipe(@NotNull Holder.BlockHolder holder, @NotNull RecipeOutput recipeConsumer) {
        //TODO
    }

    public static TextureHolder[] getTexture(MaterialType material) {
        return List.of(new TextureHolder(-1, -1, Utils.mcBlockLoc(material.key + "_log")),
                new TextureHolder(-1, -1, Utils.mcBlockLoc("stripped_" + material.key + "_log"))).toArray(TextureHolder[]::new);
    }
}
