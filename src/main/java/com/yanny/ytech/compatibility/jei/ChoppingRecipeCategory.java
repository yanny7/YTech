package com.yanny.ytech.compatibility.jei;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.recipe.ChoppingRecipe;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChoppingRecipeCategory extends AbstractRecipeCategory<ChoppingRecipe> {
    public static final RecipeType<ChoppingRecipe> RECIPE_TYPE = RecipeType.create(YTechMod.MOD_ID, "chopping", ChoppingRecipe.class);

    public ChoppingRecipeCategory(IGuiHelper guiHelper) {
        super(
                RECIPE_TYPE,
                Component.translatable("emi.category.ytech.chopping"),
                guiHelper.createDrawableItemLike(YTechItems.TREE_STUMP.get()),
                84, 41
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ChoppingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 5)
                .setStandardSlotBackground()
                .addIngredients(recipe.ingredient());
        builder.addSlot(RecipeIngredientRole.CATALYST, 29,  24)
                .setStandardSlotBackground()
                .addIngredients(recipe.tool());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 62,  5)
                .setOutputSlotBackground()
                .addItemStack(recipe.result());
    }

    @Override
    public void createRecipeExtras(@NotNull IRecipeExtrasBuilder builder, @NotNull ChoppingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addRecipeArrow().setPosition(26, 5);
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull ChoppingRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= 26 && mouseX <= 26 + 24 && mouseY >= 5 && mouseY <= 5 + 17) {
            tooltip.add(Component.translatable("emi.chopping.hit_count", recipe.hitCount()));
        }
    }

    public static List<ChoppingRecipe> getRecipes(@NotNull RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(YTechRecipeTypes.CHOPPING.get()).stream().map(RecipeHolder::value).toList();
    }

    public static void registerCatalyst(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(YTechItems.TREE_STUMP.get()), RECIPE_TYPE);
    }
}
