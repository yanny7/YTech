package com.yanny.ytech.compatibility.jei;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.recipe.TanningRecipe;
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

public class TanningRecipeCategory extends AbstractRecipeCategory<TanningRecipe> {
    public static final RecipeType<TanningRecipe> RECIPE_TYPE = RecipeType.create(YTechMod.MOD_ID, "tanning", TanningRecipe.class);

    public TanningRecipeCategory(IGuiHelper guiHelper) {
        super(
                RECIPE_TYPE,
                Component.translatable("emi.category.ytech.tanning"),
                guiHelper.createDrawableItemLike(YTechItems.TANNING_RACKS.get(MaterialType.OAK_WOOD).get()),
                84, 41
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TanningRecipe recipe, @NotNull IFocusGroup focuses) {
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
    public void createRecipeExtras(IRecipeExtrasBuilder builder, @NotNull TanningRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addRecipeArrow().setPosition(26, 5);
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull TanningRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= 26 && mouseX <= 26 + 24 && mouseY >= 5 && mouseY <= 5 + 17) {
            tooltip.add(Component.translatable("emi.tanning.hit_count", recipe.hitCount()));
        }
    }

    public static List<TanningRecipe> getRecipes(@NotNull RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(YTechRecipeTypes.TANNING.get()).stream().map(RecipeHolder::value).toList();
    }

    public static void registerCatalyst(@NotNull IRecipeCatalystRegistration registration) {
        YTechItems.TANNING_RACKS.values().forEach((item) -> registration.addRecipeCatalyst(new ItemStack(item.get()), RECIPE_TYPE));
    }
}
