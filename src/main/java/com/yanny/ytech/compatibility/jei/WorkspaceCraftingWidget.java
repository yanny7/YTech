package com.yanny.ytech.compatibility.jei;

import com.yanny.ytech.configuration.Utils;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.inputs.IJeiGuiEventListener;
import mezz.jei.api.gui.inputs.RecipeSlotUnderMouse;
import mezz.jei.api.gui.widgets.ISlottedRecipeWidget;
import mezz.jei.api.helpers.IJeiHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class WorkspaceCraftingWidget implements ISlottedRecipeWidget, IJeiGuiEventListener {
    public static final ResourceLocation TEXTURE = Utils.modLoc("textures/gui/emi.png");

    private final int x;
    private final int y;
    private final List<IRecipeSlotDrawable> ingredients;
    private final IJeiHelpers jeiHelpers;
    private final List<ButtonWidget> buttonWidgets;
    private int layer = 0;

    public WorkspaceCraftingWidget(int x, int y, List<IRecipeSlotDrawable> ingredients, IJeiHelpers jeiHelpers) {
        this.x = x;
        this.y = y;
        this.ingredients = ingredients;
        this.jeiHelpers = jeiHelpers;
        buttonWidgets = List.of(
                new ButtonWidget(x + 64, y, 12, 10, 16, 0, TEXTURE, () -> layer < 3, this::upClicked),
                new ButtonWidget(x + 64, y + 10, 12, 10, 28, 0, TEXTURE, () -> layer > 0, this::downClicked)
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
    public void drawWidget(@NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int i = 0;

        switch (layer) {
            case 0: {
                for (int y = 0; y < 3; y++) {
                    for (int z = 2; z >= 0; z--) {
                        for (int x = 0; x < 3; x++) {
                            IRecipeSlotDrawable ingredient = ingredients.get(i);

                            if (!ingredient.isEmpty()) {
                                int tmpX = this.x + 32 - x * 8 - z * 8;
                                int tmpY = this.y + 74 + x * 4 - z * 4 - y * 32;

                                guiGraphics.pose().pushPose();
                                guiGraphics.pose().translate(tmpX, tmpY, i * 5);

                                ingredient.draw(guiGraphics);

                                guiGraphics.pose().popPose();
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
                        IRecipeSlotDrawable ingredient = ingredients.get(i);

                        if (!ingredient.isEmpty()) {
                            jeiHelpers.getGuiHelper().getSlotDrawable().draw(guiGraphics, this.x + x * 18, this.y + 22 + z * 18);

                            guiGraphics.pose().pushPose();
                            guiGraphics.pose().translate(this.x + 1 + x * 18, this.y + 23 + z * 18, i * 5);

                            ingredient.draw(guiGraphics);

                            guiGraphics.pose().popPose();
                        }

                        i++;
                    }
                }

                guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("emi.workspace_crafting.layer", layer), this.x + 1, this.y + 1, 0, false);
                break;
            }
        }

        for (ButtonWidget widget : buttonWidgets) {
            widget.drawWidget(guiGraphics, mouseX, mouseY);
        }
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, double mouseX, double mouseY) {
        if (layer > 0) {
            int i = (layer - 1) * 9;

            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 3; z++) {
                    IRecipeSlotDrawable ingredient = ingredients.get(i);

                    if (!ingredient.isEmpty() && mouseX >= this.x + 1 + x * 18 && mouseX < this.x + 1 + (x + 1) * 18 && mouseY >= this.y + 22 + z * 18 && mouseY < this.y + 22 + (z + 1) * 18) {
                        ingredient.getTooltip(tooltip);
                    }

                    i++;
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (ButtonWidget widget : buttonWidgets) {
            ScreenPosition bounds = widget.getPosition();

            if (mouseX > bounds.x() && mouseX < bounds.x() + 12 && mouseY > bounds.y() && mouseY < bounds.y() + 10) {
                return widget.mouseClicked(mouseX, mouseY, button);
            }
        }

        return false;
    }

    @NotNull
    @Override
    public Optional<RecipeSlotUnderMouse> getSlotUnderMouse(double mouseX, double mouseY) {
        RecipeSlotUnderMouse slot = null;

        if (layer > 0) {
            int i = (layer - 1) * 9;

            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 3; z++) {
                    IRecipeSlotDrawable ingredient = ingredients.get(i);

                    if (!ingredient.isEmpty() && mouseX >= this.x + 1 + x * 18 && mouseX < this.x + 1 + (x + 1) * 18 && mouseY >= this.y + 22 + z * 18 && mouseY < this.y + 22 + (z + 1) * 18) {
                        slot = new RecipeSlotUnderMouse(ingredient, this.x + 1 + x * 18, this.y + 22 + z * 18);
                        break;
                    }

                    i++;
                }
            }
        }

        return Optional.ofNullable(slot);
    }

    @NotNull
    @Override
    public ScreenPosition getPosition() {
        return new ScreenPosition(x, y);
    }

    @NotNull
    @Override
    public ScreenRectangle getArea() {
        return new ScreenRectangle(x, y, x + 94, y + 97);
    }
}
