package com.yanny.ytech.network.kinetic.block_entity;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.network.kinetic.IKineticBlockEntity;
import com.yanny.ytech.network.kinetic.KineticType;
import com.yanny.ytech.network.kinetic.KineticUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KineticBlockEntity extends BlockEntity implements IKineticBlockEntity {
    private static final String NETWORK_ID = "networkId";

    protected int networkId = -1;
    private final List<BlockPos> validNeighbors;
    private final KineticType kineticType;
    private final int stress;

    public KineticBlockEntity(BlockEntityType<? extends BlockEntity> entityType, BlockPos pos, BlockState blockState, Direction currentDirection,
                              List<Direction> validConnections, KineticType kineticType, int stress) {
        super(entityType, pos, blockState);
        validNeighbors = KineticUtils.getDirections(validConnections, pos, currentDirection);
        this.kineticType = kineticType;
        this.stress = stress;
    }

    @Override
    public List<BlockPos> getValidNeighbors() {
        return validNeighbors;
    }

    @Override
    public int getNetworkId() {
        return networkId;
    }

    @Override
    public void setNetworkId(int networkId) {
        this.networkId = networkId;
        setChanged();
    }

    @Override
    public KineticType getKineticType() {
        return kineticType;
    }

    @Override
    public int getStress() {
        return stress;
    }

    @Override
    public void onRemove() {
        YTechMod.KINETIC_PROPAGATOR.remove(this);
        setChanged();
    }

    @Override
    public void onChangedState(BlockState oldBlockState, BlockState newBlockState) {
        //TODO handle rotation etc (or not handle power change)
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (tag.contains(NETWORK_ID)) {
            networkId = tag.getInt(NETWORK_ID);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (level != null && !level.isClientSide) {
            if (networkId < 0) {
                YTechMod.KINETIC_PROPAGATOR.add(this);
                setChanged();
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(NETWORK_ID, networkId);
    }
}
