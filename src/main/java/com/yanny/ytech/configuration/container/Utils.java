package com.yanny.ytech.configuration.container;

import com.yanny.ytech.configuration.block_entity.MachineBlockEntity;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.IBlockHolder;
import com.yanny.ytech.registration.IMenuEntityBlockHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class Utils {
    public static IMenuEntityBlockHolder getMenuEntityBlockHolder(Holder holder) {
        if (holder instanceof IMenuEntityBlockHolder entityHolder) {
            return entityHolder;
        } else {
            throw new IllegalStateException("Invalid holder type");
        }
    }

    public static IBlockHolder getBlockHolder(Holder holder) {
        if (holder instanceof IBlockHolder entityHolder) {
            return entityHolder;
        } else {
            throw new IllegalStateException("Invalid holder type");
        }
    }

    public static MachineBlockEntity getMachineBlockEntity(@NotNull Player player, @NotNull BlockPos pos) {
        LevelAccessor level = player.level();

        if (level.getBlockEntity(pos) instanceof MachineBlockEntity entity) {
            return entity;
        } else {
            throw new IllegalArgumentException("BlockEntity is not instanceof MachineBlockEntity");
        }
    }

    public static void layoutPlayerInventorySlots(Consumer<Slot> addSlot, Container playerInventory, int x, int y) {
        // Player inventory
        addSlotBox(addSlot, playerInventory, 9, x, y, 9, 18, 3, 18);
        // Hotbar
        y += 58;
        addSlotRange(addSlot, playerInventory, 0, x, y, 9, 18);
    }

    private static int addSlotRange(Consumer<Slot> addSlot, Container playerInventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot.accept(new Slot(playerInventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private static int addSlotBox(Consumer<Slot> addSlot, Container playerInventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(addSlot, playerInventory, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private Utils() {}
}
