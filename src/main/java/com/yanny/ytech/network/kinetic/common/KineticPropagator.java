package com.yanny.ytech.network.kinetic.common;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;

@Mod.EventBusSubscriber
public abstract class KineticPropagator<L extends LevelAccessor, K extends KineticLevel> {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected final HashMap<LevelAccessor, K> levelMap = new HashMap<>();

    public abstract void onLevelLoad(@NotNull L level);

    public abstract void onLevelUnload(@NotNull L level);

    @Nullable
    public KineticNetwork getNetwork(@NotNull IKineticBlockEntity blockEntity) {
        K level = levelMap.get(blockEntity.getLevel());

        if (level != null) {
            return level.getNetwork(blockEntity);
        } else {
            LOGGER.warn("No kinetic network for level {}", blockEntity.getLevel());
            return null;
        }
    }

    @Nullable
    protected static ResourceLocation getLevelId(@NotNull LevelAccessor level) {
        return level.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).getKey(level.dimensionType());
    }
}
