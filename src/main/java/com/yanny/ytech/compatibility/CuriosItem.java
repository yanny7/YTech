package com.yanny.ytech.compatibility;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class CuriosItem implements ICurioItem {
    private final Holder<MobEffect> effect;

    public CuriosItem(Holder<MobEffect> effect) {
        this.effect = effect;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        slotContext.entity().addEffect(new MobEffectInstance(effect, -1, 0, false, false));
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        slotContext.entity().removeEffect(effect);
    }
}
