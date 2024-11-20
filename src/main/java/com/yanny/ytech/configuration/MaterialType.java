package com.yanny.ytech.configuration;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.function.Supplier;

public enum MaterialType implements IType {
    //solid elements
    COPPER(new Builder("copper", "Copper", () -> YTechTier.COPPER)
            .temp(1085, 2562)),
    GOLD(new Builder("gold", "Gold", () -> Tiers.GOLD)
            .temp(1064, 2856)),
    IRON(new Builder("iron", "Iron", () -> Tiers.IRON)
            .temp(1538, 2861)),
    LEAD(new Builder("lead", "Lead", () -> YTechTier.LEAD)
            .temp(327, 1749)),
    TIN(new Builder("tin", "Tin", () -> YTechTier.TIN)
            .temp(232, 2602)),

    //alloys
    BRONZE(new Builder("bronze", "Bronze", () -> YTechTier.BRONZE)
            .temp(913, 2300)),

    //ores
    CASSITERITE(new Builder("cassiterite", "Cassiterite", () -> Tiers.STONE).temp(1127)),
    GALENA(new Builder("galena", "Galena", () -> Tiers.WOOD).temp(1114)),

    //woods
    WOODEN(new Builder("wooden", "Wooden", () -> Tiers.WOOD).group("wooden")),
    ACACIA_WOOD(new Builder("acacia", "Acacia", () -> Tiers.WOOD).group("wooden")),
    BIRCH_WOOD(new Builder("birch", "Birch", () -> Tiers.WOOD).group("wooden")),
    CHERRY_WOOD(new Builder("cherry", "Cherry", () -> Tiers.WOOD).group("wooden")),
    DARK_OAK_WOOD(new Builder("dark_oak", "Dark Oak", () -> Tiers.WOOD).group("wooden")),
    JUNGLE_WOOD(new Builder("jungle", "Jungle", () -> Tiers.WOOD).group("wooden")),
    MANGROVE_WOOD(new Builder("mangrove", "Mangrove", () -> Tiers.WOOD).group("wooden")),
    OAK_WOOD(new Builder("oak", "Oak", () -> Tiers.WOOD).group("wooden")),
    SPRUCE_WOOD(new Builder("spruce", "Spruce", () -> Tiers.WOOD).group("wooden")),

    FLINT(new Builder("flint", "Flint", () -> Tiers.STONE)),
    ANTLER(new Builder("antler", "Antler", () -> Tiers.STONE)),
    STONE(new Builder("stone", "Stone", () -> Tiers.STONE)),
    LEATHER(new Builder("leather", "Leather", () -> Tiers.STONE)),
    TERRACOTTA(new Builder("terracotta", "Terracotta", () -> Tiers.STONE)),
    MUDBRICK(new Builder("mudbrick", "Mudbrick", () -> Tiers.STONE)),
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
    @NotNull private final Supplier<Tier> tier;

    MaterialType(@NotNull Builder builder) {
        key = builder.key;
        name = builder.name;
        group = builder.group;
        meltingTemp = builder.meltingTemp;
        tier = builder.tier;
    }

    @NotNull
    public Tier getTier() {
        return tier.get();
    }

    @Override
    public String key() {
        return key;
    }

    private static class Builder {
        @NotNull private final String key;
        @NotNull private final String name;
        @NotNull private String group;
        @NotNull private final Supplier<Tier> tier;
        private int meltingTemp = Integer.MAX_VALUE;
        private int boilingTemp = Integer.MAX_VALUE;

        Builder(@NotNull String key, @NotNull String name, @NotNull Supplier<Tier> tier) {
            this.key = key;
            this.name = name;
            this.group = key;
            this.tier = tier;
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
