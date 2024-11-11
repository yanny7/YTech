package com.yanny.ytech.compatibility.emi;

import com.yanny.ytech.configuration.recipe.WorkspaceCraftingRecipe;
import com.yanny.ytech.registration.YTechItemTags;
import com.yanny.ytech.registration.YTechItems;
import com.yanny.ytech.registration.YTechRecipeTypes;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

import static com.yanny.ytech.compatibility.EmiCompatibility.TEXTURE;
import static com.yanny.ytech.compatibility.EmiCompatibility.ref;

public class EmiWorkspaceCraftingRecipe extends BasicEmiRecipe {
    public static final EmiStack WORKSTATION = EmiStack.of(YTechItems.CRAFTING_WORKSPACE.get());
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(ref(YTechRecipeTypes.WORKSPACE_CRAFTING), WORKSTATION, new EmiTexture(TEXTURE, 96, 240, 16, 16));

    public EmiWorkspaceCraftingRecipe(WorkspaceCraftingRecipe recipe, ResourceLocation id) {
        super(CATEGORY, id, 154, 164);
        inputs = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
        catalysts = List.of(EmiIngredient.of(Ingredient.of(YTechItemTags.SHARP_FLINTS)));
        outputs = List.of(EmiStack.of(recipe.result()));
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        int i = 0;
        int posX = 72;
        int posY = 128;

        for (int y = 0; y < 3; y++) {
            for (int z = 2; z >= 0; z--) {
                for (int x = 0; x < 3; x++) {
                    widgetHolder.addSlot(inputs.get(i), posX - x * 18 - z * 18, posY + x * 9 - z * 9 - y * 55).customBackground(TEXTURE, 0, 256-18, 18, 18);
                    i++;
                }
            }
        }

        widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 96, 74);
        widgetHolder.addSlot(catalysts.get(0), 99, 98).catalyst(true);
        widgetHolder.addSlot(outputs.get(0), 128, 69).large(true).recipeContext(this);
    }
}
