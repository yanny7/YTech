package com.yanny.ytech.configuration.entity;

import com.yanny.ytech.YTechMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class WildAnimalEntity extends Animal {
    private static final EntityDataAccessor<Integer> DATA_GENERATION_ID = SynchedEntityData.defineId(WildAnimalEntity.class, EntityDataSerializers.INT);
    private static final String TAG_GENERATION = "Generation";

    protected WildAnimalEntity(EntityType<? extends WildAnimalEntity> entityType, Level level) {
        super(entityType, level);
    }

    abstract EntityType<? extends AgeableMob> getDomesticBreedOffspring();

    public int getGeneration() {
        return entityData.get(DATA_GENERATION_ID);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob parent) {
        if (parent instanceof WildAnimalEntity parentAnimal) {
            int nextGeneration = getNextGeneration(this, parentAnimal);

            if (nextGeneration > YTechMod.CONFIGURATION.getMinBreedingGenerations()
                    && random.nextDouble() < (nextGeneration - YTechMod.CONFIGURATION.getMinBreedingGenerations()) * YTechMod.CONFIGURATION.getDomesticChance()) {
                return getDomesticBreedOffspring().create(serverLevel, EntitySpawnReason.BREEDING);
            }
        }

        return (AgeableMob) getType().create(serverLevel, EntitySpawnReason.BREEDING);
    }

    @Override
    public void finalizeSpawnChildFromBreeding(@NotNull ServerLevel level, @NotNull Animal parent, @Nullable AgeableMob baby) {
        if (baby instanceof WildAnimalEntity childAnimal && parent instanceof WildAnimalEntity parentAnimal) {
            childAnimal.entityData.set(DATA_GENERATION_ID, getNextGeneration(this, parentAnimal));
        }
        super.finalizeSpawnChildFromBreeding(level, parent, baby);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(TAG_GENERATION, getGeneration());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(DATA_GENERATION_ID, tag.getInt(TAG_GENERATION));
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_GENERATION_ID, 0);
    }

    private static int getNextGeneration(@NotNull WildAnimalEntity parent1, @NotNull WildAnimalEntity parent2) {
        return (int) Math.ceil(((parent1.getGeneration() + parent2.getGeneration()) / 2.0f) + 1);
    }
}
