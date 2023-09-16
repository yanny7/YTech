package com.yanny.ytech.configuration;

import com.google.common.base.Suppliers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.yanny.ytech.configuration.MaterialItemType.INGOT;

public enum MaterialType {
    //solid elements
    COPPER(new Builder("copper", "Copper", ToolType.PICKAXE).color(0xB87333).melting(1085).tier(160, 5.0F, 0.5F, 10, () -> INGOT)),
    GOLD(new Builder("gold", "Gold", ToolType.PICKAXE).color(0xFFDF00).melting(1064).tier(() -> Tiers.GOLD)), // 32, 12.0F, 0.0F, 22
    IRON(new Builder("iron", "Iron", ToolType.PICKAXE).color(0xAAAAAA).melting(1538).tier(() -> Tiers.IRON)), // 250, 6.0F, 2.0F, 14
    TIN(new Builder("tin", "Tin", ToolType.PICKAXE).color(0x808080).melting(232).tier(15, 18.0F, -1.0F, 16, () -> INGOT)),

    //fluid elements
    MERCURY(new Builder("mercury", "Mercury", Tiers.WOOD, ToolType.PICKAXE).color(0xDBCECA)),

    //alloys
    BRONZE(new Builder("bronze", "Bronze", ToolType.PICKAXE).color(0xD89940).melting(913).tier(200, 32.0F, 1.5F, 15, () -> INGOT)),

    //ores
    CASSITERITE(new Builder("cassiterite", "Cassiterite", Tiers.STONE, ToolType.PICKAXE).color(0x3D3D3D).melting(1127)),

    //woods
    ACACIA_WOOD(new Builder("acacia", "Acacia", Tiers.WOOD, ToolType.AXE).group("wooden")),
    BIRCH_WOOD(new Builder("birch", "Birch", Tiers.WOOD, ToolType.AXE).group("wooden")),
    CHERRY_WOOD(new Builder("cherry", "Cherry", Tiers.WOOD, ToolType.AXE).group("wooden")),
    DARK_OAK_WOOD(new Builder("dark_oak", "Dark Oak", Tiers.WOOD, ToolType.AXE).group("wooden")),
    JUNGLE_WOOD(new Builder("jungle", "Jungle", Tiers.WOOD, ToolType.AXE).group("wooden")),
    MANGROVE_WOOD(new Builder("mangrove", "Mangrove", Tiers.WOOD, ToolType.AXE).group("wooden")),
    OAK_WOOD(new Builder("oak", "Oak", Tiers.WOOD, ToolType.AXE).group("wooden").color(0xC4A570)),
    SPRUCE_WOOD(new Builder("spruce", "Spruce", Tiers.WOOD, ToolType.AXE).group("wooden")),

    FLINT(new Builder("flint", "Flint", Tiers.WOOD, ToolType.PICKAXE).color(0x666666)),
    STONE(new Builder("stone", "Stone", Tiers.STONE, ToolType.PICKAXE).color(999999)),
    ;

    public static final EnumSet<MaterialType> ALL_WOODS = EnumSet.of(ACACIA_WOOD, BIRCH_WOOD, CHERRY_WOOD, DARK_OAK_WOOD, JUNGLE_WOOD, MANGROVE_WOOD, OAK_WOOD, SPRUCE_WOOD);
    public static final EnumSet<MaterialType> ALL_METALS = EnumSet.of(BRONZE, COPPER, GOLD, IRON, TIN);
    public static final EnumSet<MaterialType> ALL_ORES = EnumSet.of(COPPER, GOLD, IRON, CASSITERITE);
    public static final EnumSet<MaterialType> ALL_FLUIDS = EnumSet.noneOf(MaterialType.class);
    public static final EnumSet<MaterialType> VANILLA_METALS = EnumSet.of(COPPER, GOLD, IRON);

    public static final List<Tier> TIERS = List.of(
                                    // Mohs hardness
            TIN.getTier(),          // 1.5
            Tiers.WOOD,             // 2
            Tiers.GOLD,             // 2.5
            Tiers.STONE,            // ?? FIXME ???
            COPPER.getTier(),       // 3
            BRONZE.getTier(),       // 3
            Tiers.IRON,             // 4
            Tiers.DIAMOND,          // 10 FIXME needs alternation
            Tiers.NETHERITE         // 10 FIXME needs alternation
    );

    static {
        for (MaterialType value : values()) {
            if (value.getTier() instanceof YTechTier && !TIERS.contains(value.getTier())) {
                throw new IllegalStateException("Missing tier in TIERS list!");
            }
        }
    }

    @NotNull public final String key;
    @NotNull public final String name;
    public final int color;
    public final int meltingTemp;
    @NotNull public final String group;
    @NotNull public final ToolType tool;
    @NotNull private final Supplier<Tier> tier;
    public final boolean hasCustomTier;

    MaterialType(@NotNull Builder builder) {
        key = builder.key;
        name = builder.name;
        group = builder.group;
        color = builder.color;
        meltingTemp = builder.meltingTemp;
        tier = builder.tierFactory.apply(this);
        tool = builder.tool;
        hasCustomTier = builder.hasCustomTier;
    }

    @NotNull
    public Tier getTier() {
        return tier.get();
    }

    private static class Builder {
        @NotNull private final String key;
        @NotNull private final String name;
        @NotNull private Function<MaterialType, Supplier<Tier>> tierFactory;
        @NotNull private final ToolType tool;
        @NotNull private String group;
        private int color = -1;
        private int meltingTemp = Integer.MAX_VALUE;
        private boolean hasCustomTier = false;

        Builder(@NotNull String key, @NotNull String name, @NotNull Tier tier, @NotNull ToolType tool) {
            this.key = key;
            this.name = name;
            this.group = key;
            this.tool = tool;
            tierFactory = (material) -> () -> tier; //TODO REMOVE
        }

        Builder(@NotNull String key, @NotNull String name, @NotNull ToolType tool) {
            this.key = key;
            this.name = name;
            this.group = key;
            this.tool = tool;
            tierFactory = materialType -> {
                throw new IllegalStateException("Invalid constructor");
            };
        }

        Builder melting(int t) {
            meltingTemp = t;
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

        Builder tier(@NotNull Supplier<Tier> tierSupplier) {
            this.tierFactory = (material) -> tierSupplier;
            return this;
        }

        Builder tier(int uses, float speed, float dmg, int e, Supplier<MaterialItemType> repairItem) {
            this.tierFactory = (material) -> Suppliers.memoize(() -> new YTechTier() {
                private final TagKey<Block> needsTool = BlockTags.create(Utils.modLoc("needs_" + material.key + "_tool"));

                @Override
                public int getUses() {
                    return uses;
                }

                @Override
                public float getSpeed() {
                    return speed;
                }

                @Override
                public float getAttackDamageBonus() {
                    return dmg;
                }

                @Override
                public int getLevel() {
                    return -1;
                }

                @Override
                public int getEnchantmentValue() {
                    return e;
                }

                @NotNull
                @Override
                public Ingredient getRepairIngredient() {
                    return Ingredient.of(repairItem.get().itemTag.get(material));
                }

                @NotNull
                @Override
                public TagKey<Block> getTag() {
                    return needsTool;
                }

                @Override
                public ResourceLocation getId() {
                    return Utils.modLoc(material.key);
                }
            });
            hasCustomTier = true;
            return this;
        }
    }
}
