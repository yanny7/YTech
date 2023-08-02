package com.yanny.ytech.network.kinetic.block;

import com.yanny.ytech.configuration.ConfigLoader;
import com.yanny.ytech.network.kinetic.common.KineticBlockType;
import net.minecraft.world.level.block.Block;

public class KineticBlockFactory {
    public static Block create(KineticBlockType kineticBlockType, ConfigLoader.KineticMaterial material) {
        return switch (kineticBlockType) {
            case SHAFT -> new ShaftBlock(material);
            case WATER_WHEEL -> new WaterWheelBlock(material);
        };
    }

    private KineticBlockFactory() {}
}
