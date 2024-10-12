package com.yanny.ytech.configuration;

import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public enum PartType implements IType {
    AXE_HEAD("axe_head", "Axe Head", 3),
    HAMMER_HEAD("hammer_head", "Hammer Head", 18),
    INGOT("ingot", "Ingot", 1),
    PICKAXE_HEAD("pickaxe_head", "Pickaxe Head", 3),
    SWORD_BLADE("sword_blade", "Sword Blade", 2),
    ;

    public static final EnumSet<PartType> ALL_PARTS = EnumSet.allOf(PartType.class);

    static {
        ALL_PARTS.remove(PartType.INGOT);
    }

    @NotNull public final String key;
    @NotNull public final String name;
    public final int ingotCount;

    PartType(@NotNull String key, @NotNull String name, int ingotCount) {
        this.key = key;
        this.name = name;
        this.ingotCount = ingotCount;
    }

    @Override
    public String key() {
        return key;
    }
}
