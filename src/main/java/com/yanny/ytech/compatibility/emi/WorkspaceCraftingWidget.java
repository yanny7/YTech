package com.yanny.ytech.compatibility.emi;

import com.yanny.ytech.compatibility.EmiCompatibility;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.ButtonWidget;
import dev.emi.emi.api.widget.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;

import java.util.LinkedList;
import java.util.List;

public class WorkspaceCraftingWidget extends Widget {
    private final int x, y;
    private final int width;
    private final int height;
    private final List<EmiIngredient> ingredients;
    private final List<ButtonWidget> buttonWidgets;
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

        return false;
    }

    @Override
    public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
        List<ClientTooltipComponent> list = new LinkedList<>();

        if (layer > 0) {
            int i = (layer - 1) * 9;
            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 3; z++) {
                    EmiIngredient ingredient = ingredients.get(i);

                    if (!ingredient.isEmpty() && mouseX >= this.x + 1 + x * 18 && mouseX < this.x + 1 + (x + 1) * 18 && mouseY >= this.y + 22 + z * 18 && mouseY < this.y + 22 + (z + 1) * 18) {
                        list.addAll(ingredient.getTooltip());
                    }

                    i++;
                }
            }
        }

        return list;
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
                i = (layer - 1) * 9;
                for (int x = 0; x < 3; x++) {
                    for (int z = 0; z < 3; z++) {
                        EmiIngredient ingredient = ingredients.get(i);

                        if (!ingredient.isEmpty()) {
                            draw.blit(EmiCompatibility.TEXTURE, this.x + x * 18, this.y + 22 + z * 18, 18, 18, 40, 0, 18, 18, 256, 256);
                            ingredient.render(draw, this.x + 1 + x * 18, this.y + 23 + z * 18, delta);
                        }

                        i++;
                    }
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
