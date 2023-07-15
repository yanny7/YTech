package com.yanny.ytech.machine.block_entity;

import com.yanny.ytech.configuration.YTechConfigLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class StoneCrusherBlockEntity extends KineticMachineBlockEntity {
    public StoneCrusherBlockEntity(BlockEntityType<? extends BlockEntity> blockEntityType, BlockPos pos, BlockState blockState, YTechConfigLoader.Tier tier) {
        super(blockEntityType, pos, blockState, tier, List.of(Direction.EAST, Direction.WEST, Direction.NORTH), 8);
    }
}
