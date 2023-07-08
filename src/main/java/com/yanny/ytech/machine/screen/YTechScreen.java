package com.yanny.ytech.machine.screen;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.machine.container.YTechContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class YTechScreen extends AbstractContainerScreen<YTechContainerMenu> {
    protected final YTechContainerMenu containerMenu;

    private final ResourceLocation GUI = new ResourceLocation(YTechMod.MOD_ID, "textures/gui/machine.png");

    public YTechScreen(YTechContainerMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.containerMenu = container;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        graphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }
}
