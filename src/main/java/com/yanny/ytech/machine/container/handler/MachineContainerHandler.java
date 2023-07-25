package com.yanny.ytech.machine.container.handler;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class MachineContainerHandler {
    private final MachineItemStackHandler playerItemStackHandler;
    private final MachineItemStackHandler machineItemStackHandler;

    private MachineContainerHandler(MachineItemStackHandler playerItemStackHandler, MachineItemStackHandler machineItemStackHandler) {
        this.playerItemStackHandler = playerItemStackHandler;
        this.machineItemStackHandler = machineItemStackHandler;
    }

    public MachineItemStackHandler getPlayerItemStackHandler() {
        return playerItemStackHandler; //TODO prohibit inserting to output
    }

    public MachineItemStackHandler getMachineItemStackHandler() {
        return machineItemStackHandler; //TODO allow inserting to output
    }

    public static class Builder {
        List<SlotHolder> inputSlotHolder = new ArrayList<>();
        List<SlotHolder> outputSlotHolder = new ArrayList<>();
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

        public MachineContainerHandler build() {
            Objects.requireNonNull(onChangeListener, "Missing onChangeListener");

            NonNullList<ItemStack> stacks = NonNullList.withSize(inputSlotHolder.size() + outputSlotHolder.size(), ItemStack.EMPTY);
            MachineItemStackHandler playerItemStackHandler = new MachineItemStackHandler(inputSlotHolder, outputSlotHolder, stacks) {
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
            MachineItemStackHandler machineItemStackHandler = new MachineItemStackHandler(inputSlotHolder, outputSlotHolder, stacks) {
                @Override
                protected void onContentsChanged(int slot) {
                    onChangeListener.run();
                }

                @Override
                public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                    if (slot < inputSlotHolder.size()) {
                        return false;
                    }

                    return super.isItemValid(slot, stack);
                }
            };

            return new MachineContainerHandler(playerItemStackHandler, machineItemStackHandler);
        }
    }

    public record SlotHolder(
            int x,
            int y,
            Predicate<ItemStack> isItemValid
    ) {}
}
