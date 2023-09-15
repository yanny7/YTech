package com.yanny.ytech.configuration.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ToolItem extends TieredItem implements Vanishable {
    public ToolItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        itemStack.hurtAndBreak(2, attacker, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    public boolean mineBlock(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull BlockState blockState,
                             @NotNull BlockPos pos, @NotNull LivingEntity livingEntity) {
        if (!level.isClientSide && blockState.getDestroySpeed(level, pos) != 0.0F) {
            itemStack.hurtAndBreak(1, livingEntity, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }

        return true;
    }

    @Override
    public boolean hasCraftingRemainingItem(@NotNull ItemStack itemStack) {
        return itemStack.getMaxDamage() - itemStack.getDamageValue() > 1;
    }

    @Override
    public ItemStack getCraftingRemainingItem(@NotNull ItemStack itemStack) {
        ItemStack result = itemStack.copy();
        result.setDamageValue(itemStack.getDamageValue() + 1);
        return result;
    }
}
