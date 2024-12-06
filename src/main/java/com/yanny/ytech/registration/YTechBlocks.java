package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.NameHolder;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class YTechBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(YTechMod.MOD_ID);

    public static final DeferredBlock<Block> AMPHORA = BLOCKS.registerBlock("amphora", AmphoraBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA));
    public static final DeferredBlock<Block> AQUEDUCT_FERTILIZER = BLOCKS.registerBlock("aqueduct_fertilizer", AqueductFertilizerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA));
    public static final DeferredBlock<Block> AQUEDUCT_HYDRATOR = BLOCKS.registerBlock("aqueduct_hydrator", AqueductHydratorBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA));
    public static final DeferredBlock<Block> AQUEDUCT_VALVE = BLOCKS.registerBlock("aqueduct_valve", AqueductValveBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA));
    public static final DeferredBlock<Block> BRICK_CHIMNEY = BLOCKS.registerBlock("brick_chimney", BrickChimneyBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS));
    public static final DeferredBlock<Block> BRONZE_ANVIL = BLOCKS.registerBlock("bronze_anvil", BronzeAnvilBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL));
    public static final DeferredBlock<Block> CRAFTING_WORKSPACE = BLOCKS.registerBlock("crafting_workspace", CraftingWorkspaceBlock::new, BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> FIRE_PIT = BLOCKS.registerBlock("fire_pit", FirePitBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));
    public static final DeferredBlock<Block> GRASS_BED = BLOCKS.registerBlock("grass_bed", GrassBedBlock::new, BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> MILLSTONE = BLOCKS.registerBlock("millstone", MillstoneBlock::new, BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> POTTERS_WHEEL = BLOCKS.registerBlock("potters_wheel", PottersWheelBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));
    public static final DeferredBlock<Block> PRIMITIVE_ALLOY_SMELTER = BLOCKS.registerBlock("primitive_alloy_smelter", PrimitiveAlloySmelterBlock::new, BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> PRIMITIVE_SMELTER = BLOCKS.registerBlock("primitive_smelter", PrimitiveSmelterBlock::new, BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> REINFORCED_BRICKS = registerBlock("reinforced_bricks", Blocks.BRICKS);
    public static final DeferredBlock<Block> REINFORCED_BRICK_CHIMNEY = BLOCKS.registerBlock("reinforced_brick_chimney", ReinforcedBrickChimneyBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS));
    public static final DeferredBlock<Block> TERRACOTTA_BRICKS = registerBlock("terracotta_bricks", Blocks.BRICKS);
    public static final DeferredBlock<Block> TERRACOTTA_BRICK_SLAB = registerSlab("terracotta_brick_slab", Blocks.BRICK_SLAB);
    public static final DeferredBlock<Block> TERRACOTTA_BRICK_STAIRS = registerStairs("terracotta_brick_stairs", YTechBlocks.TERRACOTTA_BRICKS, Blocks.BRICK_STAIRS);
    public static final DeferredBlock<Block> THATCH = registerBlock("thatch", Blocks.HAY_BLOCK);
    public static final DeferredBlock<Block> THATCH_SLAB = registerSlab("thatch_slab", Blocks.HAY_BLOCK);
    public static final DeferredBlock<Block> THATCH_STAIRS = registerStairs("thatch_stairs", YTechBlocks.THATCH, Blocks.HAY_BLOCK);
    public static final DeferredBlock<Block> TOOL_RACK = BLOCKS.registerBlock("tool_rack", ToolRackBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));
    public static final DeferredBlock<Block> TREE_STUMP = BLOCKS.registerBlock("tree_stump", TreeStumpBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD));
    public static final DeferredBlock<Block> WELL_PULLEY = BLOCKS.registerBlock("well_pulley", WellPulleyBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA));
    public static final DeferredBlock<Block> WOODEN_BOX = BLOCKS.registerBlock("wooden_box", WoodenBoxBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));

    public static final MaterialBlock AQUEDUCTS = new MaterialBlock("aqueduct", NameHolder.suffix("aqueduct"), MaterialType.AQUEDUCT_MATERIALS, AqueductBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA));
    public static final MaterialBlock DEEPSLATE_ORES = new DeepslateOreMaterialBlock();
    public static final MaterialBlock DRYING_RACKS = new MaterialBlock("drying_rack", NameHolder.suffix("drying_rack"), MaterialType.ALL_WOODS, DryingRackBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));
    public static final MaterialBlock GRAVEL_DEPOSITS = new MaterialBlock("gravel_deposit", NameHolder.suffix("gravel_deposit"), MaterialType.ALL_DEPOSIT_ORES, (type, properties) -> new ColoredFallingBlock(new ColorRGBA(-8356741), properties), BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL));
    public static final MaterialBlock NETHER_ORES = new NetherOreMaterialBlock();
    public static final MaterialBlock RAW_STORAGE_BLOCKS = new RawStorageBlockMaterialBlock();
    public static final MaterialBlock SAND_DEPOSITS = new MaterialBlock("sand_deposit", NameHolder.suffix("sand_deposit"), MaterialType.ALL_DEPOSIT_ORES, (type, properties) -> new ColoredFallingBlock(new ColorRGBA(14406560), properties), BlockBehaviour.Properties.ofFullCopy(Blocks.SAND));
    public static final MaterialBlock STONE_ORES = new StoneOreMaterialBlock();
    public static final MaterialBlock STORAGE_BLOCKS = new StorageBlockMaterialBlock();
    public static final MaterialBlock TANNING_RACKS = new MaterialBlock("tanning_rack", NameHolder.suffix("tanning_rack"), MaterialType.ALL_WOODS, TanningRackBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    private static DeferredBlock<Block> registerBlock(String name, BlockBehaviour behaviour) {
        return BLOCKS.registerSimpleBlock(name, BlockBehaviour.Properties.ofFullCopy(behaviour));
    }

    private static DeferredBlock<Block> registerSlab(String name, BlockBehaviour behaviour) {
        return BLOCKS.registerBlock(name, SlabBlock::new, BlockBehaviour.Properties.ofFullCopy(behaviour));
    }

    private static DeferredBlock<Block> registerStairs(String name, DeferredBlock<Block> baseBlock, BlockBehaviour behaviour) {
        return BLOCKS.registerBlock(name, (properties) -> new StairBlock(baseBlock.get().defaultBlockState(), properties), BlockBehaviour.Properties.ofFullCopy(behaviour));
    }

    public static class MaterialBlock {
        protected final String group;
        protected final NameHolder nameHolder;
        protected final Map<MaterialType, DeferredBlock<Block>> blocks;

        public MaterialBlock(String group, NameHolder nameHolder, EnumSet<MaterialType> materialTypes, BiFunction<MaterialType, BlockBehaviour.Properties, Block> itemSupplier, BlockBehaviour.Properties properties) {
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
                blocks.put(type, BLOCKS.registerBlock(key, (props) -> itemSupplier.apply(type, props), properties));
            });
        }

        public MaterialBlock(String group, NameHolder nameHolder, EnumSet<MaterialType> materialTypes, Function<BlockBehaviour.Properties, Block> itemSupplier, BlockBehaviour.Properties properties) {
            this(group, nameHolder, materialTypes, (m, p) -> itemSupplier.apply(p), properties);
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
            super("deepslate_ore", NameHolder.both("deepslate", "ore"), Utils.exclude(MaterialType.ALL_ORES, MaterialType.VANILLA_METALS), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));
            blocks.put(MaterialType.COPPER, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.DEEPSLATE_COPPER_ORE)));
            blocks.put(MaterialType.GOLD, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.DEEPSLATE_GOLD_ORE)));
            blocks.put(MaterialType.IRON, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.DEEPSLATE_IRON_ORE)));
        }
    }

    private static class NetherOreMaterialBlock extends MaterialBlock {
        public NetherOreMaterialBlock() {
            super("nether_ore", NameHolder.both("nether", "ore"), Utils.exclude(MaterialType.ALL_ORES, MaterialType.GOLD), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_GOLD_ORE));
            blocks.put(MaterialType.GOLD, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.NETHER_GOLD_ORE)));
        }
    }

    private static class RawStorageBlockMaterialBlock extends MaterialBlock {
        public RawStorageBlockMaterialBlock() {
            super("storage_block", NameHolder.both("raw", "block"), Utils.exclude(MaterialType.ALL_ORES, MaterialType.VANILLA_METALS), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK));
            blocks.put(MaterialType.COPPER, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.RAW_COPPER_BLOCK)));
            blocks.put(MaterialType.GOLD, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.RAW_GOLD_BLOCK)));
            blocks.put(MaterialType.IRON, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.RAW_IRON_BLOCK)));
        }
    }

    private static class StoneOreMaterialBlock extends MaterialBlock {
        public StoneOreMaterialBlock() {
            super("stone_ore", NameHolder.suffix("ore"), Utils.exclude(MaterialType.ALL_ORES, MaterialType.VANILLA_METALS), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
            blocks.put(MaterialType.COPPER, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.COPPER_ORE)));
            blocks.put(MaterialType.GOLD, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.GOLD_ORE)));
            blocks.put(MaterialType.IRON, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.IRON_ORE)));
        }
    }

    private static class StorageBlockMaterialBlock extends MaterialBlock {
        public StorageBlockMaterialBlock() {
            super("storage_block", NameHolder.suffix("block"), Utils.exclude(MaterialType.ALL_METALS, MaterialType.VANILLA_METALS), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));
            blocks.put(MaterialType.COPPER, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.COPPER_BLOCK)));
            blocks.put(MaterialType.GOLD, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.GOLD_BLOCK)));
            blocks.put(MaterialType.IRON, DeferredBlock.createBlock(BuiltInRegistries.BLOCK.getKey(Blocks.IRON_BLOCK)));
        }
    }
}
