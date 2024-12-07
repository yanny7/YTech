package com.yanny.ytech.configuration.block;

import com.mojang.serialization.MapCodec;
import com.yanny.ytech.configuration.block_entity.MachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MachineBlock extends BaseEntityBlock {
    public MachineBlock(BlockBehaviour.Properties properties) {
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
        } else if (hasServerTicker()) {
            return createTickerHelper(entityType, entityType, MachineBlock::createMachineServerTicker);
        }

        return null;
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof MachineBlockEntity blockEntity) {
                Containers.dropContents(level, pos, blockEntity.getItemStackHandler().getItems());
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new RuntimeException("Not implemented yet!");
    }

    @NotNull
    @Override
    public InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult trace) {
        if (!level.isClientSide) {
            MenuProvider menuProvider = getMenuProvider(state, level, pos);

            if (menuProvider != null) {
                player.openMenu(menuProvider, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public boolean hasClientTicker() {
        return false;
    }

    public boolean hasServerTicker() {
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
