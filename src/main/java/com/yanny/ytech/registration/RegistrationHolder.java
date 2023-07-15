package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.network.kinetic.KineticBlockType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;

public record RegistrationHolder(
        HashMap<YTechConfigLoader.Material, HashMap<Block, RegistryObject<Block>>> ore,
        HashMap<YTechConfigLoader.Material, RegistryObject<Block>> storageBlock,
        HashMap<YTechConfigLoader.Material, RegistryObject<Block>> rawStorageBlock,
        HashMap<YTechConfigLoader.Material, RegistryObject<Item>> rawMaterial,
        HashMap<YTechConfigLoader.Material, RegistryObject<Item>> ingot,
        HashMap<YTechConfigLoader.Material, RegistryObject<Item>> dust,
        HashMap<YTechConfigLoader.Material, FluidHolder> fluid,
        HashMap<YTechConfigLoader.Machine, HashMap<YTechConfigLoader.Tier, MachineHolder>> machine,
        HashMap<KineticBlockType, KineticNetworkHolder> kineticNetwork
) {
    public RegistrationHolder() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
    }
}
