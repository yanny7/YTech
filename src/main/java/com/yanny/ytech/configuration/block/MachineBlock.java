package com.yanny.ytech.configuration.block;

import com.mojang.serialization.MapCodec;
import com.yanny.ytech.configuration.block_entity.MachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class MachineBlock extends BaseEntityBlock {
    public MachineBlock(Properties properties) {
        super(properties);
    }

    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> entityType) {
        if (level.isClientSide) {
            if (hasClientTicker()) {
                return createTickerHelper(entityType, entityType, MachineBlock::createMachineClientTicker);
            }
        } else {
            return createTickerHelper(entityType, entityType, MachineBlock::createMachineServerTicker);
        }

        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.LIT);
    }

    @Override
    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new RuntimeException("Not implemented yet!");
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection().getOpposite())
                .setValue(BlockStateProperties.LIT, false);
    }

    @NotNull
    @Override
    public InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult trace) {
        if (!level.isClientSide) {
            player.openMenu(Objects.requireNonNull(getMenuProvider(state, level, pos)), pos);
        }
        return InteractionResult.SUCCESS;
    }

    public boolean hasClientTicker() {
        return false;
    }

    public static void createMachineClientTicker(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntity blockEntity) {
        if (blockEntity instanceof MachineBlockEntity block) {
            block.tickClient(level, pos, state, block);
        }
    }

    public static void createMachineServerTicker(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntity blockEntity) {
        if (blockEntity instanceof MachineBlockEntity block) {
            block.tickServer(level, pos, state, block);
        }
    }
}
