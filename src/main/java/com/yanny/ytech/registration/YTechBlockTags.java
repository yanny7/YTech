package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.Utils;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class YTechBlockTags {
    public static final TagKey<Block> BRONZE_ANVIL = create("bronze_anvil");

    private static TagKey<Block> create(String name) {
        return BlockTags.create(Utils.modLoc(name));
    }
}
