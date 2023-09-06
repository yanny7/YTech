package com.yanny.ytech.configuration.item;

import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class MaterialSawItem extends SwordItem {
    public MaterialSawItem(Tier tier) {
        super(tier, 3, -2.4f, new Properties());
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack itemStack, @NotNull BlockState blockState) {
        return blockState.is(BlockTags.LOGS) ? getTier().getSpeed() : 1.0f;
    }

    @Override
    public boolean isCorrectToolForDrops(@NotNull BlockState blockState) {
        if (TierSortingRegistry.isTierSorted(getTier())) {
            return TierSortingRegistry.isCorrectTierForDrops(getTier(), blockState) && blockState.is(BlockTags.LOGS);
        }

        @SuppressWarnings("deprecation")
        int i = this.getTier().getLevel();

        if (i < 3 && blockState.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return false;
        } else if (i < 2 && blockState.is(BlockTags.NEEDS_IRON_TOOL)) {
            return false;
        } else {
            return (i >= 1 || !blockState.is(BlockTags.NEEDS_STONE_TOOL)) && blockState.is(BlockTags.LOGS);
        }
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return state.is(BlockTags.LOGS) && TierSortingRegistry.isCorrectTierForDrops(getTier(), state);
    }

    public static void registerRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        switch (holder.material) {
            default -> throw new IllegalStateException("Recipe for material " + holder.material.key + " is not defined!");
        }
    }
}
