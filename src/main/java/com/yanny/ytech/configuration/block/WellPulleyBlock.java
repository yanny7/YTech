package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.WellPulleyBlockEntity;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public class WellPulleyBlock extends IrrigationBlock {
    public static final EnumProperty<WellPulleyPart> WELL_PART = EnumProperty.create("well", WellPulleyPart.class);
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVATED = AqueductConsumerBlock.ACTIVATED;

    private static final VoxelShape WELL_SHAPE_Z = Shapes.box(0, 0, 6/16.0, 1, 1, 10/16.0);
    private static final VoxelShape WELL_SHAPE_X = Shapes.box(6/16.0, 0, 0, 10/16.0, 1, 1);

    public WellPulleyBlock() {
        super(Properties.ofFullCopy(Blocks.TERRACOTTA).pushReaction(PushReaction.DESTROY));
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        if (pState.getValue(WELL_PART) == WellPulleyPart.BASE) {
            return Shapes.block();
        } else {
            if (pState.getValue(HORIZONTAL_FACING).getAxis() == Direction.Axis.X) {
                return WELL_SHAPE_X;
            } else {
                return WELL_SHAPE_Z;
            }
        }
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        BlockPos basePos = pos;
        BlockState aboveState = state;

        if (state.getValue(WELL_PART) == WellPulleyPart.TOP) {
            basePos = basePos.below();
        }

        if (aboveState.getValue(WELL_PART) == WellPulleyPart.BASE) {
            aboveState = level.getBlockState(basePos.above());
        }

        if (level.getBlockEntity(basePos) instanceof WellPulleyBlockEntity blockEntity) {
            return blockEntity.onUse(level, aboveState, player);
        } else {
            throw new IllegalStateException("Invalid holder type!");
        }
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public List<BlockPos> getValidNeighbors(@NotNull BlockState blockState, @NotNull BlockPos pos) {
        return Direction.Plane.HORIZONTAL.stream().map((dir) -> pos.offset(dir.getNormal())).toList();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        if (blockState.getValue(WELL_PART) == WellPulleyPart.BASE) {
            return new WellPulleyBlockEntity(blockPos, blockState);
        }

        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        if (!level.isClientSide) {
            return (level1, pos, state1, blockEntity) -> createAqueductConsumerTicker(level1, blockEntity);
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.getHorizontalDirection();
        BlockPos clickedPos = pContext.getClickedPos();
        BlockPos relative = clickedPos.relative(Direction.UP);
        Level level = pContext.getLevel();
        BlockPos below = clickedPos.below();
        BlockState blockBelow =  level.getBlockState(below);


        if (level.getBlockState(relative).canBeReplaced(pContext) && level.getWorldBorder().isWithinBounds(relative) && blockBelow.isCollisionShapeFullBlock(level, below)) {
            boolean hasNorthConnection = isValidForConnection(level, clickedPos, Direction.NORTH);
            boolean hasEastConnection = isValidForConnection(level, clickedPos, Direction.EAST);
            boolean hasSouthConnection = isValidForConnection(level, clickedPos, Direction.SOUTH);
            boolean hasWestConnection = isValidForConnection(level, clickedPos, Direction.WEST);
            BlockState state = defaultBlockState();

            state = state.setValue(NORTH, hasNorthConnection);
            state = state.setValue(EAST, hasEastConnection);
            state = state.setValue(SOUTH, hasSouthConnection);
            state = state.setValue(WEST, hasWestConnection);

            return state.setValue(WELL_PART, WellPulleyPart.BASE).setValue(HORIZONTAL_FACING, direction.getOpposite()).setValue(ACTIVATED, false);
        }

        return null;
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState,
                                  @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (state.getValue(WELL_PART) == WellPulleyPart.BASE) {
            if ((pos.above().equals(neighborPos) && !neighborState.is(YTechBlocks.WELL_PULLEY.get()))
                    || (pos.below().equals(neighborPos) && !neighborState.isCollisionShapeFullBlock(level, neighborPos))) {
                return Blocks.AIR.defaultBlockState();
            }
        }

        if (state.getValue(WELL_PART) == WellPulleyPart.TOP && pos.below().equals(neighborPos) && !neighborState.is(YTechBlocks.WELL_PULLEY.get())) {
            return Blocks.AIR.defaultBlockState();
        }

        boolean hasNorthConnection = isValidForConnection(level, pos, Direction.NORTH);
        boolean hasEastConnection = isValidForConnection(level, pos, Direction.EAST);
        boolean hasSouthConnection = isValidForConnection(level, pos, Direction.SOUTH);
        boolean hasWestConnection = isValidForConnection(level, pos, Direction.WEST);

        state = state.setValue(NORTH, hasNorthConnection);
        state = state.setValue(EAST, hasEastConnection);
        state = state.setValue(SOUTH, hasSouthConnection);
        state = state.setValue(WEST, hasWestConnection);

        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(WELL_PART, HORIZONTAL_FACING, ACTIVATED, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public void setPlacedBy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @javax.annotation.Nullable LivingEntity pPlacer, @NotNull ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);

        if (!pLevel.isClientSide) {
            pLevel.setBlock(pPos.above(), pState.setValue(WELL_PART, WellPulleyPart.TOP), Block.UPDATE_ALL);
            pLevel.blockUpdated(pPos, Blocks.AIR);
            pState.updateNeighbourShapes(pLevel, pPos, Block.UPDATE_ALL);
        }
    }

    @NotNull
    @Override
    public BlockState playerWillDestroy(Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull Player pPlayer) {
        if (!pLevel.isClientSide && pPlayer.isCreative()) {
            WellPulleyPart part = pState.getValue(WELL_PART);

            if (part == WellPulleyPart.BASE) {
                BlockPos blockPos = pPos.above();
                BlockState blockState = pLevel.getBlockState(blockPos);

                if (blockState.is(this) && blockState.getValue(WELL_PART) == WellPulleyPart.TOP) {
                    pLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_ALL);
                    pLevel.levelEvent(pPlayer, LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(blockState));
                }
            } else {
                BlockPos blockPos = pPos.below();
                BlockState blockState = pLevel.getBlockState(blockPos);

                if (blockState.is(this) && blockState.getValue(WELL_PART) == WellPulleyPart.BASE) {
                    pLevel.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                    pLevel.levelEvent(pPlayer, LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(blockState));
                }
            }
        }

        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
        return pState;
    }

    public static void registerModel(@NotNull BlockStateProvider provider) {
        ModelFile base = provider.models().cubeColumnHorizontal("well_pulley_base",
                Utils.modBlockLoc("terracotta_bricks"),
                Utils.modBlockLoc("aqueduct/aqueduct_well")
        );
        ModelFile overlay = provider.models().getBuilder(Utils.getPath(YTechBlocks.WELL_PULLEY) + "_overlay")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    if (Objects.requireNonNull(direction) == Direction.NORTH) {
                        faceBuilder.uvs(0, 0, 16, 16).texture("#1");
                    } else {
                        faceBuilder.uvs(0, 0, 16, 16).texture("#3");
                    }
                })
                .from(0, 0, 0).to(16, 16, 16).end()
                .renderType("minecraft:cutout")
                .texture("1", Utils.modBlockLoc("aqueduct/aqueduct_valve"))
                .texture("3", Utils.modBlockLoc("invisible"));
        ModelFile top = getTopModel(provider, false);
        ModelFile topActivated = getTopModel(provider, true);
        MultiPartBlockStateBuilder builder = provider.getMultipartBuilder(YTechBlocks.WELL_PULLEY.get());
        builder.part().modelFile(base).addModel().condition(WELL_PART, WellPulleyPart.BASE).end();
        builder.part().modelFile(top).addModel().condition(WELL_PART, WellPulleyPart.TOP).condition(HORIZONTAL_FACING, Direction.NORTH, Direction.SOUTH).condition(ACTIVATED, false).end();
        builder.part().modelFile(top).rotationY(90).addModel().condition(WELL_PART, WellPulleyPart.TOP).condition(HORIZONTAL_FACING, Direction.EAST, Direction.WEST).condition(ACTIVATED, false).end();
        builder.part().modelFile(topActivated).addModel().condition(WELL_PART, WellPulleyPart.TOP).condition(HORIZONTAL_FACING, Direction.NORTH, Direction.SOUTH).condition(ACTIVATED, true).end();
        builder.part().modelFile(topActivated).rotationY(90).addModel().condition(WELL_PART, WellPulleyPart.TOP).condition(HORIZONTAL_FACING, Direction.EAST, Direction.WEST).condition(ACTIVATED, true).end();
        PROPERTY_BY_DIRECTION.forEach((dir, value) -> builder.part().modelFile(overlay).rotationY(ANGLE_BY_DIRECTION.get(dir)).addModel().condition(value, true).end());

        provider.itemModels().getBuilder(Utils.getPath(YTechBlocks.WELL_PULLEY)).parent(top);
    }

    protected static void createAqueductConsumerTicker(@NotNull Level level, @NotNull BlockEntity blockEntity) {
        if (blockEntity instanceof WellPulleyBlockEntity wellPulleyBlockEntity) {
            wellPulleyBlockEntity.tick((ServerLevel) level);
        }
    }

    private static ModelFile getTopModel(@NotNull BlockStateProvider provider, boolean activated) {
        return provider.models().getBuilder(Utils.getPath(YTechBlocks.WELL_PULLEY) + (activated ? "_activated" : ""))
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 0, 4, 16).texture("#0");
                        case UP -> faceBuilder.uvs(6, 6, 10, 10).texture("#1");
                    }
                })
                .from(0, 0, 6).to(4, 16, 10)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 0, 4, 16).texture("#0");
                        case UP -> faceBuilder.uvs(6, 6, 10, 10).texture("#1");
                    }
                })
                .from(12, 0, 6).to(16, 16, 10)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH, UP, DOWN -> faceBuilder.uvs(0, 0, 3, 2).texture("#2");
                    }
                })
                .from(4, 13, 7).to(7, 15, 9)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH, UP, DOWN -> faceBuilder.uvs(0, 0, 3, 2).texture("#2");
                    }
                })
                .from(9, 13, 7).to(12, 15, 9)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 1, 2, 16).texture("#3");
                        case UP -> faceBuilder.uvs(2, 0, 4, 2).texture("#3");
                    }
                })
                .from(7, 0, 7).to(9, 15, 9)
                .end()
                .texture("particle", Utils.mcBlockLoc("oak_log"))
                .texture("0", Utils.mcBlockLoc("oak_log"))
                .texture("1", Utils.modBlockLoc("well_top"))
                .texture("2", Utils.mcBlockLoc("oak_planks"))
                .texture("3", Utils.modBlockLoc(activated ? "vertical_rope_activated" : "vertical_rope"));
    }

    public enum WellPulleyPart implements StringRepresentable {
        BASE("base"),
        TOP("top")
        ;

        public final String name;

        WellPulleyPart(String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
