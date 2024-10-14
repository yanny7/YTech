package com.yanny.ytech.configuration.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ToolItem extends TieredItem {
    private ToolItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    public static ToolItem customToolItem(Tier tier, Properties properties) {
        return new ToolItem(tier, properties.component(DataComponents.TOOL, createToolProperties()));
    }

    public static ToolItem attackableToolItem(Tier tier) {
        return new ToolItem(tier, new Properties().durability(tier.getUses()).attributes(createAttributes(tier, true)).component(DataComponents.TOOL, createToolProperties()));
    }

    public static ToolItem toolItem(Tier tier) {
        return new ToolItem(tier, new Properties().durability(tier.getUses()).attributes(createAttributes(tier, false)).component(DataComponents.TOOL, createToolProperties()));
    }

    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        Tool tool = itemStack.get(DataComponents.TOOL);

        if (tool == null) {
            return false;
        }

        itemStack.hurtAndBreak(tool.damagePerBlock(), attacker, EquipmentSlot.MAINHAND);
        return true;
    }

    public boolean mineBlock(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull BlockState blockState,
                             @NotNull BlockPos pos, @NotNull LivingEntity livingEntity) {
        Tool tool = itemStack.get(DataComponents.TOOL);

        if (tool == null) {
            return false;
        }

        if (!level.isClientSide && blockState.getDestroySpeed(level, pos) != 0.0F && tool.damagePerBlock() > 0) {
            itemStack.hurtAndBreak(tool.damagePerBlock(), livingEntity, EquipmentSlot.MAINHAND);
        }

        return true;
    }

    private static Tool createToolProperties() {
        return new Tool(List.of(), 1.0F, 2);
    }

    private static ItemAttributeModifiers createAttributes(@NotNull Tier tier, boolean canAttack) {
        double attackDamageBaseline = tier.getAttackDamageBonus();
        double attackSpeedModifier = -2.5;
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

        if (canAttack) {
            builder.add(
                            Attributes.ATTACK_DAMAGE,
                            new AttributeModifier(BASE_ATTACK_DAMAGE_ID, attackDamageBaseline, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.MAINHAND
                    )
                    .add(
                            Attributes.ATTACK_SPEED,
                            new AttributeModifier(BASE_ATTACK_SPEED_ID, attackSpeedModifier, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.MAINHAND
                    );
        }

        return builder.build();
    }
}
