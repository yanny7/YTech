package com.yanny.ytech.configuration.renderer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// https://gist.github.com/XFactHD/337be1471ca4b60c1959dc02691a24fc
public final class FakeLevel implements BlockAndTintGetter {
    private Level level;
    private BlockPos originalPos;
    private BlockState fluid;

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
        return pPos.getY() <= 0 ? fluid : Blocks.AIR.defaultBlockState();
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockPos pPos) {
        return (pPos.getY() <= 0 ? fluid : Blocks.AIR.defaultBlockState()).getFluidState();
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

    public void setData(@NotNull BlockEntity blockEntity, @NotNull BlockState fluid) {
        originalPos = blockEntity.getBlockPos();
        level = blockEntity.getLevel();
        this.fluid = fluid;
    }

    public void clearData() {
        originalPos = null;
        level = null;
        fluid = null;
    }
}
