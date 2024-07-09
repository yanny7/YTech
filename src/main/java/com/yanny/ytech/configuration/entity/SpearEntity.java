package com.yanny.ytech.configuration.entity;

import com.yanny.ytech.configuration.SpearType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

public class SpearEntity extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(SpearEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(SpearEntity.class, EntityDataSerializers.BOOLEAN);
    private static final String TAG_DEALT_DAMAGE = "DealtDamage";

    private final SpearType spearType;
    private boolean dealtDamage;
    public int clientSideReturnSpearTickCount;

    public SpearEntity(EntityType<SpearEntity> entityType, Level level, SpearType spearType) {
        super(entityType, level);
        this.spearType = spearType;
    }

    public SpearEntity(Level level, LivingEntity shooter, ItemStack stack, SpearType spearType) {
        super(spearType.entityType.get(), shooter, level, stack, null);
        entityData.set(ID_LOYALTY, getLoyaltyFromItem(stack));
        entityData.set(ID_FOIL, stack.hasFoil());
        this.spearType = spearType;
    }

    public SpearEntity(Level level, double x, double y, double z, ItemStack throwItem, SpearType spearType) {
        super(EntityType.TRIDENT, x, y, z, level, throwItem, throwItem);
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(throwItem));
        this.entityData.set(ID_FOIL, throwItem.hasFoil());
        this.spearType = spearType;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_LOYALTY, (byte)0);
        builder.define(ID_FOIL, false);
    }

    @Override
    public void tick() {
        if (inGroundTime > 4) {
            dealtDamage = true;
        }

        Entity entity = getOwner();
        int loyalty = entityData.get(ID_LOYALTY);

        if (loyalty > 0 && (dealtDamage || isNoPhysics()) && entity != null) {
            if (!isAcceptableReturnOwner()) {
                if (!level().isClientSide && pickup == AbstractArrow.Pickup.ALLOWED) {
                    spawnAtLocation(getPickupItem(), 0.1F);
                }

                discard();
            } else {
                setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(position());
                setPosRaw(getX(), getY() + vec3.y * 0.015D * (double)loyalty, getZ());

                if (level().isClientSide) {
                    yOld = getY();
                }

                double d0 = 0.05D * (double)loyalty;
                setDeltaMovement(getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));

                if (clientSideReturnSpearTickCount == 0) {
                    playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++clientSideReturnSpearTickCount;
            }
        }

        super.tick();
    }

    private boolean isAcceptableReturnOwner() {
        Entity entity = getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    @NotNull
    @Override
    protected ItemStack getDefaultPickupItem() {
        return getPickupItemStackOrigin();
    }

    public boolean isFoil() {
        return entityData.get(ID_FOIL);
    }

    @Override
    @Nullable
    protected EntityHitResult findHitEntity(@NotNull Vec3 startVec, @NotNull Vec3 endVec) {
        return dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float damage = spearType.baseDamage;
        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().trident(this, entity1 == null ? this : entity1);

        if (this.level() instanceof ServerLevel serverlevel) {
            damage = EnchantmentHelper.modifyDamage(serverlevel, Objects.requireNonNull(this.getWeaponItem()), entity, damagesource, damage);
        }

        this.dealtDamage = true;

        if (entity.hurt(damagesource, damage)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (this.level() instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, entity, damagesource, this.getWeaponItem());
            }

            if (entity instanceof LivingEntity livingentity) {
                this.doKnockback(livingentity, damagesource);
                this.doPostHurtEffects(livingentity);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
    }

    @Override
    public ItemStack getWeaponItem() {
        return this.getPickupItemStackOrigin();
    }

    @Override
    protected boolean tryPickup(@NotNull Player player) {
        return super.tryPickup(player) || isNoPhysics() && ownedBy(player) && player.getInventory().add(getPickupItem());
    }

    @NotNull
    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(@NotNull Player player) {
        if (ownedBy(player) || getOwner() == null) {
            super.playerTouch(player);
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        dealtDamage = tag.getBoolean(TAG_DEALT_DAMAGE);
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(this.getPickupItemStackOrigin()));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean(TAG_DEALT_DAMAGE, dealtDamage);
    }

    @Override
    public void tickDespawn() {
        int loyalty = entityData.get(ID_LOYALTY);

        if (pickup != AbstractArrow.Pickup.ALLOWED || loyalty <= 0) {
            super.tickDespawn();
        }
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    private byte getLoyaltyFromItem(ItemStack itemStack) {
        return this.level() instanceof ServerLevel serverlevel ? (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverlevel, itemStack, this), 0, 127) : 0;
    }
}
