package com.yanny.ytech.configuration.block;

import com.mojang.serialization.MapCodec;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.RemainingShapedRecipe;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.world.level.block.BedBlock.*;
import static net.minecraft.world.level.block.state.properties.BedPart.FOOT;
import static net.minecraft.world.level.block.state.properties.BedPart.HEAD;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.BED_PART;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class GrassBedBlock extends HorizontalDirectionalBlock {
    private static final MapCodec<GrassBedBlock> CODEC = simpleCodec((prop) -> new GrassBedBlock());
    private static final VoxelShape BASE = Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0);

    public GrassBedBlock() {
        super(Properties.of().sound(SoundType.WOOD).strength(0.2F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY));
    }

    @NotNull
    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean isBed(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @Nullable Entity player) {
        return true;
    }

    @NotNull
    @Override
    protected ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                                              @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide) {
            return ItemInteractionResult.CONSUME;
        } else {
            if (state.getValue(PART) != HEAD) {
                pos = pos.relative(state.getValue(FACING));
                state = level.getBlockState(pos);

                if (!state.is(this)) {
                    return ItemInteractionResult.CONSUME;
                }
            }

            if (!canSetSpawn(level)) {
                level.removeBlock(pos, false);
                BlockPos relative = pos.relative(state.getValue(FACING).getOpposite());

                if (level.getBlockState(relative).is(this)) {
                    level.removeBlock(relative, false);
                }

                Vec3 center = pos.getCenter();
                level.explode(null, level.damageSources().badRespawnPointExplosion(center), null, center, 5.0F, true, Level.ExplosionInteraction.BLOCK);
                return ItemInteractionResult.SUCCESS;
            } else if (state.getValue(OCCUPIED)) {
                if (!this.kickVillagerOutOfBed(level, pos)) {
                    player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
                }

                return ItemInteractionResult.SUCCESS;
            }  else {
                player.startSleepInBed(pos).ifLeft((sleepingProblem) -> {
                    if (sleepingProblem.getMessage() != null) {
                        player.displayClientMessage(sleepingProblem.getMessage(), true);
                    }
                });
                return ItemInteractionResult.SUCCESS;
            }
        }
    }

    private boolean kickVillagerOutOfBed(Level level, BlockPos pos) {
        List<Villager> villagers = level.getEntitiesOfClass(Villager.class, new AABB(pos), LivingEntity::isSleeping);

        if (villagers.isEmpty()) {
            return false;
        } else {
            villagers.getFirst().stopSleeping();
            return true;
        }
    }

    @NotNull
    @Override
    public BlockState updateShape(BlockState state, @NotNull Direction pFacing, @NotNull BlockState pFacingState, @NotNull LevelAccessor level, @NotNull BlockPos pCurrentPos, @NotNull BlockPos pFacingPos) {
        if (pFacing == getNeighbourDirection(state.getValue(PART), state.getValue(FACING))) {
            return pFacingState.is(this) && pFacingState.getValue(PART) != state.getValue(PART) ? state.setValue(OCCUPIED, pFacingState.getValue(OCCUPIED)) : Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, pFacing, pFacingState, level, pCurrentPos, pFacingPos);
        }
    }

    private static Direction getNeighbourDirection(BedPart pPart, Direction pDirection) {
        return pPart == FOOT ? pDirection : pDirection.getOpposite();
    }

    @NotNull
    @Override
    public BlockState playerWillDestroy(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        if (!level.isClientSide && player.isCreative()) {
            BedPart part = state.getValue(PART);

            if (part == BedPart.FOOT) {
                BlockPos blockPos = pos.relative(getNeighbourDirection(part, state.getValue(FACING)));
                BlockState blockState = level.getBlockState(blockPos);

                if (blockState.is(this) && blockState.getValue(PART) == BedPart.HEAD) {
                    level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_ALL);
                    level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(blockState));
                }
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.getHorizontalDirection();
        BlockPos clickedPos = pContext.getClickedPos();
        BlockPos relative = clickedPos.relative(direction);
        Level level = pContext.getLevel();
        return level.getBlockState(relative).canBeReplaced(pContext) && level.getWorldBorder().isWithinBounds(relative) ? this.defaultBlockState().setValue(FACING, direction).setValue(PART, FOOT).setValue(OCCUPIED, false) : null;
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext pContext) {
        return BASE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, PART, OCCUPIED);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity pPlacer, @NotNull ItemStack pStack) {
        super.setPlacedBy(level, pos, state, pPlacer, pStack);

        if (!level.isClientSide) {
            BlockPos relative = pos.relative(state.getValue(FACING));
            level.setBlock(relative, state.setValue(PART, HEAD), Block.UPDATE_ALL);
            level.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(level, pos, Block.UPDATE_ALL);
        }
    }

    @Override
    protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType type) {
        return false;
    }

    public static void registerModel(@NotNull BlockStateProvider provider) {
        String name = Utils.getPath(YTechBlocks.GRASS_BED);
        ModelFile model = provider.models().getBuilder(name)
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case EAST -> faceBuilder.uvs(9, 2, 11, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case SOUTH -> faceBuilder.uvs(1, 2, 3, 14).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case WEST -> faceBuilder.uvs(9, 0, 11, 14).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#1");
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 12, 14).texture("#1");
                    }
                })
                .from(2, 0, 0).to(14, 2, 14).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case UP, DOWN -> faceBuilder.uvs(0, 4, 16, 10).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#0");
                    }
                })
                .from(13, 0, 0).to(19, 0, 16).rotation().angle(22.5f).axis(Direction.Axis.Z).origin(8, 0, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case UP, DOWN -> faceBuilder.uvs(0, 4, 16, 10).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#0");
                    }
                })
                .from(-3, 0, -1).to(3, 0, 15).rotation().angle(-22.5f).axis(Direction.Axis.Z).origin(8, 0, 8).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case UP -> faceBuilder.uvs(0, 3, 16, 9).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#0");
                        case DOWN -> faceBuilder.uvs(0, 3, 16, 9).texture("#0");
                    }
                })
                .from(0, 0, 13).to(16, 0, 19).rotation().angle(-22.5f).axis(Direction.Axis.X).origin(8, 0, 8).end()
                .end()
                .renderType("minecraft:cutout")
                .texture("particle", Utils.modBlockLoc("thatch"))
                .texture("0", Utils.modBlockLoc("dried_grass"))
                .texture("1", Utils.modBlockLoc("thatch"));

        provider.itemModels().getBuilder(name).parent(provider.itemModels().getExistingFile(Utils.mcItemLoc("generated")))
                .texture("layer0", Utils.modItemLoc("grass_bed"));

        provider.getVariantBuilder(YTechBlocks.GRASS_BED.get())
                .partialState().with(HORIZONTAL_FACING, Direction.NORTH).with(BED_PART, FOOT).setModels(ConfiguredModel.builder().modelFile(model).build())
                .partialState().with(HORIZONTAL_FACING, Direction.EAST).with(BED_PART, FOOT).setModels(ConfiguredModel.builder().modelFile(model).rotationY(90).build())
                .partialState().with(HORIZONTAL_FACING, Direction.SOUTH).with(BED_PART, FOOT).setModels(ConfiguredModel.builder().modelFile(model).rotationY(180).build())
                .partialState().with(HORIZONTAL_FACING, Direction.WEST).with(BED_PART, FOOT).setModels(ConfiguredModel.builder().modelFile(model).rotationY(270).build())
                .partialState().with(HORIZONTAL_FACING, Direction.NORTH).with(BED_PART, HEAD).setModels(ConfiguredModel.builder().modelFile(model).rotationY(180).build())
                .partialState().with(HORIZONTAL_FACING, Direction.EAST).with(BED_PART, HEAD).setModels(ConfiguredModel.builder().modelFile(model).rotationY(270).build())
                .partialState().with(HORIZONTAL_FACING, Direction.SOUTH).with(BED_PART, HEAD).setModels(ConfiguredModel.builder().modelFile(model).build())
                .partialState().with(HORIZONTAL_FACING, Direction.WEST).with(BED_PART, HEAD).setModels(ConfiguredModel.builder().modelFile(model).rotationY(90).build());
    }

    public static void registerLootTable(@NotNull BlockLootSubProvider provider) {
        provider.add(YTechBlocks.GRASS_BED.get(), b -> provider.createSinglePropConditionTable(b, PART, HEAD));
    }

    public static void registerRecipe(RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, YTechBlocks.GRASS_BED.get())
                .define('G', YTechItemTags.GRASS_FIBERS)
                .pattern("GGG")
                .pattern("GGG")
                .pattern("GGG")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.GRASS_FIBERS))
                .save(recipeConsumer, Utils.modLoc(YTechBlocks.GRASS_BED));
    }
}
