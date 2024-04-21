package com.yanny.ytech.configuration.screen.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class FireComponent extends BottomUpAnimationComponent {
    @NotNull private final Supplier<Integer> progressGetter;
    @NotNull private final Supplier<Boolean> animationRendering;

    public FireComponent(int x, int y, @NotNull Supplier<Integer> progressGetter, @NotNull Supplier<Boolean> animationRendering) {
        super(x, y, 14, 14, 44, 166, 58, 166);
        this.progressGetter = progressGetter;
        this.animationRendering = animationRendering;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int left, int top) {
        set(100, progressGetter.get());
        setRenderAnimation(animationRendering.get());
        super.render(graphics, left, top);
    }

    @Override
    public void renderTooltip(@NotNull GuiGraphics guiGraphics, int left, int top, int mouseX, int mouseY) {
        if (mouseX >= left + x && mouseX < left + x + uWidth && mouseY >= top + y && mouseY < top + y + vHeight && animationRendering.get()) {
            guiGraphics.renderTooltip(FONT, Component.translatable(progressGetter.get() + "%"), mouseX, mouseY);
        }
    }
}
