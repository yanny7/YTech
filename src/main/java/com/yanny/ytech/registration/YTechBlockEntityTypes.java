package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block_entity.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;

public class YTechBlockEntityTypes {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, YTechMod.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AmphoraBlockEntity>> AMPHORA = register("amphora", AmphoraBlockEntity::new, YTechBlocks.AMPHORA);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AqueductBlockEntity>> AQUEDUCT = register("aqueduct", AqueductBlockEntity::new, YTechBlocks.AQUEDUCT);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AqueductFertilizerBlockEntity>> AQUEDUCT_FERTILIZER = register("aqueduct_fertilizer", AqueductFertilizerBlockEntity::new, YTechBlocks.AQUEDUCT_FERTILIZER);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AqueductHydratorBlockEntity>> AQUEDUCT_HYDRATOR = register("aqueduct_hydrator", (pos, state) -> new AqueductHydratorBlockEntity(YTechBlockEntityTypes.AQUEDUCT_HYDRATOR.get(), pos, state), YTechBlocks.AQUEDUCT_HYDRATOR);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AqueductValveBlockEntity>> AQUEDUCT_VALVE = register("aqueduct_valve", AqueductValveBlockEntity::new, YTechBlocks.AQUEDUCT_VALVE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BrickChimneyBlockEntity>> BRICK_CHIMNEY = register("brick_chimney", BrickChimneyBlockEntity::new, YTechBlocks.BRICK_CHIMNEY, YTechBlocks.REINFORCED_BRICK_CHIMNEY);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BronzeAnvilBlockEntity>> BRONZE_ANVIL = register("bronze_anvil", BronzeAnvilBlockEntity::new, YTechBlocks.BRONZE_ANVIL);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CraftingWorkspaceBlockEntity>> CRAFTING_WORKSPACE = register("crafting_workspace", CraftingWorkspaceBlockEntity::new, YTechBlocks.CRAFTING_WORKSPACE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DryingRackBlockEntity>> DRYING_RACK = register("drying_rack", DryingRackBlockEntity::new, YTechBlocks.DRYING_RACKS);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FirePitBlockEntity>> FIRE_PIT = register("fire_pit", FirePitBlockEntity::new, YTechBlocks.FIRE_PIT);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MillstoneBlockEntity>> MILLSTONE = register("millstone", MillstoneBlockEntity::new, YTechBlocks.MILLSTONE);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PottersWheelBlockEntity>> POTTERS_WHEEL = register("potters_wheel", PottersWheelBlockEntity::new, YTechBlocks.POTTERS_WHEEL);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PrimitiveAlloySmelterBlockEntity>> PRIMITIVE_ALLOY_SMELTER = register("primitive_alloy_smelter", PrimitiveAlloySmelterBlockEntity::new, YTechBlocks.PRIMITIVE_ALLOY_SMELTER);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PrimitiveSmelterBlockEntity>> PRIMITIVE_SMELTER = register("primitive_smelter", PrimitiveSmelterBlockEntity::new, YTechBlocks.PRIMITIVE_SMELTER);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TanningRackBlockEntity>> TANNING_RACK = register("tanning_rack", TanningRackBlockEntity::new, YTechBlocks.TANNING_RACKS);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ToolRackBlockEntity>> TOOL_RACK = register("tool_rack", ToolRackBlockEntity::new, YTechBlocks.TOOL_RACK);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TreeStumpBlockEntity>> TREE_STUMP = register("tree_stump", TreeStumpBlockEntity::new, YTechBlocks.TREE_STUMP);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WoodenBoxBlockEntity>> WOODEN_BOX = register("wooden_box", WoodenBoxBlockEntity::new, YTechBlocks.WOODEN_BOX);

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, YTechBlocks.MaterialBlock blocks) {
        return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(supplier, blocks.blocks().stream().map(DeferredHolder::get).toArray(Block[]::new)).build(null));
    }

    @SafeVarargs
    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, DeferredBlock<Block>... blocks) {
        return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(supplier, Arrays.stream(blocks).map(DeferredHolder::get).toArray(Block[]::new)).build(null));
    }
}
