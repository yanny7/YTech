package com.yanny.ytech.machine;

import java.util.HashMap;
import java.util.Map;

public enum MachineType {
    FURNACE("furnace"),
    CRUSHER("crusher");

    private static final Map<String, MachineType> MACHINE_TYPE_MAP = new HashMap<>();

    public final String id;

    static {
        for(final MachineType machineType : MachineType.values()) {
            MACHINE_TYPE_MAP.put(machineType.id, machineType);
        }
    }

    MachineType(String id) {
        this.id = id;
    }

    public static MachineType fromConfiguration(String id) {
        return MACHINE_TYPE_MAP.get(id);
    }
}
