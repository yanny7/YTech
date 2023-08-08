package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.IModel;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.TextureHolder;
import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DryingRack extends Block {
    private static final VoxelShape SHAPE_EAST_WEST = Shapes.box(0, 0, 7/16.0, 1, 1, 9/16.0);
    private static final VoxelShape SHAPE_NORTH_SOUTH = Shapes.box(7/16.0, 0, 0, 9/16.0, 1, 1);

    public DryingRack() {
        super(Properties.of());
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

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull CollisionContext collisionContext) {
        Direction direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);

        return (direction == Direction.EAST || direction == Direction.WEST) ? SHAPE_NORTH_SOUTH : SHAPE_EAST_WEST;
    }

    public static void registerModel(@NotNull Holder.BlockHolder holder, @NotNull BlockStateProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures(holder.material);
        ModelFile model = provider.models().getBuilder(holder.key)
                .parent(provider.models().getExistingFile(IModel.mcLoc("block/block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch (direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 0, 4, 16).texture("#all");
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 4, 4).texture("#all");
                    }
                }).from(0, 0, 7).to(2, 16, 9).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch (direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 0, 4, 16).texture("#all");
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 4, 4).texture("#all");
                    }
                }).from(14, 0, 7).to(16, 16, 9).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch (direction) {
                        case NORTH, SOUTH, UP, DOWN -> faceBuilder.uvs(0, 1, 16, 3).texture("#stick");
                        case EAST, WEST -> faceBuilder.uvs(0, 0, 1, 1).texture("#stick");
                    }
                }).from(2, 14, 7.5f).to(14, 15, 8.5f).end()
                .texture("particle", textures[0])
                .texture("all", textures[0])
                .texture("stick", textures[1]);
        provider.horizontalBlock(holder.block.get(), model);
        provider.itemModels().getBuilder(holder.key).parent(model);
    }

    public static TextureHolder[] getTexture(MaterialType material) {
        String woodName = material.key.substring(0, material.key.length() - 5);
        return List.of(new TextureHolder(-1, IModel.mcBlockLoc(woodName + "_log")),
                new TextureHolder(-1, IModel.mcBlockLoc(woodName + "_planks"))).toArray(TextureHolder[]::new);
    }
}
