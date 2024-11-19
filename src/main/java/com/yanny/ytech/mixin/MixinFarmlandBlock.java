package com.yanny.ytech.mixin;

import com.yanny.ytech.YTechMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = FarmBlock.class, remap = false)
public abstract class MixinFarmlandBlock implements IBlockExtension {
    @Override
    public boolean canBeHydrated(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, FluidState fluid, @NotNull BlockPos fluidPos) {
        boolean canHydrate = fluid.canHydrate(getter, fluidPos, state, pos);

        if (YTechMod.CONFIGURATION.farmlandConsumesWater() && getter instanceof ServerLevel serverLevel)  {
            if (fluid.is(Fluids.WATER) && serverLevel.random.nextInt(10) == 0) {
                BlockState blockState = getter.getBlockState(fluidPos);
                if (blockState.is(Blocks.WATER)) {
                    serverLevel.setBlock(fluidPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                } else if (blockState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                    serverLevel.setBlock(fluidPos, blockState.setValue(BlockStateProperties.WATERLOGGED, false), Block.UPDATE_CLIENTS);
                }
            } else if (fluid.is(Fluids.FLOWING_WATER)) {
                return false;
            }
        }

        return canHydrate;
    }
}
