package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.*;

import java.util.HashMap;

public record RegistrationHolder(
        HashMap<MaterialBlockType, HashMap<MaterialType, Holder.BlockHolder>> blocks,
        HashMap<MaterialFluidType, HashMap<MaterialType, Holder.FluidHolder>> fluids,
        HashMap<SimpleEntityType, Holder.SimpleEntityHolder> simpleEntities,
        HashMap<AnimalEntityType, Holder.AnimalEntityHolder> entities
) {
    public RegistrationHolder() {
        this(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
    }
}
