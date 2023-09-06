package com.yanny.ytech.configuration.screen;

import com.yanny.ytech.configuration.screen.components.FireComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public class PrimitiveSmelterScreen extends MachineScreen {
    private final FireComponent fire;

    public PrimitiveSmelterScreen(AbstractContainerMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);

        fire = new FireComponent(128, 32);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(graphics, partialTicks, mouseX, mouseY);
        fire.render(graphics);
    }
}
