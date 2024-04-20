package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.AnimalEntityType;
import com.yanny.ytech.configuration.MaterialFluidType;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.SimpleEntityType;

import java.util.HashMap;

public record RegistrationHolder(
        HashMap<MaterialFluidType, HashMap<MaterialType, Holder.FluidHolder>> fluids,
        HashMap<SimpleEntityType, Holder.SimpleEntityHolder> simpleEntities,
        HashMap<AnimalEntityType, Holder.AnimalEntityHolder> entities
) {
    public RegistrationHolder() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>());
    }
}
