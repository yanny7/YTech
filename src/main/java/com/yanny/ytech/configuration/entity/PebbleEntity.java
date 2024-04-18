package com.yanny.ytech.configuration.entity;

import com.yanny.ytech.configuration.SimpleEntityType;
import com.yanny.ytech.registration.Registration;
import com.yanny.ytech.registration.YTechItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class PebbleEntity extends ThrowableItemProjectile {
    public PebbleEntity(EntityType<? extends Entity> entityType, Level level) {
        super((EntityType<? extends ThrowableItemProjectile>) entityType, level);
    }

    public PebbleEntity(Level level, LivingEntity shooter) {
        super(Registration.entityType(SimpleEntityType.PEBBLE), shooter, level);
    }

    @NotNull
    @Override
    protected Item getDefaultItem() {
        return YTechItems.PEBBLE.get();
    }

    private ParticleOptions getParticle() {
        return new ItemParticleOption(ParticleTypes.ITEM, getDefaultItem().getDefaultInstance());
    }

    public void handleEntityEvent(byte pId) {
        if (pId == EntityEvent.DEATH) {
            ParticleOptions particleOptions = this.getParticle();

            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(particleOptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();

        entity.hurt(this.damageSources().thrown(this, this.getOwner()), 1.0f);
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);

        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, EntityEvent.DEATH);
            this.discard();
        }
    }
}
