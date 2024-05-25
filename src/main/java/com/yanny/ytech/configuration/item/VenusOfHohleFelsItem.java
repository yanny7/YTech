package com.yanny.ytech.configuration.item;

import com.yanny.ytech.compatibility.CuriosCapability;
import com.yanny.ytech.registration.YTechMobEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.ChatFormatting.DARK_GRAY;
import static net.minecraft.ChatFormatting.GOLD;

public class VenusOfHohleFelsItem extends Item {
    public VenusOfHohleFelsItem() {
        super(new Properties().stacksTo(1).durability(0));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (ModList.get().isLoaded("curios")) {
            return CuriosCapability.createCapability(stack, YTechMobEffects.VENUS_TOUCH.get());
        }

        return super.initCapabilities(stack, nbt);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        tooltipComponents.add(Component.translatable("text.ytech.hover.venus_of_hohle_fels").withStyle(DARK_GRAY));

        if (!ModList.get().isLoaded("curios")) {
            tooltipComponents.add(Component.translatable("text.ytech.info.missing_curios").withStyle(GOLD));
        }
    }
}
