package com.yanny.ytech.compatibility.jei;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.recipe.PotteryRecipe;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItems;
import com.yanny.ytech.registration.YTechRecipeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PotteryRecipeCategory extends AbstractRecipeCategory<PotteryRecipe> {
    public static final RecipeType<PotteryRecipe> RECIPE_TYPE = RecipeType.create(YTechMod.MOD_ID, "pottery", PotteryRecipe.class);

    public PotteryRecipeCategory(IGuiHelper guiHelper) {
        super(
                RECIPE_TYPE,
                Component.translatable("emi.category.ytech.pottery"),
                guiHelper.createDrawableItemLike(YTechItems.POTTERY_WHEEL.get()),
                84, 26
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PotteryRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 5)
                .setStandardSlotBackground()
                .addIngredients(Ingredient.of(new ItemStack(Items.CLAY_BALL, recipe.count())));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 62,  5)
                .setOutputSlotBackground()
                .addItemStack(recipe.result());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, @NotNull PotteryRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addRecipeArrow().setPosition(26, 5);
    }

    public static List<PotteryRecipe> getRecipes(@NotNull RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(YTechRecipeTypes.POTTERY.get()).stream().map(RecipeHolder::value).toList();
    }

    public static void registerCatalyst(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(YTechBlocks.POTTERS_WHEEL.get()), PotteryRecipeCategory.RECIPE_TYPE);
    }
}
