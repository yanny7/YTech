package com.yanny.ytech.compatibility.emi;

import com.yanny.ytech.configuration.recipe.MillingRecipe;
import com.yanny.ytech.registration.YTechItems;
import com.yanny.ytech.registration.YTechRecipeTypes;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

import static com.yanny.ytech.compatibility.EmiCompatibility.TEXTURE;
import static com.yanny.ytech.compatibility.EmiCompatibility.ref;

public class EmiMillingRecipe extends BasicEmiRecipe {
    public static final EmiStack WORKSTATION = EmiStack.of(YTechItems.MILLSTONE.get());
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(ref(YTechRecipeTypes.MILLING), WORKSTATION, new EmiTexture(TEXTURE, 112, 240, 16, 16));

    public EmiMillingRecipe(MillingRecipe recipe, ResourceLocation id) {
        super(CATEGORY, id, 84, 26);
        inputs = List.of(EmiIngredient.of(recipe.ingredient()));
        outputs = List.of(EmiStack.of(recipe.result()));
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 26, 5);
        widgetHolder.addSlot(inputs.get(0), 0, 4);
        widgetHolder.addSlot(outputs.get(0), 58, 0).large(true).recipeContext(this);
    }
}
