package com.yanny.ytech.network.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class NetworkUtils {
    private static int messageId = 0;

    public static int getMessageId() {
        return ++messageId;
    }

    @NotNull
    public static BlockPos loadBlockPos(@NotNull CompoundTag tag) {
        return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
    }

    @NotNull
    public static CompoundTag saveBlockPos(@NotNull BlockPos pos) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
        return tag;
    }

    @Nullable
    public static ResourceLocation getLevelId(@NotNull LevelAccessor level) {
        return level.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).getKey(level.dimensionType());
    }

    @NotNull
    public static List<BlockPos> getDirections(@NotNull List<Direction> validDirections, @NotNull BlockPos position, @NotNull Direction currentDirection) {
        return validDirections.stream().map((direction -> position.offset(rotateDirection(currentDirection, direction).getNormal()))).collect(Collectors.toList());
    }

    @NotNull
    private static Direction rotateDirection(@NotNull Direction currentDirection, @NotNull Direction baseDirection) {
        return Direction.from2DDataValue((currentDirection.get2DDataValue() + baseDirection.get2DDataValue()) % 4);
    }
}
