package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.BlockObjectType;
import com.yanny.ytech.configuration.ConfigLoader;
import com.yanny.ytech.configuration.FluidObjectType;
import com.yanny.ytech.configuration.ItemObjectType;
import com.yanny.ytech.network.kinetic.common.KineticBlockType;

import java.util.HashMap;

public record RegistrationHolder(
        HashMap<ItemObjectType, HashMap<ConfigLoader.Material, Holder.ItemHolder>> items,
        HashMap<BlockObjectType, HashMap<ConfigLoader.Material, Holder.BlockHolder>> blocks,
        HashMap<FluidObjectType, HashMap<ConfigLoader.Material, Holder.FluidHolder>> fluids,
        HashMap<ConfigLoader.Machine, HashMap<ConfigLoader.Tier, MachineHolder>> machine,
        HashMap<KineticBlockType, HashMap<ConfigLoader.Material, KineticNetworkHolder>> kineticNetwork
) {
    public RegistrationHolder() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
    }
}
