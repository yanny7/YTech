package com.yanny.ytech.configuration.entity;

import com.yanny.ytech.registration.YTechItemTags;
import com.yanny.ytech.registration.YTechSoundEvents;
import net.minecraft.sounds.SoundEvent;
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
import org.jetbrains.annotations.NotNull;

public class WoollyMammothEntity extends WildDangerousEntity {
    public WoollyMammothEntity(EntityType<? extends WildDangerousEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public EntityType<? extends AgeableMob> getDomesticBreedOffspring() {
        return null;
    }

    @Override
    public Class<? extends WildDangerousEntity> getMineClass() {
        return WoollyMammothEntity.class;
    }

    @Override
    public boolean isFood(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return YTechSoundEvents.WOOLLY_MAMMOTH_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return YTechSoundEvents.WOOLLY_MAMMOTH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return YTechSoundEvents.WOOLLY_MAMMOTH_DEATH.get();
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new TemptGoal(this, 1.25D, Ingredient.of(YTechItemTags.WOOLLY_MAMMOTH_TEMP_ITEMS), false));
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        registerTargetGoals();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.ARMOR, 2.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_DAMAGE, 8.0);
    }
}
