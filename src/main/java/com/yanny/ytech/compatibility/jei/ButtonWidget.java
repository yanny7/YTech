package com.yanny.ytech.compatibility.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

public class ButtonWidget implements IRecipeWidget {
    protected final int x, y, width, height, u, v;
    protected final BooleanSupplier isActive;
    protected final ClickAction action;
    protected final ResourceLocation texture;

    public ButtonWidget(int x, int y, int width, int height, int u, int v, ResourceLocation texture, BooleanSupplier isActive, ClickAction action) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
        this.texture = texture;
        this.isActive = isActive;
        this.action = action;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        action.click(mouseX, mouseY, button);
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        return true;
    }

    @NotNull
    @Override
    public ScreenPosition getPosition() {
        return new ScreenPosition(x, y);
    }

    @Override
    public void drawWidget(@NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int v = this.v;
        boolean active = this.isActive.getAsBoolean();
        if (!active) {
            v += height * 2;
        } else if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
            v += this.height;
        }
        RenderSystem.enableDepthTest();
        guiGraphics.blit(texture, this.x, this.y, this.u, v, this.width, this.height);
    }

    public static interface ClickAction {

        void click(double mouseX, double mouseY, int button);
    }
}
