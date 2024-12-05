package com.yanny.ytech.configuration.screen.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.NotNull;

class LeftRightAnimationComponent implements IComponent {
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

    LeftRightAnimationComponent(int x, int y, int uWidth, int vHeight, int uOffset, int vOffset, int uAnimationOffset, int vAnimationOffset) {
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
        int width = Math.min(Math.round((value / (float)maxValue) * uWidth), uWidth);
        int offset = Math.max(uWidth - width, 0);
        graphics.blit(RenderType::guiTextured, GUI, left + x, top + y, uOffset, vOffset, uWidth, vHeight, 256, 256);

        if (renderAnimation) {
            graphics.blit(RenderType::guiTextured, GUI, left + x, top + y, uAnimationOffset, vAnimationOffset, uWidth - offset, vHeight, 256, 256);
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
