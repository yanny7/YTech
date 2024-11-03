package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum YTechTier implements Tier {
    COPPER(64, 5.0F, 0.5F, 10, () -> Ingredient.of(YTechItemTags.INGOTS.get(MaterialType.COPPER))),
    LEAD(32, 3.0F, 3.0F, 21, () -> Ingredient.of(YTechItemTags.INGOTS.get(MaterialType.LEAD))),
    TIN(32, 10.0F, -1.0F, 16, () -> Ingredient.of(YTechItemTags.INGOTS.get(MaterialType.TIN))),
    BRONZE(384, 32.0F, 1.5F, 15, () -> Ingredient.of(YTechItemTags.INGOTS.get(MaterialType.BRONZE))),
    ;

    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final Supplier<Ingredient> repairIngredient;

    YTechTier(int pUses, float pSpeed, float pDamage, int pEnchantmentValue, Supplier<Ingredient> pRepairIngredient) {
        this.uses = pUses;
        this.speed = pSpeed;
        this.damage = pDamage;
        this.enchantmentValue = pEnchantmentValue;
        this.repairIngredient = pRepairIngredient;
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public int getLevel() {
        return 0;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @NotNull
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @NotNull
    public TagKey<Block> getTag() {
        return Tags.Blocks.NEEDS_WOOD_TOOL;
    }
}
