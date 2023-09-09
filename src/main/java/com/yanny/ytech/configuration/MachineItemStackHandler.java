package com.yanny.ytech.configuration;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/* Commoble:
 * Generally this is the superset of itemhandlers you may want
 *
 * 1) Storage. Your blockentity probably wants an ItemStackHandler or extension thereof that physically stores items.
 * Your blockentity can serialize this to nbt on save. This is the "core" itemhandler.
 *
 * 2) getCapability. If you want to expose the capability to block automation, you can return an IItemHandler from getCapability.
 * If you want to do any filtering/validation on the inserts and extracts, this should be its own IItemHandler that delegates to the
 * core ItemStackHandler as necessary.
 *
 * 3) Menus. If your blockentity has an inventory menu, the clientside menu should use SlotItemHandlers backed by a dummy
 * ItemStackHandler(n) where n is the size of your actual itemhandler (don't use ItemStackHandler(NonNullList). The serverside
 * menu should have SlotItemHandlers backed by your core itemhandler. What I do to handle insert validation in the menu is I extend
 * SlotItemHandler and then e.g. I can use a no-insert SlotItemHandler for the output slots of the machine.
 */
public class MachineItemStackHandler extends ItemStackHandler {
    @NotNull private final List<SlotHolder> inputSlotHolder;
    @NotNull private final List<SlotHolder> outputSlotHolder;

    MachineItemStackHandler(@NotNull List<SlotHolder> inputSlotHolder, @NotNull List<SlotHolder> outputSlotHolder, @NotNull NonNullList<ItemStack> stacks) {
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

    @Override
    public void setSize(int size) {
        if (size != getSlots()) {
            throw new IllegalStateException("Invalid size of slots, expected " + size + ", found " + getSlots());
        }
    }

    public static class Builder {
        final List<SlotHolder> inputSlotHolder = new ArrayList<>();
        final List<SlotHolder> outputSlotHolder = new ArrayList<>();
        @Nullable Runnable onChangeListener;

        public Builder() {

        }

        public Builder addInputSlot(int x, int y, Predicate<ItemStack> isItemValid) {
            inputSlotHolder.add(new SlotHolder(x, y, isItemValid));
            return this;
        }

        public Builder addOutputSlot(int x, int y) {
            outputSlotHolder.add(new SlotHolder(x, y, (item) -> true));
            return this;
        }

        public Builder setOnChangeListener(@NotNull Runnable onChangeListener) {
            this.onChangeListener = onChangeListener;
            return this;
        }

        public MachineItemStackHandler build() {
            Objects.requireNonNull(onChangeListener, "Missing onChangeListener");

            NonNullList<ItemStack> stacks = NonNullList.withSize(inputSlotHolder.size() + outputSlotHolder.size(), ItemStack.EMPTY);
            return new MachineItemStackHandler(inputSlotHolder, outputSlotHolder, stacks) {
                @Override
                protected void onContentsChanged(int slot) {
                    onChangeListener.run();
                }

                @Override
                public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                    if (slot < inputSlotHolder.size()) {
                        return inputSlotHolder.get(slot).isItemValid.test(stack);
                    } else {
                        return false;
                    }
                }
            };
        }
    }

    public record SlotHolder(
            int x,
            int y,
            Predicate<ItemStack> isItemValid
    ) {}
}
