package com.yanny.ytech.configuration.block;

import com.mojang.serialization.MapCodec;
import com.yanny.ytech.configuration.*;
import com.yanny.ytech.configuration.block_entity.BronzeAnvilBlockEntity;
import com.yanny.ytech.configuration.recipe.RemainingShapedRecipe;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class BronzeAnvilBlock extends FallingBlock implements EntityBlock {
    private static final VoxelShape BASE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
    private static final VoxelShape X_LEG1 = Block.box(3.0D, 4.0D, 4.0D, 13.0D, 5.0D, 12.0D);
    private static final VoxelShape X_LEG2 = Block.box(4.0D, 5.0D, 6.0D, 12.0D, 10.0D, 10.0D);
    private static final VoxelShape X_TOP = Block.box(0.0D, 10.0D, 3.0D, 16.0D, 16.0D, 13.0D);
    private static final VoxelShape Z_LEG1 = Block.box(4.0D, 4.0D, 3.0D, 12.0D, 5.0D, 13.0D);
    private static final VoxelShape Z_LEG2 = Block.box(6.0D, 5.0D, 4.0D, 10.0D, 10.0D, 12.0D);
    private static final VoxelShape Z_TOP = Block.box(3.0D, 10.0D, 0.0D, 13.0D, 16.0D, 16.0D);
    private static final VoxelShape X_AXIS_AABB = Shapes.or(BASE, X_LEG1, X_LEG2, X_TOP);
    private static final VoxelShape Z_AXIS_AABB = Shapes.or(BASE, Z_LEG1, Z_LEG2, Z_TOP);

    public BronzeAnvilBlock() {
        super(Properties.ofFullCopy(Blocks.ANVIL));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BronzeAnvilBlockEntity(pos, state);
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                 @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof BronzeAnvilBlockEntity anvil) {
            return anvil.onUse(state, level, pos, player, hand, hitResult);
        } else {
            throw new IllegalStateException("Invalid holder type!");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof BronzeAnvilBlockEntity anvil) {
                NonNullList<ItemStack> items = NonNullList.withSize(anvil.getItemStackHandler().getSlots(), ItemStack.EMPTY);

                for (int index = 0; index < anvil.getItemStackHandler().getSlots(); index++) {
                    items.set(index, anvil.getItemStackHandler().getStackInSlot(index));
                }

                Containers.dropContents(level, pos, items);
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @NotNull
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection().getClockWise());
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        return direction.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
    }

    @Override
    protected void falling(FallingBlockEntity blockEntity) {
        blockEntity.setHurtsEntities(2.0F, 40);
    }

    @Override
    public void onLand(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockState replaceableState,
                       @NotNull FallingBlockEntity fallingBlock) {
        if (!fallingBlock.isSilent()) {
            level.levelEvent(LevelEvent.SOUND_ANVIL_LAND, pos, 0);
        }
    }

    @Override
    public void onBrokenAfterFall(@NotNull Level level, @NotNull BlockPos pos, @NotNull FallingBlockEntity fallingBlock) {
        if (!fallingBlock.isSilent()) {
            level.levelEvent(LevelEvent.SOUND_ANVIL_BROKEN, pos, 0);
        }
    }

    @NotNull
    @Override
    public DamageSource getFallDamageSource(@NotNull Entity entity) {
        return entity.damageSources().anvil(entity);
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull PathComputationType type) {
        return false;
    }

    @Override
    @NotNull
    protected MapCodec<? extends BronzeAnvilBlock> codec() {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public int getDustColor(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return state.getMapColor(level, pos).col;
    }

    public static void registerModel(@NotNull BlockStateProvider provider) {
        ModelFile model = provider.models().getBuilder(Utils.getPath(YTechBlocks.BRONZE_ANVIL))
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(2, 12, 14, 16).texture("#1");
                        case UP, DOWN -> faceBuilder.uvs(2, 0, 14, 12).texture("#1");
                    }
                })
                .from(2, 0, 2).to(14, 4, 14).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(4, 3, 12, 4).texture("#1");
                        case EAST -> faceBuilder.uvs(4, 4, 12, 5).texture("#1");
                        case SOUTH -> faceBuilder.uvs(4, 1, 12, 2).texture("#1");
                        case WEST -> faceBuilder.uvs(4, 2, 12, 3).texture("#1");
                        case UP -> faceBuilder.uvs(4, 3, 12, 13).texture("#1");
                    }
                })
                .from(4, 4, 3).to(12, 5, 13).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(6, 6, 10, 11).texture("#1");
                        case EAST, WEST -> faceBuilder.uvs(4, 6, 12, 11).texture("#1");
                    }
                })
                .from(6, 5, 4).to(10, 10, 12).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(10, 0, 16, 10).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case EAST, WEST -> faceBuilder.uvs(0, 10, 16, 16).texture("#1");
                        case UP -> faceBuilder.uvs(3, 0, 13, 16).texture("#2");
                        case DOWN -> faceBuilder.uvs(0, 1, 16, 11).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                    }
                })
                .from(3, 10, 0).to(13, 16, 16).end()
                .texture("particle", Utils.modBlockLoc("bronze_anvil"))
                .texture("1", Utils.modBlockLoc("bronze_anvil"))
                .texture("2", Utils.modBlockLoc("bronze_anvil_top"));
        provider.horizontalBlock(YTechBlocks.BRONZE_ANVIL.get(), model);
        provider.itemModels().getBuilder(Utils.getPath(YTechBlocks.BRONZE_ANVIL)).parent(model);
    }

    public static void registerRecipe(@NotNull RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, YTechBlocks.BRONZE_ANVIL.get())
                .define('B', YTechItemTags.STORAGE_BLOCKS.get(MaterialType.BRONZE))
                .define('I', YTechItemTags.INGOTS.get(MaterialType.BRONZE))
                .pattern("BBB")
                .pattern(" I ")
                .pattern("III")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.INGOTS.get(MaterialType.BRONZE)))
                .save(recipeConsumer, Utils.modLoc(YTechBlocks.BRONZE_ANVIL));
    }
}
