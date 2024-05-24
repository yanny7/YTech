package com.yanny.ytech.compatibility;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class CuriosCapability implements ICurio {
    private final ItemStack stack;
    private final MobEffect effect;

    private CuriosCapability(ItemStack stack, MobEffect effect) {
        this.stack = stack;
        this.effect = effect;
    }

    @Override
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack stack) {
        slotContext.entity().addEffect(new MobEffectInstance(effect, -1, 0, false, false));
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack stack) {
        slotContext.entity().removeEffect(effect);
    }

    public static ICapabilityProvider createCapability(ItemStack stack, MobEffect effect) {
        return CuriosApi.createCurioProvider(new CuriosCapability(stack, effect));
    }
}
