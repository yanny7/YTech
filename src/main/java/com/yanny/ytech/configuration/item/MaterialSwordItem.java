package com.yanny.ytech.configuration.item;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialItemType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class MaterialSwordItem extends SwordItem {
    public MaterialSwordItem(@NotNull Holder.ItemHolder holder) {
        super(holder.material.getTier(), 3, -2.4f, new Properties());
    }

    public static void registerRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, holder.item.get())
                .define('S', Items.STICK)
                .define('#', MaterialItemType.PLATE.itemTag.get(holder.material))
                .pattern("#")
                .pattern("#")
                .pattern("S")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(MaterialItemType.PLATE.itemTag.get(holder.material)))
                .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, holder.key));
    }
}
