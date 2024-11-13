package com.yanny.ytech.compatibility.jei;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.recipe.WorkspaceCraftingRecipe;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItemTags;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorkspaceCraftingCategory extends AbstractRecipeCategory<WorkspaceCraftingRecipe> {
    public static final RecipeType<WorkspaceCraftingRecipe> RECIPE_TYPE = RecipeType.create(YTechMod.MOD_ID, "workspace_crafting", WorkspaceCraftingRecipe.class);

    public WorkspaceCraftingCategory(IGuiHelper guiHelper) {
        super(
                RECIPE_TYPE,
                Component.translatable("emi.category.ytech.workspace_crafting"),
                guiHelper.createDrawableItemLike(YTechItems.CRAFTING_WORKSPACE.get()),
                152, 163
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WorkspaceCraftingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.CATALYST, 99,  93)
                .setStandardSlotBackground()
                .addIngredients(Ingredient.of(YTechItemTags.SHARP_FLINTS));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 132,  73)
                .setOutputSlotBackground()
                .addItemStack(recipe.result());

        int i = 0;
        int posX = 72;
        int posY = 128;

        for (int y = 0; y < 3; y++) {
            for (int z = 2; z >= 0; z--) {
                for (int x = 0; x < 3; x++) {
                    builder.addSlot(RecipeIngredientRole.INPUT, posX - x * 18 - z * 18, posY + x * 9 - z * 9 - y * 55)
                            .setStandardSlotBackground()
                            .addIngredients(recipe.getIngredients().get(i));
                    i++;
                }
            }
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, @NotNull WorkspaceCraftingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addRecipeArrow().setPosition(96, 74);
    }

    public static List<WorkspaceCraftingRecipe> getRecipes(@NotNull RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(YTechRecipeTypes.WORKSPACE_CRAFTING.get()).stream().map(RecipeHolder::value).toList();
    }

    public static void registerCatalyst(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(YTechBlocks.CRAFTING_WORKSPACE.get()), RECIPE_TYPE);
    }
}
