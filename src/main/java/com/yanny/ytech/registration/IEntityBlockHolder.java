package com.yanny.ytech.registration;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface IEntityBlockHolder {
    <T extends BlockEntity> BlockEntityType<T> getBlockEntityType();
}
