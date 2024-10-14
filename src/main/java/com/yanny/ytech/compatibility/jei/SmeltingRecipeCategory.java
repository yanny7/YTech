package com.yanny.ytech.compatibility.jei;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.SmeltingRecipe;
import com.yanny.ytech.registration.YTechItems;
import com.yanny.ytech.registration.YTechRecipeTypes;
import mezz.jei.api.constants.RecipeTypes;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class SmeltingRecipeCategory implements IRecipeCategory<SmeltingRecipe> {
    public static final RecipeType<SmeltingRecipe> RECIPE_TYPE = RecipeType.create(YTechMod.MOD_ID, "smelting", SmeltingRecipe.class);

    @NotNull private final Font font = Minecraft.getInstance().font;
    @NotNull private final IDrawable background;
    @NotNull private final IDrawable icon;
    @NotNull private final Component localizedName;

    public SmeltingRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = Utils.modLoc("textures/gui/jei.png");
        background = guiHelper.createDrawable(location, 0, 138, 82, 62);
        icon = guiHelper.createDrawableItemStack(new ItemStack(YTechItems.PRIMITIVE_SMELTER.get()));
        localizedName = Component.translatable("gui.ytech.category.smelting");
    }

    @NotNull
    @Override
    public RecipeType<SmeltingRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, SmeltingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 5).addItemStacks(Arrays.stream(recipe.ingredient().getItems()).map((i) -> i.copyWithCount(recipe.inputCount())).toList());
        builder.addSlot(RecipeIngredientRole.INPUT, 28, 41).addIngredients(recipe.mold());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61,  23).addItemStack(recipe.result());
    }

    @Override
    public void draw(@NotNull SmeltingRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        String smeltingText = (recipe.smeltingTime() / 20) + "s";
        String temperatureText = recipe.minTemperature() + "°C";

        int stringWidth = font.width(smeltingText);
        guiGraphics.drawString(font, smeltingText, getWidth() - stringWidth, getHeight() - 8, 0xFF808080, false);

        stringWidth = font.width(temperatureText);
        guiGraphics.drawString(font, temperatureText, getWidth() - stringWidth, 0, 0xFF808080, false);
    }

    public static List<SmeltingRecipe> getRecipes(@NotNull RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(YTechRecipeTypes.SMELTING.get()).stream().map(RecipeHolder::value).toList();
    }

    public static void registerCatalyst(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(YTechItems.PRIMITIVE_SMELTER.get()), RECIPE_TYPE, RecipeTypes.FUELING);
        registration.addRecipeCatalyst(new ItemStack(YTechItems.BRICK_CHIMNEY.get()), RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(YTechItems.REINFORCED_BRICK_CHIMNEY.get()), RECIPE_TYPE);
    }
}
