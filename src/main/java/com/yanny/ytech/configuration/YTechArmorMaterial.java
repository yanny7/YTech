package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public enum YTechArmorMaterial {
    IRON(MaterialType.IRON, ArmorMaterials.IRON),
    GOLD(MaterialType.GOLD, ArmorMaterials.GOLD),
    COPPER(MaterialType.COPPER, 7,
            getHealthFunctionForType(1, 2, 4, 1),
            10, SoundEvents.ARMOR_EQUIP_IRON, 0, 0,
            () -> Ingredient.of(YTechItemTags.INGOTS.get(MaterialType.IRON))),
    BRONZE(MaterialType.BRONZE, 10,
            getHealthFunctionForType(2, 3, 5, 2),
            19, SoundEvents.ARMOR_EQUIP_IRON, 0, 0,
            () -> Ingredient.of(YTechItemTags.INGOTS.get(MaterialType.BRONZE)))
    ;

    private static final Map<MaterialType, ArmorMaterial> ARMORS = new HashMap<>();

    private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (type) -> {
        type.put(ArmorItem.Type.BOOTS, 13);
        type.put(ArmorItem.Type.LEGGINGS, 15);
        type.put(ArmorItem.Type.CHESTPLATE, 16);
        type.put(ArmorItem.Type.HELMET, 11);
    });

    static {
        for (YTechArmorMaterial value : values()) {
            ARMORS.put(value.material, value.armorMaterial);
        }
    }

    private final MaterialType material;
    private final ArmorMaterial armorMaterial;

    YTechArmorMaterial(MaterialType material, ArmorMaterial armorMaterial) {
        this.material = material;
        this.armorMaterial = armorMaterial;
    }

    YTechArmorMaterial(MaterialType material, int durabilityMultiplier, EnumMap<ArmorItem.Type, Integer> protectionFunctionForType,
                       int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.material = material;
        this.armorMaterial = new ArmorMaterial() {
            public int getDurabilityForType(@NotNull ArmorItem.Type pType) {
                return HEALTH_FUNCTION_FOR_TYPE.get(pType) * durabilityMultiplier;
            }

            public int getDefenseForType(@NotNull ArmorItem.Type pType) {
                return protectionFunctionForType.get(pType);
            }

            public int getEnchantmentValue() {
                return enchantmentValue;
            }

            @NotNull
            public SoundEvent getEquipSound() {
                return sound;
            }

            @NotNull
            public Ingredient getRepairIngredient() {
                return repairIngredient.get();
            }

            @NotNull
            public String getName() {
                return material.name;
            }

            public float getToughness() {
                return toughness;
            }

            public float getKnockbackResistance() {
                return knockbackResistance;
            }
        };
    }

    private static EnumMap<ArmorItem.Type, Integer> getHealthFunctionForType(int boots, int leggings, int chestplate, int helmet) {
        return Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
            map.put(ArmorItem.Type.BOOTS, boots);
            map.put(ArmorItem.Type.LEGGINGS, leggings);
            map.put(ArmorItem.Type.CHESTPLATE, chestplate);
            map.put(ArmorItem.Type.HELMET, helmet);
        });
    }

    public static ArmorMaterial get(MaterialType materialType) {
        return ARMORS.get(materialType);
    }
}
