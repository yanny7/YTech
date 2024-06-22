package com.yanny.ytech.configuration.mob_effect;

import com.yanny.ytech.configuration.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LionHeartMobEffect extends MobEffect {
    private static final ResourceLocation LION_HEART_ATTACK_DAMAGE_ID = Utils.modLoc("lion_heart_attack_damage");

    public LionHeartMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFFFF);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, LION_HEART_ATTACK_DAMAGE_ID, 2.0, AttributeModifier.Operation.ADD_VALUE);
    }
}
