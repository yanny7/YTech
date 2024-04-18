package com.yanny.ytech.configuration.item;

import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.entity.ArrowEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

public class MaterialArrowItem extends ArrowItem {
    @NotNull private final MaterialType material;

    public MaterialArrowItem(@NotNull MaterialType material) {
        super(new Properties());
        this.material = material;
    }

    @NotNull
    @Override
    public AbstractArrow createArrow(@NotNull Level level, @NotNull ItemStack stack, @NotNull LivingEntity shooter) {
        ArrowEntity arrow = new ArrowEntity(level, shooter, stack.getItem());
        Triple<MobEffect, Integer, Integer> effect = material.effect;

        if (effect != null) {
            arrow.addEffect(new MobEffectInstance(effect.getLeft(), effect.getMiddle(), effect.getRight()));
        }

        return arrow;
    }
}
