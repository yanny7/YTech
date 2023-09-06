package com.yanny.ytech.configuration.item;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.SimpleItemType;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.yanny.ytech.registration.Registration.HOLDER;

public class MaterialHammerItem extends SwordItem {
    public MaterialHammerItem(Tier tier) {
        super(tier, 3, -2.4f, new Properties());
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
            case STONE -> ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, holder.item.get())
                    .define('S', Items.STICK)
                    .define('T', SimpleItemType.LEATHER_STRIPS.itemTag)
                    .define('#', Items.STONE)
                    .pattern(" #T")
                    .pattern(" S#")
                    .pattern("S  ")
                    .unlockedBy(RecipeProvider.getHasName(HOLDER.simpleItems().get(SimpleItemType.LEATHER_STRIPS).item.get()), RecipeProvider.has(SimpleItemType.LEATHER_STRIPS.itemTag))
                    .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, holder.key));
            default -> throw new IllegalStateException("Recipe for material " + holder.material.key + " is not defined!");
        }
    }
}
