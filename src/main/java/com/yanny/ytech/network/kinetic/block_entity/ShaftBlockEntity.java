package com.yanny.ytech.network.kinetic.block_entity;

import com.yanny.ytech.network.kinetic.IKineticBlockEntity;
import com.yanny.ytech.network.kinetic.KineticType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;

public class ShaftBlockEntity extends KineticBlockEntity implements IKineticBlockEntity {
    public ShaftBlockEntity(BlockEntityType<? extends BlockEntity> entityType, BlockPos pos, BlockState blockState) {
        super(entityType, pos, blockState, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING), List.of(Direction.EAST, Direction.WEST), KineticType.CONSUMER, 1);
    }
}