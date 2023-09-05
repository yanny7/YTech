package com.yanny.ytech.compatibility.jei;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialBlockType;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.DryingRecipe;
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

import static com.yanny.ytech.registration.Registration.HOLDER;

public class DryingRecipeCategory implements IRecipeCategory<DryingRecipe> {
    public static final RecipeType<DryingRecipe> RECIPE_TYPE = RecipeType.create(YTechMod.MOD_ID, "drying", DryingRecipe.class);

    private final Font font = Minecraft.getInstance().font;
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;

    public DryingRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = Utils.modLoc("textures/gui/jei.png");
        background = guiHelper.createDrawable(location, 0, 0, 82, 34);
        icon = guiHelper.createDrawableItemStack(new ItemStack(HOLDER.blocks().get(MaterialBlockType.DRYING_RACK).get(MaterialType.OAK_WOOD).block.get()));
        localizedName = Component.translatable("gui.jei.category.drying");
    }

    @NotNull
    @Override
    public RecipeType<DryingRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, DryingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addIngredients(recipe.ingredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61,  9).addItemStack(recipe.result());
    }

    @Override
    public void draw(@NotNull DryingRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        String text = (recipe.dryingTime() / 20) + "s";
        int stringWidth = font.width(text);

        //TODO render tooltip for best/worst biomes
        guiGraphics.drawString(font, text, (getWidth() - stringWidth) / 2 - 4, 26, 0xFF808080, false);
    }

    public static List<DryingRecipe> getRecipes(@NotNull RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(DryingRecipe.RECIPE_TYPE).stream().toList();
    }

    public static void registerCatalyst(@NotNull IRecipeCatalystRegistration registration) {
        HOLDER.blocks().get(MaterialBlockType.DRYING_RACK).forEach((material, holder) -> registration.addRecipeCatalyst(new ItemStack(holder.block.get()), RECIPE_TYPE));
    }
}
