package com.yanny.ytech.compatibility.emi;

import com.yanny.ytech.compatibility.EmiCompatibility;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.ButtonWidget;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WorkspaceCraftingWidget extends Widget {
    private final int x, y;
    private final int width;
    private final int height;
    private final List<EmiIngredient> ingredients;
    private final List<ButtonWidget> buttonWidgets;
    private final Map<Integer, List<SlotWidget>> slotWidgets = new HashMap<>();
    private int layer = 0;

    public WorkspaceCraftingWidget(int x, int y, int width, int height, List<EmiIngredient> ingredients) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.ingredients = ingredients;
        buttonWidgets = List.of(
                new ButtonWidget(x + 64, y, 12, 10, 16, 0, EmiCompatibility.TEXTURE, () -> layer < 3, this::upClicked),
                new ButtonWidget(x + 64, y + 10, 12, 10, 28, 0, EmiCompatibility.TEXTURE, () -> layer > 0, this::downClicked)
        );

        for(int layer = 0; layer < 3; layer++) {
            int i = layer * 9;

            slotWidgets.computeIfAbsent(layer, (k) -> new LinkedList<>());

            for (int px = 0; px < 3; px++) {
                for (int pz = 0; pz < 3; pz++) {
                    EmiIngredient ingredient = ingredients.get(i);

                    if (!ingredient.isEmpty()) {
                        slotWidgets.get(layer).add(new SlotWidget(ingredient, x + px * 18, y + 22 + pz * 18));
                    }

                    i++;
                }
            }
        }
    }

    void upClicked(double mouseX, double mouseY, int button) {
        if (layer < 3) {
            layer++;
        }
    }

    void downClicked(double mouseX, double mouseY, int button) {
        if (layer > 0) {
            layer--;
        }
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, width, height);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        for (ButtonWidget widget : buttonWidgets) {
            Bounds bounds = widget.getBounds();

            if (mouseX > bounds.left() && mouseX < bounds.right() && mouseY > bounds.top() && mouseY < bounds.bottom()) {
                return widget.mouseClicked(mouseX, mouseY, button);
            }
        }

        if (layer > 0) {
            for (SlotWidget slotWidget : slotWidgets.get(layer - 1)) {
                if (slotWidget.getBounds().contains(mouseX, mouseY)) {
                    return slotWidget.mouseClicked(mouseX, mouseY, button);
                }
            }
        }

        return false;
    }

    @Override
    public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
        if (layer > 0) {
            for (SlotWidget slotWidget : slotWidgets.get(layer - 1)) {
                if (slotWidget.getBounds().contains(mouseX, mouseY)) {
                    return slotWidget.getTooltip(mouseX, mouseY);
                }
            }
        }

        return List.of();
    }

    @Override
    public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        int i = 0;

        switch (layer) {
            case 0: {
                for (int y = 0; y < 3; y++) {
                    for (int z = 2; z >= 0; z--) {
                        for (int x = 0; x < 3; x++) {
                            EmiIngredient ingredient = ingredients.get(i);

                            if (!ingredient.isEmpty()) {
                                draw.pose().pushPose();
                                draw.pose().translate(0, 0, i * 5);
                                ingredient.render(draw, this.x + 32 - x * 8 - z * 8, this.y + 74 + x * 4 - z * 4 - y * 32, delta, EmiIngredient.RENDER_ICON);
                                draw.pose().popPose();
                            }

                            i++;
                        }
                    }
                }

                break;
            }
            case 1, 2, 3: {
                for (SlotWidget slotWidget : slotWidgets.get(layer - 1)) {
                    slotWidget.render(draw, mouseX, mouseY, delta);
                }

                draw.drawString(Minecraft.getInstance().font, Component.translatable("emi.workspace_crafting.layer", layer), this.x + 1, this.y + 1, 0, false);
                break;
            }
        }

        for (ButtonWidget widget : buttonWidgets) {
            widget.render(draw, mouseX, mouseY, delta);
        }
    }
}
