package com.yanny.ytech.configuration;

import com.google.common.base.Suppliers;
import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Function;
import java.util.function.Supplier;

public enum MaterialType implements IType {
    //solid elements
    COPPER(new Builder("copper", "Copper", ToolType.PICKAXE)
            .color(0xB87333).temp(1085, 2562)
            .effect(MobEffects.MOVEMENT_SLOWDOWN, 100, 1)
            .tier(160, 5.0F, 0.5F, 10, BlockTags.INCORRECT_FOR_STONE_TOOL)),
    GOLD(new Builder("gold", "Gold", ToolType.PICKAXE)
            .color(0xFFDF00).temp(1064, 2856)
            .tier(() -> Tiers.GOLD)), // 32, 12.0F, 0.0F, 22
    IRON(new Builder("iron", "Iron", ToolType.PICKAXE)
            .color(0xAAAAAA).temp(1538, 2861)
            .effect(MobEffects.MOVEMENT_SLOWDOWN, 200, 2)
            .tier(() -> Tiers.IRON)), // 250, 6.0F, 2.0F, 14
    LEAD(new Builder("lead", "Lead", ToolType.PICKAXE)
            .color(0x5C6274).temp(327, 1749)
            .tier(16, 3.0F, 3.0F, 21, BlockTags.INCORRECT_FOR_STONE_TOOL)),
    TIN(new Builder("tin", "Tin", ToolType.PICKAXE)
            .color(0x808080).temp(232, 2602)
            .tier(15, 10.0F, -1.0F, 16, BlockTags.INCORRECT_FOR_STONE_TOOL)),

    //fluid elements
    MERCURY(new Builder("mercury", "Mercury", Tiers.WOOD, ToolType.PICKAXE).color(0xDBCECA)),

    //alloys
    BRONZE(new Builder("bronze", "Bronze", ToolType.PICKAXE)
            .color(0xD89940).temp(913, 2300)
            .effect(MobEffects.MOVEMENT_SLOWDOWN, 100, 2)
            .tier(200, 32.0F, 1.5F, 15, BlockTags.INCORRECT_FOR_IRON_TOOL)),

    //ores
    CASSITERITE(new Builder("cassiterite", "Cassiterite", Tiers.STONE, ToolType.PICKAXE).color(0x3D3D3D).temp(1127)),
    GALENA(new Builder("galena", "Galena", Tiers.WOOD, ToolType.PICKAXE).color(0x8493A8).temp(1114)),

    //woods
    WOODEN(new Builder("wooden", "Wooden", Tiers.WOOD, ToolType.AXE).group("wooden")),
    ACACIA_WOOD(new Builder("acacia", "Acacia", Tiers.WOOD, ToolType.AXE).group("wooden")),
    BIRCH_WOOD(new Builder("birch", "Birch", Tiers.WOOD, ToolType.AXE).group("wooden")),
    CHERRY_WOOD(new Builder("cherry", "Cherry", Tiers.WOOD, ToolType.AXE).group("wooden")),
    DARK_OAK_WOOD(new Builder("dark_oak", "Dark Oak", Tiers.WOOD, ToolType.AXE).group("wooden")),
    JUNGLE_WOOD(new Builder("jungle", "Jungle", Tiers.WOOD, ToolType.AXE).group("wooden")),
    MANGROVE_WOOD(new Builder("mangrove", "Mangrove", Tiers.WOOD, ToolType.AXE).group("wooden")),
    OAK_WOOD(new Builder("oak", "Oak", Tiers.WOOD, ToolType.AXE).group("wooden").color(0xC4A570)),
    SPRUCE_WOOD(new Builder("spruce", "Spruce", Tiers.WOOD, ToolType.AXE).group("wooden")),

    FLINT(new Builder("flint", "Flint", Tiers.STONE, ToolType.PICKAXE).color(0x666666)),
    ANTLER(new Builder("antler", "Antler", Tiers.STONE, ToolType.PICKAXE).color(0xFCFBED)),
    STONE(new Builder("stone", "Stone", Tiers.STONE, ToolType.PICKAXE).color(999999)),
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
    @NotNull public final ToolType tool;
    @Nullable public final Triple<Holder<MobEffect>, Integer, Integer> effect;
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
        effect = builder.effect;
        hasCustomTier = builder.hasCustomTier;
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
        @NotNull private Function<MaterialType, Supplier<Tier>> tierFactory;
        @NotNull private final ToolType tool;
        @Nullable private Triple<Holder<MobEffect>, Integer, Integer> effect = null;
        @NotNull private String group;
        private int color = -1;
        private int meltingTemp = Integer.MAX_VALUE;
        private int boilingTemp = Integer.MAX_VALUE;
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

        Builder tier(@NotNull Supplier<Tier> tierSupplier) {
            this.tierFactory = (material) -> tierSupplier;
            return this;
        }

        Builder tier(int uses, float speed, float dmg, int e, TagKey<Block> invToolFor) {
            this.tierFactory = (material) -> Suppliers.memoize(() -> new Tier() {
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

                @NotNull
                @Override
                public TagKey<Block> getIncorrectBlocksForDrops() {
                    return invToolFor;
                }

                @Override
                public int getEnchantmentValue() {
                    return e;
                }

                @NotNull
                @Override
                public Ingredient getRepairIngredient() {
                    return Ingredient.of(YTechItemTags.INGOTS.get(material));
                }
            });
            hasCustomTier = true;
            return this;
        }
    }
}
