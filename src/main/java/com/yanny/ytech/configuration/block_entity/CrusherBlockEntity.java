package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.MachineItemStackHandler;
import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CrusherBlockEntity extends MachineBlockEntity {
    public CrusherBlockEntity(Holder holder, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(holder, blockEntityType, pos, blockState);
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
