package com.yanny.ytech.configuration.screen.components;

import com.yanny.ytech.YTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FireComponent {
    private static final ResourceLocation GUI = new ResourceLocation(YTechMod.MOD_ID, "textures/gui/machine.png");

    private final int x;
    private final int y;

    private int maxValue = 100;
    private int value = 25;

    public FireComponent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void render(@NotNull GuiGraphics graphics, int left, int top) {
        int height = Math.min(Math.round((value / (float)maxValue) * 14), 14);
        int offset = Math.max(14 - height, 0);
        graphics.blit(GUI, left + x, top + y, 44, 166, 14, 14);
        graphics.blit(GUI, left + x, top + y + offset, 58, 166 + offset, 14, 14 - offset);
    }

    public void set(int maxValue, int value) {
        this.maxValue = maxValue;
        this.value = value;
    }
}
