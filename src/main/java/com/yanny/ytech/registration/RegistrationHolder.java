package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.ProductType;
import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.network.kinetic.common.KineticBlockType;

import java.util.HashMap;

public record RegistrationHolder(
        HashMap<ProductType, HashMap<YTechConfigLoader.Material, Holder>> products,
        HashMap<YTechConfigLoader.Machine, HashMap<YTechConfigLoader.Tier, MachineHolder>> machine,
        HashMap<KineticBlockType, HashMap<YTechConfigLoader.Material, KineticNetworkHolder>> kineticNetwork
) {
    public RegistrationHolder() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>());
    }
}
