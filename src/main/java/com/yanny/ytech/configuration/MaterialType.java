package com.yanny.ytech.configuration;

import org.jetbrains.annotations.NotNull;

public enum MaterialType {
    COPPER("copper", "Copper", hex2rgb("#B87333")),
    GOLD("gold", "Gold", hex2rgb("#FFDF00")),
    IRON("iron", "Iron", hex2rgb("#AAAAAA")),
    MERCURY("mercury", "Mercury", hex2rgb("#DBCECA")),
    TIN("tin", "Tin", hex2rgb("#808080")),

    ACACIA_WOOD("acacia_wood", "Acacia", -1, "wooden"),
    OAK_WOOD("oak_wood", "Oak", -1, "wooden"),
    FLINT("flint", "Flint", hex2rgb("#666666")),
    ;

    @NotNull public final String key;
    @NotNull public final String name;
    public final int color;
    @NotNull public final String group;

    MaterialType(@NotNull String key, @NotNull String name) {
        this.key = key;
        this.name = name;
        this.color = -1;
        this.group = key;
    }

    MaterialType(@NotNull String key, @NotNull String name, int color) {
        this.key = key;
        this.name = name;
        this.color = color;
        this.group = key;
    }

    MaterialType(@NotNull String key, @NotNull String name, int color, @NotNull String group) {
        this.key = key;
        this.name = name;
        this.color = color;
        this.group = group;
    }

    private static int hex2rgb(@NotNull String colorStr) {
        return Integer.valueOf( colorStr.substring( 1, 3 ), 16 ) << 16 |
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ) << 8 |
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 );
    }
}
