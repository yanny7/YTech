package com.yanny.ytech.network.kinetic.common;

import com.yanny.ytech.configuration.MaterialType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum KineticBlockType {
    SHAFT("shaft", "Shaft", List.of(new KineticMaterial(MaterialType.OAK_WOOD, 2f)).toArray(KineticMaterial[]::new)),
    WATER_WHEEL("water_wheel", "Water Wheel", List.of(new KineticMaterial(MaterialType.OAK_WOOD, 0.2f)).toArray(KineticMaterial[]::new)),
    ;

    @NotNull public final String key;
    @NotNull public final String name;
    @NotNull public final KineticMaterial[] materials;

    KineticBlockType(@NotNull String key, @NotNull String name, @NotNull KineticMaterial[] materials) {

        this.key = key;
        this.name = name;
        this.materials = materials;
    }

    public record KineticMaterial(
            @NotNull MaterialType material,
            float stressMultiplier
    ) {}
}
