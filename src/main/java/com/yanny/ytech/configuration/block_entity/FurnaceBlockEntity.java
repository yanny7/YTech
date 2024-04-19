package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.MachineItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class FurnaceBlockEntity extends MachineBlockEntity {
    public FurnaceBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }

    @NotNull
    @Override
    public MachineItemStackHandler createItemStackHandler() {
        return new MachineItemStackHandler.Builder()
                .addInputSlot(32, 32)
                .addInputSlot(32, 32 + 18, (itemStackHandler, slot, itemStack) -> AbstractFurnaceBlockEntity.isFuel(itemStack))
                .addOutputSlot(64, 32)
                .setOnChangeListener(this::setChanged)
                .build();
    }

    @Override
    public @NotNull ContainerData createContainerData() {
        return new SimpleContainerData(0);
    }
}
