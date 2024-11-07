package com.yanny.ytech.compatibility.emi;

import com.yanny.ytech.configuration.recipe.HammeringRecipe;
import com.yanny.ytech.registration.YTechItems;
import com.yanny.ytech.registration.YTechRecipeTypes;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import java.util.List;

import static com.yanny.ytech.compatibility.EmiCompatibility.TEXTURE;
import static com.yanny.ytech.compatibility.EmiCompatibility.ref;

public class EmiHammeringRecipe extends BasicEmiRecipe {
    public static final EmiStack WORKSTATION = EmiStack.of(YTechItems.BRONZE_ANVIL.get());
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(ref(YTechRecipeTypes.HAMMERING), WORKSTATION, new EmiTexture(TEXTURE, 208, 240, 16, 16));

    public EmiHammeringRecipe(HammeringRecipe recipe) {
        super(CATEGORY, recipe.getId(), 84, 41);
        id = recipe.getId();
        inputs = List.of(EmiIngredient.of(recipe.ingredient()));
        catalysts = List.of(EmiIngredient.of(recipe.tool()));
        outputs = List.of(EmiStack.of(recipe.result()));
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 26, 5);
        widgetHolder.addSlot(inputs.get(0), 0, 4);
        widgetHolder.addSlot(catalysts.get(0),29, 23).catalyst(true);
        widgetHolder.addSlot(outputs.get(0), 58, 0).large(true).recipeContext(this);
    }
}
