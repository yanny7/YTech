package com.yanny.ytech.machine.block;

import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.machine.block_entity.YTechBlockEntity;
import com.yanny.ytech.machine.container.ContainerMenuFactory;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class YTechBlock extends Block implements EntityBlock {
    protected final YTechConfigLoader.Machine machine;
    protected final YTechConfigLoader.Tier tier;

    public YTechBlock(YTechConfigLoader.Machine machine, YTechConfigLoader.Tier tier) {
        super(BlockBehaviour.Properties.of());
        this.machine = machine;
        this.tier = tier;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> entityType) {
        return EntityBlock.super.getTicker(level, blockState, entityType);
    }

    @NotNull
    @Override
    public abstract BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.POWERED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection().getOpposite())
                .setValue(BlockStateProperties.POWERED, true);
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult trace) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof YTechBlockEntity) {
                MenuProvider containerProvider = new MenuProvider() {
                    @NotNull
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable(Registration.REGISTRATION_HOLDER.machine().get(machine).get(tier).block().getId().toLanguageKey("block"));
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int windowId, @NotNull Inventory playerInventory, @NotNull Player playerEntity) {
                        return ContainerMenuFactory.create(windowId, playerEntity, pos, machine, tier);
                    }
                };

                NetworkHooks.openScreen((ServerPlayer) player, containerProvider, blockEntity.getBlockPos());
            } else {
                throw new IllegalStateException("Container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }
}
