package com.yanny.ytech.configuration.mob_effect;

import com.yanny.ytech.configuration.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LuckyStoneMobEffect extends MobEffect {
    private static final ResourceLocation LUCKY_STONE_LUCK_ID = Utils.modLoc("lucky_stone_luck");

    public LuckyStoneMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFFFF);
        addAttributeModifier(Attributes.LUCK, LUCKY_STONE_LUCK_ID, 1.0, AttributeModifier.Operation.ADD_VALUE);
    }
}
