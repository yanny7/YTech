package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block_entity.BronzeAnvilBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class YTechBlockEntityTypes {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, YTechMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<BronzeAnvilBlockEntity>> BRONZE_ANVIL = register("bronze_anvil", YTechBlocks.BRONZE_ANVIL, BronzeAnvilBlockEntity::new);

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, RegistryObject<Block> block,  BlockEntityType.BlockEntitySupplier<T> supplier) {
        return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
    }
}
