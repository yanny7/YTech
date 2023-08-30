package com.yanny.ytech.configuration.item;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.SimpleItemType;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class MaterialPickaxeItem extends PickaxeItem {
    public MaterialPickaxeItem(Tier pTier) {
        super(pTier, 1, -2.8f, new Properties());
    }

    public static void registerRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        switch (holder.material) {
            case FLINT -> ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, holder.item.get())
                    .define('S', Items.STICK)
                    .define('F', Items.FLINT)
                    .define('T', SimpleItemType.LEATHER_STRIPS.itemTag)
                    .define('#', SimpleItemType.SHARP_FLINT.itemTag)
                    .pattern("FTF")
                    .pattern("#S ")
                    .pattern(" S ")
                    .unlockedBy(RecipeProvider.getHasName(Items.FLINT), RecipeProvider.has(Items.FLINT))
                    .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, holder.key));
            default -> throw new IllegalStateException("Recipe for material " + holder.material.key + " is not defined!");
        }
    }
}
