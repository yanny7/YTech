package com.yanny.ytech.machine.block_entity;

import com.yanny.ytech.configuration.YTechConfigLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityFactory {
    public static BlockEntity create(BlockEntityType<? extends BlockEntity> blockEntityType, BlockPos pos, BlockState blockState, YTechConfigLoader.Machine machine, YTechConfigLoader.Tier tier) {
        return switch (machine.machineType()) {
            case FURNACE -> switch (tier.tierType()) {
                case STONE, STEAM -> new FurnaceBlockEntity(blockEntityType, pos, blockState, tier);
            };
            case CRUSHER -> switch (tier.tierType()) {
                case STONE -> new StoneCrusherBlockEntity(blockEntityType, pos, blockState, tier);
                case STEAM -> new CrusherBlockEntity(blockEntityType, pos, blockState, tier);
            };
        };
    }
}
