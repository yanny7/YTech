package com.yanny.ytech.machine.block_entity;

import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.network.kinetic.common.KineticNetworkType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;

public class StoneCrusherBlockEntity extends KineticMachineBlockEntity {
    public StoneCrusherBlockEntity(BlockEntityType<? extends BlockEntity> blockEntityType, BlockPos pos, BlockState blockState, YTechConfigLoader.Machine machine, YTechConfigLoader.Tier tier) {
        super(blockEntityType, pos, blockState, machine, tier, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING), List.of(Direction.EAST, Direction.WEST, Direction.NORTH), KineticNetworkType.CONSUMER, 8);
    }
}
