package com.yanny.ytech.configuration;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import org.jetbrains.annotations.NotNull;

public enum MaterialType {
    COPPER("copper", "Copper", hex2rgb("#B87333"), Tiers.IRON),
    GOLD("gold", "Gold", hex2rgb("#FFDF00"), Tiers.GOLD),
    IRON("iron", "Iron", hex2rgb("#AAAAAA"), Tiers.IRON),
    MERCURY("mercury", "Mercury", hex2rgb("#DBCECA"), Tiers.DIAMOND),
    TIN("tin", "Tin", hex2rgb("#808080"), Tiers.IRON),

    ACACIA_WOOD("acacia", "Acacia", -1, "wooden", Tiers.WOOD),
    OAK_WOOD("oak", "Oak", -1, "wooden", Tiers.WOOD),
    FLINT("flint", "Flint", hex2rgb("#666666"), Tiers.WOOD),
    ;

    @NotNull public final String key;
    @NotNull public final String name;
    public final int color;
    @NotNull public final String group;
    @NotNull public final Tier tier;

    MaterialType(@NotNull String key, @NotNull String name, int color, @NotNull Tier tier) {
        this.key = key;
        this.name = name;
        this.color = color;
        this.group = key;
        this.tier = tier;
    }

    MaterialType(@NotNull String key, @NotNull String name, int color, @NotNull String group, @NotNull Tier tier) {
        this.key = key;
        this.name = name;
        this.color = color;
        this.group = group;
        this.tier = tier;
    }

    private static int hex2rgb(@NotNull String colorStr) {
        return Integer.valueOf( colorStr.substring( 1, 3 ), 16 ) << 16 |
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ) << 8 |
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 );
    }
}
