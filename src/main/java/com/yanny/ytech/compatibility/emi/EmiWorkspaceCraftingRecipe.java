package com.yanny.ytech.compatibility.emi;

import com.yanny.ytech.configuration.recipe.WorkspaceCraftingRecipe;
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

public class EmiWorkspaceCraftingRecipe extends BasicEmiRecipe {
    public static final EmiStack WORKSTATION = EmiStack.of(YTechItems.CRAFTING_WORKSPACE.get());
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(ref(YTechRecipeTypes.WORKSPACE_CRAFTING), WORKSTATION, new EmiTexture(TEXTURE, 96, 240, 16, 16));

    public EmiWorkspaceCraftingRecipe(WorkspaceCraftingRecipe recipe) {
        super(CATEGORY, recipe.getId(), 118, 98);
        id = recipe.getId();
        inputs = recipe.recipeItems().stream().map(EmiIngredient::of).toList();
        catalysts = List.of(EmiIngredient.of(recipe.tool()));
        outputs = List.of(EmiStack.of(recipe.result()));
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        widgetHolder.add(new WorkspaceCraftingWidget(0, 0, 154, 164, inputs));
        widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 59, 41);
        widgetHolder.addSlot(catalysts.get(0), 61, 60).catalyst(true);
        widgetHolder.addSlot(outputs.get(0), 91, 36).large(true).recipeContext(this);
    }
}
