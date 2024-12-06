package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.YTechArmorMaterials;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.EquipmentModel;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class YTechEquipmentModelProvider implements DataProvider {
    private final PackOutput.PathProvider path;
    private final Map<ResourceLocation, EquipmentModel> map = new HashMap<>();

    public YTechEquipmentModelProvider(PackOutput output) {
        path = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/equipment");
    }

    public void registerModels() {
        Utils.exclude(EnumSet.allOf(YTechArmorMaterials.class), YTechArmorMaterials.IRON, YTechArmorMaterials.GOLD).forEach((armor) -> add(armor.armorMaterial.modelId()));
    }

    private void add(ResourceLocation id) {
        add(id, EquipmentModel.builder().addHumanoidLayers(id).build()
        );
    }

    private void add(ResourceLocation id, EquipmentModel model) {
        if (map.putIfAbsent(id, model) != null) {
            throw new IllegalStateException("Tried to register equipment model twice for id: " + id);
        }
    }

    @NotNull
    @Override
    public CompletableFuture<?> run(@NotNull CachedOutput cache) {
        registerModels();
        return DataProvider.saveAll(cache, EquipmentModel.CODEC, path, map);
    }

    @NotNull
    @Override
    public String getName() {
        return "Equipment Model Definitions: " + YTechMod.MOD_ID;
    }
}
