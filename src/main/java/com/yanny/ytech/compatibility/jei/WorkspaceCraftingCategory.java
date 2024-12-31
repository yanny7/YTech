package com.yanny.ytech.compatibility.jei;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.recipe.WorkspaceCraftingRecipe;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItems;
import com.yanny.ytech.registration.YTechRecipeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IJeiHelpers;
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

public class WorkspaceCraftingCategory extends AbstractRecipeCategory<WorkspaceCraftingRecipe> {
    public static final RecipeType<WorkspaceCraftingRecipe> RECIPE_TYPE = RecipeType.create(YTechMod.MOD_ID, "workspace_crafting", WorkspaceCraftingRecipe.class);

    private final IJeiHelpers jeiHelpers;

    public WorkspaceCraftingCategory(IJeiHelpers jeiHelpers) {
        super(
                RECIPE_TYPE,
                Component.translatable("emi.category.ytech.workspace_crafting"),
                jeiHelpers.getGuiHelper().createDrawableItemLike(YTechItems.CRAFTING_WORKSPACE.get()),
                116, 97
        );
        this.jeiHelpers = jeiHelpers;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WorkspaceCraftingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.CATALYST, 62,  61)
                .setStandardSlotBackground()
                .addIngredients(recipe.tool());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 94,  40)
                .setOutputSlotBackground()
                .addItemStack(recipe.result());

        int i = 0;

        for (int y = 0; y < 3; y++) {
            for (int z = 0; z < 3; z++) {
                for (int x = 0; x < 3; x++) {
                    builder.addSlot(RecipeIngredientRole.INPUT)
                            .addIngredients(recipe.recipeItems().get(i));
                    i++;
                }
            }
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, @NotNull WorkspaceCraftingRecipe recipe, @NotNull IFocusGroup focuses) {
        WorkspaceCraftingWidget widget = new WorkspaceCraftingWidget(0, 0, builder.getRecipeSlots().getSlots(RecipeIngredientRole.INPUT), jeiHelpers);
        builder.addRecipeArrow().setPosition(59, 41);
        builder.addSlottedWidget(widget, builder.getRecipeSlots().getSlots(RecipeIngredientRole.INPUT));
        builder.addGuiEventListener(widget);
    }

    public static List<WorkspaceCraftingRecipe> getRecipes(@NotNull RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(YTechRecipeTypes.WORKSPACE_CRAFTING.get()).stream().toList();
    }

    public static void registerCatalyst(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(YTechBlocks.CRAFTING_WORKSPACE.get()), RECIPE_TYPE);
    }
}
