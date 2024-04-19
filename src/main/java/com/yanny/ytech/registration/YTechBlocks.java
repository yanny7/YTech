package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.block.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.function.Function;

public class YTechBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, YTechMod.MOD_ID);

    public static final RegistryObject<Block> AQUEDUCT = BLOCKS.register("aqueduct", AqueductBlock::new);
    public static final RegistryObject<Block> AQUEDUCT_FERTILIZER = BLOCKS.register("aqueduct_fertilizer", AqueductFertilizerBlock::new);
    public static final RegistryObject<Block> AQUEDUCT_HYDRATOR = BLOCKS.register("aqueduct_hydrator", AqueductHydratorBlock::new);
    public static final RegistryObject<Block> AQUEDUCT_VALVE = BLOCKS.register("aqueduct_valve", AqueductValveBlock::new);
    public static final RegistryObject<Block> BRICK_CHIMNEY = BLOCKS.register("brick_chimney", BrickChimneyBlock::new);
    public static final RegistryObject<Block> BRONZE_ANVIL = BLOCKS.register("bronze_anvil", BronzeAnvilBlock::new);
    public static final RegistryObject<Block> FIRE_PIT = BLOCKS.register("fire_pit", FirePitBlock::new);
    public static final RegistryObject<Block> GRASS_BED = BLOCKS.register("grass_bed", GrassBedBlock::new);
    public static final RegistryObject<Block> MILLSTONE = BLOCKS.register("millstone", MillstoneBlock::new);
    public static final RegistryObject<Block> PRIMITIVE_ALLOY_SMELTER = BLOCKS.register("primitive_alloy_smelter", PrimitiveAlloySmelterBlock::new);
    public static final RegistryObject<Block> PRIMITIVE_SMELTER = BLOCKS.register("primitive_smelter", PrimitiveSmelterBlock::new);
    public static final RegistryObject<Block> REINFORCED_BRICKS = registerBlock("reinforced_bricks", Blocks.BRICKS);
    public static final RegistryObject<Block> REINFORCED_BRICK_CHIMNEY = BLOCKS.register("reinforced_brick_chimney", ReinforcedBrickChimneyBlock::new);
    public static final RegistryObject<Block> TERRACOTTA_BRICKS = registerBlock("terracotta_bricks", Blocks.BRICKS);
    public static final RegistryObject<Block> TERRACOTTA_BRICK_SLAB = registerSlab("terracotta_brick_slab", Blocks.BRICK_SLAB);
    public static final RegistryObject<Block> TERRACOTTA_BRICK_STAIRS = registerStairs("terracotta_brick_stairs", YTechBlocks.TERRACOTTA_BRICKS, Blocks.BRICK_STAIRS);
    public static final RegistryObject<Block> THATCH = registerBlock("thatch", Blocks.HAY_BLOCK);
    public static final RegistryObject<Block> THATCH_SLAB = registerSlab("thatch_slab", Blocks.HAY_BLOCK);
    public static final RegistryObject<Block> THATCH_STAIRS = registerStairs("thatch_stairs", YTechBlocks.THATCH, Blocks.HAY_BLOCK);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    private static RegistryObject<Block> registerBlock(String name, BlockBehaviour behaviour) {
        return BLOCKS.register(name, () -> new Block(BlockBehaviour.Properties.copy(behaviour)));
    }

    private static RegistryObject<Block> registerSlab(String name, BlockBehaviour behaviour) {
        return BLOCKS.register(name, () -> new SlabBlock(BlockBehaviour.Properties.copy(behaviour)));
    }

    private static RegistryObject<Block> registerStairs(String name, RegistryObject<Block> baseBlock, BlockBehaviour behaviour) {
        return BLOCKS.register(name, () -> new StairBlock(() -> baseBlock.get().defaultBlockState(), BlockBehaviour.Properties.copy(behaviour)));
    }

    public static class MaterialBlock {
        protected final String group;
        protected final GroupLocation groupLocation;
        protected final Map<MaterialType, RegistryObject<Block>> blocks;

        public MaterialBlock(String group, GroupLocation groupLocation, EnumSet<MaterialType> materialTypes, Function<MaterialType, Block> blockSupplier) {
            this(group, group, groupLocation, materialTypes, blockSupplier);
        }

        public MaterialBlock(String group, String groupShort, GroupLocation groupLocation, EnumSet<MaterialType> materialTypes, Function<MaterialType, Block> itemSupplier) {
            this.group = group;
            this.groupLocation = groupLocation;
            blocks = new HashMap<>();
            materialTypes.forEach((type) -> {
                String key;

                if (groupLocation == GroupLocation.PREFIX) {
                    key = groupShort + "_" + type.key;
                } else {
                    if (type.key.equals("gold")) {
                        key = "golden";
                    } else {
                        key = type.key;
                    }

                    key += "_" + groupShort;
                }

                blocks.put(type, BLOCKS.register(key, () -> itemSupplier.apply(type)));
            });
        }

        public RegistryObject<Block> of(MaterialType material) {
            return Objects.requireNonNull(blocks.get(material));
        }

        public Collection<RegistryObject<Block>> items() {
            return blocks.values();
        }

        public Set<MaterialType> materials() {
            return blocks.keySet();
        }

        public Set<Map.Entry<MaterialType, RegistryObject<Block>>> entries() {
            return blocks.entrySet();
        }

        public String getGroup() {
            return group;
        }

        public GroupLocation getGroupLocation() {
            return groupLocation;
        }
    }
}
