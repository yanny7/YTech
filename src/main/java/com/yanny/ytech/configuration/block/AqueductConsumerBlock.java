package com.yanny.ytech.configuration.block;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block_entity.AqueductConsumerBlockEntity;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public abstract class AqueductConsumerBlock extends IrrigationBlock {
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");
    public AqueductConsumerBlock(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState state) {
        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        BlockState state = defaultBlockState().setValue(ACTIVATED, false);
        Level level = blockPlaceContext.getLevel();
        BlockPos pos = blockPlaceContext.getClickedPos();
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

    @NotNull
    @Override
    public BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess tickAccess, @NotNull BlockPos pos,
                                  @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState, @NotNull RandomSource random) {
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
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block neighborBlock,
                                @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);

        if (!level.isClientSide && level.getBlockEntity(pos) instanceof AqueductConsumerBlockEntity blockEntity) {
            IrrigationServerNetwork network = YTechMod.IRRIGATION_PROPAGATOR.server().getNetwork(blockEntity);

            if (network != null) {
                blockEntity.neighborChanged();
            }
        }
    }

    @Override
    public List<BlockPos> getValidNeighbors(@NotNull BlockState blockState, @NotNull BlockPos pos) {
        return Direction.Plane.HORIZONTAL.stream().map((dir) -> pos.offset(dir.getUnitVec3i())).toList();
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (level.getBlockEntity(pos) instanceof AqueductConsumerBlockEntity hydratorBlock && hydratorBlock.isHydrating()) {
            if (random.nextInt(10) == 0) {
                level.playLocalSound(pos, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(EAST).add(WEST).add(SOUTH).add(NORTH).add(ACTIVATED);
    }

    protected static void createAqueductConsumerTicker(@NotNull Level level, @NotNull BlockEntity blockEntity) {
        if (blockEntity instanceof AqueductConsumerBlockEntity block) {
            block.tick((ServerLevel) level);
        }
    }
}
