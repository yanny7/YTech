package com.yanny.ytech.compatibility.jei;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.WorkspaceCraftingRecipe;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItemTags;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CraftingWorkspaceCategory implements IRecipeCategory<WorkspaceCraftingRecipe> {
    public static final RecipeType<WorkspaceCraftingRecipe> RECIPE_TYPE = RecipeType.create(YTechMod.MOD_ID, "workspace_crafting", WorkspaceCraftingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;

    public CraftingWorkspaceCategory(IGuiHelper guiHelper) {
        ResourceLocation location = Utils.modLoc("textures/gui/workspace_crafting.png");
        background = guiHelper.createDrawable(location, 0, 0, 150, 144);
        icon = guiHelper.createDrawableItemStack(new ItemStack(YTechBlocks.CRAFTING_WORKSPACE.get()));
        localizedName = Component.translatable("emi.category.ytech.workspace_crafting");
    }

    @NotNull
    @Override
    public RecipeType<WorkspaceCraftingRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, WorkspaceCraftingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 129,  65).addItemStack(recipe.result());
        builder.addSlot(RecipeIngredientRole.CATALYST, 96,  84).addIngredients(Ingredient.of(YTechItemTags.SHARP_FLINTS));

        int i = 0;
        int posX = 64;
        int posY = 113;

        for (int y = 0; y < 3; y++) {
            for (int z = 2; z >= 0; z--) {
                for (int x = 0; x < 3; x++) {
                    builder.addSlot(RecipeIngredientRole.INPUT, posX - x * 16 - z * 16, posY + x * 8 - z * 8 - y * 49).addIngredients(recipe.recipeItems().get(i));
                    i++;
                }
            }
        }
    }

    public static List<WorkspaceCraftingRecipe> getRecipes(@NotNull RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(YTechRecipeTypes.WORKSPACE_CRAFTING.get()).stream().toList();
    }

    public static void registerCatalyst(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(YTechBlocks.CRAFTING_WORKSPACE.get()), RECIPE_TYPE);
    }
}
