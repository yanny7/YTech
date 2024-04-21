package com.yanny.ytech.configuration.entity;

import com.yanny.ytech.configuration.block_entity.MillstoneBlockEntity;
import com.yanny.ytech.configuration.goal.GoAround;
import com.yanny.ytech.registration.YTechEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GoAroundEntity extends Mob implements IEntityWithComplexSpawn {
    @NotNull private static final Logger LOGGER = LogManager.getLogger();
    @Nullable private BlockPos position = null;
    @Nullable private MillstoneBlockEntity millstoneBlockEntity;

    public GoAroundEntity(EntityType<GoAroundEntity> entityType, Level pLevel) {
        super(entityType, pLevel);
    }

    public GoAroundEntity(@NotNull Mob mob, @NotNull BlockPos position, @NotNull Level level) {
        super(YTechEntityTypes.GO_AROUND.get(), level);
        this.position = position;
        millstoneBlockEntity = (MillstoneBlockEntity) level().getBlockEntity(position);
        this.setPos(mob.getX(), mob.getY(), mob.getZ());
        this.setXRot(mob.getXRot());
        this.setYRot(mob.getYRot());
        this.setYHeadRot(mob.getYHeadRot());
        this.setYBodyRot(mob.getVisualRotationYInDegrees());
        this.setPose(mob.getPose());
        leashToMillstone();
        registerGoals();
    }

    @Override
    protected void registerGoals() {
        if (!level().isClientSide && this.position != null) {
            this.goalSelector.removeAllGoals(goal -> true);
            goalSelector.addGoal(0, new GoAround(this, this.position.getCenter()));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 5.0D).add(Attributes.MOVEMENT_SPEED, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);

        if (position != null) {
            pCompound.putInt("pX", this.position.getX());
            pCompound.putInt("pY", this.position.getY());
            pCompound.putInt("pZ", this.position.getZ());
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        if (pCompound.contains("pX") && pCompound.contains("pY") && pCompound.contains("pZ")) {
            this.position = new BlockPos(pCompound.getInt("pX"), pCompound.getInt("pY"), pCompound.getInt("pZ"));
            registerGoals();
        }
    }

    @Override
    public boolean startRiding(@NotNull Entity entity, boolean pForce) {
        if (entity instanceof Mob mob) {
            mob.navigation = new FixedGroundPathNavigation(mob, mob.level());
        } else {
            LOGGER.warn("Failed to start riding entity (not a Mob): {}", entity);
        }

        return super.startRiding(entity, pForce);
    }

    @Override
    public void stopRiding() {
        super.stopRiding();

        if (!level().isClientSide && getRemovalReason() == null) {
            if (this.millstoneBlockEntity != null) {
                this.millstoneBlockEntity.removeLeash();
            } else {
                LOGGER.warn("Failed to remove BE because is NULL");
            }

            this.remove(RemovalReason.DISCARDED);
            this.spawnAtLocation(Items.LEAD);
        }
    }

    @Override
    public void tick() {
        if (this.millstoneBlockEntity == null) {
            this.leashToMillstone();
        }

        super.tick();
    }

    @Override
    public void rideTick() {
        Entity vehicle = getVehicle();

        super.rideTick();

        if (vehicle != null) {
            // rotate in vehicle direction
            this.yBodyRot = vehicle.getVisualRotationYInDegrees();
        }
    }

    @Nullable
    public MillstoneBlockEntity getDevice() {
        return this.millstoneBlockEntity;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(position != null);

        if (position != null) {
            friendlyByteBuf.writeInt(this.position.getX());
            friendlyByteBuf.writeInt(this.position.getY());
            friendlyByteBuf.writeInt(this.position.getZ());
        } else {
            LOGGER.warn("Sending spawn packet without position!");
        }
    }

    @Override
    public void readSpawnData(FriendlyByteBuf friendlyByteBuf) {
        if (friendlyByteBuf.readBoolean()) {
            this.position = new BlockPos(friendlyByteBuf.readInt(), friendlyByteBuf.readInt(), friendlyByteBuf.readInt());
        } else {
            LOGGER.warn("Received spawn packet without position!");
        }
    }

    private void leashToMillstone() {
        if (position != null) {
            millstoneBlockEntity = (MillstoneBlockEntity) level().getBlockEntity(position);

            if (millstoneBlockEntity != null) {
                millstoneBlockEntity.setLeashed(this);
            } else {
                LOGGER.warn("Failed to load BE at {}", position);
            }
        } else {
            LOGGER.warn("NULL position!");
        }
    }

    static class FixedGroundPathNavigation extends GroundPathNavigation {
        public FixedGroundPathNavigation(Mob pMob, Level pLevel) {
            super(pMob, pLevel);
        }

        @Override
        protected void doStuckDetection(@NotNull Vec3 pPositionVec3) {
            if (this.tick - this.lastStuckCheck > 100) {
                float f = this.mob.getSpeed() >= 1.0F ? this.mob.getSpeed() : this.mob.getSpeed() * this.mob.getSpeed();
                float f1 = f * 100.0F * 0.25F;

                if (pPositionVec3.distanceToSqr(this.lastStuckCheckPos) < (double)(f1 * f1)) {
                    this.isStuck = true;
                    this.stop();
                } else {
                    this.isStuck = false;
                }

                this.lastStuckCheck = this.tick;
                this.lastStuckCheckPos = pPositionVec3;
            }

            if (this.path != null && !this.path.isDone()) {
                Vec3i vec3i = this.path.getNextNodePos();
                long i = this.level.getGameTime();

                if (vec3i.equals(this.timeoutCachedNode)) {
                    this.timeoutTimer += i - this.lastTimeoutCheck;
                } else {
                    this.timeoutCachedNode = vec3i;
                    double d0 = pPositionVec3.distanceTo(Vec3.atBottomCenterOf(this.timeoutCachedNode));
                    this.timeoutLimit = this.mob.getSpeed() > 0.0F ? d0 / (double)this.mob.getSpeed() * 20.0 : 0.0;
                    // fix bug in navigation code
                    this.timeoutTimer = 0;
                }

                if (this.timeoutLimit > 0.0 && (double)this.timeoutTimer > this.timeoutLimit * 3.0) {
                    this.timeoutPath();
                }

                this.lastTimeoutCheck = i;
            }
        }
    }
}
