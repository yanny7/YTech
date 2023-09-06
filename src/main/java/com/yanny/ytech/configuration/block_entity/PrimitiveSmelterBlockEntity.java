package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.MachineItemStackHandler;
import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PrimitiveSmelterBlockEntity extends MachineBlockEntity {
    private int test = 0;

    public PrimitiveSmelterBlockEntity(Holder holder, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
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

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState blockState, @NotNull MachineBlockEntity blockEntity) {
        test++;

        if (test > 100) {
            test = 0;
        }
    }

    @Override
    protected @NotNull ContainerData getContainerData() {
        return new ContainerData() {
            @Override
            public int get(int pIndex) {
                return PrimitiveSmelterBlockEntity.this.test;
            }

            @Override
            public void set(int pIndex, int pValue) {
                PrimitiveSmelterBlockEntity.this.test = pValue;
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }
}
