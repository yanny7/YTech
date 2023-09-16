package com.yanny.ytech.configuration.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import org.jetbrains.annotations.NotNull;

public class BrickMoldItem extends Item implements Vanishable {
    public BrickMoldItem() {
        super(new Properties().durability(256));
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
