package com.yanny.ytech.network.kinetic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.List;
import java.util.stream.Collectors;

public class KineticUtils {
    private KineticUtils() {
    }

    public static List<BlockPos> getDirections(List<Direction> validDirections, BlockPos position, Direction currentDirection) {
        return validDirections.stream().map((direction -> position.offset(rotateDirection(currentDirection, direction).getNormal()))).collect(Collectors.toList());
    }

    public static Direction rotateDirection(Direction currentDirection, Direction baseDirection) {
        return Direction.from2DDataValue((currentDirection.get2DDataValue() + baseDirection.get2DDataValue()) % 4);
    }
}
