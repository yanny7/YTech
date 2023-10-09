package com.yanny.ytech.configuration.screen.components;

import com.yanny.ytech.configuration.MachineItemStackHandler;
import com.yanny.ytech.configuration.Utils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;

public class AbstractScreenHandler<T extends AbstractContainerMenu> {
    private static final ResourceLocation GUI = Utils.modLoc("textures/gui/machine.png");

    @NotNull private final MachineItemStackHandler itemStackHandler;
    @NotNull private final Set<IComponent> components;
    private int leftPos;
    private int topPos;
    private int imageWidth;
    private int imageHeight;

    private AbstractScreenHandler(@NotNull MachineItemStackHandler itemStackHandler, @NotNull Set<IComponent> components) {
        this.itemStackHandler = itemStackHandler;
        this.components = components;
    }

    public void init(@NotNull AbstractContainerScreen<T> screen) {
        leftPos = screen.getGuiLeft();
        topPos = screen.getGuiTop();
        imageWidth = screen.getXSize();
        imageHeight = screen.getYSize();
    }

    public void render(@NotNull GuiGraphics guiGraphics) {
        guiGraphics.blit(GUI, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            guiGraphics.blit(GUI, leftPos + itemStackHandler.getX(i) - 1, topPos + itemStackHandler.getY(i) - 1, 7, 83, 18, 18);
        }

        components.forEach(component -> component.render(guiGraphics, leftPos, topPos));
    }

    public void renderTooltip(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        components.forEach(component -> component.renderTooltip(guiGraphics, leftPos, topPos, mouseX, mouseY));
    }

    public static class Builder<T extends AbstractContainerMenu> {
        @NotNull final MachineItemStackHandler itemStackHandler;
        @NotNull final Set<IComponent> components = new LinkedHashSet<>();

        public Builder(@NotNull MachineItemStackHandler itemStackHandler) {
            this.itemStackHandler = itemStackHandler;
        }

        public Builder<T> addComponent(@NotNull IComponent component) {
            components.add(component);
            return this;
        }

        public AbstractScreenHandler<T> build() {
            return new AbstractScreenHandler<>(itemStackHandler, components);
        }
    }
}
