package com.yanny.ytech.compatibility.jei;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.recipe.BlockHitRecipe;
import com.yanny.ytech.registration.YTechRecipeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlockHitRecipeCategory extends AbstractRecipeCategory<BlockHitRecipe> {
    public static final RecipeType<BlockHitRecipe> RECIPE_TYPE = RecipeType.create(YTechMod.MOD_ID, "block_hit", BlockHitRecipe.class);

    public BlockHitRecipeCategory(IGuiHelper guiHelper) {
        super(
            RECIPE_TYPE,
            Component.translatable("emi.category.ytech.block_hit"),
            guiHelper.createDrawableItemLike(Items.STONE),
            84, 41
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BlockHitRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 5)
                .setStandardSlotBackground()
                .addIngredients(recipe.ingredient());
        builder.addSlot(RecipeIngredientRole.CATALYST, 29,  24)
                .setStandardSlotBackground()
                .addIngredients(recipe.block());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 62,  5)
                .setOutputSlotBackground()
                .addItemStack(recipe.result());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, @NotNull BlockHitRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addRecipeArrow().setPosition(26, 5);
    }

    public static List<BlockHitRecipe> getRecipes(@NotNull RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(YTechRecipeTypes.BLOCK_HIT.get()).stream().toList();
    }
}
