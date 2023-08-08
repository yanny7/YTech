package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.*;
import com.yanny.ytech.machine.MachineType;
import com.yanny.ytech.machine.TierType;
import com.yanny.ytech.network.kinetic.common.KineticBlockType;

import java.util.HashMap;

public record RegistrationHolder(
        HashMap<MaterialItemType, HashMap<MaterialType, Holder.ItemHolder>> items,
        HashMap<MaterialBlockType, HashMap<MaterialType, Holder.BlockHolder>> blocks,
        HashMap<MaterialFluidType, HashMap<MaterialType, Holder.FluidHolder>> fluids,
        HashMap<MachineType, HashMap<TierType, MachineHolder>> machine,
        HashMap<KineticBlockType, HashMap<MaterialType, KineticNetworkHolder>> kineticNetwork,
        HashMap<SimpleItemType, Holder.SimpleItemHolder> simpleItems,
        HashMap<SimpleBlockType, Holder.SimpleBlockHolder> simpleBlocks
) {
    public RegistrationHolder() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
    }
}
