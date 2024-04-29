package com.yanny.ytech.configuration.entity;

import com.mojang.logging.LogUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Objects;

public class ArrowEntity extends AbstractArrow {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int NO_EFFECT_COLOR = -1;
    private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR = SynchedEntityData.defineId(ArrowEntity.class, EntityDataSerializers.INT);
    private static final byte EVENT_POTION_PUFF = 0;

    private PotionContents potionContents = PotionContents.EMPTY;

    public ArrowEntity(@NotNull Level level, @NotNull LivingEntity shooter, @NotNull Item arrowType) {
        super(EntityType.ARROW, shooter, level, arrowType.getDefaultInstance());
        this.updateColor();
    }

    private PotionContents getPotionContents() {
        return potionContents;
    }

    private void setPotionContents(PotionContents contents) {
        potionContents = contents;
        this.updateColor();
    }

    @Override
    protected void setPickupItemStack(@NotNull ItemStack stack) {
        super.setPickupItemStack(stack);
        this.updateColor();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_EFFECT_COLOR, NO_EFFECT_COLOR);
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
        tag.put("effect", Objects.requireNonNull(DataComponents.POTION_CONTENTS.codec()).encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), potionContents).getOrThrow());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("effect")) {
            setPotionContents(Objects.requireNonNull(DataComponents.POTION_CONTENTS.codec()).parse(registryAccess().createSerializationContext(NbtOps.INSTANCE), tag.get("effect"))
                    .resultOrPartial(p_330102_ -> LOGGER.error("Tried to load invalid item: '{}'", p_330102_)).orElse(PotionContents.EMPTY));
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EVENT_POTION_PUFF) {
            int i = this.getColor();

            if (i != NO_EFFECT_COLOR) {
                float d0 = (i >> 16 & 255) / 255.0F;
                float d1 = (i >> 8 & 255) / 255.0F;
                float d2 = (i & 255) / 255.0F;

                for(int j = 0; j < 20; ++j) {
                    this.level().addParticle(
                            ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, d0, d1, d2),
                            this.getRandomX(0.5D),
                            this.getRandomY(),
                            this.getRandomZ(0.5D),
                            d0,
                            d1,
                            d2
                    );
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
        this.setPotionContents(this.getPotionContents().withEffectAdded(mobEffectInstance));
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity living) {
        super.doPostHurtEffects(living);
        Entity entity = this.getEffectSource();
        PotionContents potionContents = this.getPotionContents();

        if (potionContents.potion().isPresent()) {
            for (MobEffectInstance mobEffectInstance : potionContents.potion().get().value().getEffects()) {
                living.addEffect(
                        new MobEffectInstance(
                                mobEffectInstance.getEffect(),
                                Math.max(mobEffectInstance.mapDuration(duration -> duration / 8), 1),
                                mobEffectInstance.getAmplifier(),
                                mobEffectInstance.isAmbient(),
                                mobEffectInstance.isVisible()
                        ),
                        entity
                );
            }
        }

        for (MobEffectInstance mobEffectInstance : potionContents.customEffects()) {
            living.addEffect(mobEffectInstance, entity);
        }
    }

    @NotNull
    @Override
    protected ItemStack getDefaultPickupItem() {
        return getPickupItemStackOrigin();
    }

    private void updateColor() {
        PotionContents potioncontents = this.getPotionContents();
        this.entityData.set(ID_EFFECT_COLOR, potioncontents.equals(PotionContents.EMPTY) ? -1 : potioncontents.getColor());
    }

    private void makeParticle(int particleAmount) {
        int i = this.getColor();
        if (i != NO_EFFECT_COLOR && particleAmount > 0) {
            double d0 = (double)(i >> 16 & 255) / 255.0D;
            double d1 = (double)(i >> 8 & 255) / 255.0D;
            double d2 = (double)(i & 255) / 255.0D;

            for(int j = 0; j < particleAmount; ++j) {
                this.level().addParticle(
                        ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, i),
                        this.getRandomX(0.5D),
                        this.getRandomY(),
                        this.getRandomZ(0.5D),
                        d0,
                        d1,
                        d2
                );
            }
        }
    }
}
