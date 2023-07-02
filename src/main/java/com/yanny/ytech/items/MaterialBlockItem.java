package com.yanny.ytech.items;

import com.yanny.ytech.configuration.YTechConfigLoader;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class MaterialBlockItem extends BlockItem {
    private final YTechConfigLoader.Material material;

    public MaterialBlockItem(Block block, YTechConfigLoader.Material material) {
        super(block, new Properties());
        this.material = material;
    }

    public YTechConfigLoader.Material getMaterial() {
        return material;
    }
}
