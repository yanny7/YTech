package com.yanny.ytech.configuration.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LionHeartMobEffect extends MobEffect {
    public LionHeartMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFFFF);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, "244928b7-82c7-4a28-8392-a404337436cc", 2.0, AttributeModifier.Operation.ADD_VALUE);
    }
}
