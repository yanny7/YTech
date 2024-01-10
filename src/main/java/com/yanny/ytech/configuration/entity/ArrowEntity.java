package com.yanny.ytech.configuration.entity;

import com.google.common.collect.Sets;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

public class ArrowEntity extends AbstractArrow {
    private static final int NO_EFFECT_COLOR = -1;
    private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR = SynchedEntityData.defineId(ArrowEntity.class, EntityDataSerializers.INT);
    private static final byte EVENT_POTION_PUFF = 0;

    private final Set<MobEffectInstance> effects = Sets.newHashSet();
    private String arrowType;
    private Potion potion = Potions.EMPTY;
    private boolean fixedColor;

    public ArrowEntity(@NotNull Level level, @NotNull LivingEntity shooter, @NotNull Item arrowType) {
        super(EntityType.ARROW, shooter, level, arrowType.getDefaultInstance());
        this.arrowType = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(arrowType)).toString();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_EFFECT_COLOR, NO_EFFECT_COLOR);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.makeParticle(1);
                }
            } else {
                this.makeParticle(2);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        if (this.potion != Potions.EMPTY) {
            tag.putString("Potion", Objects.requireNonNull(BuiltInRegistries.POTION.getKey(this.potion)).toString());
        }

        if (this.fixedColor) {
            tag.putInt("Color", this.getColor());
        }

        if (!this.effects.isEmpty()) {
            ListTag list = new ListTag();

            for(MobEffectInstance mobeffectinstance : this.effects) {
                list.add(mobeffectinstance.save(new CompoundTag()));
            }

            tag.put("CustomPotionEffects", list);
        }

        tag.putString("Type", arrowType);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("Potion", 8)) {
            this.potion = PotionUtils.getPotion(tag);
        }

        for(MobEffectInstance mobeffectinstance : PotionUtils.getCustomEffects(tag)) {
            this.addEffect(mobeffectinstance);
        }

        if (tag.contains("Color", 99)) {
            this.setFixedColor(tag.getInt("Color"));
        } else {
            this.updateColor();
        }

        arrowType = tag.getString("Type");
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EVENT_POTION_PUFF) {
            int i = this.getColor();

            if (i != NO_EFFECT_COLOR) {
                double d0 = (double)(i >> 16 & 255) / 255.0D;
                double d1 = (double)(i >> 8 & 255) / 255.0D;
                double d2 = (double)(i & 255) / 255.0D;

                for(int j = 0; j < 20; ++j) {
                    this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
                }
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    public int getColor() {
        return this.entityData.get(ID_EFFECT_COLOR);
    }

    public void addEffect(@NotNull MobEffectInstance mobEffectInstance) {
        this.effects.add(mobEffectInstance);
        this.getEntityData().set(ID_EFFECT_COLOR, PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects)));
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity living) {
        super.doPostHurtEffects(living);
        Entity entity = this.getEffectSource();

        for(MobEffectInstance effect : this.potion.getEffects()) {
            living.addEffect(new MobEffectInstance(effect.getEffect(), Math.max(effect.mapDuration((duration) -> duration / 8), 1),
                    effect.getAmplifier(), effect.isAmbient(), effect.isVisible()), entity);
        }

        if (!this.effects.isEmpty()) {
            for(MobEffectInstance mobEffectInstance : this.effects) {
                living.addEffect(mobEffectInstance, entity);
            }
        }
    }

    @NotNull
    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(arrowType)));
    }

    private void updateColor() {
        this.fixedColor = false;
        if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
            this.entityData.set(ID_EFFECT_COLOR, NO_EFFECT_COLOR);
        } else {
            this.entityData.set(ID_EFFECT_COLOR, PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects)));
        }
    }

    private void makeParticle(int particleAmount) {
        int i = this.getColor();
        if (i != NO_EFFECT_COLOR && particleAmount > 0) {
            double d0 = (double)(i >> 16 & 255) / 255.0D;
            double d1 = (double)(i >> 8 & 255) / 255.0D;
            double d2 = (double)(i & 255) / 255.0D;

            for(int j = 0; j < particleAmount; ++j) {
                this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
            }
        }
    }

    private void setFixedColor(int fixedColor) {
        this.fixedColor = true;
        this.entityData.set(ID_EFFECT_COLOR, fixedColor);
    }
}
