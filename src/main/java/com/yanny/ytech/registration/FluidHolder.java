package com.yanny.ytech.registration;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.RegistryObject;

public record FluidHolder(
        RegistryObject<FluidType> type,
        RegistryObject<Fluid> source,
        RegistryObject<Fluid> flowing,
        RegistryObject<Item> bucket,
        RegistryObject<Block> block
) {}
