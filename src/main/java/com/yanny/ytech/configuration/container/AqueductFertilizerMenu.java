package com.yanny.ytech.configuration.container;

import com.yanny.ytech.configuration.MachineItemStackHandler;
import com.yanny.ytech.configuration.block_entity.AqueductFertilizerBlockEntity;
import com.yanny.ytech.registration.YTechMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class AqueductFertilizerMenu extends AbstractContainerMenu {
    protected final BlockPos pos;
    @NotNull
    protected final ItemStackHandler itemStackHandler;
    @NotNull protected final AqueductFertilizerBlockEntity blockEntity;
    @NotNull protected final ContainerData containerData;
    private final int inputSlots;

    public AqueductFertilizerMenu(int windowId, @NotNull Player player, @NotNull BlockPos pos,
                                  @NotNull MachineItemStackHandler itemStackHandler, @NotNull ContainerData data) {
        super(YTechMenuTypes.AQUEDUCT_FERTILIZER.get(), windowId);
        this.pos = pos;
        containerData = data;
        blockEntity = getAqueductFertilizerBlockEntity(player, pos);
        this.itemStackHandler = itemStackHandler;
        inputSlots = itemStackHandler.getInputSlots();

        for (int index = 0; index < itemStackHandler.getSlots(); index++) {
            addSlot(new SlotItemHandler(itemStackHandler, index, itemStackHandler.getX(index), itemStackHandler.getY(index)));
        }

        Utils.layoutPlayerInventorySlots(this::addSlot, player.getInventory(), 8, 84);
        addDataSlots(data);
    }

    @NotNull
    @Override
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        int slotCount = itemStackHandler.getSlots();

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();

            if (index < slotCount) {
                if (!moveItemStackTo(stack, slotCount, Inventory.INVENTORY_SIZE + slotCount, true)) {
                    return ItemStack.EMPTY;
                }
            }
            if (!moveItemStackTo(stack, 0, inputSlots, false)) {
                if (index < Inventory.INVENTORY_SIZE - 9 + slotCount) {
                    if (!moveItemStackTo(stack, Inventory.INVENTORY_SIZE - 9 + slotCount, Inventory.INVENTORY_SIZE + slotCount, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < Inventory.INVENTORY_SIZE + slotCount && !moveItemStackTo(stack, slotCount, Inventory.INVENTORY_SIZE - 9 + slotCount, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, player.level().getBlockState(pos).getBlock());
    }

    @NotNull
    public AqueductFertilizerBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public static AqueductFertilizerBlockEntity getAqueductFertilizerBlockEntity(@NotNull Player player, @NotNull BlockPos pos) {
        LevelAccessor level = player.level();

        if (level.getBlockEntity(pos) instanceof AqueductFertilizerBlockEntity entity) {
            return entity;
        } else {
            throw new IllegalArgumentException("BlockEntity is not instanceof MachineBlockEntity");
        }
    }
}
