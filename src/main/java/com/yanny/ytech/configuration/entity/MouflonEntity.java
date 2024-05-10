package com.yanny.ytech.configuration.entity;

import com.yanny.ytech.configuration.goal.IRaidGarden;
import com.yanny.ytech.configuration.goal.RaidGardenGoal;
import com.yanny.ytech.registration.YTechBlockTags;
import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
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

public class MouflonEntity extends WildAnimalEntity implements IRaidGarden {
    private static final String TAG_MORE_WHEAT_TICKS = "MoreWheatTicks";

    private int moreWheatTicks;

    public MouflonEntity(EntityType<? extends WildAnimalEntity> entityType, Level level) {
        super(entityType, level);
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
    public EntityType<? extends AgeableMob> getDomesticBreedOffspring() {
        return EntityType.SHEEP;
    }

    @Override
    public float getVoicePitch() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.6F;
    }

    @Override
    public boolean isFood(@NotNull ItemStack pStack) {
        return pStack.is(YTechItemTags.MOUFLON_FOOD);
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

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SHEEP_AMBIENT;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.SHEEP_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SHEEP_DEATH;
    }

    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        goalSelector.addGoal(2, new TemptGoal(this, 1.25D, Ingredient.of(YTechItemTags.MOUFLON_TEMP_ITEMS), false));
        goalSelector.addGoal(3, new FollowParentGoal(this, 1.25D));
        goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 6.0F, 2.2D, 2.2D));
        goalSelector.addGoal(4, new AvoidEntityGoal<>(this, SaberToothTigerEntity.class, 8.0F, 2.2D, 2.2D));
        goalSelector.addGoal(5, new RaidGardenGoal<>(this, YTechBlockTags.MOUFLON_RAID_BLOCKS));
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3F);
    }
}
