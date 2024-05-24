package com.yanny.ytech.configuration.item;

import com.yanny.ytech.registration.YTechMobEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

import static net.minecraft.ChatFormatting.DARK_GRAY;

public class LionManItem extends Item implements ICurioItem {
    public LionManItem() {
        super(new Properties().stacksTo(1).durability(0));
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        slotContext.entity().addEffect(new MobEffectInstance(YTechMobEffects.LION_HEART.get(), -1, 0, false, false));
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        slotContext.entity().removeEffect(YTechMobEffects.LION_HEART.get());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        tooltipComponents.add(Component.translatable("text.ytech.hover.lion_man").withStyle(DARK_GRAY));
    }
}
