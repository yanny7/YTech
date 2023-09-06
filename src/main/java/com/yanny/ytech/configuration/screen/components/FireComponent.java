package com.yanny.ytech.configuration.screen.components;

import com.yanny.ytech.YTechMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FireComponent {
    private static final ResourceLocation GUI = new ResourceLocation(YTechMod.MOD_ID, "textures/gui/machine.png");

    private final int x;
    private final int y;

    private int maxValue = 1;
    private int value = 0;

    public FireComponent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void render(@NotNull GuiGraphics graphics) {
        graphics.blit(GUI, x, y, 44, 166, 14, 14);
        graphics.blit(GUI, x, y, 58, 166, 14, 14);
    }

    public void updateValue(int value) {
        this.value = value;
    }

    public void set(int maxValue, int value) {
        this.maxValue = maxValue;
        this.value = value;
    }
}
