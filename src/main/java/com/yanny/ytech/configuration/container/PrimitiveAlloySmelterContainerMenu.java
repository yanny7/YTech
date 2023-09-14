package com.yanny.ytech.configuration.container;

import com.yanny.ytech.configuration.MachineItemStackHandler;
import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import org.jetbrains.annotations.NotNull;

public class PrimitiveAlloySmelterContainerMenu extends MachineContainerMenu {
    public PrimitiveAlloySmelterContainerMenu(@NotNull Holder holder, int windowId, @NotNull Player player, @NotNull BlockPos pos,
                                              @NotNull MachineItemStackHandler itemStackHandler, @NotNull ContainerData data) {
        super(holder, windowId, player, pos, itemStackHandler, data);
    }

    public int getFuelLeft() {
        return containerData.get(0);
    }

    public int getMaxTemperature() {
        return containerData.get(1);
    }

    public int getTemperature() {
        return containerData.get(2);
    }

    public int getSmeltingProgress() {
        return containerData.get(3);
    }

    public boolean inProgress() {
        return containerData.get(4) != 0;
    }

    public boolean burning() {
        return containerData.get(5) != 0;
    }
}
