package com.yanny.ytech.network.kinetic.block;

import com.yanny.ytech.network.kinetic.block_entity.WaterWheelBlockEntity;
import com.yanny.ytech.network.kinetic.common.IKineticBlockEntity;
import com.yanny.ytech.network.kinetic.common.KineticBlockType;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class WaterWheelBlock extends KineticBlock {
    public WaterWheelBlock() {
        super(Properties.copy(Blocks.STONE).noOcclusion());
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

    @NotNull
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        return new WaterWheelBlockEntity(Registration.REGISTRATION_HOLDER.kineticNetwork().get(KineticBlockType.WATER_WHEEL).entityType().get(), pos, blockState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block oldBlock, @NotNull BlockPos neighbour, boolean b) {
        super.neighborChanged(state, level, pos, oldBlock, neighbour, b);

        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof IKineticBlockEntity kineticBlockEntity) {
                Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

                if (pos.above().equals(neighbour) || pos.below().equals(neighbour) || pos.relative(direction).equals(neighbour) || pos.relative(direction.getOpposite()).equals(neighbour)) {
                    kineticBlockEntity.onChangedState(state, state);
                }
            }
        }
    }
}
