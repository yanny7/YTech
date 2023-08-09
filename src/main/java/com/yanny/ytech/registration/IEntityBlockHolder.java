package com.yanny.ytech.registration;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public interface IEntityBlockHolder {
    RegistryObject<BlockEntityType<?>> getEntityTypeRegistry();
}
