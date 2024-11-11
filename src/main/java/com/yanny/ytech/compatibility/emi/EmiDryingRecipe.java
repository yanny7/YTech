package com.yanny.ytech.compatibility.emi;

import com.yanny.ytech.configuration.recipe.DryingRecipe;
import com.yanny.ytech.registration.YTechItemTags;
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

public class EmiDryingRecipe extends BasicEmiRecipe {
    public static final EmiIngredient WORKSTATION = EmiIngredient.of(YTechItemTags.DRYING_RACKS.tag);
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(ref(YTechRecipeTypes.DRYING), WORKSTATION, new EmiTexture(TEXTURE, 176, 240, 16, 16));

    private final int time;

    public EmiDryingRecipe(DryingRecipe recipe, ResourceLocation id) {
        super(CATEGORY, id, 84, 26);
        inputs = List.of(EmiIngredient.of(recipe.ingredient()));
        outputs = List.of(EmiStack.of(recipe.result()));
        time = recipe.dryingTime();
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        widgetHolder.addFillingArrow(26, 5, 50 * time).tooltip((mx, my) -> {
            return List.of(ClientTooltipComponent.create(Component.translatable("emi.cooking.time", time / 20.0F).getVisualOrderText()));
        });
        widgetHolder.addSlot(inputs.get(0), 0, 4);
        widgetHolder.addSlot(outputs.get(0), 58, 0).large(true).recipeContext(this);
    }
}
