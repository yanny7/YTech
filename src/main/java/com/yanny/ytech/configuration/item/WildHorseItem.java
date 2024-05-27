package com.yanny.ytech.configuration.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.minecraft.ChatFormatting.DARK_GRAY;
import static net.minecraft.ChatFormatting.GOLD;

public class WildHorseItem extends Item {
    public WildHorseItem() {
        super(new Properties().stacksTo(1).durability(0));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> components, @NotNull TooltipFlag isAdvanced) {
        super.appendHoverText(stack, tooltipContext, components, isAdvanced);
        components.add(Component.translatable("text.ytech.hover.wild_horse").withStyle(DARK_GRAY));

        if (!ModList.get().isLoaded("curios")) {
            components.add(Component.translatable("text.ytech.info.missing_curios").withStyle(GOLD));
        }
    }
}
