package com.yanny.ytech.configuration.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class WildRideMobEffect extends MobEffect {
    public WildRideMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFFFF);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "2c6b5d60-fefa-4897-b3c9-26256a75043f", 0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
