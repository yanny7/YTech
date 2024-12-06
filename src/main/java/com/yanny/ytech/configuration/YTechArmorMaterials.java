package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.HashMap;
import java.util.Map;

public enum YTechArmorMaterials {
    IRON(MaterialType.IRON, ArmorMaterials.IRON),
    GOLD(MaterialType.GOLD, ArmorMaterials.GOLD),
    COPPER(MaterialType.COPPER, 100, 20, 0, 0, 1, 2, 3, 3, 3), //FIXME durability
    BRONZE(MaterialType.BRONZE, 150, 25, 0, 0, 1, 2, 4, 3, 3),
    ;

    private static final Map<MaterialType, ArmorMaterial> ARMORS = new HashMap<>();

    static {
        for (YTechArmorMaterials value : values()) {
            ARMORS.put(value.materialType, value.armorMaterial);
        }
    }

    public final MaterialType materialType;
    public final ArmorMaterial armorMaterial;

    YTechArmorMaterials(MaterialType materialType, ArmorMaterial armorMaterial) {
        this.materialType = materialType;
        this.armorMaterial = armorMaterial;
    }

    YTechArmorMaterials(MaterialType materialType, int durability, int enc, int toughness, int knockbackResistance,
                        int armBody, int armBoots, int armChestplate, int armHelmet, int armLeggings) {
        this.materialType = materialType;
        this.armorMaterial = new ArmorMaterial(
                durability,
                Map.of(ArmorType.BODY, armBody, ArmorType.BOOTS, armBoots, ArmorType.CHESTPLATE, armChestplate, ArmorType.HELMET, armHelmet, ArmorType.LEGGINGS, armLeggings),
                enc,
                SoundEvents.ARMOR_EQUIP_IRON,
                toughness,
                knockbackResistance,
                YTechItemTags.INGOTS.get(materialType),
                Utils.modLoc(materialType.key)
        );
    }

    public static ArmorMaterial get(MaterialType materialType) {
        return ARMORS.get(materialType);
    }
}
