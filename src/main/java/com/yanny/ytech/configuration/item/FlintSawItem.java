package com.yanny.ytech.configuration.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import org.jetbrains.annotations.NotNull;

public class FlintSawItem extends ToolItem {
    public FlintSawItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_AXE) ? getTier().getSpeed() : 1.0F;
    }

    @Override
    public boolean isCorrectToolForDrops(@NotNull ItemStack stack, @NotNull BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_AXE) && TierSortingRegistry.isCorrectTierForDrops(getTier(), state);
    }
}
