package com.yanny.ytech.configuration.screen.components;

import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

class BottomUpAnimationComponent implements IComponent {
    protected final int x;
    protected final int y;
    protected final int uWidth;
    protected final int vHeight;
    protected final int uOffset;
    protected final int vOffset;
    protected final int uAnimationOffset;
    protected final int vAnimationOffset;

    protected int maxValue = 100;
    protected int value = 0;
    protected boolean renderAnimation = true;

    BottomUpAnimationComponent(int x, int y, int uWidth, int vHeight, int uOffset, int vOffset, int uAnimationOffset, int vAnimationOffset) {
        this.x = x;
        this.y = y;
        this.uWidth = uWidth;
        this.vHeight = vHeight;
        this.uOffset = uOffset;
        this.vOffset = vOffset;
        this.uAnimationOffset = uAnimationOffset;
        this.vAnimationOffset = vAnimationOffset;
    }

    public void render(@NotNull GuiGraphics graphics, int left, int top) {
        int height = Math.min(Math.round((value / (float)maxValue) * vHeight), vHeight);
        int offset = Math.max(vHeight - height, 0);
        graphics.blit(GUI, left + x, top + y, uOffset, vOffset, uWidth, vHeight);

        if (renderAnimation) {
            graphics.blit(GUI, left + x, top + y + offset, uAnimationOffset, vAnimationOffset + offset, uWidth, vHeight - offset);
        }
    }

    public void set(int maxValue, int value) {
        this.maxValue = maxValue;
        this.value = value;
    }

    public void setRenderAnimation(boolean renderAnimation) {
        this.renderAnimation = renderAnimation;
    }
}
