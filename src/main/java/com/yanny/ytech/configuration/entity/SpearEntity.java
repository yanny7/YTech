package com.yanny.ytech.configuration.entity;

import com.yanny.ytech.configuration.MaterialItemType;
import com.yanny.ytech.configuration.item.SpearItem;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
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

public class SpearEntity extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(SpearEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(SpearEntity.class, EntityDataSerializers.BOOLEAN);
    private static final String TAG_SPEAR = "Spear";
    private static final String TAG_DEALT_DAMAGE = "DealtDamage";

    private final SpearItem.SpearType spearType;
    private ItemStack spearItem;
    private boolean dealtDamage;
    public int clientSideReturnSpearTickCount;

    public SpearEntity(EntityType<? extends Entity> entityType, Level level, SpearItem.SpearType spearType) {
        //noinspection unchecked
        super((EntityType<? extends AbstractArrow>) entityType, level);
        spearItem = new ItemStack(Registration.item(MaterialItemType.SPEAR, spearType.materialType));
        this.spearType = spearType;
    }

    public SpearEntity(Level level, LivingEntity shooter, ItemStack stack, SpearItem.SpearType spearType) {
        super(Registration.entityType(spearType.entityType), shooter, level);
        spearItem = stack.copy();
        entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(stack));
        entityData.set(ID_FOIL, stack.hasFoil());
        this.spearType = spearType;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ID_LOYALTY, (byte)0);
        entityData.define(ID_FOIL, false);
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
    protected ItemStack getPickupItem() {
        return spearItem.copy();
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

        if (entity instanceof LivingEntity livingentity) {
            damage += EnchantmentHelper.getDamageBonus(spearItem, livingentity.getMobType());
        }

        Entity owner = getOwner();
        DamageSource damagesource = damageSources().trident(this, owner == null ? this : owner); //TODO custom damage source
        dealtDamage = true;
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;

        if (entity.hurt(damagesource, damage)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity livingEntity) {
                if (owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingEntity, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)owner, livingEntity);
                }

                doPostHurtEffects(livingEntity);
            }
        }

        setDeltaMovement(getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        float volume = 1.0F;

        if (level() instanceof ServerLevel && level().isThundering() && isChanneling()) {
            BlockPos blockpos = entity.blockPosition();

            if (level().canSeeSky(blockpos)) {
                LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level());

                if (lightningBolt != null) {
                    lightningBolt.moveTo(Vec3.atBottomCenterOf(blockpos));
                    lightningBolt.setCause(owner instanceof ServerPlayer ? (ServerPlayer)owner : null);
                    level().addFreshEntity(lightningBolt);
                    soundevent = SoundEvents.TRIDENT_THUNDER;
                    volume = 5.0F;
                }
            }
        }

        playSound(soundevent, volume, 1.0F);
    }

    public boolean isChanneling() {
        return EnchantmentHelper.hasChanneling(spearItem);
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

        if (tag.contains(TAG_SPEAR, Tag.TAG_COMPOUND)) {
            spearItem = ItemStack.of(tag.getCompound(TAG_SPEAR));
        }

        dealtDamage = tag.getBoolean(TAG_DEALT_DAMAGE);
        entityData.set(ID_LOYALTY, (byte)EnchantmentHelper.getLoyalty(spearItem));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.put(TAG_SPEAR, spearItem.save(new CompoundTag()));
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
}
