package com.yanny.ytech.compatibility.jei;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.recipe.DryingRecipe;
import com.yanny.ytech.registration.YTechItems;
import com.yanny.ytech.registration.YTechRecipeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DryingRecipeCategory extends AbstractRecipeCategory<DryingRecipe> {
    public static final RecipeType<DryingRecipe> RECIPE_TYPE = RecipeType.create(YTechMod.MOD_ID, "drying", DryingRecipe.class);

    public DryingRecipeCategory(IGuiHelper guiHelper) {
        super(
                RECIPE_TYPE,
                Component.translatable("emi.category.ytech.drying"),
                guiHelper.createDrawableItemLike(YTechItems.DRYING_RACKS.get(MaterialType.OAK_WOOD).get()),
                84, 26
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DryingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 5)
                .setStandardSlotBackground()
                .addIngredients(recipe.ingredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 62,  5)
                .setOutputSlotBackground()
                .addItemStack(recipe.result());
    }

    @Override
    public void createRecipeExtras(@NotNull IRecipeExtrasBuilder builder, @NotNull DryingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addAnimatedRecipeArrow(recipe.dryingTime()).setPosition(26, 5);
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull DryingRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= 26 && mouseX <= 26 + 24 && mouseY >= 5 && mouseY <= 5 + 17) {
            tooltip.add(Component.translatable("emi.drying.time", recipe.dryingTime() / 20.0F));
        }
    }

    public static List<DryingRecipe> getRecipes(@NotNull RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(YTechRecipeTypes.DRYING.get()).stream().toList();
    }

    public static void registerCatalyst(@NotNull IRecipeCatalystRegistration registration) {
        YTechItems.DRYING_RACKS.values().forEach((item) -> registration.addRecipeCatalyst(new ItemStack(item.get()), RECIPE_TYPE));
    }
}
