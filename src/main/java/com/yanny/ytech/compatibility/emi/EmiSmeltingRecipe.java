package com.yanny.ytech.compatibility.emi;

import com.yanny.ytech.configuration.recipe.SmeltingRecipe;
import com.yanny.ytech.registration.YTechItems;
import com.yanny.ytech.registration.YTechRecipeTypes;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

import static com.yanny.ytech.compatibility.EmiCompatibility.TEXTURE;
import static com.yanny.ytech.compatibility.EmiCompatibility.ref;

public class EmiSmeltingRecipe extends BasicEmiRecipe {
    public static final EmiStack WORKSTATION = EmiStack.of(YTechItems.PRIMITIVE_SMELTER.get());
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(ref(YTechRecipeTypes.SMELTING), WORKSTATION, new EmiTexture(TEXTURE, 144, 240, 16, 16));

    public static final EmiTexture EMPTY_TEMPERATURE = new EmiTexture(TEXTURE, 0, 0, 8, 38);
    public static final EmiTexture FULL_TEMPERATURE = new EmiTexture(TEXTURE, 9, 0, 8, 38);

    private final int time;
    private final int temperature;

    public EmiSmeltingRecipe(SmeltingRecipe recipe, ResourceLocation id) {
        super(CATEGORY, id, 94, 41);
        inputs = List.of(EmiIngredient.of(recipe.ingredient(), recipe.inputCount()));
        catalysts = List.of(EmiIngredient.of(recipe.mold()));
        outputs = List.of(EmiStack.of(recipe.result()));
        time = recipe.smeltingTime();
        temperature = recipe.minTemperature();
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        widgetHolder.addFillingArrow(36, 5, 50 * time).tooltip((mx, my) -> {
            return List.of(ClientTooltipComponent.create(Component.translatable("emi.cooking.time", time / 20.0F).getVisualOrderText()));
        });
        widgetHolder.addSlot(inputs.get(0), 10, 4);

        widgetHolder.addTexture(EMPTY_TEMPERATURE, 0, 2);
        widgetHolder.addAnimatedTexture(FULL_TEMPERATURE, 0, 2, 4000, false, true, false).tooltip((mx, my) -> {
            return List.of(ClientTooltipComponent.create(Component.translatable("emi.alloying.temperature", temperature).getVisualOrderText()));
        });;

        widgetHolder.addTexture(EmiTexture.EMPTY_FLAME, 10, 24);
        widgetHolder.addAnimatedTexture(EmiTexture.FULL_FLAME, 10, 24, 4000, false, true, true);
        widgetHolder.addSlot(catalysts.get(0),39, 23).catalyst(true);
        widgetHolder.addSlot(outputs.get(0), 68, 0).large(true).recipeContext(this);
    }
}
