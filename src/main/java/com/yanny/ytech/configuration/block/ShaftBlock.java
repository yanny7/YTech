package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.MaterialBlockType;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.TextureHolder;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.ShaftBlockEntity;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShaftBlock extends KineticBlock {
    private static final VoxelShape SHAPE_EAST_WEST = Shapes.box(0, 6/16.0, 6/16.0, 1, 10/16.0, 10/16.0);
    private static final VoxelShape SHAPE_NORTH_SOUTH = Shapes.box(6/16.0, 6/16.0, 0, 10/16.0, 10/16.0, 1);

    @NotNull private final MaterialType material;
    private final float stressMultiplier;

    public ShaftBlock(@NotNull MaterialType material, float stressMultiplier) {
        super(Properties.copy(Blocks.STONE));
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

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos) {
        return true;
    }

    @NotNull
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        Holder.BlockHolder blockHolder = Registration.HOLDER.blocks().get(MaterialBlockType.SHAFT).get(material);

        if (blockHolder instanceof Holder.EntityBlockHolder holder) {
            return new ShaftBlockEntity(holder.getBlockEntityType(), pos, blockState, stressMultiplier);
        } else {
            throw new IllegalStateException("Invalid holder type!");
        }
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
        ModelFile modelFile = provider.models().getBuilder(holder.key)
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch (direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(0, 0, 4, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#all");
                        case EAST, WEST -> faceBuilder.uvs(1, 0, 3, 4).cullface(direction).texture("#all");
                        case UP, DOWN -> faceBuilder.uvs(1, 0, 3, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#all");
                    }
                }).from(0, 6, 7).to(16, 10, 9).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch (direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(0, 0, 3, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#all");
                        case EAST, WEST -> faceBuilder.uvs(0, 1, 4, 3).cullface(direction).texture("#all");
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 4, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#all");
                    }
                }).from(0, 7, 6).to(16, 9, 10).end()
                .texture("particle", textures[0])
                .texture("all", textures[0]);
        provider.getVariantBuilder(holder.block.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(modelFile).build());
        provider.itemModels().getBuilder(holder.key).parent(modelFile);
    }

    public static void registerRecipe(@NotNull Holder.BlockHolder holder, @NotNull RecipeOutput recipeConsumer) {
        //TODO
    }

    public static TextureHolder[] getTexture(MaterialType material) {
        return List.of(new TextureHolder(-1, -1, Utils.mcBlockLoc(material.key + "_log"))).toArray(TextureHolder[]::new);
    }
}
