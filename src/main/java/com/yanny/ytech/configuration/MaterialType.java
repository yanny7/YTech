package com.yanny.ytech.configuration;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public enum MaterialType implements IType {
    //solid elements
    COPPER(new Builder("copper", "Copper")
            .color(0xB87333).temp(1085, 2562)
            .effect(MobEffects.MOVEMENT_SLOWDOWN, 100, 1)),
    GOLD(new Builder("gold", "Gold")
            .color(0xFFDF00).temp(1064, 2856)),
    IRON(new Builder("iron", "Iron")
            .color(0xAAAAAA).temp(1538, 2861)
            .effect(MobEffects.MOVEMENT_SLOWDOWN, 200, 2)),
    LEAD(new Builder("lead", "Lead")
            .color(0x5C6274).temp(327, 1749)),
    TIN(new Builder("tin", "Tin")
            .color(0x808080).temp(232, 2602)),

    //fluid elements
    MERCURY(new Builder("mercury", "Mercury").color(0xDBCECA)),

    //alloys
    BRONZE(new Builder("bronze", "Bronze")
            .color(0xD89940).temp(913, 2300)
            .effect(MobEffects.MOVEMENT_SLOWDOWN, 100, 2)),

    //ores
    CASSITERITE(new Builder("cassiterite", "Cassiterite").color(0x3D3D3D).temp(1127)),
    GALENA(new Builder("galena", "Galena").color(0x8493A8).temp(1114)),

    //woods
    WOODEN(new Builder("wooden", "Wooden").group("wooden")),
    ACACIA_WOOD(new Builder("acacia", "Acacia").group("wooden")),
    BIRCH_WOOD(new Builder("birch", "Birch").group("wooden")),
    CHERRY_WOOD(new Builder("cherry", "Cherry").group("wooden")),
    DARK_OAK_WOOD(new Builder("dark_oak", "Dark Oak").group("wooden")),
    JUNGLE_WOOD(new Builder("jungle", "Jungle").group("wooden")),
    MANGROVE_WOOD(new Builder("mangrove", "Mangrove").group("wooden")),
    OAK_WOOD(new Builder("oak", "Oak").group("wooden").color(0xC4A570)),
    SPRUCE_WOOD(new Builder("spruce", "Spruce").group("wooden")),

    FLINT(new Builder("flint", "Flint").color(0x666666)),
    ANTLER(new Builder("antler", "Antler").color(0xFCFBED)),
    STONE(new Builder("stone", "Stone").color(999999)),
    ;

    public static final EnumSet<MaterialType> ALL_WOODS = EnumSet.of(ACACIA_WOOD, BIRCH_WOOD, CHERRY_WOOD, DARK_OAK_WOOD, JUNGLE_WOOD, MANGROVE_WOOD, OAK_WOOD, SPRUCE_WOOD);
    public static final EnumSet<MaterialType> ALL_METALS = EnumSet.of(BRONZE, COPPER, GOLD, IRON, LEAD, TIN);
    public static final EnumSet<MaterialType> ALL_ORES = EnumSet.of(COPPER, GOLD, IRON, CASSITERITE, GALENA);
    public static final EnumSet<MaterialType> ALL_DEPOSIT_ORES = EnumSet.of(GOLD, CASSITERITE);
    public static final EnumSet<MaterialType> ALL_HARD_METALS = EnumSet.of(BRONZE, COPPER, IRON); // used for arrow heads
    public static final EnumSet<MaterialType> VANILLA_METALS = EnumSet.of(COPPER, GOLD, IRON);

    @NotNull public final String key;
    @NotNull public final String name;
    public final int color;
    public final int meltingTemp;
    @NotNull public final String group;
    @Nullable public final Triple<Holder<MobEffect>, Integer, Integer> effect;

    MaterialType(@NotNull Builder builder) {
        key = builder.key;
        name = builder.name;
        group = builder.group;
        color = builder.color;
        meltingTemp = builder.meltingTemp;
        effect = builder.effect;
    }

    @Override
    public String key() {
        return key;
    }

    private static class Builder {
        private final String key;
        private final String name;
        @Nullable private Triple<Holder<MobEffect>, Integer, Integer> effect = null;
        private String group;
        private int color = -1;
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

        Builder effect(Holder<MobEffect> potion, int dur, int mul) {
            effect = Triple.of(potion, dur, mul);
            return this;
        }

        Builder color(int c) {
            color = c;
            return this;
        }

        Builder group(@NotNull String group) {
            this.group = group;
            return this;
        }
    }
}
