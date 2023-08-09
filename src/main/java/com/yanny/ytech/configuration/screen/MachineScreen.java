package com.yanny.ytech.configuration.screen;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MachineItemStackHandler;
import com.yanny.ytech.configuration.block_entity.MachineBlockEntity;
import com.yanny.ytech.configuration.container.MachineContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public class MachineScreen extends BaseScreen {
    protected final MachineContainerMenu containerMenu;
    protected final MachineBlockEntity blockEntity;
    protected final MachineItemStackHandler itemStackHandler;

    private final ResourceLocation GUI = new ResourceLocation(YTechMod.MOD_ID, "textures/gui/machine.png");

    public MachineScreen(AbstractContainerMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.containerMenu = (MachineContainerMenu) container;
        blockEntity = this.containerMenu.getBlockEntity();
        itemStackHandler = blockEntity.getItems();
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
