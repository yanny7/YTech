package com.yanny.ytech.registration;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public record KineticNetworkHolder(
        RegistryObject<Block> block,
        RegistryObject<Item> item,
        RegistryObject<BlockEntityType<? extends BlockEntity>> entityType
) {}
