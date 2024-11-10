package com.yanny.ytech.compatibility.emi;

import com.yanny.ytech.configuration.recipe.AlloyingRecipe;
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

import java.util.List;

import static com.yanny.ytech.compatibility.EmiCompatibility.TEXTURE;
import static com.yanny.ytech.compatibility.EmiCompatibility.ref;

public class EmiAlloyingRecipe extends BasicEmiRecipe {
    public static final EmiStack WORKSTATION = EmiStack.of(YTechItems.PRIMITIVE_ALLOY_SMELTER.get());
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(ref(YTechRecipeTypes.ALLOYING), WORKSTATION, new EmiTexture(TEXTURE, 240, 240, 16, 16));

    public static final EmiTexture EMPTY_TEMPERATURE = new EmiTexture(TEXTURE, 0, 0, 8, 38);
    public static final EmiTexture FULL_TEMPERATURE = new EmiTexture(TEXTURE, 8, 0, 8, 38);

    private final int time;
    private final int temperature;

    public EmiAlloyingRecipe(AlloyingRecipe recipe) {
        super(CATEGORY, recipe.getId(), 112, 38);
        id = recipe.getId();
        inputs = List.of(EmiIngredient.of(recipe.ingredient1(), recipe.count1()), EmiIngredient.of(recipe.ingredient2(), recipe.count2()));
        outputs = List.of(EmiStack.of(recipe.result()));
        time = recipe.smeltingTime();
        temperature = recipe.minTemperature();
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        widgetHolder.addFillingArrow(54, 5, 50 * time).tooltip((mx, my) -> {
            return List.of(ClientTooltipComponent.create(Component.translatable("emi.alloying.time", time / 20.0F).getVisualOrderText()));
        });
        widgetHolder.addSlot(inputs.get(0), 10, 4);
        widgetHolder.addSlot(inputs.get(1), 28, 4);

        widgetHolder.addTexture(EMPTY_TEMPERATURE, 0, 0);
        widgetHolder.addAnimatedTexture(FULL_TEMPERATURE, 0, 0, 4000, false, true, false).tooltip((mx, my) -> {
            return List.of(ClientTooltipComponent.create(Component.translatable("emi.alloying.temperature", temperature).getVisualOrderText()));
        });;

        widgetHolder.addTexture(EmiTexture.EMPTY_FLAME, 19, 24);
        widgetHolder.addAnimatedTexture(EmiTexture.FULL_FLAME, 19, 24, 4000, false, true, true);
        widgetHolder.addSlot(outputs.get(0), 86, 0).large(true).recipeContext(this);
    }
}
