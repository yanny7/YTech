package com.yanny.ytech.compatibility.jei;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AnimatedTextureWidget implements IRecipeWidget {
    protected final ScreenPosition position;
    private final ResourceLocation texture;
    private final int time;
    private final boolean horizontal, endToStart, fullToEmpty;
    private final int width, height;
    private final int u, v;
    private final int regionWidth, regionHeight;
    private final int textureWidth, textureHeight;

    public AnimatedTextureWidget(ResourceLocation texture, int x, int y, int width, int height, int u, int v,
                                 int regionWidth, int regionHeight, int textureWidth, int textureHeight, int time,
                                 boolean horizontal, boolean endToStart, boolean fullToEmpty) {
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
        this.regionWidth = regionWidth;
        this.regionHeight = regionHeight;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.time = time;
        this.horizontal = horizontal;
        this.endToStart = endToStart;
        this.fullToEmpty = fullToEmpty;
        position = new ScreenPosition(x, y);
    }

    public AnimatedTextureWidget(ResourceLocation texture, int x, int y, int width, int height, int u, int v, int time,
                                 boolean horizontal, boolean endToStart, boolean fullToEmpty) {
        this(texture, x, y, width, height, u, v, width, height, 256, 256, time, horizontal, endToStart, fullToEmpty);
    }

    @NotNull
    @Override
    public ScreenPosition getPosition() {
        return position;
    }

    @Override
    public void drawWidget(@NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int subTime = (int) (System.currentTimeMillis() % time);
        int x = position.x();
        int y = position.y();

        if (endToStart ^ fullToEmpty) {
            subTime = time - subTime;
        }

        int mx = x, my = y;
        int mw = width, mh = height;
        int mu = u, mv = v;
        int mrw = regionWidth, mrh = regionHeight;

        if (horizontal) {
            if (endToStart) {
                mx = x + width * subTime / time;
                mu = u + regionWidth * subTime / time;
                mw = width - (mx - x);
                mrw = regionWidth - (mu - u);
            } else {
                mw = width * subTime / time;
                mrw = regionWidth * subTime / time;
            }
        } else {
            if (endToStart) {
                my = y + height * subTime / time;
                mv = v + regionHeight * subTime / time;
                mh = height - (my - y);
                mrh = regionHeight - (mv - v);
            } else {
                mh = height * subTime / time;
                mrh = regionHeight * subTime / time;
            }
        }

        guiGraphics.blit(texture, mx, my, mw, mh, mu, mv, mrw, mrh, textureWidth, textureHeight);
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, double mouseX, double mouseY) {
        IRecipeWidget.super.getTooltip(tooltip, mouseX, mouseY);
    }
}
