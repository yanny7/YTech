package com.yanny.ytech.machine.block_entity;

import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.machine.IMachineBlockEntity;
import com.yanny.ytech.machine.container.ContainerMenuFactory;
import com.yanny.ytech.network.kinetic.block_entity.KineticBlockEntity;
import com.yanny.ytech.network.kinetic.common.KineticNetworkType;
import com.yanny.ytech.network.kinetic.common.RotationDirection;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KineticMachineBlockEntity extends KineticBlockEntity implements IMachineBlockEntity, BlockEntityTicker<KineticMachineBlockEntity>, MenuProvider {
    protected final YTechConfigLoader.Machine machine;
    protected final YTechConfigLoader.Tier tier;

    public KineticMachineBlockEntity(BlockEntityType<? extends BlockEntity> entityType, BlockPos pos, BlockState blockState,
                                     YTechConfigLoader.Machine machine, YTechConfigLoader.Tier tier, List<Direction> validConnections, int stress) {
        super(entityType, pos, blockState, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING), validConnections, KineticNetworkType.CONSUMER, stress);
        this.machine = machine;
        this.tier = tier;
    }

    @Override
    public YTechConfigLoader.Tier getTier() {
        return tier;
    }

    @NotNull
    @Override
    public RotationDirection getRotationDirection() {
        return RotationDirection.NONE;
    }

    @Override
    public void tick(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull KineticMachineBlockEntity pBlockEntity) {

    }

    @NotNull
    @Override
    public Component getDisplayName() {
        return Component.translatable(Registration.REGISTRATION_HOLDER.machine().get(machine).get(tier).block().getId().toLanguageKey("block"));
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return ContainerMenuFactory.create(pContainerId, pPlayer, getBlockPos(), machine, tier);
    }
}
