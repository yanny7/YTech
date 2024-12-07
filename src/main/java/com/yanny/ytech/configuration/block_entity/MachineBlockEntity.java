package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.MachineItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class MachineBlockEntity extends BlockEntity implements MenuProvider, IMenuBlockEntity {
    private static final String TAG_ITEMS = "items";

    @NotNull protected final MachineItemStackHandler itemStackHandler;
    @NotNull protected final ContainerData containerData;

    public MachineBlockEntity(@NotNull BlockEntityType<?> blockEntityType, @NotNull BlockPos pos, @NotNull BlockState blockState) {
        super(blockEntityType, pos, blockState);
        itemStackHandler = createItemStackHandler();
        containerData = createContainerData();
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(tag, provider);

        if (tag.contains(TAG_ITEMS)) {
            itemStackHandler.deserializeNBT(provider, tag.getCompound(TAG_ITEMS));
        }
    }

    @NotNull
    public MachineItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    @Override
    public int getDataSize() {
        return containerData.getCount();
    }

    public void tickServer(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState blockState, @NotNull MachineBlockEntity blockEntity) {}

    public void tickClient(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState blockState, @NotNull MachineBlockEntity blockEntity) {}

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put(TAG_ITEMS, itemStackHandler.serializeNBT(provider));
    }

    @NotNull abstract public MachineItemStackHandler createItemStackHandler();

    @NotNull abstract public ContainerData createContainerData();
}
