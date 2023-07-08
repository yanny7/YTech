package com.yanny.ytech.registration;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public record MachineHolder(
        RegistryObject<Block> block,
        RegistryObject<Item> item,
        RegistryObject<BlockEntityType<?>> blockEntityType,
        RegistryObject<MenuType<?>> menuType
) { }
