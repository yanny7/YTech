package com.yanny.ytech.machine.block_entity;

import com.yanny.ytech.machine.MachineType;
import com.yanny.ytech.machine.TierType;
import com.yanny.ytech.machine.handler.MachineItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CrusherBlockEntity extends MachineBlockEntity {
    public CrusherBlockEntity(BlockEntityType<? extends BlockEntity> blockEntityType, BlockPos pos, BlockState blockState, MachineType machine, TierType tier) {
        super(blockEntityType, pos, blockState, machine, tier);
    }

    @NotNull
    @Override
    protected MachineItemStackHandler getContainerHandler() {
        return new MachineItemStackHandler.Builder()
                .addInputSlot(32, 32, (itemStack) -> true)
                .addOutputSlot(64, 32)
                .setOnChangeListener(this::setChanged)
                .build();
    }
}
