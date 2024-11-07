package com.yanny.ytech.compatibility.jei;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.PotteryRecipe;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItems;
import com.yanny.ytech.registration.YTechRecipeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PotteryRecipeCategory implements IRecipeCategory<PotteryRecipe> {
    public static final RecipeType<PotteryRecipe> RECIPE_TYPE = RecipeType.create(YTechMod.MOD_ID, "pottery", PotteryRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;

    public PotteryRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = Utils.modLoc("textures/gui/jei.png");
        background = guiHelper.createDrawable(location, 0, 0, 82, 34);
        icon = guiHelper.createDrawableItemStack(new ItemStack(YTechItems.POTTERY_WHEEL.get()));
        localizedName = Component.translatable("emi.category.ytech.pottery");
    }

    @NotNull
    @Override
    public RecipeType<PotteryRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @NotNull
    @Override
    public Component getTitle() {
        return localizedName;
    }

    @NotNull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @NotNull
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PotteryRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addIngredients(Ingredient.of(new ItemStack(Items.CLAY_BALL, recipe.count())));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61,  9).addItemStack(recipe.result());
    }

    public static List<PotteryRecipe> getRecipes(@NotNull RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(YTechRecipeTypes.POTTERY.get()).stream().toList();
    }

    public static void registerCatalyst(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(YTechBlocks.POTTERS_WHEEL.get()), PotteryRecipeCategory.RECIPE_TYPE);
    }
}
