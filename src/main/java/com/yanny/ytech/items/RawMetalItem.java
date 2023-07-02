package com.yanny.ytech.items;

import com.yanny.ytech.configuration.YTechConfigLoader;
import net.minecraft.world.item.Item;

public class RawMetalItem extends Item {
    private final YTechConfigLoader.Material material;
    public RawMetalItem(YTechConfigLoader.Material material) {
        super(new Properties());
        this.material = material;
    }

    public YTechConfigLoader.Material getMaterial() {
        return this.material;
    }
}
