package com.yanny.ytech.configuration.renderer;

import com.yanny.ytech.configuration.block.CraftingWorkspaceBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakeCraftingWorkspaceLevel implements BlockAndTintGetter {
    private Level level;
    private BlockPos originalPos;
    private NonNullList<ItemStack> items;

    @Override
    public float getShade(@NotNull Direction pDirection, boolean pShade) {
        return level.getShade(pDirection, pShade);
    }

    @Override
    public @NotNull LevelLightEngine getLightEngine() {
        return level.getLightEngine();
    }

    @Override
    public int getBrightness(@NotNull LightLayer pLightType, @NotNull BlockPos pBlockPos) {
        return level.getBrightness(pLightType, originalPos);
    }

    @Override
    public int getBlockTint(@NotNull BlockPos pPos, @NotNull ColorResolver pColorResolver) {
        return level.getBlockTint(originalPos, pColorResolver);
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(@NotNull BlockPos pPos) {
        return null;
    }

    @Override
    public @NotNull BlockState getBlockState(@NotNull BlockPos pPos) {
        if (pPos.getX() > 0 && pPos.getX() <= 3 && pPos.getY() > 0 && pPos.getY() <= 3 && pPos.getZ() > 0 && pPos.getZ() <= 3) {
            int[] position = CraftingWorkspaceBlock.getPosition(pPos.getX() - 1 + (pPos.getZ() - 1) * 3 + (pPos.getY() - 1) * 9);
            int index = CraftingWorkspaceBlock.getIndex(position);
            ItemStack itemStack = items.get(index);

            if (position == null || itemStack.isEmpty() || !(itemStack.getItem() instanceof BlockItem blockItem)) {
                return Blocks.AIR.defaultBlockState();
            }

            Block block = blockItem.getBlock();
            return block.defaultBlockState();
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockPos pPos) {
        return Blocks.AIR.defaultBlockState().getFluidState();
    }

    @Override
    public int getHeight() {
        return level.getHeight();
    }

    @Override
    public int getMinBuildHeight() {
        return level.getMinBuildHeight();
    }

    @Override
    public int getRawBrightness(@NotNull BlockPos pBlockPos, int pAmount) {
        return getLightEngine().getRawBrightness(originalPos, pAmount);
    }

    @Override
    public boolean canSeeSky(@NotNull BlockPos pBlockPos) {
        return this.getBrightness(LightLayer.SKY, originalPos) >= this.getMaxLightLevel();
    }

    public void setData(@NotNull BlockPos pos, @NotNull Level level, @NotNull NonNullList<ItemStack> items) {
        originalPos = pos;
        this.level = level;
        this.items = items;
    }

    public void clearData() {
        originalPos = null;
        level = null;
        items = null;
    }
}
