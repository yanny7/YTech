package com.yanny.ytech.configuration.item;

import com.yanny.ytech.configuration.MaterialItemType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class MaterialHoeItem extends HoeItem {
    public MaterialHoeItem(@NotNull Holder.ItemHolder holder) {
        super(holder.material.getTier(), 0, -3.0f, new Properties());
    }

    public static void registerRecipe(@NotNull Holder.ItemHolder holder, @NotNull RecipeOutput recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, holder.item.get())
                .define('S', Items.STICK)
                .define('#', MaterialItemType.PLATE.itemTag.get(holder.material))
                .pattern("##")
                .pattern(" S")
                .pattern(" S")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(MaterialItemType.PLATE.itemTag.get(holder.material)))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }
}
