package com.yanny.ytech.machine.block_entity;

import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.machine.IMachineBlockEntity;
import com.yanny.ytech.network.kinetic.block_entity.KineticBlockEntity;
import com.yanny.ytech.network.kinetic.common.KineticNetworkType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;

public class KineticMachineBlockEntity extends KineticBlockEntity implements IMachineBlockEntity {
    protected final YTechConfigLoader.Tier tier;

    public KineticMachineBlockEntity(BlockEntityType<? extends BlockEntity> entityType, BlockPos pos, BlockState blockState,
                                     YTechConfigLoader.Tier tier, List<Direction> validConnections, int stress) {
        super(entityType, pos, blockState, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING), validConnections, KineticNetworkType.CONSUMER, stress);
        this.tier = tier;
    }

    @Override
    public YTechConfigLoader.Tier getTier() {
        return tier;
    }
}
