package com.yanny.ytech.network;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.machine.block_entity.KineticBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;

public class KineticPropagator {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final HashMap<LevelAccessor, KineticLevel> levelMap = new HashMap<>();

    public void onLevelLoad(LevelAccessor levelAccessor) {
        if (levelAccessor instanceof ServerLevel level) {
            LOGGER.debug("Preparing rotary propagator for {}", getLevelId(levelAccessor));
            levelMap.put(level, level.getDataStorage().computeIfAbsent(this::load, this::create, YTechMod.MOD_ID + "_rotary"));
            LOGGER.debug("Prepared rotary propagator for {}", getLevelId(levelAccessor));
        }
    }

    public void onLevelUnload(LevelAccessor levelAccessor) {
        if (levelAccessor instanceof ServerLevel) {
            LOGGER.debug("Removing rotary propagator for {}", getLevelId(levelAccessor));
            levelMap.remove(levelAccessor);
            LOGGER.debug("Removed rotary propagator for {}", getLevelId(levelAccessor));
        }
    }

    @NotNull
    public KineticNetwork getOrCreateNetwork(KineticBlockEntity blockEntity) {
        return levelMap.get(blockEntity.getLevel()).getOrCreateNetwork(blockEntity);
    }

    @Nullable
    public KineticNetwork getNetwork(KineticBlockEntity blockEntity) {
        return levelMap.get(blockEntity.getLevel()).getNetwork(blockEntity);
    }

    @Nullable
    private static ResourceLocation getLevelId(LevelAccessor levelAccessor) {
        return levelAccessor.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).getKey(levelAccessor.dimensionType());
    }

    @NotNull
    private KineticLevel load(@NotNull CompoundTag tag) {
        return new KineticLevel(tag);
    }

    private KineticLevel create() {
        return new KineticLevel();
    }
}
