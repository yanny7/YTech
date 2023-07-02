package com.yanny.ytech.blocks;

import com.yanny.ytech.model.YTechConfigLoader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class MaterialBlock extends Block {
    private final YTechConfigLoader.Material material;

    public MaterialBlock(YTechConfigLoader.Material material) {
        super(Properties.of().strength(material.hardness()).requiresCorrectToolForDrops().sound(SoundType.METAL));
        this.material = material;
    }

    public YTechConfigLoader.Material getMaterial() {
        return material;
    }
}