package com.yanny.ytech.generation;

import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.registration.Holder;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;

class Utils {
    @NotNull
    static Comparator<Map.Entry<MaterialType, Holder.FluidHolder>> fluidComparator() {
        return Comparator.comparing(h -> h.getValue().object.id + h.getKey());
    }
}
