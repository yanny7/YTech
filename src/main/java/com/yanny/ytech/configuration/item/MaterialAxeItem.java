package com.yanny.ytech.configuration.item;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialItemType;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.SimpleItemType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class MaterialAxeItem extends AxeItem {
    @NotNull private final Holder.ItemHolder holder;

    public MaterialAxeItem(@NotNull Holder.ItemHolder holder) {
        super(holder.material.getTier(), 6.0f, -3.2f, new Item.Properties());
        this.holder = holder;
    }

    @Override
    public boolean hasCraftingRemainingItem(@NotNull ItemStack stack) {
        return holder.material == MaterialType.FLINT && stack.getMaxDamage() - stack.getDamageValue() > 1;
    }

    @Override
    public ItemStack getCraftingRemainingItem(@NotNull ItemStack itemStack) {
        ItemStack result = itemStack.copy();
        result.setDamageValue(itemStack.getDamageValue() + 1);
        return result;
    }

    public static void registerRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        switch (holder.material) {
            case FLINT ->
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, holder.item.get())
                            .requires(Items.STICK)
                            .requires(Items.FLINT)
                            .requires(SimpleItemType.GRASS_TWINE.itemTag)
                            .unlockedBy(RecipeProvider.getHasName(Items.STICK), RecipeProvider.has(Items.STICK))
                            .save(recipeConsumer, Utils.modLoc(holder.key));
            default -> ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, holder.item.get())
                    .define('S', Items.STICK)
                    .define('#', MaterialItemType.PLATE.itemTag.get(holder.material))
                    .pattern("##")
                    .pattern("#S")
                    .pattern(" S")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(MaterialItemType.PLATE.itemTag.get(holder.material)))
                    .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, holder.key));
        }
    }
}
