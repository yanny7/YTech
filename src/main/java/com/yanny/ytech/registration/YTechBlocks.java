package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.NameHolder;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;
import java.util.function.Supplier;

public class YTechBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(YTechMod.MOD_ID);

    public static final DeferredBlock<Block> AQUEDUCT = BLOCKS.register("aqueduct", AqueductBlock::new);
    public static final DeferredBlock<Block> AQUEDUCT_FERTILIZER = BLOCKS.register("aqueduct_fertilizer", AqueductFertilizerBlock::new);
    public static final DeferredBlock<Block> AQUEDUCT_HYDRATOR = BLOCKS.register("aqueduct_hydrator", AqueductHydratorBlock::new);
    public static final DeferredBlock<Block> AQUEDUCT_VALVE = BLOCKS.register("aqueduct_valve", AqueductValveBlock::new);
    public static final DeferredBlock<Block> BRICK_CHIMNEY = BLOCKS.register("brick_chimney", BrickChimneyBlock::new);
    public static final DeferredBlock<Block> BRONZE_ANVIL = BLOCKS.register("bronze_anvil", BronzeAnvilBlock::new);
    public static final DeferredBlock<Block> FIRE_PIT = BLOCKS.register("fire_pit", FirePitBlock::new);
    public static final DeferredBlock<Block> GRASS_BED = BLOCKS.register("grass_bed", GrassBedBlock::new);
    public static final DeferredBlock<Block> MILLSTONE = BLOCKS.register("millstone", MillstoneBlock::new);
    public static final DeferredBlock<Block> POTTERS_WHEEL = BLOCKS.register("potters_wheel", PottersWheelBlock::new);
    public static final DeferredBlock<Block> PRIMITIVE_ALLOY_SMELTER = BLOCKS.register("primitive_alloy_smelter", PrimitiveAlloySmelterBlock::new);
    public static final DeferredBlock<Block> PRIMITIVE_SMELTER = BLOCKS.register("primitive_smelter", PrimitiveSmelterBlock::new);
    public static final DeferredBlock<Block> REINFORCED_BRICKS = registerBlock("reinforced_bricks", Blocks.BRICKS);
    public static final DeferredBlock<Block> REINFORCED_BRICK_CHIMNEY = BLOCKS.register("reinforced_brick_chimney", ReinforcedBrickChimneyBlock::new);
    public static final DeferredBlock<Block> TERRACOTTA_BRICKS = registerBlock("terracotta_bricks", Blocks.BRICKS);
    public static final DeferredBlock<Block> TERRACOTTA_BRICK_SLAB = registerSlab("terracotta_brick_slab", Blocks.BRICK_SLAB);
    public static final DeferredBlock<Block> TERRACOTTA_BRICK_STAIRS = registerStairs("terracotta_brick_stairs", YTechBlocks.TERRACOTTA_BRICKS, Blocks.BRICK_STAIRS);
    public static final DeferredBlock<Block> THATCH = registerBlock("thatch", Blocks.HAY_BLOCK);
    public static final DeferredBlock<Block> THATCH_SLAB = registerSlab("thatch_slab", Blocks.HAY_BLOCK);
    public static final DeferredBlock<Block> THATCH_STAIRS = registerStairs("thatch_stairs", YTechBlocks.THATCH, Blocks.HAY_BLOCK);

    public static final MaterialBlock DEEPSLATE_ORES = new DeepslateOreMaterialBlock();
    public static final MaterialBlock DRYING_RACKS = new MaterialBlock("drying_rack", NameHolder.suffix("drying_rack"), MaterialType.ALL_WOODS, DryingRackBlock::new);
    public static final MaterialBlock GRAVEL_DEPOSITS = new MaterialBlock("gravel_deposit", NameHolder.suffix("gravel_deposit"), MaterialType.ALL_DEPOSIT_ORES, () -> new GravelBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL)));
    public static final MaterialBlock NETHER_ORES = new NetherOreMaterialBlock();
    public static final MaterialBlock RAW_STORAGE_BLOCKS = new RawStorageBlockMaterialBlock();
    public static final MaterialBlock SAND_DEPOSITS = new MaterialBlock("sand_deposit", NameHolder.suffix("sand_deposit"), MaterialType.ALL_DEPOSIT_ORES, () -> new SandBlock(14406560, BlockBehaviour.Properties.copy(Blocks.SAND)));
    public static final MaterialBlock STONE_ORES = new StoneOreMaterialBlock();
    public static final MaterialBlock STORAGE_BLOCKS = new StorageBlockMaterialBlock();
    public static final MaterialBlock TANNING_RACKS = new MaterialBlock("tanning_rack", NameHolder.suffix("tanning_rack"), MaterialType.ALL_WOODS, TanningRackBlock::new);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    private static DeferredBlock<Block> registerBlock(String name, BlockBehaviour behaviour) {
        return BLOCKS.register(name, () -> new Block(BlockBehaviour.Properties.copy(behaviour)));
    }

    private static DeferredBlock<Block> registerSlab(String name, BlockBehaviour behaviour) {
        return BLOCKS.register(name, () -> new SlabBlock(BlockBehaviour.Properties.copy(behaviour)));
    }

    private static DeferredBlock<Block> registerStairs(String name, DeferredBlock<Block> baseBlock, BlockBehaviour behaviour) {
        return BLOCKS.register(name, () -> new StairBlock(() -> baseBlock.get().defaultBlockState(), BlockBehaviour.Properties.copy(behaviour)));
    }

    public static class MaterialBlock {
        protected final String group;
        protected final NameHolder nameHolder;
        protected final Map<MaterialType, DeferredBlock<Block>> blocks;

        public MaterialBlock(String group, NameHolder nameHolder, EnumSet<MaterialType> materialTypes, Supplier<Block> itemSupplier) {
            this.group = group;
            this.nameHolder = nameHolder;
            blocks = new HashMap<>();
            materialTypes.forEach((type) -> {
                String key = nameHolder.prefix() != null ? nameHolder.prefix() + "_" : "";

                if (type.key.equals("gold") && nameHolder.prefix() == null) {
                    key += "golden";
                } else {
                    key += type.key;
                }

                key += nameHolder.suffix() != null ? "_" + nameHolder.suffix() : "";
                blocks.put(type, BLOCKS.register(key, itemSupplier));
            });
        }

        public DeferredBlock<Block> of(MaterialType material) {
            return Objects.requireNonNull(blocks.get(material));
        }

        public Collection<DeferredBlock<Block>> blocks() {
            return blocks.values();
        }

        public Set<MaterialType> materials() {
            return blocks.keySet();
        }

        public Set<Map.Entry<MaterialType, DeferredBlock<Block>>> entries() {
            return blocks.entrySet();
        }

        public String getGroup() {
            return group;
        }
    }

    private static class DeepslateOreMaterialBlock extends MaterialBlock {
        public DeepslateOreMaterialBlock() {
            super("deepslate_ore", NameHolder.both("deepslate", "ore"), Utils.exclude(MaterialType.ALL_ORES, MaterialType.VANILLA_METALS), () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)));
            blocks.put(MaterialType.COPPER, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.DEEPSLATE_COPPER_ORE)));
            blocks.put(MaterialType.GOLD, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.DEEPSLATE_GOLD_ORE)));
            blocks.put(MaterialType.IRON, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.DEEPSLATE_IRON_ORE)));
        }
    }

    private static class NetherOreMaterialBlock extends MaterialBlock {
        public NetherOreMaterialBlock() {
            super("nether_ore", NameHolder.both("nether", "ore"), Utils.exclude(MaterialType.ALL_ORES, MaterialType.GOLD), () -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHER_GOLD_ORE)));
            blocks.put(MaterialType.GOLD, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.NETHER_GOLD_ORE)));
        }
    }

    private static class RawStorageBlockMaterialBlock extends MaterialBlock {
        public RawStorageBlockMaterialBlock() {
            super("storage_block", NameHolder.both("raw", "block"), Utils.exclude(MaterialType.ALL_ORES, MaterialType.VANILLA_METALS), () -> new Block(BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK)));
            blocks.put(MaterialType.COPPER, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.RAW_COPPER_BLOCK)));
            blocks.put(MaterialType.GOLD, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.RAW_GOLD_BLOCK)));
            blocks.put(MaterialType.IRON, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.RAW_IRON_BLOCK)));
        }
    }

    private static class StoneOreMaterialBlock extends MaterialBlock {
        public StoneOreMaterialBlock() {
            super("stone_ore", NameHolder.suffix("ore"), Utils.exclude(MaterialType.ALL_ORES, MaterialType.VANILLA_METALS), () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)));
            blocks.put(MaterialType.COPPER, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.COPPER_ORE)));
            blocks.put(MaterialType.GOLD, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.GOLD_ORE)));
            blocks.put(MaterialType.IRON, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.IRON_ORE)));
        }
    }

    private static class StorageBlockMaterialBlock extends MaterialBlock {
        public StorageBlockMaterialBlock() {
            super("storage_block", NameHolder.suffix("block"), Utils.exclude(MaterialType.ALL_METALS, MaterialType.VANILLA_METALS), () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
            blocks.put(MaterialType.COPPER, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.COPPER_BLOCK)));
            blocks.put(MaterialType.GOLD, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.GOLD_BLOCK)));
            blocks.put(MaterialType.IRON, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.IRON_BLOCK)));
        }
    }
}
