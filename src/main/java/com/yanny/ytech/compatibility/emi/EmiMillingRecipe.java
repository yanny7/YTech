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
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import static com.yanny.ytech.compatibility.EmiCompatibility.TEXTURE;
import static com.yanny.ytech.compatibility.EmiCompatibility.ref;

public class EmiMillingRecipe extends BasicEmiRecipe {
    public static final EmiStack WORKSTATION = EmiStack.of(YTechItems.MILLSTONE.get());
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(ref(YTechRecipeTypes.MILLING), WORKSTATION, new EmiTexture(TEXTURE, 112, 240, 16, 16));

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));

    private final MillingRecipe recipe;

    public EmiMillingRecipe(MillingRecipe recipe) {
        super(CATEGORY, recipe.getId(), 84, 37);
        this.recipe = recipe;
        id = recipe.getId();
        inputs = List.of(EmiIngredient.of(recipe.ingredient()));
        outputs = List.of(EmiStack.of(recipe.result()));
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        Component chance = Component.translatable("emi.milling.chance", DECIMAL_FORMAT.format(recipe.bonusChance() * 100));
        int width = Minecraft.getInstance().font.width(chance);

        widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 26, 5);
        widgetHolder.addSlot(inputs.get(0), 0, 4);
        widgetHolder.addSlot(outputs.get(0), 58, 0).large(true).recipeContext(this);
        widgetHolder.addText(chance, 42 - width / 2, 29, 0, false);
    }
}
