package com.yanny.ytech.blocks;

import com.yanny.ytech.model.YTechConfigLoader;
import net.minecraft.world.level.block.Block;

public class OreBlock extends MaterialBlock {
    private final Block baseBlock;

    public OreBlock(YTechConfigLoader.Material material, Block base) {
        super(material);
        this.baseBlock = base;
    }

    public Block getBaseBlock() {
        return baseBlock;
    }
}