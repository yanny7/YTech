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

        MaterialArmor(EnumSet<MaterialType> materialTypes) {
            armors = new HashMap<>();

            materialTypes.forEach((material) -> {
                String key = material.key + "_armor";
                switch (material) {
                    case IRON -> armors.put(material, DeferredHolder.create(ArmorMaterials.IRON.unwrap().orThrow()));
                    case GOLD -> armors.put(material, DeferredHolder.create(ArmorMaterials.GOLD.unwrap().orThrow()));
                    default -> armors.put(material, ARMOR_MATERIALS.register(key, () -> new ArmorMaterial(
                            Map.of(ArmorItem.Type.BODY, 1, ArmorItem.Type.BOOTS, 2, ArmorItem.Type.CHESTPLATE, 3, ArmorItem.Type.HELMET, 3, ArmorItem.Type.LEGGINGS, 3),//material.defense,
                            material.getTier().getEnchantmentValue(),
                            SoundEvents.ARMOR_EQUIP_IRON,
                            () -> material.getTier().getRepairIngredient(),
                            List.of(new ArmorMaterial.Layer(Utils.modLoc(material.key))),
                            0,//material.thoughness,
                            0//material.knockbackResistance
                    )));
                }
            });
        }

        public DeferredHolder<ArmorMaterial, ArmorMaterial> of(MaterialType material) {
            return Objects.requireNonNull(armors.get(material), material.key);
        }
    }

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }
}
