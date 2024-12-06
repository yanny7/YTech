package com.yanny.ytech.configuration.block_entity;

import com.google.common.collect.ImmutableList;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block.WellPulleyBlock;
import com.yanny.ytech.network.irrigation.NetworkType;
import com.yanny.ytech.registration.YTechBiomeTags;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import org.jetbrains.annotations.NotNull;

public class WellPulleyBlockEntity extends IrrigationBlockEntity {
    private static final String TAG_TIMER = "timer";
    private static final PerlinSimplexNoise WATER_SOURCE_NOISE = new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(5432)),  ImmutableList.of(0));

    protected int timer = 0;
    private int cachedFlow = -1;

    public WellPulleyBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        super(YTechBlockEntityTypes.WELL_PULLEY.get(), pos, blockState, ((WellPulleyBlock)blockState.getBlock()).getValidNeighbors(blockState, pos));
    }

    @Override
    public int getFlow() {
        if (level != null) {
            BlockState blockState = level.getBlockState(worldPosition.above());

            // cache flow
            if (cachedFlow < 0) {
                cachedFlow = getNoiseFlow(level);
            }

            if (blockState.is(YTechBlocks.WELL_PULLEY.get())) {
                return level.getBlockState(worldPosition.above()).getValue(WellPulleyBlock.ACTIVATED) ? cachedFlow: 0;
            }
        }

        return 0;
    }

    @NotNull
    @Override
    public NetworkType getNetworkType() {
        return NetworkType.PROVIDER;
    }

    @NotNull
    public InteractionResult onUse(@NotNull Level level, @NotNull BlockState aboveState, @NotNull Player player) {
        if (aboveState.is(YTechBlocks.WELL_PULLEY.get())) {
            timer = 20;

            if (!aboveState.getValue(WellPulleyBlock.ACTIVATED)) {
                level.setBlock(worldPosition.above(), aboveState.setValue(WellPulleyBlock.ACTIVATED, !aboveState.getValue(WellPulleyBlock.ACTIVATED)), Block.UPDATE_ALL);
                YTechMod.IRRIGATION_PROPAGATOR.server().changed(this);
            }

            level.playSound(null, worldPosition, YTechSoundEvents.WELL_PULLEY_USE.get(), SoundSource.BLOCKS, 1.0f, 0.8f + level.random.nextFloat() * 0.4f);
            level.blockEntityChanged(worldPosition);
            player.causeFoodExhaustion(0.5f);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider pProvider) {
        super.loadAdditional(tag, pProvider);
        timer = tag.getInt(TAG_TIMER);
    }

    public void tick(ServerLevel level) {
        if (timer > 0) {
            timer--;
            level.blockEntityChanged(worldPosition);

            if (timer == 0) {
                BlockState aboveState = level.getBlockState(worldPosition.above());

                if (aboveState.is(YTechBlocks.WELL_PULLEY.get())) {
                    level.setBlock(worldPosition.above(), aboveState.setValue(WellPulleyBlock.ACTIVATED, false), Block.UPDATE_ALL);
                    YTechMod.IRRIGATION_PROPAGATOR.server().changed(this);
                }
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider pProvider) {
        super.saveAdditional(tag, pProvider);
        tag.putInt(TAG_TIMER, timer);
    }

    public static double getWaterAbundance(BlockPos pos) {
        return WATER_SOURCE_NOISE.getValue(pos.getX() * 0.01, pos.getZ() * 0.01, false);
    }

    private int getNoiseFlow(@NotNull Level level) {
        Holder<Biome> biome = level.getBiome(worldPosition);

        if (!biome.is(YTechBiomeTags.WELL_DISABLED_BIOMES)) {
            double biomeModifier = 1;
            double waterAbundance = getWaterAbundance(worldPosition);
            boolean dryBiome = biome.is(YTechBiomeTags.WELL_DRY_BIOMES);
            boolean wetBiome = biome.is(YTechBiomeTags.WELL_WET_BIOMES);

            if (dryBiome) {
                biomeModifier = YTechMod.CONFIGURATION.getWellPulleyDryBonus();
            } else if (wetBiome) {
                biomeModifier = YTechMod.CONFIGURATION.getWellPulleyWetBonus();
            }

            return (int) Math.max(Math.round(waterAbundance * YTechMod.CONFIGURATION.getWellPulleyGeneration() * biomeModifier), 1);
        }

        return 1;
    }
}
