package com.yanny.ytech.machine.block_entity;

import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.machine.handler.MachineItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FurnaceBlockEntity extends MachineBlockEntity {
    public FurnaceBlockEntity(BlockEntityType<? extends BlockEntity> blockEntityType, BlockPos pos, BlockState blockState, YTechConfigLoader.Machine machine, YTechConfigLoader.Tier tier) {
        super(blockEntityType, pos, blockState, machine, tier);
    }

    @NotNull
    @Override
    protected MachineItemStackHandler getContainerHandler() {
        return new MachineItemStackHandler.Builder()
                .addInputSlot(32, 32, (itemStack) -> true)
                .addInputSlot(32, 32 + 18, (AbstractFurnaceBlockEntity::isFuel))
                .addOutputSlot(64, 32)
                .setOnChangeListener(this::setChanged)
                .build();
    }
}
