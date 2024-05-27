package com.yanny.ytech.configuration.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LuckyStoneMobEffect extends MobEffect {
    public LuckyStoneMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFFFF);
        addAttributeModifier(Attributes.LUCK, "c1fb546b-7c2a-44fe-8951-539a4d19d7f1", 1.0, AttributeModifier.Operation.ADDITION);
    }
}
