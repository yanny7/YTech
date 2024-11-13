package com.yanny.ytech.compatibility.jei;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.recipe.SmeltingRecipe;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItems;
import com.yanny.ytech.registration.YTechRecipeTypes;
import mezz.jei.api.constants.RecipeTypes;
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

import java.util.Arrays;
import java.util.List;

public class SmeltingRecipeCategory extends AbstractRecipeCategory<SmeltingRecipe> {
    public static final RecipeType<SmeltingRecipe> RECIPE_TYPE = RecipeType.create(YTechMod.MOD_ID, "smelting", SmeltingRecipe.class);

    public SmeltingRecipeCategory(IGuiHelper guiHelper) {
        super(
                RECIPE_TYPE,
                Component.translatable("emi.category.ytech.smelting"),
                guiHelper.createDrawableItemLike(YTechItems.PRIMITIVE_SMELTER.get()),
                94, 41
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SmeltingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 5)
                .setStandardSlotBackground()
                .addItemStacks(Arrays.stream(recipe.ingredient().getItems()).map((i) -> i.copyWithCount(recipe.inputCount())).toList());
        builder.addSlot(RecipeIngredientRole.CATALYST, 39,  24)
                .setStandardSlotBackground()
                .addIngredients(recipe.mold());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 72, 5)
                .setOutputSlotBackground()
                .addItemStack(recipe.result());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, SmeltingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addAnimatedRecipeArrow(recipe.smeltingTime()).setPosition(36, 5);
        builder.addWidget(new TemperatureWidget(0, 2, 4000));
        builder.addAnimatedRecipeFlame(1600).setPosition(10, 24);
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull SmeltingRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        super.getTooltip(tooltip, recipe, recipeSlotsView, mouseX, mouseY);

        if (mouseX >= 36 && mouseX <= 54 + 24 && mouseY >= 5 && mouseY <= 5 + 17) {
            tooltip.add(Component.translatable("emi.smelting.time", recipe.smeltingTime() / 20.0F));
        }

        if (mouseX >= 0 && mouseX <= 8 && mouseY >= 2 && mouseY <= 2 + 38) {
            tooltip.add(Component.translatable("emi.smelting.temperature", recipe.minTemperature()));
        }
    }

    public static List<SmeltingRecipe> getRecipes(@NotNull RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(YTechRecipeTypes.SMELTING.get()).stream().map(RecipeHolder::value).toList();
    }

    public static void registerCatalyst(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(YTechBlocks.PRIMITIVE_SMELTER.get()), RECIPE_TYPE, RecipeTypes.FUELING);
        registration.addRecipeCatalyst(new ItemStack(YTechBlocks.BRICK_CHIMNEY.get()), RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(YTechBlocks.REINFORCED_BRICK_CHIMNEY.get()), RECIPE_TYPE);
    }
}
