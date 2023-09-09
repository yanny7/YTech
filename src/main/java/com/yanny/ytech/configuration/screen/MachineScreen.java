package com.yanny.ytech.configuration.screen;

import com.yanny.ytech.configuration.container.MachineContainerMenu;
import com.yanny.ytech.configuration.screen.components.MachineScreenHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public abstract class MachineScreen<T extends MachineContainerMenu> extends AbstractContainerScreen<T> {
    @NotNull protected final MachineScreenHandler<T> screenHandler;

    public MachineScreen(T container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.screenHandler = getScreenHandler();
    }

    @Override
    protected void init() {
        super.init();
        screenHandler.init(this);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        renderBackground(graphics);
        screenHandler.render(graphics);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
        screenHandler.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    abstract MachineScreenHandler<T> getScreenHandler();
}
