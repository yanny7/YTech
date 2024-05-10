package com.yanny.ytech.configuration.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yanny.ytech.configuration.goal.IRaidGarden;
import com.yanny.ytech.configuration.goal.RaidGardenGoal;
import com.yanny.ytech.registration.YTechBlockTags;
import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeerEntity extends Animal implements IRaidGarden {
    private static final EntityDataAccessor<Boolean> DATA_HAS_ANTLERS_ID = SynchedEntityData.defineId(DeerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final String TAG_HAS_ANTLERS = "HasAntlers";
    private static final String TAG_MORE_WHEAT_TICKS = "MoreWheatTicks";
    private static final int EAT_ANIMATION_TICKS = 40;

    private int moreWheatTicks;
    private int eatAnimationTick;
    private EatBlockGoal eatBlockGoal;

    public DeerEntity(@NotNull EntityType<? extends Animal> entityType, @NotNull Level level) {
        super(entityType, level);
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason,
                                        @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag) {
        entityData.set(DATA_HAS_ANTLERS_ID, random.nextBoolean());
        return super.finalizeSpawn(level, difficulty, reason, spawnData, tag);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return null;
    }

    @Override
    public boolean canBreed() {
        return false;
    }

    @Override
    public boolean isFood(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    public void customServerAiStep() {
        eatAnimationTick = eatBlockGoal.getEatAnimationTick();
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
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(TAG_MORE_WHEAT_TICKS, moreWheatTicks);
        tag.putBoolean(TAG_HAS_ANTLERS, entityData.get(DATA_HAS_ANTLERS_ID));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        moreWheatTicks = tag.getInt(TAG_MORE_WHEAT_TICKS);
        entityData.set(DATA_HAS_ANTLERS_ID, tag.getBoolean(TAG_HAS_ANTLERS));
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
    public boolean wantsMoreFood() {
        return moreWheatTicks <= 0;
    }

    @Override
    public void setWantsMoreFoodTicks() {
        moreWheatTicks = 40;
    }

    public boolean hasAntlers() {
        return entityData.get(DATA_HAS_ANTLERS_ID);
    }

    @Override
    protected void onOffspringSpawnedFromEgg(@NotNull Player pPlayer, @NotNull Mob pChild) {
        entityData.set(DATA_HAS_ANTLERS_ID, random.nextBoolean());
        super.onOffspringSpawnedFromEgg(pPlayer, pChild);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_HAS_ANTLERS_ID, false);
    }

    @Override
    protected void registerGoals() {
        eatBlockGoal = new EatBlockGoal(this);
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new PanicGoal(this, 2.0D));
        goalSelector.addGoal(2, new TemptGoal(this, 1.25D, Ingredient.of(YTechItemTags.DEER_TEMP_ITEMS), true));
        goalSelector.addGoal(3, new AvoidEntityGoal<>(this, SaberToothTigerEntity.class, 12.0F, 2.2D, 2.2D));
        goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 8.0F, 2.2D, 2.2D));
        goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 10.0F, 2.2D, 2.2D));
        goalSelector.addGoal(4, eatBlockGoal);
        goalSelector.addGoal(4, new RaidGardenGoal<>(this, YTechBlockTags.DEER_RAID_BLOCKS));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    public static String hasAntlersStr() {
        JsonObject object = new JsonObject();
        object.addProperty(TAG_HAS_ANTLERS, true);
        return new Gson().toJson(object);
    }
}
