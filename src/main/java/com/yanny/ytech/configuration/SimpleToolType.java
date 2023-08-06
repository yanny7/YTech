package com.yanny.ytech.configuration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum SimpleToolType {
    SHARP_FLINT("sharp_flint", "Sharp Flint", () -> new SwordItem(Tiers.WOOD, 0, 0, new Item.Properties()) {
        @Override
        public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull LivingEntity entity) {
            stack.hurtAndBreak(1, entity, (livingEntity) -> livingEntity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            return true;
        }
    }),
    ;

    @NotNull public final String key;
    @NotNull public final String name;
    @NotNull public final Supplier<Item> itemSupplier;

    SimpleToolType(@NotNull String key, @NotNull String name, @NotNull Supplier<Item> itemSupplier) {
        this.key = key;
        this.name = name;
        this.itemSupplier = itemSupplier;
    }
}
