package com.yanny.ytech.network.kinetic.block;

import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.network.kinetic.common.KineticBlockType;
import net.minecraft.world.level.block.Block;

public class KineticBlockFactory {
    public static Block create(KineticBlockType kineticBlockType, YTechConfigLoader.KineticMaterial material) {
        return switch (kineticBlockType) {
            case SHAFT -> new ShaftBlock(material);
            case WATER_WHEEL -> new WaterWheelBlock(material);
        };
    }

    private KineticBlockFactory() {}
}
