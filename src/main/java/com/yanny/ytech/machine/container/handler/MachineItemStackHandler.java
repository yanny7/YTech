package com.yanny.ytech.machine.container.handler;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class MachineItemStackHandler extends ItemStackHandler {
    List<MachineContainerHandler.SlotHolder> inputSlotHolder;
    List<MachineContainerHandler.SlotHolder> outputSlotHolder;

    MachineItemStackHandler(List<MachineContainerHandler.SlotHolder> inputSlotHolder, List<MachineContainerHandler.SlotHolder> outputSlotHolder, NonNullList<ItemStack> stacks) {
        super(stacks);
        this.inputSlotHolder = inputSlotHolder;
        this.outputSlotHolder = outputSlotHolder;
    }

    public int getX(int slot) {
        validateSlotIndex(slot);

        if (slot < inputSlotHolder.size()) {
            return inputSlotHolder.get(slot).x();
        } else {
            return outputSlotHolder.get(slot - inputSlotHolder.size()).x();
        }
    }

    public int getY(int slot) {
        validateSlotIndex(slot);

        if (slot < inputSlotHolder.size()) {
            return inputSlotHolder.get(slot).y();
        } else {
            return outputSlotHolder.get(slot - inputSlotHolder.size()).y();
        }
    }

    public int getInputSlots() {
        return inputSlotHolder.size();
    }

    public int getOutputSlots() {
        return outputSlotHolder.size();
    }

    @Override
    public void setSize(int size) {
        if (size != getSlots()) {
            throw new IllegalStateException("Invalid size of slots, expected " + size + ", found " + getSlots());
        }
    }
}
