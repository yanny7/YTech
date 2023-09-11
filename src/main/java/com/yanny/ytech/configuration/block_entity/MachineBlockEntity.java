package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.MachineItemStackHandler;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.IBlockHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MachineBlockEntity extends BlockEntity implements MenuProvider {
    private static final String TAG_ITEMS = "items";

    @NotNull protected final MachineItemStackHandler itemStackHandler;
    @NotNull protected final ContainerData containerData;
    @NotNull protected final Holder holder;

    public MachineBlockEntity(@NotNull Holder holder, @NotNull BlockEntityType<?> blockEntityType, @NotNull BlockPos pos, @NotNull BlockState blockState) {
        super(blockEntityType, pos, blockState);
        this.holder = holder;
        itemStackHandler = createItemStackHandler();
        containerData = createContainerData();
    }

    @NotNull
    @Override
    public Component getDisplayName() {
        return Component.translatable(holder.name);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        if (holder instanceof IBlockHolder blockHolder) {
            return blockHolder.getMenu().getContainerMenu(holder, pContainerId, pPlayerInventory, worldPosition, itemStackHandler, containerData);
        } else {
            throw new IllegalStateException("Invalid holder type");
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (tag.contains(TAG_ITEMS)) {
            itemStackHandler.deserializeNBT(tag.getCompound(TAG_ITEMS));
        }
    }

    @NotNull
    public MachineItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    public int getDataSize() {
        return containerData.getCount();
    }

    public void tickServer(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState blockState, @NotNull MachineBlockEntity blockEntity) {

    }

    public void tickClient(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState blockState, @NotNull MachineBlockEntity blockEntity) {

    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(TAG_ITEMS, itemStackHandler.serializeNBT());
    }

    @NotNull abstract public MachineItemStackHandler createItemStackHandler();

    @NotNull abstract protected ContainerData createContainerData();
}
