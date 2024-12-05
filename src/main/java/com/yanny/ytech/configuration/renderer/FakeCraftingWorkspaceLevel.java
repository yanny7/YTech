package com.yanny.ytech.configuration.renderer;

import com.yanny.ytech.configuration.block.CraftingWorkspaceBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class FakeCraftingWorkspaceLevel extends Level {
    private Level level;
    private BlockPos originalPos;
    @Nullable private NonNullList<ItemStack> items;
    @Nullable private NonNullList<BlockState> blockStates;
    private LevelTickAccess<Block> blockTickAccess;
    private LevelTickAccess<Fluid> fluidTickAccess;
    private LevelEntityGetter<Entity> levelEntityGetter;

    public FakeCraftingWorkspaceLevel() {
        // never be called - we use object construction without calling constructor
        //noinspection DataFlowIssue
        super(null, null, null, null, null, false, false, 0, 0);
    }

    public void init() {
        blockTickAccess = new LevelTickAccess<>() {
            @Override
            public void schedule(@NotNull ScheduledTick pTick) {

            }

            @Override
            public boolean hasScheduledTick(@NotNull BlockPos pPos, @NotNull Block pType) {
                return false;
            }

            @Override
            public int count() {
                return 0;
            }

            @Override
            public boolean willTickThisTick(@NotNull BlockPos pPos, @NotNull Block pType) {
                return false;
            }
        };
        fluidTickAccess = new LevelTickAccess<>() {
            @Override
            public boolean willTickThisTick(@NotNull BlockPos pPos, @NotNull Fluid pType) {
                return false;
            }

            @Override
            public void schedule(@NotNull ScheduledTick<Fluid> pTick) {

            }

            @Override
            public boolean hasScheduledTick(@NotNull BlockPos pPos, @NotNull Fluid pType) {
                return false;
            }

            @Override
            public int count() {
                return 0;
            }
        };
        levelEntityGetter = new LevelEntityGetter<>() {
            @Override
            public @Nullable Entity get(int pId) {
                return null;
            }

            @Override
            public @Nullable Entity get(@NotNull UUID pUuid) {
                return null;
            }

            @NotNull
            @Override
            public Iterable<Entity> getAll() {
                return List.of();
            }

            @Override
            public <U extends Entity> void get(@NotNull EntityTypeTest<Entity, U> pTest, @NotNull AbortableIterationConsumer<U> pConsumer) {

            }

            @Override
            public void get(@NotNull AABB pBoundingBox, @NotNull Consumer<Entity> pConsumer) {

            }

            @Override
            public <U extends Entity> void get(@NotNull EntityTypeTest<Entity, U> pTest, @NotNull AABB pBounds, @NotNull AbortableIterationConsumer<U> pConsumer) {

            }
        };
    }

    @Override
    public float getShade(@NotNull Direction pDirection, boolean pShade) {
        return level.getShade(pDirection, pShade);
    }

    @Override
    public void sendBlockUpdated(@NotNull BlockPos pPos, @NotNull BlockState pOldState, @NotNull BlockState pNewState, int pFlags) {}

    @Override
    public @NotNull LevelLightEngine getLightEngine() {
        return level.getLightEngine();
    }

    @Override
    public int getBrightness(@NotNull LightLayer pLightType, @NotNull BlockPos pBlockPos) {
        return level.getBrightness(pLightType, originalPos);
    }

    @Override
    public int getBlockTint(@NotNull BlockPos pPos, @NotNull ColorResolver pColorResolver) {
        return level.getBlockTint(originalPos, pColorResolver);
    }

    @NotNull
    @Override
    public Holder<Biome> getUncachedNoiseBiome(int pX, int pY, int pZ) {
        return level.getUncachedNoiseBiome(pX, pY, pZ);
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(@NotNull BlockPos pPos) {
        return null;
    }

    @Override
    public @Nullable Entity getEntity(int pId) {
        return level.getEntity(pId);
    }

    @NotNull
    @Override
    public TickRateManager tickRateManager() {
        return null;
    }

    @Nullable
    @Override
    public MapItemSavedData getMapData(@NotNull MapId p_324234_) {
        return null;
    }

    @Override
    public void setMapData(@NotNull MapId p_324009_, @NotNull MapItemSavedData p_151534_) {

    }

    @NotNull
    @Override
    public MapId getFreeMapId() {
        return level.getFreeMapId();
    }

    @Override
    public void destroyBlockProgress(int pBreakerId, @NotNull BlockPos pPos, int pProgress) {
        
    }

    @NotNull
    @Override
    public Scoreboard getScoreboard() {
        return level.getScoreboard();
    }

    @NotNull
    @Override
    public RecipeManager getRecipeManager() {
        return level.getRecipeManager();
    }

    @NotNull
    @Override
    protected LevelEntityGetter<Entity> getEntities() {
        return levelEntityGetter;
    }

    @NotNull
    @Override
    public PotionBrewing potionBrewing() {
        return null;
    }

    @Override
    public void setDayTimeFraction(float v) {

    }

    @Override
    public float getDayTimeFraction() {
        return 0;
    }

    @Override
    public float getDayTimePerTick() {
        return 0;
    }

    @Override
    public void setDayTimePerTick(float v) {

    }

    @Override
    public @NotNull BlockState getBlockState(@NotNull BlockPos pPos) {
        if (pPos.getX() > 0 && pPos.getX() <= 3 && pPos.getY() > 0 && pPos.getY() <= 3 && pPos.getZ() > 0 && pPos.getZ() <= 3 && items != null && blockStates != null) {
            int[] position = CraftingWorkspaceBlock.getPosition(pPos.getX() - 1 + (pPos.getZ() - 1) * 3 + (pPos.getY() - 1) * 9);
            int index = CraftingWorkspaceBlock.getIndex(position);

            if (position == null) {
                return Blocks.AIR.defaultBlockState();
            }

            return blockStates.get(index);
        } else {
            if (pPos.getY() == 0) {
                return Blocks.STONE.defaultBlockState();
            } else {
                return Blocks.AIR.defaultBlockState();
            }
        }
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockPos pPos) {
        return Blocks.AIR.defaultBlockState().getFluidState();
    }

    @Override
    public void playSeededSound(@Nullable Player pPlayer, double pX, double pY, double pZ, @NotNull Holder<SoundEvent> pSound, @NotNull SoundSource pSource, float pVolume, float pPitch, long pSeed) {

    }

    @Override
    public void playSeededSound(@Nullable Player pPlayer, @NotNull Entity pEntity, @NotNull Holder<SoundEvent> pSound, @NotNull SoundSource pCategory, float pVolume, float pPitch, long pSeed) {

    }

    @NotNull
    @Override
    public String gatherChunkSourceStats() {
        return "";
    }

    @Override
    public int getHeight() {
        return level.getHeight();
    }

    @NotNull
    @Override
    public FeatureFlagSet enabledFeatures() {
        return level.enabledFeatures();
    }

    @Override
    public int getMinBuildHeight() {
        return level.getMinBuildHeight();
    }

    @Override
    public int getRawBrightness(@NotNull BlockPos pBlockPos, int pAmount) {
        return getLightEngine().getRawBrightness(originalPos, pAmount);
    }

    @Override
    public boolean canSeeSky(@NotNull BlockPos pBlockPos) {
        return this.getBrightness(LightLayer.SKY, originalPos) >= this.getMaxLightLevel();
    }

    public void setData(@NotNull BlockPos pos, @NotNull Level level, @NotNull NonNullList<ItemStack> items, NonNullList<BlockState> blockStates) {
        originalPos = pos;
        this.level = level;
        this.items = items;
        this.blockStates = blockStates;
    }

    public void clearData() {
        originalPos = null;
        level = null;
        items = null;
        blockStates = null;
    }

    @NotNull
    @Override
    public LevelTickAccess<Block> getBlockTicks() {
        return blockTickAccess;
    }

    @NotNull
    @Override
    public LevelTickAccess<Fluid> getFluidTicks() {
        return fluidTickAccess;
    }

    @NotNull
    @Override
    public ChunkSource getChunkSource() {
        return null;
    }

    @Override
    public void levelEvent(@Nullable Player pPlayer, int pType, @NotNull BlockPos pPos, int pData) {

    }

    @Override
    public void gameEvent(@Nullable Entity p_151549_, @NotNull Holder<GameEvent> p_316314_, @NotNull Vec3 p_316613_) {
    }

    @Override
    public void gameEvent(@NotNull Holder<GameEvent> p_316267_, @NotNull Vec3 p_220405_, @NotNull GameEvent.Context p_220406_) {

    }

    @Override
    public void gameEvent(@Nullable Entity p_316772_, @NotNull Holder<GameEvent> p_316248_, @NotNull BlockPos p_316282_) {

    }

    @Override
    public void gameEvent(@NotNull Holder<GameEvent> p_316320_, @NotNull BlockPos p_220409_, @NotNull GameEvent.Context p_220410_) {

    }

    @Override
    public void gameEvent(@NotNull ResourceKey<GameEvent> p_316780_, @NotNull BlockPos p_316509_, @NotNull GameEvent.Context p_316524_) {

    }

    @NotNull
    @Override
    public List<? extends Player> players() {
        return List.of();
    }

    @Override
    public void scheduleTick(@NotNull BlockPos pPos, @NotNull Block pBlock, int pDelay) {

    }

    @Override
    public void scheduleTick(@NotNull BlockPos pPos, @NotNull Fluid pFluid, int pDelay) {

    }

    @Override
    public void scheduleTick(@NotNull BlockPos pPos, @NotNull Block pBlock, int pDelay, @NotNull TickPriority pPriority) {

    }

    @Override
    public void scheduleTick(@NotNull BlockPos pPos, @NotNull Fluid pFluid, int pDelay, @NotNull TickPriority pPriority) {

    }
}
