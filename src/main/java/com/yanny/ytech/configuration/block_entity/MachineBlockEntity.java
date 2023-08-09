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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MachineBlockEntity extends BlockEntity implements BlockEntityTicker<MachineBlockEntity>, MenuProvider {
    private static final String TAG_ITEMS = "items";

    protected final MachineItemStackHandler items;
    protected final Holder holder;

    public MachineBlockEntity(Holder holder, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
        this.holder = holder;
        this.items = getContainerHandler();
    }

    @Override
    public void tick(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull MachineBlockEntity pBlockEntity) {

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
            return blockHolder.getMenu().getContainerMenu(holder, pContainerId, pPlayerInventory, getBlockPos());
        } else {
            throw new IllegalStateException("Invalid holder type");
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (tag.contains(TAG_ITEMS)) {
            items.deserializeNBT(tag.getCompound(TAG_ITEMS));
        }
    }

    @NotNull
    public MachineItemStackHandler getItems() {
        return items;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(TAG_ITEMS, items.serializeNBT());
    }

    @NotNull abstract protected MachineItemStackHandler getContainerHandler();
}
