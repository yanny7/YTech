package com.yanny.ytech.registration;

import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.network.kinetic.common.KineticBlockType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public class KineticNetworkHolder {
    public final String key;
    public final String name;
    public final KineticBlockType blockType;
    public final YTechConfigLoader.Material material;
    public final RegistryObject<Block> block;
    public final RegistryObject<Item> item;
    public final RegistryObject<BlockEntityType<? extends BlockEntity>> entityType;

    public KineticNetworkHolder(KineticBlockType blockType, YTechConfigLoader.Material material, RegistryObject<Block> block,
                                RegistryObject<Item> item, RegistryObject<BlockEntityType<? extends BlockEntity>> entityType) {
        this.blockType = blockType;
        this.material = material;
        this.block = block;
        this.item = item;
        this.entityType = entityType;
        key = material.id().toLowerCase() + "_" + blockType.id;
        name = material.name() + " " + blockType.lang;
    }
}
