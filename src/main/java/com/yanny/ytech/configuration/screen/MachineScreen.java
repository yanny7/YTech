package com.yanny.ytech.configuration.screen;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MachineItemStackHandler;
import com.yanny.ytech.configuration.container.MachineContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class MachineScreen<T extends MachineContainerMenu> extends AbstractContainerScreen<T> {
    private static final ResourceLocation GUI = new ResourceLocation(YTechMod.MOD_ID, "textures/gui/machine.png");

    protected final MachineItemStackHandler itemStackHandler;

    public MachineScreen(T container, Inventory inventory, Component title) {
        super(container, inventory, title);
        itemStackHandler = container.getBlockEntity().getItems();
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        renderBackground(graphics);

        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        graphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            graphics.blit(GUI, leftPos + itemStackHandler.getX(i) - 1, topPos + itemStackHandler.getY(i) - 1, 7, 83, 18, 18);
        }
    }
}
