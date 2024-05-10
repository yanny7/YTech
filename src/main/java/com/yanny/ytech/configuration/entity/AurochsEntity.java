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
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AurochsEntity extends WildDangerousEntity implements IRaidGarden {
    private static final int EAT_ANIMATION_TICKS = 40;
    private static final String TAG_MORE_WHEAT_TICKS = "MoreWheatTicks";

    private int moreWheatTicks;
    private int eatAnimationTick;
    private EatBlockGoal eatBlockGoal;

    public AurochsEntity(EntityType<? extends WildDangerousEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public float getVoicePitch() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.6F;
    }

    @Override
    public boolean isFood(@NotNull ItemStack pStack) {
        return pStack.is(YTechItemTags.AUROCHS_FOOD);
    }

    @Override
    public void aiStep() {
        if (level().isClientSide) {
            eatAnimationTick = Math.max(0, eatAnimationTick - 1);
        }

        super.aiStep();
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == EntityEvent.EAT_GRASS) {
            this.eatAnimationTick = EAT_ANIMATION_TICKS;
        } else {
            super.handleEntityEvent(pId);
        }

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

    public float getHeadEatPositionScale(float pPartialTick) {
        if (this.eatAnimationTick <= 0) {
            return 0.0F;
        } else if (this.eatAnimationTick >= 4 && this.eatAnimationTick <= 36) {
            return 1.0F;
        } else {
            return this.eatAnimationTick < 4 ? (this.eatAnimationTick - pPartialTick) / 4.0F : -(this.eatAnimationTick - 40 - pPartialTick) / 4.0F;
        }
    }

    public float getHeadEatAngleScale(float pPartialTick) {
        if (this.eatAnimationTick > 4 && this.eatAnimationTick <= 36) {
            float f = ((float)(this.eatAnimationTick - 4) - pPartialTick) / 32.0F;
            return 0.62831855F + 0.21991149F * Mth.sin(f * 28.7F);
        } else {
            return this.eatAnimationTick > 0 ? 0.62831855F : this.getXRot() * 0.017453292F;
        }
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected EntityType<? extends AgeableMob> getDomesticBreedOffspring() {
        return EntityType.COW;
    }

    @Override
    protected Class<? extends WildDangerousEntity> getMineClass() {
        return AurochsEntity.class;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.COW_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COW_DEATH;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.playSound(SoundEvents.COW_STEP, 0.15F, 1.0F);
    }

    @Override
    protected void registerGoals() {
        eatBlockGoal = new EatBlockGoal(this);
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(YTechItemTags.AUROCHS_TEMP_ITEMS), false));
        goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        goalSelector.addGoal(5, new AvoidEntityGoal<>(this, SaberToothTigerEntity.class, 8.0F, 2.2D, 2.2D));
        goalSelector.addGoal(6, eatBlockGoal);
        goalSelector.addGoal(6, new RaidGardenGoal<>(this, YTechBlockTags.AUROCHS_RAID_BLOCKS));
        goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        registerTargetGoals();
    }

    protected void customServerAiStep() {
        eatAnimationTick = eatBlockGoal.getEatAnimationTick();
        super.customServerAiStep();

        if (moreWheatTicks > 0) {
            moreWheatTicks -= random.nextInt(3);
            if (moreWheatTicks < 0) {
                moreWheatTicks = 0;
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ARMOR, 1.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0);
    }
}
