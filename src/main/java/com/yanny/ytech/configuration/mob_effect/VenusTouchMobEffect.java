package com.yanny.ytech.configuration.mob_effect;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import org.jetbrains.annotations.NotNull;

public class VenusTouchMobEffect extends MobEffect {
    public VenusTouchMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFFFF);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide && pLivingEntity.getRandom().nextFloat() < 0.001) {
            BlockPos pos = pLivingEntity.getOnPos();
            Level level = pLivingEntity.level();

            BlockPos.betweenClosedStream(new AABB(pos.north(4).east(4).above(1).getCenter(), pos.south(4).west(4).below(1).getCenter()))
                    .forEach((blockPos) -> BoneMealItem.applyBonemeal(ItemStack.EMPTY, level, blockPos, FakePlayerFactory.getMinecraft((ServerLevel)level)));
        }
        return true;
    }
}
