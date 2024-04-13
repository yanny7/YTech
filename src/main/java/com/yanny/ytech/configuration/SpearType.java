package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Registration;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public enum SpearType {
    FLINT(MaterialType.FLINT, SimpleEntityType.FLINT_SPEAR, 20, 4.0f, -3.5f, 2.0f, 1.5f, 10),
    COPPER(MaterialType.COPPER, SimpleEntityType.COPPER_SPEAR, 40, 4.5f, -3.4f, 2.1f, 1.3f, 10),
    BRONZE(MaterialType.BRONZE, SimpleEntityType.BRONZE_SPEAR, 80, 5.0f, -3.3F, 2.3f, 1.2f, 10),
    IRON(MaterialType.IRON, SimpleEntityType.IRON_SPEAR, 160, 6.0f, -3.2F, 2.5f, 1.1f, 10),
    ;

    public static final ResourceLocation TEXTURE_LOCATION = Utils.modLoc("textures/entity/spear.png");
    public static final Map<SimpleEntityType, SpearType> BY_ENTITY_TYPE = new HashMap<>();
    public static final Map<MaterialType, SpearType> BY_MATERIAL_TYPE = new HashMap<>();

    static {
        for (SpearType spearType : SpearType.values()) {
            BY_ENTITY_TYPE.put(spearType.entityType, spearType);
            BY_MATERIAL_TYPE.put(spearType.materialType, spearType);
        }
    }

    public final MaterialType materialType;
    public final SimpleEntityType entityType;
    public final int durability;
    public final float baseDamage;
    public final float attackSpeed;
    public final float shootPower;
    public final float accuracy;
    public final int throwThreshold;

    SpearType(MaterialType materialType, SimpleEntityType entityType, int durability, float baseDamage, float attackSpeed, float shootPower, float accuracy, int throwThreshold) {
        String key = Registration.HOLDER.items().get(MaterialItemType.SPEAR).get(materialType).key;
        this.materialType = materialType;
        this.entityType = entityType;
        this.durability = durability;
        this.baseDamage = baseDamage;
        this.attackSpeed = attackSpeed;
        this.shootPower = shootPower;
        this.accuracy = accuracy;
        this.throwThreshold = throwThreshold;
    }
}
