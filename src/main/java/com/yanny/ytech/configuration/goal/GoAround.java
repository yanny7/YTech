package com.yanny.ytech.configuration.goal;

import com.yanny.ytech.configuration.block_entity.MillstoneBlockEntity;
import com.yanny.ytech.configuration.entity.GoAroundEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

public class GoAround extends Goal {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final double STEP = Math.PI * 2 / 32.0;
    private static final int RADIUS = 3;
    @NotNull GoAroundEntity mob;
    Vec3 pos;

    public GoAround(@NotNull GoAroundEntity mob, Vec3 pos) {
        this.pos = pos;
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        MillstoneBlockEntity blockEntity = this.mob.getDevice();

        if (blockEntity != null) {
            return blockEntity.isMilling();
        }

        return false;
    }

    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone() && this.mob.isAlive() && this.mob.getDevice() != null && this.mob.getDevice().isMilling();
    }

    @Override
    public void start() {
        Path path = this.mob.getNavigation().getPath();

        if (path == null || path.isDone()) {
            if (!this.mob.getNavigation().moveTo(createPath(), 1.0)) {
                LOGGER.warn("Failed to create path!");
            }
        }
    }

    @Override
    public void stop() {
        super.stop();

        if (this.mob.getVehicle() instanceof Mob mobVehicle) {
            mobVehicle.navigation.stop();
        }

        MillstoneBlockEntity millstoneBlockEntity = this.mob.getDevice();

        if (millstoneBlockEntity != null) {
            this.mob.getDevice().onFinished();
        }
    }

    private Path createPath() {
        BlockPos startingPos = null;
        Vec3 mobPos = this.mob.getOnPos().getCenter();
        ArrayList<BlockPos> posList = new ArrayList<>(17);

        for (double angle = 0; angle < Math.PI * 2 + STEP; angle += STEP) {
            double x = pos.x + RADIUS * Math.cos(angle);
            double y = pos.y;
            double z = pos.z + RADIUS * Math.sin(angle);
            BlockPos blockPos = BlockPos.containing(x, y, z);

            if (startingPos == null || startingPos.distToCenterSqr(mobPos) > blockPos.distToCenterSqr(mobPos)) {
                startingPos = blockPos;
            }

            if (!posList.contains(blockPos)) {
                posList.add(blockPos);
            }
        }

        int index = posList.indexOf(startingPos);
        Stream<BlockPos> ordered = Stream.concat(posList.subList(index, posList.size()).stream(), posList.subList(0, index).stream());

        return new SamePathIsFine(ordered.map((n) -> new Node(n.getX(), n.getY(), n.getZ())).toList(), startingPos, true);
    }

    static class SamePathIsFine extends Path {
        public SamePathIsFine(List<Node> pNodes, BlockPos pTarget, boolean pReached) {
            super(pNodes, pTarget, pReached);
        }

        @Override
        public boolean sameAs(@Nullable Path path) {
            return false;
        }
    }
}
