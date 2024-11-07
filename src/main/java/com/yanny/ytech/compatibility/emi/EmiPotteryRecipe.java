package com.yanny.ytech.compatibility.emi;

import com.yanny.ytech.configuration.recipe.PotteryRecipe;
import com.yanny.ytech.registration.YTechItems;
import com.yanny.ytech.registration.YTechRecipeTypes;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

import static com.yanny.ytech.compatibility.EmiCompatibility.TEXTURE;
import static com.yanny.ytech.compatibility.EmiCompatibility.ref;

public class EmiPotteryRecipe extends BasicEmiRecipe {
    public static final EmiStack WORKSTATION = EmiStack.of(YTechItems.POTTERY_WHEEL.get());
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(ref(YTechRecipeTypes.POTTERY), WORKSTATION, new EmiTexture(TEXTURE, 128, 240, 16, 16));

    public EmiPotteryRecipe(PotteryRecipe recipe) {
        super(CATEGORY, recipe.getId(), 84, 26);
        id = recipe.getId();
        inputs = List.of(EmiIngredient.of(Ingredient.of(Items.CLAY_BALL.getDefaultInstance()), recipe.count()));
        outputs = List.of(EmiStack.of(recipe.result()));
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 26, 5);
        widgetHolder.addSlot(inputs.get(0), 0, 4);
        widgetHolder.addSlot(outputs.get(0), 58, 0).large(true).recipeContext(this);
    }
}
