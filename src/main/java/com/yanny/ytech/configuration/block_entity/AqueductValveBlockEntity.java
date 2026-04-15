package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block.AqueductValveBlock;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import com.yanny.ytech.network.irrigation.NetworkType;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AqueductValveBlockEntity extends IrrigationBlockEntity {
    private static final String TAG_FLOW = "flow";

    private int flow = 0;

    public AqueductValveBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        super(YTechBlockEntityTypes.AQUEDUCT_VALVE.get(), pos, blockState, ((AqueductValveBlock)blockState.getBlock()).getValidNeighbors(blockState, pos));
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        flow = tag.getInt(TAG_FLOW);
    }

    @Override
    protected void onLevelSet(@NotNull ServerLevel level) {
        flow = calculateFlow(level);
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag(@NotNull HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        saveAdditional(tag, provider);
        return tag;
    }

    @Override
    public int getFlow() {
        return flow;
    }

    @Override
    public @NotNull NetworkType getNetworkType() {
        return NetworkType.PROVIDER;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt(TAG_FLOW, flow);
    }

    public void neighborChanged() {
        if (level != null && !level.isClientSide) {
            int oldValue = flow;

            flow = calculateFlow((ServerLevel) level);

            if (oldValue != flow) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }

            YTechMod.IRRIGATION_PROPAGATOR.server().changed(this);
        }
    }

    public void randomTick(@NotNull ServerLevel level) {
        if (flow > 0) {
            IrrigationServerNetwork network = YTechMod.IRRIGATION_PROPAGATOR.server().getNetwork(this);

            if (network != null && level.random.nextInt(YTechMod.CONFIGURATION.getValveChanceToDrainWater()) == 0
                    && network.getFluidHandler().getFluidAmount() + YTechMod.CONFIGURATION.getValveFillAmount() <= network.getFluidHandler().getCapacity()) {
                Set<BlockPos> checkedBlocks = new HashSet<>();

                for (BlockPos pos : getValidNeighbors()) {
                    BlockState blockState = level.getBlockState(pos);
                    FluidState fluidState = blockState.getFluidState();

                    if (!fluidState.is(Fluids.WATER) && !fluidState.is(Fluids.FLOWING_WATER)) {
                        continue;
                    }

                    checkedBlocks.add(pos);

                    BlockPos waterPos = fluidState.isSource() ? pos : findWaterSource(0, level, pos, checkedBlocks);

                    if (waterPos != null) {
                        List<String> biomes = YTechMod.CONFIGURATION.getValveInfiniteWaterBiomes();
                        Holder<Biome> currentBiome = level.getBiome(waterPos);

                        for (String biome : biomes) {
                            if (biome.startsWith("#")) {
                                TagKey<Biome> tagKey = TagKey.create(Registries.BIOME, ResourceLocation.parse(biome.substring(1)));

                                if (currentBiome.is(tagKey)) {
                                    return;
                                }
                            } else {
                                ResourceLocation location = ResourceLocation.parse(biome);

                                if (currentBiome.is(location)) {
                                    return;
                                }
                            }
                        }

                        BlockState waterBlockState = level.getBlockState(waterPos);

                        if (waterBlockState.is(Blocks.WATER)) {
                            level.setBlockAndUpdate(waterPos, Blocks.AIR.defaultBlockState());
                        } else if (waterBlockState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                            level.setBlockAndUpdate(waterPos, waterBlockState.setValue(BlockStateProperties.WATERLOGGED, false));
                        }

                        neighborChanged();
                        return;
                    }
                }
            }
        }
    }

    private int calculateFlow(@NotNull ServerLevel level) {
        return getValidNeighbors().stream().anyMatch((pos) -> {
            BlockState blockState = level.getBlockState(pos);

            if (blockState.getBlock() == Blocks.WATER) {
                FluidState fluidState = blockState.getFluidState();
                return !fluidState.hasProperty(FlowingFluid.FALLING) || !fluidState.getValue(FlowingFluid.FALLING);
            }

            return false;
        }) ? YTechMod.CONFIGURATION.getValveFillAmount() : 0;
    }

    @Nullable
    private BlockPos findWaterSource(int waterLevel, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Set<BlockPos> checkedBlocks) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos offsetPos = pos.offset(direction.getNormal());
            BlockState blockState = level.getBlockState(offsetPos);
            FluidState fluidState = blockState.getFluidState();

            if ((fluidState.is(Fluids.WATER) || fluidState.is(Fluids.FLOWING_WATER)) && !checkedBlocks.contains(offsetPos)) {
                checkedBlocks.add(offsetPos);

                if (fluidState.isSource()) {
                    return offsetPos;
                } else if (fluidState.getValue(BlockStateProperties.LEVEL_FLOWING) > waterLevel) {
                    BlockPos waterPos = findWaterSource(fluidState.getValue(BlockStateProperties.LEVEL_FLOWING), level, offsetPos, checkedBlocks);

                    if (waterPos != null) {
                        return waterPos;
                    }
                }
            }
        }

        return null;
    }
}
