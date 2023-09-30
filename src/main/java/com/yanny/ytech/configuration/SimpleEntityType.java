package com.yanny.ytech.configuration;

import com.yanny.ytech.configuration.entity.SpearEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public enum SimpleEntityType {
    SPEAR("spear", "Spear",
            (builder) -> builder.sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20),
            SpearEntity::new)
    ;

    @NotNull public final String key;
    @NotNull public final String name;
    @NotNull public final Consumer<EntityType.Builder<Entity>> entityTypeBuilder;
    @NotNull private final SimpleEntityType.SimpleEntityGetter entityGetter;

    SimpleEntityType(@NotNull String key, @NotNull String name, @NotNull Consumer<EntityType.Builder<Entity>> entityTypeBuilder,
                     @NotNull SimpleEntityType.SimpleEntityGetter entityGetter) {
        this.key = key;
        this.name = name;
        this.entityTypeBuilder = entityTypeBuilder;
        this.entityGetter = entityGetter;
    }

    public Entity getEntity(@NotNull EntityType<? extends Entity> entityType, @NotNull Level level) {
        return entityGetter.get(entityType, level);
    }

    interface SimpleEntityGetter {
        @NotNull Entity get(@NotNull EntityType<? extends Entity> entityType, @NotNull Level level);
    }
}
