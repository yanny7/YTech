package com.yanny.ytech.configuration.screen;

import com.yanny.ytech.configuration.screen.components.AbstractScreenHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    @NotNull protected final AbstractScreenHandler<T> screenHandler;

    public AbstractScreen(T container, Inventory inventory, Component title) {
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

    abstract AbstractScreenHandler<T> getScreenHandler();
}
