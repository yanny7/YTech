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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public abstract class AqueductConsumerBlock extends IrrigationBlock {
    public AqueductConsumerBlock() {
        super(Properties.ofFullCopy(Blocks.TERRACOTTA));
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        BlockState state = defaultBlockState().setValue(WATERLOGGED, false);
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
    public BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState,
                                  @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
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
                                @NotNull BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        if (!level.isClientSide && level.getBlockEntity(pos) instanceof AqueductConsumerBlockEntity blockEntity) {
            IrrigationServerNetwork network = YTechMod.IRRIGATION_PROPAGATOR.server().getNetwork(blockEntity);

            if (network != null) {
                blockEntity.neighborChanged();
            }
        }
    }

    @Override
    public List<BlockPos> getValidNeighbors(@NotNull BlockState blockState, @NotNull BlockPos pos) {
        return Direction.Plane.HORIZONTAL.stream().map((dir) -> pos.offset(dir.getNormal())).toList();
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (level.getBlockEntity(pos) instanceof AqueductConsumerBlockEntity hydratorBlock && hydratorBlock.isHydrating()) {
            if (random.nextInt(10) == 0) {
                level.playLocalSound(pos, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
            }
        }
    }

    @NotNull
    @Override
    public FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.defaultFluidState() : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public boolean onDestroyedByPlayer(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                       boolean willHarvest, @NotNull FluidState fluid) {
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, Fluids.EMPTY.defaultFluidState()); // prevent fluid spawning after block break
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(EAST).add(WEST).add(SOUTH).add(NORTH).add(WATERLOGGED);
    }

    protected static void createAqueductConsumerTicker(@NotNull Level level, @NotNull BlockEntity blockEntity) {
        if (blockEntity instanceof AqueductConsumerBlockEntity block) {
            block.tick((ServerLevel) level);
        }
    }
}
