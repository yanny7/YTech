package com.yanny.ytech.configuration.mob_effect;

import com.yanny.ytech.configuration.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class WildRideMobEffect extends MobEffect {
    private static final ResourceLocation WILD_RIDE_MOVEMENT_SPEED_ID = Utils.modLoc("wild_ride_movement_speed");

    public WildRideMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFFFF);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, WILD_RIDE_MOVEMENT_SPEED_ID, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
