package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.PottersWheelBlockEntity;
import com.yanny.ytech.configuration.recipe.RemainingShapedRecipe;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PottersWheelBlock extends Block implements EntityBlock {
    private static final VoxelShape SHAPE = Shapes.or(
            Shapes.box(2/16.0, 0, 2/16.0, 14/16.0, 3/16.0, 14/16.0),
            Shapes.box(7/16.0, 3/16.0, 7/16.0, 9/16.0, 11/16.0, 9/16.0),
            Shapes.box(4/16.0, 11/16.0, 4/16.0, 12/16.0, 14/16.0, 12/16.0)
    );

    public PottersWheelBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).mapColor(MapColor.TERRACOTTA_BROWN));
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull CollisionContext collisionContext) {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new PottersWheelBlockEntity(blockPos, blockState);
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                 @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof  PottersWheelBlockEntity pottersWheel) {
            return pottersWheel.onUse(state, level, pos, player, hand);
        } else {
            throw new IllegalStateException("Invalid holder type!");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof PottersWheelBlockEntity pottersWheel) {
                Containers.dropContents(level, pos, NonNullList.withSize(1, pottersWheel.getItem()));
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    public static void registerModel(@NotNull BlockStateProvider provider) {
        ModelFile model = provider.models().getBuilder(Utils.getPath(YTechBlocks.POTTERS_WHEEL))
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 11, 12, 14).texture("#0");
                        case EAST -> faceBuilder.uvs(1, 11, 13, 14).texture("#0");
                        case SOUTH -> faceBuilder.uvs(4, 11, 16, 14).texture("#0");
                        case WEST -> faceBuilder.uvs(2, 11, 14, 14).texture("#0");
                        case UP -> faceBuilder.uvs(2, 2, 14, 14).texture("#1");
                        case DOWN -> faceBuilder.uvs(0, 0, 12, 12).texture("#1");
                    }
                })
                .from(2, 0, 2).to(14, 3, 14).rotation().angle(0).axis(Direction.Axis.Y).origin(8, 0, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(3, 0, 11, 3).texture("#0");
                        case EAST -> faceBuilder.uvs(1, 0, 9, 3).texture("#0");
                        case SOUTH -> faceBuilder.uvs(4, 0, 12, 3).texture("#0");
                        case WEST -> faceBuilder.uvs(5, 0, 13, 3).texture("#0");
                        case UP -> faceBuilder.uvs(8, 3, 16, 11).texture("#0");
                        case DOWN -> faceBuilder.uvs(0, 1, 8, 9).texture("#1");
                    }
                })
                .from(4, 11, 4).to(12, 14, 12).rotation().angle(0).axis(Direction.Axis.Y).origin(8, 11, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 3, 2, 11).texture("#0");
                        case EAST -> faceBuilder.uvs(2, 3, 4, 11).texture("#0");
                        case SOUTH -> faceBuilder.uvs(4, 3, 6, 11).texture("#0");
                        case WEST -> faceBuilder.uvs(6, 3, 8, 11).texture("#0");
                    }
                })
                .from(7, 3, 7).to(9, 11, 9).rotation().angle(0).axis(Direction.Axis.Y).origin(7, 3, 8).end()
                .end()

                .texture("particle", Utils.mcBlockLoc("oak_planks"))
                .texture("0", Utils.modBlockLoc("potters_wheel"))
                .texture("1", Utils.mcBlockLoc("oak_planks"));
        provider.getVariantBuilder(YTechBlocks.POTTERS_WHEEL.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(Utils.getPath(YTechBlocks.POTTERS_WHEEL)).parent(model);
    }

    public static void registerRecipe(@NotNull RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, YTechBlocks.POTTERS_WHEEL.get())
                .define('W', ItemTags.LOGS)
                .define('S', ItemTags.WOODEN_SLABS)
                .define('A', YTechItemTags.AXES.tag)
                .define('B', YTechItemTags.SAWS.tag)
                .pattern("ASB")
                .pattern(" W ")
                .pattern("SSS")
                .unlockedBy("has_logs", RecipeProvider.has(ItemTags.LOGS))
                .save(recipeConsumer, Utils.modLoc(YTechBlocks.POTTERS_WHEEL));
    }
}
