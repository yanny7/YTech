package com.yanny.ytech.configuration.entity;

import com.yanny.ytech.configuration.goal.IRaidGarden;
import com.yanny.ytech.configuration.goal.RaidGardenGoal;
import com.yanny.ytech.registration.YTechBlockTags;
import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class FowlEntity extends WildAnimalEntity implements IRaidGarden {
    private static final String TAG_MORE_WHEAT_TICKS = "MoreWheatTicks";

    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping = 1.0F;
    private float nextFlap = 1.0F;
    private int moreWheatTicks;

    public FowlEntity(EntityType<? extends WildAnimalEntity> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();

        if (moreWheatTicks > 0) {
            moreWheatTicks -= random.nextInt(3);
            if (moreWheatTicks < 0) {
                moreWheatTicks = 0;
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (this.onGround() ? -1.0F : 4.0F) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);

        if (!this.onGround() && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping *= 0.9F;
        Vec3 $$0 = this.getDeltaMovement();

        if (!this.onGround() && $$0.y < 0.0) {
            this.setDeltaMovement($$0.multiply(1.0, 0.6, 1.0));
        }

        this.flap += this.flapping * 2.0F;
    }

    @Override
    public float getVoicePitch() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.6F;
    }

    @Override
    public boolean isFood(@NotNull ItemStack pStack) {
        return pStack.is(YTechItemTags.FOWL_FOOD);
    }

    @Override
    public boolean wantsMoreFood() {
        return moreWheatTicks <= 0;
    }

    @Override
    public void setWantsMoreFoodTicks() {
        moreWheatTicks = 40;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(TAG_MORE_WHEAT_TICKS, moreWheatTicks);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        moreWheatTicks = tag.getInt(TAG_MORE_WHEAT_TICKS);
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions size) {
        return this.isBaby() ? size.height * 0.85F : size.height * 0.92F;
    }

    @Override
    protected EntityType<? extends AgeableMob> getDomesticBreedOffspring() {
        return EntityType.CHICKEN;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.CHICKEN_AMBIENT;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.CHICKEN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.CHICKEN_DEATH;
    }

    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }

    protected void onFlap() {
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        goalSelector.addGoal(2, new TemptGoal(this, 1.25D, Ingredient.of(YTechItemTags.FOWL_TEMP_ITEMS), false));
        goalSelector.addGoal(3, new FollowParentGoal(this, 1.25D));
        goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 8.0F, 2.2D, 2.2D));
        goalSelector.addGoal(4, new AvoidEntityGoal<>(this, SaberToothTigerEntity.class, 8.0F, 2.2D, 2.2D));
        goalSelector.addGoal(4, new RaidGardenGoal<>(this, YTechBlockTags.FOWL_RAID_BLOCKS));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3F);
    }
}
