package com.yanny.ytech.compatibility.jei;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.AlloyingRecipe;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItems;
import com.yanny.ytech.registration.YTechRecipeTypes;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public class AlloyingRecipeCategory extends AbstractRecipeCategory<AlloyingRecipe> {
    public static final RecipeType<AlloyingRecipe> RECIPE_TYPE = RecipeType.create(YTechMod.MOD_ID, "alloying", AlloyingRecipe.class);
    public static final ResourceLocation TEXTURE = Utils.modLoc("textures/gui/emi.png");

    public AlloyingRecipeCategory(IGuiHelper guiHelper) {
        super(
                RECIPE_TYPE,
                Component.translatable("emi.category.ytech.alloying"),
                guiHelper.createDrawableItemLike(YTechItems.PRIMITIVE_ALLOY_SMELTER.get()),
                112, 38
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AlloyingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 5)
                .setStandardSlotBackground()
                .addIngredients(VanillaTypes.ITEM_STACK, Stream.of(recipe.ingredient1().getItems()).peek((r) -> r.setCount(recipe.count1())).toList());
        builder.addSlot(RecipeIngredientRole.INPUT, 28, 5)
                .setStandardSlotBackground()
                .addIngredients(VanillaTypes.ITEM_STACK, Stream.of(recipe.ingredient2().getItems()).peek((r) -> r.setCount(recipe.count2())).toList());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 91,  5)
                .setOutputSlotBackground()
                .addItemStack(recipe.result());
    }

    @Override
    public void createRecipeExtras(@NotNull IRecipeExtrasBuilder builder, @NotNull AlloyingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addAnimatedRecipeArrow(recipe.smeltingTime()).setPosition(54, 5);
        builder.addAnimatedRecipeFlame(1600).setPosition(19, 24);
        builder.addWidget(new TemperatureWidget(0, 0, 4000));
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull AlloyingRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        super.getTooltip(tooltip, recipe, recipeSlotsView, mouseX, mouseY);

        if (mouseX >= 54 && mouseX <= 54 + 24 && mouseY >= 5 && mouseY <= 5 + 17) {
            tooltip.add(Component.translatable("emi.alloying.time", recipe.smeltingTime() / 20.0F));
        }

        if (mouseX >= 0 && mouseX <= 8 && mouseY >= 0 && mouseY <= 38) {
            tooltip.add(Component.translatable("emi.alloying.temperature", recipe.minTemperature()));
        }
    }

    public static List<AlloyingRecipe> getRecipes(@NotNull RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(YTechRecipeTypes.ALLOYING.get()).stream().toList();
    }

    public static void registerCatalyst(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(YTechBlocks.PRIMITIVE_ALLOY_SMELTER.get()), RECIPE_TYPE, RecipeTypes.FUELING);
        registration.addRecipeCatalyst(new ItemStack(YTechBlocks.BRICK_CHIMNEY.get()), RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(YTechBlocks.REINFORCED_BRICK_CHIMNEY.get()), RECIPE_TYPE);
    }
}
