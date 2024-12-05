package com.yanny.ytech.configuration;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public enum MaterialType implements IType {
    //solid elements
    COPPER(new Builder("copper", "Copper")
            .temp(1085, 2562)),
    GOLD(new Builder("gold", "Gold")
            .temp(1064, 2856)),
    IRON(new Builder("iron", "Iron")
            .temp(1538, 2861)),
    LEAD(new Builder("lead", "Lead")
            .temp(327, 1749)),
    TIN(new Builder("tin", "Tin")
            .temp(232, 2602)),

    //alloys
    BRONZE(new Builder("bronze", "Bronze")
            .temp(913, 2300)),

    //ores
    CASSITERITE(new Builder("cassiterite", "Cassiterite").temp(1127)),
    GALENA(new Builder("galena", "Galena").temp(1114)),

    //woods
    WOODEN(new Builder("wooden", "Wooden").group("wooden")),
    ACACIA_WOOD(new Builder("acacia", "Acacia").group("wooden")),
    BIRCH_WOOD(new Builder("birch", "Birch").group("wooden")),
    CHERRY_WOOD(new Builder("cherry", "Cherry").group("wooden")),
    DARK_OAK_WOOD(new Builder("dark_oak", "Dark Oak").group("wooden")),
    JUNGLE_WOOD(new Builder("jungle", "Jungle").group("wooden")),
    MANGROVE_WOOD(new Builder("mangrove", "Mangrove").group("wooden")),
    OAK_WOOD(new Builder("oak", "Oak").group("wooden")),
    SPRUCE_WOOD(new Builder("spruce", "Spruce").group("wooden")),

    FLINT(new Builder("flint", "Flint")),
    ANTLER(new Builder("antler", "Antler")),
    STONE(new Builder("stone", "Stone")),
    LEATHER(new Builder("leather", "Leather")),
    TERRACOTTA(new Builder("terracotta", "Terracotta")),
    MUDBRICK(new Builder("mudbrick", "Mudbrick")),
    ;

    public static final EnumSet<MaterialType> ALL_WOODS = EnumSet.of(ACACIA_WOOD, BIRCH_WOOD, CHERRY_WOOD, DARK_OAK_WOOD, JUNGLE_WOOD, MANGROVE_WOOD, OAK_WOOD, SPRUCE_WOOD);
    public static final EnumSet<MaterialType> ALL_METALS = EnumSet.of(BRONZE, COPPER, GOLD, IRON, LEAD, TIN);
    public static final EnumSet<MaterialType> ALL_ORES = EnumSet.of(COPPER, GOLD, IRON, CASSITERITE, GALENA);
    public static final EnumSet<MaterialType> ALL_DEPOSIT_ORES = EnumSet.of(GOLD, CASSITERITE);
    public static final EnumSet<MaterialType> ALL_HARD_METALS = EnumSet.of(BRONZE, COPPER, IRON); // used for arrow heads
    public static final EnumSet<MaterialType> AQUEDUCT_MATERIALS = EnumSet.of(MUDBRICK, STONE, TERRACOTTA);
    public static final EnumSet<MaterialType> VANILLA_METALS = EnumSet.of(COPPER, GOLD, IRON);

    @NotNull public final String key;
    @NotNull public final String name;
    public final int meltingTemp;
    @NotNull public final String group;

    MaterialType(@NotNull Builder builder) {
        key = builder.key;
        name = builder.name;
        group = builder.group;
        meltingTemp = builder.meltingTemp;
    }

    @Override
    public String key() {
        return key;
    }

    private static class Builder {
        @NotNull private final String key;
        @NotNull private final String name;
        @NotNull private String group;
        private int meltingTemp = Integer.MAX_VALUE;
        private int boilingTemp = Integer.MAX_VALUE;

        Builder(@NotNull String key, @NotNull String name) {
            this.key = key;
            this.name = name;
            this.group = key;
        }

        Builder temp(int m, int b) {
            meltingTemp = m;
            boilingTemp = b;
            return this;
        }

        Builder temp(int m) {
            meltingTemp = m;
            return this;
        }

        Builder group(@NotNull String group) {
            this.group = group;
            return this;
        }
    }
}
