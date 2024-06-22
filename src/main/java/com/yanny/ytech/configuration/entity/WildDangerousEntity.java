package com.yanny.ytech.configuration.entity;

import com.yanny.ytech.configuration.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public abstract class WildDangerousEntity extends WildAnimalEntity implements NeutralMob {
    private static final ResourceLocation SPEED_MODIFIER_ATTACKING_ID = Utils.modLoc("wild_speed_modifier_attacking");
    private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_ID, 0.05, AttributeModifier.Operation.ADD_VALUE);
    private static final EntityDataAccessor<Integer> DATA_GENERATION_ID = SynchedEntityData.defineId(WildDangerousEntity.class, EntityDataSerializers.INT);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final UniformInt FIRST_ANGER_SOUND_DELAY = TimeUtil.rangeOfSeconds(0, 1);
    private static final UniformInt ALERT_INTERVAL = TimeUtil.rangeOfSeconds(4, 6);

    private int ticksUntilNextAlert;
    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;
    private int playFirstAngerSoundIn;

    protected WildDangerousEntity(EntityType<? extends WildDangerousEntity> entityType, Level level) {
        super(entityType, level);
    }

    abstract Class<? extends WildDangerousEntity> getMineClass();

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        addPersistentAngerSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        readPersistentAngerSaveData(level(), tag);
    }

    public void setTarget(@Nullable LivingEntity pLivingEntity) {
        if (this.getTarget() == null && pLivingEntity != null) {
            this.playFirstAngerSoundIn = FIRST_ANGER_SOUND_DELAY.sample(this.random);
            this.ticksUntilNextAlert = ALERT_INTERVAL.sample(this.random);
        }

        if (pLivingEntity instanceof Player) {
            this.setLastHurtByPlayer((Player)pLivingEntity);
        }

        super.setTarget(pLivingEntity);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        remainingPersistentAngerTime = time;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uuid) {
        persistentAngerTarget = uuid;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(random));
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_GENERATION_ID, 0);
    }

    protected void registerTargetGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    protected void customServerAiStep() {
        AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);

        if (attributeInstance != null) {
            if (this.isAngry()) {
                if (!this.isBaby() && !attributeInstance.hasModifier(SPEED_MODIFIER_ATTACKING_ID)) {
                    attributeInstance.addTransientModifier(SPEED_MODIFIER_ATTACKING);
                }

                this.maybePlayFirstAngerSound();
            } else if (attributeInstance.hasModifier(SPEED_MODIFIER_ATTACKING_ID)) {
                attributeInstance.removeModifier(SPEED_MODIFIER_ATTACKING);
            }
        }

        this.updatePersistentAnger((ServerLevel)this.level(), true);

        if (this.getTarget() != null) {
            this.maybeAlertOthers();
        }

        if (this.isAngry()) {
            this.lastHurtByPlayerTime = this.tickCount;
        }

        super.customServerAiStep();
    }

    private void maybePlayFirstAngerSound() {
        if (this.playFirstAngerSoundIn > 0) {
            --this.playFirstAngerSoundIn;
            if (this.playFirstAngerSoundIn == 0) {
                this.playAngerSound();
            }
        }
    }

    private void playAngerSound() {
        this.playSound(Objects.requireNonNull(getDeathSound()), this.getSoundVolume() * 2.0F, this.getVoicePitch() * 0.8F);
    }

    private void maybeAlertOthers() {
        if (this.ticksUntilNextAlert > 0) {
            --this.ticksUntilNextAlert;
        } else {
            if (this.getSensing().hasLineOfSight(Objects.requireNonNull(this.getTarget()))) {
                this.alertOthers();
            }

            this.ticksUntilNextAlert = ALERT_INTERVAL.sample(this.random);
        }
    }

    private void alertOthers() {
        double followRange = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        AABB area = AABB.unitCubeFromLowerCorner(this.position()).inflate(followRange, 10.0, followRange);

        this.level().getEntitiesOfClass(getMineClass(), area, EntitySelector.NO_SPECTATORS)
                .stream()
                .filter((animal) -> animal != this)
                .filter((animal) -> animal.getTarget() == null)
                .filter((animal) -> !animal.isAlliedTo(Objects.requireNonNull(this.getTarget())))
                .forEach((animal) -> animal.setTarget(this.getTarget()));
    }
}
