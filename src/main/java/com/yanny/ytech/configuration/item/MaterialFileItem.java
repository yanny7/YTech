package com.yanny.ytech.configuration.item;

import com.yanny.ytech.configuration.MaterialItemType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class MaterialFileItem extends SwordItem {
    public MaterialFileItem(Tier tier) {
        super(tier, 1, -2.4f, new Properties());
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack itemStack, @NotNull BlockState blockState) {
        return 1.0f;
    }

    @Override
    public boolean isCorrectToolForDrops(@NotNull BlockState blockState) {
        return false;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return false;
    }

    public static void registerRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        switch (holder.material) {
            case ARSENICAL_BRONZE -> {
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, holder.item.get())
                        .define('#', MaterialItemType.PLATE.itemTag.get(holder.material))
                        .define('S', Items.STICK)
                        .pattern("#")
                        .pattern("#")
                        .pattern("S")
                        .unlockedBy(Utils.getHasName(), RecipeProvider.has(MaterialItemType.PLATE.itemTag.get(holder.material)))
                        .save(recipeConsumer, Utils.modLoc(holder.key));
            }
            default -> throw new IllegalStateException("Recipe for material " + holder.material.key + " is not defined!");
        }
    }
}
