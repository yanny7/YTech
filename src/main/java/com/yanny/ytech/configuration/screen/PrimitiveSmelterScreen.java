package com.yanny.ytech.configuration.screen;

import com.yanny.ytech.configuration.container.PrimitiveSmelterContainerMenu;
import com.yanny.ytech.configuration.screen.components.FireComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public class PrimitiveSmelterScreen extends MachineScreen<PrimitiveSmelterContainerMenu> {
    private final FireComponent fire;

    public PrimitiveSmelterScreen(AbstractContainerMenu container, Inventory inventory, Component title) {
        super((PrimitiveSmelterContainerMenu) container, inventory, title);

        fire = new FireComponent(128, 32);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(graphics, partialTicks, mouseX, mouseY);
        fire.set(100, menu.getTest());
        fire.render(graphics, leftPos, topPos);
    }
}
