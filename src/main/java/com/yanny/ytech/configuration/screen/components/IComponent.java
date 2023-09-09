package com.yanny.ytech.configuration.screen.components;

import com.yanny.ytech.YTechMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface IComponent {
    ResourceLocation GUI = new ResourceLocation(YTechMod.MOD_ID, "textures/gui/machine.png");
    Font FONT = Minecraft.getInstance().font;

    void render(@NotNull GuiGraphics guiGraphics, int leftPos, int topPos);

    default void renderTooltip(@NotNull GuiGraphics guiGraphics, int left, int top, int mouseX, int mouseY) {}
}
