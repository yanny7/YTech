package com.yanny.ytech.machine.screen;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.machine.block_entity.MachineBlockEntity;
import com.yanny.ytech.machine.container.MachineContainerMenu;
import com.yanny.ytech.machine.container.handler.MachineItemStackHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MachineScreen extends AbstractContainerScreen<MachineContainerMenu> {
    protected final MachineContainerMenu containerMenu;
    protected final MachineBlockEntity blockEntity;
    protected final MachineItemStackHandler itemStackHandler;

    private final ResourceLocation GUI = new ResourceLocation(YTechMod.MOD_ID, "textures/gui/machine.png");

    public MachineScreen(MachineContainerMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.containerMenu = container;
        blockEntity = container.getBlockEntity();
        itemStackHandler = blockEntity.getPlayerItemStackHandler();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        graphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            graphics.blit(GUI, leftPos + itemStackHandler.getX(i) - 1, topPos + itemStackHandler.getY(i) - 1, 7, 83, 18, 18);
        }
    }
}
