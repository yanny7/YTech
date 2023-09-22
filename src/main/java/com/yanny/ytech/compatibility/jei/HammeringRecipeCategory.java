package com.yanny.ytech.compatibility.jei;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.SimpleBlockType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.HammeringRecipe;
import com.yanny.ytech.registration.Registration;
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
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HammeringRecipeCategory implements IRecipeCategory<HammeringRecipe> {
    public static final RecipeType<HammeringRecipe> RECIPE_TYPE = RecipeType.create(YTechMod.MOD_ID, "hammering", HammeringRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;

    public HammeringRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = Utils.modLoc("textures/gui/jei.png");
        background = guiHelper.createDrawable(location, 0, 86, 82, 52);
        icon = guiHelper.createDrawableItemStack(new ItemStack(Registration.block(SimpleBlockType.BRONZE_ANVIL)));
        localizedName = Component.translatable("gui.ytech.category.hammering");
    }

    @NotNull
    @Override
    public RecipeType<HammeringRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, HammeringRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addIngredients(recipe.ingredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61,  9).addItemStack(recipe.result());
        builder.addSlot(RecipeIngredientRole.CATALYST, 29,  31).addIngredients(recipe.tool());
    }

    public static List<HammeringRecipe> getRecipes(@NotNull RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(HammeringRecipe.RECIPE_TYPE).stream().toList();
    }

    public static void registerCatalyst(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Registration.block(SimpleBlockType.BRONZE_ANVIL)), RECIPE_TYPE);
    }
}
