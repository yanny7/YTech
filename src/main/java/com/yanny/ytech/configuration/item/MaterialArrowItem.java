package com.yanny.ytech.configuration.item;

import com.yanny.ytech.configuration.MaterialItemType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.ArrowEntity;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

public class MaterialArrowItem extends ArrowItem {
    @NotNull private final Holder.ItemHolder holder;

    public MaterialArrowItem(@NotNull Holder.ItemHolder holder) {
        super(new Properties());
        this.holder = holder;
    }

    @NotNull
    @Override
    public AbstractArrow createArrow(@NotNull Level level, @NotNull ItemStack stack, @NotNull LivingEntity shooter) {
        ArrowEntity arrow = new ArrowEntity(level, shooter, stack.getItem());
        Triple<MobEffect, Integer, Integer> effect = holder.material.effect;

        if (effect != null) {
            arrow.addEffect(new MobEffectInstance(effect.getLeft(), effect.getMiddle(), effect.getRight()));
        }

        return arrow;
    }

    public static void registerRecipe(@NotNull Holder.ItemHolder holder, @NotNull RecipeOutput recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, holder.item.get())
                .define('S', Items.STICK)
                .define('F', Items.FEATHER)
                .define('#', MaterialItemType.BOLT.itemTag.get(holder.material))
                .pattern("#")
                .pattern("S")
                .pattern("F")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(MaterialItemType.BOLT.itemTag.get(holder.material)))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }
}
