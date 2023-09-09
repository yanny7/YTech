package com.yanny.ytech.configuration.screen.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class TemperatureComponent extends BottomUpAnimationComponent {
    private final Supplier<Integer> maxTemperatureGetter;
    private final Supplier<Integer> temperatureGetter;

    public TemperatureComponent(int x, int y, @NotNull Supplier<Integer> maxTemperatureGetter, @NotNull Supplier<Integer> temperatureGetter) {
        super(x, y, 8, 55, 176, 0, 184, 0);
        this.maxTemperatureGetter = maxTemperatureGetter;
        this.temperatureGetter = temperatureGetter;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int left, int top) {
        set(maxTemperatureGetter.get(), temperatureGetter.get());
        super.render(graphics, left, top);
    }

    @Override
    public void renderTooltip(@NotNull GuiGraphics guiGraphics, int left, int top, int mouseX, int mouseY) {
        if (mouseX >= left + x && mouseX < left + x + uWidth && mouseY >= top + y && mouseY < top + y + vHeight) {
            guiGraphics.renderTooltip(FONT, Component.translatable(temperatureGetter.get() + "°C"), mouseX, mouseY);
        }
    }
}
