package com.yanny.ytech.machine.container;

import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.machine.block_entity.YTechBlockEntity;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class YTechContainerMenu extends AbstractContainerMenu {
    //FIXME move to blockEntity
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_INPUT_COUNT = 1;

    public static final int SLOT_OUTPUT = 0;
    public static final int SLOT_OUTPUT_COUNT = 6;

    public static final int SLOT_COUNT = SLOT_INPUT_COUNT + SLOT_OUTPUT_COUNT;

    public final YTechConfigLoader.Machine machine;
    public final YTechConfigLoader.Tier tier;
    protected final Block block;
    protected final BlockPos pos;

    public YTechContainerMenu(int windowId, Player player, BlockPos pos, YTechConfigLoader.Machine machine, YTechConfigLoader.Tier tier) {
        super(Registration.REGISTRATION_HOLDER.machine().get(machine).get(tier).menuType().get(), windowId);
        this.block = Registration.REGISTRATION_HOLDER.machine().get(machine).get(tier).block().get();
        this.machine = machine;
        this.tier = tier;
        this.pos = pos;

        if (player.level().getBlockEntity(pos) instanceof YTechBlockEntity blockEntity) {
            /*addSlot(new SlotItemHandler(blockEntity.getInputItems(), SLOT_INPUT, 64, 24));
            addSlot(new SlotItemHandler(blockEntity.getOutputItems(), ProcessorBlockEntity.SLOT_OUTPUT+0, 108, 24));
            addSlot(new SlotItemHandler(blockEntity.getOutputItems(), ProcessorBlockEntity.SLOT_OUTPUT+1, 126, 24));
            addSlot(new SlotItemHandler(blockEntity.getOutputItems(), ProcessorBlockEntity.SLOT_OUTPUT+2, 144, 24));
            addSlot(new SlotItemHandler(blockEntity.getOutputItems(), ProcessorBlockEntity.SLOT_OUTPUT+3, 108, 42));
            addSlot(new SlotItemHandler(blockEntity.getOutputItems(), ProcessorBlockEntity.SLOT_OUTPUT+4, 126, 42));
            addSlot(new SlotItemHandler(blockEntity.getOutputItems(), ProcessorBlockEntity.SLOT_OUTPUT+5, 144, 42));
             */
        }

        layoutPlayerInventorySlots(player.getInventory(), 8, 84);
    }

    private int addSlotRange(Container playerInventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new Slot(playerInventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(Container playerInventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(playerInventory, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(Container playerInventory, int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);
        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @NotNull
    @Override
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();

            if (index < SLOT_COUNT) {
                if (!this.moveItemStackTo(stack, SLOT_COUNT, Inventory.INVENTORY_SIZE + SLOT_COUNT, true)) {
                    return ItemStack.EMPTY;
                }
            }
            if (!this.moveItemStackTo(stack, SLOT_INPUT, SLOT_INPUT+1, false)) {
                if (index < 27 + SLOT_COUNT) {
                    if (!this.moveItemStackTo(stack, 27 + SLOT_COUNT, 36 + SLOT_COUNT, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < Inventory.INVENTORY_SIZE + SLOT_COUNT && !this.moveItemStackTo(stack, SLOT_COUNT, 27 + SLOT_COUNT, false)) {
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
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, block);
    }
}
