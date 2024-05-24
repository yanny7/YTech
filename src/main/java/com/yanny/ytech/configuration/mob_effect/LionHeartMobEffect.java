package com.yanny.ytech.configuration.mob_effect;

import net.minecraft.world.effect.AttackDamageMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LionHeartMobEffect extends AttackDamageMobEffect {
    public LionHeartMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFFFF, 2);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, "244928b7-82c7-4a28-8392-a404337436cc", 0.0, AttributeModifier.Operation.ADDITION);
    }
}
