package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.AnimalEntityType;
import com.yanny.ytech.configuration.MaterialFluidType;
import com.yanny.ytech.configuration.MaterialType;

import java.util.HashMap;

public record RegistrationHolder(
        HashMap<MaterialFluidType, HashMap<MaterialType, Holder.FluidHolder>> fluids,
        HashMap<AnimalEntityType, Holder.AnimalEntityHolder> entities
) {
    public RegistrationHolder() {
        this(new HashMap<>(), new HashMap<>());
    }
}
