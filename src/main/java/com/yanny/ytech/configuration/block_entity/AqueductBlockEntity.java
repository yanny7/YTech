package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.network.irrigation.NetworkType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AqueductBlockEntity extends IrrigationBlockEntity {
    @NotNull private final AABB renderBox;
    @NotNull private final List<BlockPos> validNeighbors;

    public AqueductBlockEntity(@NotNull BlockEntityType<? extends BlockEntity> entityType, @NotNull BlockPos pos, @NotNull BlockState blockState) {
        super(entityType, pos, blockState);
        validNeighbors = Direction.Plane.HORIZONTAL.stream().map((dir) -> pos.offset(dir.getNormal())).toList();
        renderBox = new AABB(pos, pos.offset(1, 1, 1));
    }

    @Override
    public @NotNull List<BlockPos> getValidNeighbors() {
        return validNeighbors;
    }

    @Override
    public int getFlow() {
        return 0;
    }

    @Override
    public @NotNull NetworkType getNetworkType() {
        return NetworkType.STORAGE;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return renderBox;
    }
}
