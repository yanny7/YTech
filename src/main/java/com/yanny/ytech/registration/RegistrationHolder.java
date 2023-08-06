package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.*;
import com.yanny.ytech.network.kinetic.common.KineticBlockType;

import java.util.HashMap;

public record RegistrationHolder(
        HashMap<ItemObjectType, HashMap<ConfigLoader.Material, Holder.ItemHolder>> items,
        HashMap<BlockObjectType, HashMap<ConfigLoader.Material, Holder.BlockHolder>> blocks,
        HashMap<FluidObjectType, HashMap<ConfigLoader.Material, Holder.FluidHolder>> fluids,
        HashMap<ToolObjectType, HashMap<ConfigLoader.Material, Holder.ToolHolder>> tools,
        HashMap<ConfigLoader.Machine, HashMap<ConfigLoader.Tier, MachineHolder>> machine,
        HashMap<KineticBlockType, HashMap<ConfigLoader.Material, KineticNetworkHolder>> kineticNetwork,
        HashMap<SimpleItemType, Holder.SimpleItemHolder> simpleItems,
        HashMap<SimpleToolType, Holder.SimpleToolHolder> simpleTools
) {
    public RegistrationHolder() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
    }
}
