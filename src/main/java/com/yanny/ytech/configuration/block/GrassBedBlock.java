package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.SimpleItemType;
import com.yanny.ytech.configuration.TextureHolder;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.RemainingShapedRecipe;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.IBlockHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
    private static final VoxelShape BASE = Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0);

    public GrassBedBlock() {
        super(Properties.of().sound(SoundType.WOOD).strength(0.2F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY));
    }

    @Override
    public boolean isBed(BlockState state, BlockGetter level, BlockPos pos, @org.jetbrains.annotations.Nullable Entity player) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            if (pState.getValue(PART) != HEAD) {
                pPos = pPos.relative(pState.getValue(FACING));
                pState = pLevel.getBlockState(pPos);

                if (!pState.is(this)) {
                    return InteractionResult.CONSUME;
                }
            }

            if (!canSetSpawn(pLevel)) {
                pLevel.removeBlock(pPos, false);
                BlockPos pos = pPos.relative(pState.getValue(FACING).getOpposite());

                if (pLevel.getBlockState(pos).is(this)) {
                    pLevel.removeBlock(pos, false);
                }

                Vec3 center = pPos.getCenter();
                pLevel.explode(null, pLevel.damageSources().badRespawnPointExplosion(center), null, center, 5.0F, true, Level.ExplosionInteraction.BLOCK);
                return InteractionResult.SUCCESS;
            } else if (pState.getValue(OCCUPIED)) {
                if (!this.kickVillagerOutOfBed(pLevel, pPos)) {
                    pPlayer.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
                }

                return InteractionResult.SUCCESS;
            }  else {
                pPlayer.startSleepInBed(pPos).ifLeft((sleepingProblem) -> {
                    if (sleepingProblem.getMessage() != null) {
                        pPlayer.displayClientMessage(sleepingProblem.getMessage(), true);
                    }
                });
                return InteractionResult.SUCCESS;
            }
        }
    }

    private boolean kickVillagerOutOfBed(Level pLevel, BlockPos pPos) {
        List<Villager> villagers = pLevel.getEntitiesOfClass(Villager.class, new AABB(pPos), LivingEntity::isSleeping);

        if (villagers.isEmpty()) {
            return false;
        } else {
            villagers.get(0).stopSleeping();
            return true;
        }
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public BlockState updateShape(BlockState pState, @NotNull Direction pFacing, @NotNull BlockState pFacingState, @NotNull LevelAccessor pLevel, @NotNull BlockPos pCurrentPos, @NotNull BlockPos pFacingPos) {
        if (pFacing == getNeighbourDirection(pState.getValue(PART), pState.getValue(FACING))) {
            return pFacingState.is(this) && pFacingState.getValue(PART) != pState.getValue(PART) ? pState.setValue(OCCUPIED, pFacingState.getValue(OCCUPIED)) : Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
        }
    }

    private static Direction getNeighbourDirection(BedPart pPart, Direction pDirection) {
        return pPart == FOOT ? pDirection : pDirection.getOpposite();
    }

    public void playerWillDestroy(Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull Player pPlayer) {
        if (!pLevel.isClientSide && pPlayer.isCreative()) {
            BedPart part = pState.getValue(PART);

            if (part == BedPart.FOOT) {
                BlockPos blockPos = pPos.relative(getNeighbourDirection(part, pState.getValue(FACING)));
                BlockState blockState = pLevel.getBlockState(blockPos);

                if (blockState.is(this) && blockState.getValue(PART) == BedPart.HEAD) {
                    pLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_ALL);
                    pLevel.levelEvent(pPlayer, LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(blockState));
                }
            }
        }

        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.getHorizontalDirection();
        BlockPos clickedPos = pContext.getClickedPos();
        BlockPos relative = clickedPos.relative(direction);
        Level level = pContext.getLevel();
        return level.getBlockState(relative).canBeReplaced(pContext) && level.getWorldBorder().isWithinBounds(relative) ? this.defaultBlockState().setValue(FACING, direction).setValue(PART, FOOT).setValue(OCCUPIED, false) : null;
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return BASE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, PART, OCCUPIED);
    }

    @Override
    public void setPlacedBy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @Nullable LivingEntity pPlacer, @NotNull ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);

        if (!pLevel.isClientSide) {
            BlockPos pos = pPos.relative(pState.getValue(FACING));
            pLevel.setBlock(pos, pState.setValue(PART, HEAD), Block.UPDATE_ALL);
            pLevel.blockUpdated(pPos, Blocks.AIR);
            pState.updateNeighbourShapes(pLevel, pPos, Block.UPDATE_ALL);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull PathComputationType pType) {
        return false;
    }

    @NotNull
    public static TextureHolder[] getTexture() {
        return List.of(
                new TextureHolder(-1, -1, Utils.modBlockLoc("dried_grass")),
                new TextureHolder(-1, -1, Utils.modBlockLoc("thatch"))
        ).toArray(TextureHolder[]::new);
    }

    public static void registerModel(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockStateProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures();
        ModelFile model = provider.models().getBuilder(holder.key)
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
                .texture("particle", textures[1])
                .texture("0", textures[0])
                .texture("1", textures[1]);

        provider.itemModels().getBuilder(holder.key).parent(provider.itemModels().getExistingFile(Utils.mcItemLoc("generated")))
                .texture("layer0", Utils.modItemLoc("grass_bed"));

        provider.getVariantBuilder(holder.block.get())
                .partialState().with(HORIZONTAL_FACING, Direction.NORTH).with(BED_PART, FOOT).setModels(ConfiguredModel.builder().modelFile(model).build())
                .partialState().with(HORIZONTAL_FACING, Direction.EAST).with(BED_PART, FOOT).setModels(ConfiguredModel.builder().modelFile(model).rotationY(90).build())
                .partialState().with(HORIZONTAL_FACING, Direction.SOUTH).with(BED_PART, FOOT).setModels(ConfiguredModel.builder().modelFile(model).rotationY(180).build())
                .partialState().with(HORIZONTAL_FACING, Direction.WEST).with(BED_PART, FOOT).setModels(ConfiguredModel.builder().modelFile(model).rotationY(270).build())
                .partialState().with(HORIZONTAL_FACING, Direction.NORTH).with(BED_PART, HEAD).setModels(ConfiguredModel.builder().modelFile(model).rotationY(180).build())
                .partialState().with(HORIZONTAL_FACING, Direction.EAST).with(BED_PART, HEAD).setModels(ConfiguredModel.builder().modelFile(model).rotationY(270).build())
                .partialState().with(HORIZONTAL_FACING, Direction.SOUTH).with(BED_PART, HEAD).setModels(ConfiguredModel.builder().modelFile(model).build())
                .partialState().with(HORIZONTAL_FACING, Direction.WEST).with(BED_PART, HEAD).setModels(ConfiguredModel.builder().modelFile(model).rotationY(90).build());
    }

    public static void registerLootTable(@NotNull IBlockHolder holder, @NotNull BlockLootSubProvider provider) {
        Block block = holder.getBlockRegistry().get();
        provider.add(block, b -> provider.createSinglePropConditionTable(b, PART, HEAD));
    }

    public static void registerRecipe(Holder.SimpleBlockHolder holder, RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, holder.block.get())
                .define('G', SimpleItemType.GRASS_FIBERS.itemTag)
                .pattern("GGG")
                .pattern("GGG")
                .pattern("GGG")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(SimpleItemType.GRASS_FIBERS.itemTag))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }
}
