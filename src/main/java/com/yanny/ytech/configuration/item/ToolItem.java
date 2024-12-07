package com.yanny.ytech.configuration.item;

import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.YTechToolMaterials;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ToolItem extends Item {
    private ToolItem(Properties properties) {
        super(properties);
    }

    public static Item tool(MaterialType materialType, Properties properties) {
        properties = YTechToolMaterials.get(materialType).applyCommonProperties(properties);
        properties = properties.component(DataComponents.TOOL, new Tool(List.of(), 1.0F, 1));
        return new ToolItem(properties);
    }

    public static Item toolAndWeapon(MaterialType materialType, Properties properties) {
        ToolMaterial toolMaterial = YTechToolMaterials.get(materialType);
        properties = toolMaterial.applyCommonProperties(properties);
        properties = properties.component(DataComponents.TOOL, new Tool(List.of(), 1.0F, 1));
        properties = properties.attributes(toolMaterial.createToolAttributes(0.0F, 4.0F));
        return new ToolItem(properties);
    }

    @Override
    public boolean canAttackBlock(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity entity, @NotNull LivingEntity other) {
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack itemStack, @NotNull LivingEntity entity, @NotNull LivingEntity other) {
        itemStack.hurtAndBreak(1, other, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean canPerformAction(@NotNull ItemStack stack, @NotNull ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(itemAbility);
    }
}
