package com.yanny.ytech.compatibility.jei;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.ChoppingRecipe;
import com.yanny.ytech.registration.YTechItems;
import com.yanny.ytech.registration.YTechRecipeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChoppingRecipeCategory implements IRecipeCategory<ChoppingRecipe> {
    public static final RecipeType<ChoppingRecipe> RECIPE_TYPE = RecipeType.create(YTechMod.MOD_ID, "chopping", ChoppingRecipe.class);

    private final Font font = Minecraft.getInstance().font;
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;

    public ChoppingRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = Utils.modLoc("textures/gui/jei.png");
        background = guiHelper.createDrawable(location, 0, 34, 82, 52);
        icon = guiHelper.createDrawableItemStack(new ItemStack(YTechItems.TREE_STUMP.get()));
        localizedName = Component.translatable("emi.category.ytech.chopping");
    }

    @NotNull
    @Override
    public RecipeType<ChoppingRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, ChoppingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addIngredients(recipe.ingredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61,  9).addItemStack(recipe.result());
        builder.addSlot(RecipeIngredientRole.CATALYST, 29,  31).addIngredients(recipe.tool());
    }

    @Override
    public void draw(@NotNull ChoppingRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.drawString(font, recipe.hitCount() + "x", 48, 35, 0xFF808080, false);
    }

    public static List<ChoppingRecipe> getRecipes(@NotNull RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(YTechRecipeTypes.CHOPPING.get()).stream().toList();
    }

    public static void registerCatalyst(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(YTechItems.TREE_STUMP.get()), RECIPE_TYPE);
    }
}
