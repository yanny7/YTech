package com.yanny.ytech.machine.block_entity;

import com.yanny.ytech.machine.MachineType;
import com.yanny.ytech.machine.TierType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityFactory {
    public static BlockEntity create(BlockEntityType<? extends BlockEntity> blockEntityType, BlockPos pos, BlockState blockState, MachineType machine, TierType tier) {
        return switch (machine) {
            case FURNACE -> switch (tier) {
                case STONE, STEAM -> new FurnaceBlockEntity(blockEntityType, pos, blockState, machine, tier);
            };
            case CRUSHER -> switch (tier) {
                case STONE -> new StoneCrusherBlockEntity(blockEntityType, pos, blockState, machine, tier);
                case STEAM -> new CrusherBlockEntity(blockEntityType, pos, blockState, machine, tier);
            };
        };
    }
}
