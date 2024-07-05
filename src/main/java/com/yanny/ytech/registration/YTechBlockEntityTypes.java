package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block_entity.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;

public class YTechBlockEntityTypes {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, YTechMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<AqueductBlockEntity>> AQUEDUCT = register("aqueduct", AqueductBlockEntity::new, YTechBlocks.AQUEDUCT);
    public static final RegistryObject<BlockEntityType<AqueductFertilizerBlockEntity>> AQUEDUCT_FERTILIZER = register("aqueduct_fertilizer", AqueductFertilizerBlockEntity::new, YTechBlocks.AQUEDUCT_FERTILIZER);
    public static final RegistryObject<BlockEntityType<AqueductHydratorBlockEntity>> AQUEDUCT_HYDRATOR = register("aqueduct_hydrator", (pos, state) -> new AqueductHydratorBlockEntity(YTechBlockEntityTypes.AQUEDUCT_HYDRATOR.get(), pos, state), YTechBlocks.AQUEDUCT_HYDRATOR);
    public static final RegistryObject<BlockEntityType<AqueductValveBlockEntity>> AQUEDUCT_VALVE = register("aqueduct_valve", AqueductValveBlockEntity::new, YTechBlocks.AQUEDUCT_VALVE);
    public static final RegistryObject<BlockEntityType<BrickChimneyBlockEntity>> BRICK_CHIMNEY = register("brick_chimney", BrickChimneyBlockEntity::new, YTechBlocks.BRICK_CHIMNEY, YTechBlocks.REINFORCED_BRICK_CHIMNEY);
    public static final RegistryObject<BlockEntityType<BronzeAnvilBlockEntity>> BRONZE_ANVIL = register("bronze_anvil", BronzeAnvilBlockEntity::new, YTechBlocks.BRONZE_ANVIL);
    public static final RegistryObject<BlockEntityType<DryingRackBlockEntity>> DRYING_RACK = register("drying_rack", DryingRackBlockEntity::new, YTechBlocks.DRYING_RACKS);
    public static final RegistryObject<BlockEntityType<MillstoneBlockEntity>> MILLSTONE = register("millstone", MillstoneBlockEntity::new, YTechBlocks.MILLSTONE);
    public static final RegistryObject<BlockEntityType<PottersWheelBlockEntity>> POTTERS_WHEEL = register("potters_wheel", PottersWheelBlockEntity::new, YTechBlocks.POTTERS_WHEEL);
    public static final RegistryObject<BlockEntityType<PrimitiveAlloySmelterBlockEntity>> PRIMITIVE_ALLOY_SMELTER = register("primitive_alloy_smelter", PrimitiveAlloySmelterBlockEntity::new, YTechBlocks.PRIMITIVE_ALLOY_SMELTER);
    public static final RegistryObject<BlockEntityType<PrimitiveSmelterBlockEntity>> PRIMITIVE_SMELTER = register("primitive_smelter", PrimitiveSmelterBlockEntity::new, YTechBlocks.PRIMITIVE_SMELTER);
    public static final RegistryObject<BlockEntityType<TanningRackBlockEntity>> TANNING_RACK = register("tanning_rack", TanningRackBlockEntity::new, YTechBlocks.TANNING_RACKS);

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, YTechBlocks.MaterialBlock blocks) {
        return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(supplier, blocks.blocks().stream().map(RegistryObject::get).toArray(Block[]::new)).build(null));
    }

    @SafeVarargs
    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, RegistryObject<Block>... blocks) {
        return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(supplier, Arrays.stream(blocks).map(RegistryObject::get).toArray(Block[]::new)).build(null));
    }
}
