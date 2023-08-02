package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.ConfigLoader;
import com.yanny.ytech.configuration.ProductType;
import com.yanny.ytech.network.kinetic.common.KineticBlockType;

import java.util.HashMap;

public record RegistrationHolder(
        HashMap<ProductType, HashMap<ConfigLoader.Material, Holder>> products,
        HashMap<ConfigLoader.Machine, HashMap<ConfigLoader.Tier, MachineHolder>> machine,
        HashMap<KineticBlockType, HashMap<ConfigLoader.Material, KineticNetworkHolder>> kineticNetwork
) {
    public RegistrationHolder() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>());
    }
}
