package com.yanny.ytech.configuration.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yanny.ytech.registration.YTechEntityTypes;
import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeerEntity extends Animal {
    private static final EntityDataAccessor<Boolean> DATA_HAS_ANTLERS_ID = SynchedEntityData.defineId(DeerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final String TAG_HAS_ANTLERS = "HasAntlers";
    private static final String TAG_MORE_WHEAT_TICKS = "MoreWheatTicks";
    private int moreWheatTicks;
    private boolean hasAntlers;

    public DeerEntity(@NotNull EntityType<? extends Animal> entityType, @NotNull Level level) {
        super(entityType, level);
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason,
                                        @Nullable SpawnGroupData spawnData) {
        hasAntlers = random.nextBoolean();
        entityData.set(DATA_HAS_ANTLERS_ID, hasAntlers);
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    public void finalizeSpawnChildFromBreeding(@NotNull ServerLevel level, @NotNull Animal animal, @Nullable AgeableMob baby) {
        hasAntlers = random.nextBoolean();
        entityData.set(DATA_HAS_ANTLERS_ID, hasAntlers);
        super.finalizeSpawnChildFromBreeding(level, animal, baby);
    }

    @Override
    protected void onOffspringSpawnedFromEgg(@NotNull Player pPlayer, @NotNull Mob pChild) {
        hasAntlers = random.nextBoolean();
        entityData.set(DATA_HAS_ANTLERS_ID, hasAntlers);
        super.onOffspringSpawnedFromEgg(pPlayer, pChild);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_HAS_ANTLERS_ID, false);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        if (DATA_HAS_ANTLERS_ID.equals(key)) {
            hasAntlers = entityData.get(DATA_HAS_ANTLERS_ID);
        }

        super.onSyncedDataUpdated(key);
    }

    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherMob) {
        return YTechEntityTypes.DEER.get().create(level);
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
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(TAG_MORE_WHEAT_TICKS, moreWheatTicks);
        tag.putBoolean(TAG_HAS_ANTLERS, hasAntlers);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        moreWheatTicks = tag.getInt(TAG_MORE_WHEAT_TICKS);
        hasAntlers = tag.getBoolean(TAG_HAS_ANTLERS);
        entityData.set(DATA_HAS_ANTLERS_ID, hasAntlers);
    }

    @Override
    public boolean isFood(@NotNull ItemStack itemStack) {
        return itemStack.is(YTechItemTags.DEER_FOOD);
    }

    public boolean hasAntler() {
        return hasAntlers && !isBaby();
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new PanicGoal(this, 2.0D));
        goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(Items.WHEAT), false));
        goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        goalSelector.addGoal(5, new AvoidEntityGoal<>(this, Player.class, 8.0F, 2.2D, 2.2D));
        goalSelector.addGoal(5, new AvoidEntityGoal<>(this, Wolf.class, 10.0F, 2.2D, 2.2D));
        goalSelector.addGoal(6, new RaidGardenGoal(this));
        goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    private boolean wantsMoreFood() {
        return moreWheatTicks <= 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    public static String hasAntlersStr() {
        JsonObject object = new JsonObject();
        object.addProperty(TAG_HAS_ANTLERS, true);
        return new Gson().toJson(object);
    }

    static class RaidGardenGoal extends MoveToBlockGoal {
        @NotNull private final DeerEntity deer;
        private boolean wantsToRaid;
        private boolean canRaid;

        public RaidGardenGoal(@NotNull DeerEntity deer) {
            super(deer, 0.7F, 16);
            this.deer = deer;
        }

        @Override
        public boolean canUse() {
            if (nextStartTick <= 0) {
                if (!EventHooks.getMobGriefingEvent(deer.level(), deer)) {
                    return false;
                }

                canRaid = false;
                wantsToRaid = deer.wantsMoreFood();
            }

            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return canRaid && super.canContinueToUse();
        }

        @Override
        public void tick() {
            super.tick();
            deer.getLookControl().setLookAt(
                    (double)blockPos.getX() + 0.5D, 
                    blockPos.getY() + 1,
                    (double)blockPos.getZ() + 0.5D, 
                    10.0F,
                    (float)deer.getMaxHeadXRot()
            );
            
            if (isReachedTarget()) {
                Level level = deer.level();
                BlockPos pos = blockPos.above();
                BlockState blockState = level.getBlockState(pos);
                
                if (canRaid && blockState.is(Blocks.WHEAT)) {
                    int age = blockState.getValue(CropBlock.AGE);

                    if (age == 0) {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                        level.destroyBlock(pos, true, deer);
                    } else {
                        level.setBlock(pos, blockState.setValue(CarrotBlock.AGE, age - 1), 2);
                        level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(blockState));
                    }

                    deer.moreWheatTicks = 40;
                }

                canRaid = false;
                nextStartTick = 10;
            }
        }

        protected boolean isValidTarget(@NotNull LevelReader level, @NotNull BlockPos pos) {
            BlockState state = level.getBlockState(pos);

            if (state.is(Blocks.FARMLAND) && wantsToRaid && !canRaid) {
                state = level.getBlockState(pos.above());

                if (state.is(Blocks.WHEAT) && ((CropBlock)state.getBlock()).isMaxAge(state)) {
                    canRaid = true;
                    return true;
                }
            }

            return false;
        }
    }
}
