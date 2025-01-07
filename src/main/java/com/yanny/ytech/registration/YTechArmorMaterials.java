package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.Utils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;

public class YTechArmorMaterials {
    private static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, YTechMod.MOD_ID);

    public static final MaterialArmor ARMORS = new MaterialArmor(EnumSet.of(MaterialType.IRON, MaterialType.GOLD, MaterialType.COPPER, MaterialType.BRONZE));

    public static class MaterialArmor {
        protected final Map<MaterialType, DeferredHolder<ArmorMaterial, ArmorMaterial>> armors;
        protected final Map<MaterialType, Integer> durability;

        MaterialArmor(EnumSet<MaterialType> materialTypes) {
            armors = new HashMap<>();
            durability = new HashMap<>();

            materialTypes.forEach((material) -> {
                String key = material.key + "_armor";
                switch (material) {
                    case IRON -> {
                        armors.put(material, DeferredHolder.create(ArmorMaterials.IRON.unwrap().orThrow()));
                        durability.put(material, 15);
                    }
                    case GOLD -> {
                        armors.put(material, DeferredHolder.create(ArmorMaterials.GOLD.unwrap().orThrow()));
                        durability.put(material, 7);
                    }
                    default -> {
                        armors.put(material, ARMOR_MATERIALS.register(key, () -> new ArmorMaterial(
                                Map.of(ArmorItem.Type.BODY, 1, ArmorItem.Type.BOOTS, 2, ArmorItem.Type.CHESTPLATE, 3, ArmorItem.Type.HELMET, 3, ArmorItem.Type.LEGGINGS, 3),//material.defense,
                                material.getTier().getEnchantmentValue(),
                                SoundEvents.ARMOR_EQUIP_IRON,
                                () -> material.getTier().getRepairIngredient(),
                                List.of(new ArmorMaterial.Layer(Utils.modLoc(material.key))),
                                0,
                                0
                        )));
                        switch (material) {
                            case COPPER -> durability.put(material, 7);
                            case BRONZE -> durability.put(material, 10);
                        }
                    }
                }
            });
        }

        public DeferredHolder<ArmorMaterial, ArmorMaterial> get(MaterialType material) {
            return Objects.requireNonNull(armors.get(material), material.key);
        }

        public int getDurability(MaterialType material) {
            return Objects.requireNonNull(durability.get(material), material.key);
        }
    }

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }
}
