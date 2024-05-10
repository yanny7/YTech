package com.yanny.ytech.configuration.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.NotNull;

public class RaidGardenGoal<T extends PathfinderMob & IRaidGarden> extends MoveToBlockGoal {
    @NotNull
    private final T animal;
    private final TagKey<Block> raidBlocks;
    private boolean wantsToRaid;
    private boolean canRaid;

    public RaidGardenGoal(@NotNull T animal, TagKey<Block> raidBlocks) {
        super(animal, 0.7F, 16);
        this.animal = animal;
        this.raidBlocks = raidBlocks;
    }

    @Override
    public boolean canUse() {
        if (nextStartTick <= 0) {
            if (!EventHooks.getMobGriefingEvent(animal.level(), animal)) {
                return false;
            }

            canRaid = false;
            wantsToRaid = animal.wantsMoreFood();
        }

        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return canRaid && super.canContinueToUse();
    }

    @Override
    public void tick() {
        super.tick();
        animal.getLookControl().setLookAt(
                (double) blockPos.getX() + 0.5D,
                blockPos.getY() + 1,
                (double) blockPos.getZ() + 0.5D,
                10.0F,
                (float) animal.getMaxHeadXRot()
        );

        if (isReachedTarget()) {
            Level level = animal.level();
            BlockPos pos = blockPos.above();
            BlockState blockState = level.getBlockState(pos);

            if (canRaid && blockState.is(raidBlocks)) {
                blockState.getOptionalValue(CropBlock.AGE).ifPresentOrElse((age) -> {
                    if (age == 0) {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
                        level.destroyBlock(pos, true, animal);
                    } else {
                        level.setBlock(pos, blockState.setValue(CarrotBlock.AGE, age - 1), Block.UPDATE_CLIENTS);
                        level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(blockState));
                    }
                }, () -> {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
                    level.destroyBlock(pos, true, animal);
                });

                animal.setWantsMoreFoodTicks();
            }

            canRaid = false;
            nextStartTick = 10;
        }
    }

    @Override
    protected boolean isValidTarget(@NotNull LevelReader level, @NotNull BlockPos pos) {
        if (wantsToRaid && !canRaid) {
            BlockState state = level.getBlockState(pos.above());

            if (state.is(raidBlocks)) {
                canRaid = true;
                return true;
            }
        }

        return false;
    }
}
