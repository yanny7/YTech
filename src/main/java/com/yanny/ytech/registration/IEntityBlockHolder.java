package com.yanny.ytech.registration;

import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface IEntityBlockHolder {
    Supplier<BlockEntityType<?>> getEntityTypeRegistry();
}
