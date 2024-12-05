package com.yanny.ytech.compatibility.jei;

import com.yanny.ytech.configuration.Utils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TemperatureWidget extends AnimatedTextureWidget {
    private static final ResourceLocation TEXTURE = Utils.modLoc("textures/gui/emi.png");

    public TemperatureWidget(int x, int y, int time) {
        super(TEXTURE, x, y, 8, 38, 8, 0, 8, 38, 255, 255, time, false, true, false);
    }

    @Override
    public void drawWidget(@NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, position.x(), position.y(), 0, 0, 8, 38, 255, 255);
        super.drawWidget(guiGraphics, mouseX, mouseY);
    }
}
