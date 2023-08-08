package com.yanny.ytech.registration;

import com.yanny.ytech.machine.MachineType;
import com.yanny.ytech.machine.TierType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public class MachineHolder {
    public final String key;
    public final String name;
    public final MachineType machine;
    public final TierType tier;
    public final RegistryObject<Block> block;
    public final RegistryObject<Item> item;
    public final RegistryObject<BlockEntityType<? extends BlockEntity>> blockEntityType;
    public final RegistryObject<MenuType<?>> menuType;

    public MachineHolder(MachineType machine, TierType tier, RegistryObject<Block> block, RegistryObject<Item> item,
                         RegistryObject<BlockEntityType<? extends BlockEntity>> blockEntityType, RegistryObject<MenuType<?>> menuType) {
        this.machine = machine;
        this.tier = tier;
        this.block = block;
        this.item = item;
        this.blockEntityType = blockEntityType;
        this.menuType = menuType;
        key = tier.key + "_" + machine.key;
        name = tier.name + " " + machine.name;
    }
}
