package com.yanny.ytech.machine.block_entity;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.machine.TierType;
import com.yanny.ytech.network.KineticNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class KineticBlockEntity extends YTechBlockEntity {
    private static final String NETWORK_ID = "networkId";
    private static final Logger LOGGER = LogUtils.getLogger();

    protected int networkId = -1;
    protected List<Direction> validConnections;

    public KineticBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState, YTechConfigLoader.Tier tier, List<Direction> validConnections) {
        super(blockEntityType, pos, blockState, tier);
        this.validConnections = validConnections;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (tag.contains(NETWORK_ID)) {
            networkId = tag.getInt(NETWORK_ID);
        }
    }

    public int getNetworkId() {
        return networkId;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (level != null && !level.isClientSide && tier.tierType() == TierType.STONE) {
            if (networkId < 0) {
                networkId = registerToNetwork();
            }
        }
    }

    public List<BlockPos> getValidNeighbors() {
        Direction placementDir = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockPos blockPos = getBlockPos();
        return validConnections.stream().map((direction -> blockPos.offset(rotate(placementDir, direction).getNormal()))).collect(Collectors.toList());
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(NETWORK_ID, networkId);
    }

    private int registerToNetwork() {
        KineticNetwork network = YTechMod.ROTARY_PROPAGATOR.getOrCreateNetwork(this);
        int networkId = network.addConsumer(this);

        setChanged();
        return networkId;
    }

    private Direction rotate(Direction placement, Direction rotation) {
        return Direction.from2DDataValue((placement.get2DDataValue() + rotation.get2DDataValue()) % 4);
    }

    public void onRemove() {
        KineticNetwork network = YTechMod.ROTARY_PROPAGATOR.getNetwork(this);

        if (network != null) {
            network.removeConsumer(this);
            networkId = -1;
            setChanged();
        } else {
            LOGGER.warn("No network defined for blockEntity at {}", getBlockPos());
        }
    }

    public void onChangedState(BlockState oldBlockState, BlockState newBlockState) {
        //TODO handle rotation etc (or not handle power change)
    }
}
