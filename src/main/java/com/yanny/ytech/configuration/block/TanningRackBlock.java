package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.TanningRackBlockEntity;
import com.yanny.ytech.configuration.recipe.WorkspaceCraftingRecipe;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TanningRackBlock extends Block implements EntityBlock {
    private static final VoxelShape SHAPE_EAST_WEST = Shapes.box(0, 0, 7/16.0, 1, 1, 9/16.0);
    private static final VoxelShape SHAPE_NORTH_SOUTH = Shapes.box(7/16.0, 0, 0, 9/16.0, 1, 1);

    public TanningRackBlock() {
        super(Properties.ofFullCopy(Blocks.OAK_PLANKS));
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

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TanningRackBlockEntity(pos, state);
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                 @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof TanningRackBlockEntity tanningRack) {
            return tanningRack.onUse(state, level, pos, player, hand, hitResult);
        } else {
            throw new IllegalStateException("Invalid holder type!");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof TanningRackBlockEntity tanningRack) {
                Containers.dropContents(level, pos, NonNullList.withSize(1, tanningRack.getItem()));
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    public static void registerModel(@NotNull BlockStateProvider provider, @NotNull DeferredBlock<Block> block, MaterialType material) {
        ModelFile model = provider.models().getBuilder(Utils.getPath(block))
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(2, 2, 14, 4).texture("#2");
                    }
                })
                .from(2, 13, 8).to(14, 15, 8).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(2, 2, 14, 4).texture("#2");
                    }
                })
                .from(2, 1, 8).to(14, 3, 8).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 2, 16).texture("#3");
                        case EAST -> faceBuilder.uvs(2, 0, 4, 16).texture("#3");
                        case SOUTH -> faceBuilder.uvs(4, 0, 6, 16).texture("#3");
                        case WEST -> faceBuilder.uvs(6, 0, 8, 16).texture("#3").cullface(direction);
                        case UP -> faceBuilder.uvs(0, 0, 2, 2).texture("#3").cullface(direction);
                        case DOWN -> faceBuilder.uvs(0, 2, 2, 4).texture("#3").cullface(direction);
                    }
                })
                .from(0, 0, 7).to(2, 16, 9).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(8, 0, 10, 16).texture("#3");
                        case EAST -> faceBuilder.uvs(10, 0, 12, 16).texture("#3").cullface(direction);
                        case SOUTH -> faceBuilder.uvs(12, 0, 14, 16).texture("#3");
                        case WEST -> faceBuilder.uvs(14, 0, 16, 16).texture("#3");
                        case UP -> faceBuilder.uvs(8, 0, 10, 2).texture("#3").cullface(direction);
                        case DOWN -> faceBuilder.uvs(8, 2, 10, 4).texture("#3").cullface(direction);
                    }
                })
                .from(14, 0, 7).to(16, 16, 9).end()
                .texture("particle", Utils.modBlockLoc("wood/dark_bottom_" + material.key + "_log"))
                .texture("3", Utils.modBlockLoc("wood/dark_bottom_" + material.key + "_log"))
                .texture("2", Utils.modBlockLoc("horizontal_rope"));
        provider.horizontalBlock(block.get(), model);
        provider.itemModels().getBuilder(Utils.getPath(block)).parent(model);
    }

    public static void registerRecipe(@NotNull RecipeOutput recipeConsumer, @NotNull DeferredItem<Item> item, MaterialType material) {
        WorkspaceCraftingRecipe.Builder.recipe(item.get())
                .define('W', Utils.getLogFromMaterial(material))
                .define('T', YTechItemTags.GRASS_TWINES)
                .bottomPattern("   ")
                .bottomPattern("WTW")
                .bottomPattern("   ")
                .middlePattern("   ")
                .middlePattern("W W")
                .middlePattern("   ")
                .topPattern("   ")
                .topPattern("WTW")
                .topPattern("   ")
                .group(YTechBlocks.TANNING_RACKS.getGroup() + "_" + material.group)
                .unlockedBy("has_logs", RecipeProvider.has(ItemTags.LOGS))
                .save(recipeConsumer, Utils.modLoc(item));
    }
}
