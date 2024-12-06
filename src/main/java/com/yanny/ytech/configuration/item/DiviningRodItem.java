package com.yanny.ytech.configuration.item;

import com.yanny.ytech.configuration.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class DiviningRodItem extends Item {
    public static final ResourceLocation ABUNDANCE_PREDICATE = Utils.modLoc("right_spot");

    public DiviningRodItem(Properties properties) {
        super(properties.stacksTo(1));
    }
}
