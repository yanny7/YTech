package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.*;
import com.yanny.ytech.configuration.block.AqueductBlock;
import com.yanny.ytech.configuration.block_entity.AbstractPrimitiveMachineBlockEntity;
import com.yanny.ytech.configuration.item.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.yanny.ytech.YTechMod.CONFIGURATION;
import static net.minecraft.ChatFormatting.DARK_GRAY;

public class YTechItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(YTechMod.MOD_ID);

    public static final DeferredItem<Item> ANTLER = ITEMS.registerSimpleItem("antler");
    public static final DeferredItem<Item> BASKET = ITEMS.registerItem("basket", BasketItem::new);
    public static final DeferredItem<Item> BEESWAX = ITEMS.registerSimpleItem("beeswax");
    public static final DeferredItem<Item> BONE_NEEDLE = ITEMS.registerItem("bone_needle", (properties) -> simpleItem(properties.durability(5)));
    public static final DeferredItem<Item> BREAD_DOUGH = ITEMS.registerSimpleItem("bread_dough");
    public static final DeferredItem<Item> BRICK_MOLD = ITEMS.registerItem("brick_mold", (properties) -> new Item(properties.durability(256)));
    public static final DeferredItem<Item> CLAY_BUCKET = ITEMS.registerItem("clay_bucket", (properties) -> new ClayBucketItem(Fluids.EMPTY, properties.stacksTo(8)));
    public static final DeferredItem<Item> COOKED_VENISON = ITEMS.registerItem("cooked_venison", (properties) -> foodItem(7, 0.8f, properties));
    public static final DeferredItem<Item> DIVINING_ROD = ITEMS.registerItem("divining_rod", DiviningRodItem::new);
    public static final DeferredItem<Item> DRIED_BEEF = ITEMS.registerItem("dried_beef", (properties) -> foodItem(6, 0.7f, properties));
    public static final DeferredItem<Item> DRIED_CHICKEN = ITEMS.registerItem("dried_chicken", (properties) -> foodItem(4, 0.5f, properties));
    public static final DeferredItem<Item> DRIED_COD = ITEMS.registerItem("dried_cod", (properties) -> foodItem(4, 0.5f, properties));
    public static final DeferredItem<Item> DRIED_MUTTON = ITEMS.registerItem("dried_mutton", (properties) -> foodItem(4, 0.5f, properties));
    public static final DeferredItem<Item> DRIED_PORKCHOP = ITEMS.registerItem("dried_porkchop", (properties) -> foodItem(6, 0.7f, properties));
    public static final DeferredItem<Item> DRIED_RABBIT = ITEMS.registerItem("dried_rabbit", (properties) -> foodItem(4, 0.5f, properties));
    public static final DeferredItem<Item> DRIED_SALMON = ITEMS.registerItem("dried_salmon", (properties) -> foodItem(4, 0.5f, properties));
    public static final DeferredItem<Item> DRIED_VENISON = ITEMS.registerItem("dried_venison", (properties) -> foodItem(5, 0.7f, properties));
    public static final DeferredItem<Item> FLOUR = ITEMS.registerSimpleItem("flour");
    public static final DeferredItem<Item> GRASS_FIBERS = ITEMS.registerItem("grass_fibers", (properties) -> descriptionItem(List.of(Component.translatable("text.ytech.hover.grass_fibers").withStyle(DARK_GRAY)), properties));
    public static final DeferredItem<Item> GRASS_TWINE = ITEMS.registerSimpleItem("grass_twine");
    public static final DeferredItem<Item> IRON_BLOOM = ITEMS.registerSimpleItem("iron_bloom");
    public static final DeferredItem<Item> LAVA_CLAY_BUCKET = ITEMS.registerItem("lava_clay_bucket", (properties) -> new ClayBucketItem(Fluids.LAVA, properties.craftRemainder(YTechItems.CLAY_BUCKET.get()).stacksTo(1)));
    public static final DeferredItem<Item> LEATHER_STRIPS = ITEMS.registerSimpleItem("leather_strips");
    public static final DeferredItem<Item> MAMMOTH_TUSK = ITEMS.registerSimpleItem("mammoth_tusk");
    public static final DeferredItem<Item> PEBBLE = ITEMS.registerItem("pebble", PebbleItem::new);
    public static final DeferredItem<Item> RAW_HIDE = ITEMS.registerSimpleItem("raw_hide");
    public static final DeferredItem<Item> RHINO_HORN = ITEMS.registerSimpleItem("rhino_horn");
    public static final DeferredItem<Item> SHARP_FLINT = ITEMS.registerItem("sharp_flint", (properties) -> ToolItem.knife(MaterialType.FLINT, properties));
    public static final DeferredItem<Item> UNFIRED_AMPHORA = ITEMS.registerSimpleItem("unfired_amphora");
    public static final DeferredItem<Item> UNFIRED_BRICK = ITEMS.registerSimpleItem("unfired_brick");
    public static final DeferredItem<Item> UNFIRED_CLAY_BUCKET = ITEMS.registerSimpleItem("unfired_clay_bucket");
    public static final DeferredItem<Item> UNFIRED_DECORATED_POT = ITEMS.registerSimpleItem("unfired_decoration_pot");
    public static final DeferredItem<Item> UNFIRED_FLOWER_POT = ITEMS.registerSimpleItem("unfired_flower_pot");
    public static final DeferredItem<Item> UNLIT_TORCH = ITEMS.registerItem("unlit_torch", UnlitTorchItem::new);
    public static final DeferredItem<Item> VENISON = ITEMS.registerItem("venison", (properties) -> foodItem(2, 0.3f, properties));
    public static final DeferredItem<Item> WATER_CLAY_BUCKET = ITEMS.registerItem("water_clay_bucket", (properties) -> new ClayBucketItem(Fluids.WATER, properties.craftRemainder(YTechItems.CLAY_BUCKET.get()).stacksTo(1)));

    public static final DeferredItem<BlockItem> AMPHORA = ITEMS.registerItem("amphora", (properties) -> descriptionItem(YTechBlocks.AMPHORA, List.of(Component.translatable("text.ytech.hover.amphora1").withStyle(DARK_GRAY), Component.translatable("text.ytech.hover.amphora2").withStyle(DARK_GRAY)), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> AQUEDUCT_FERTILIZER = ITEMS.registerItem("aqueduct_fertilizer", YTechItems::aqueductFertilizerBlockItem);
    public static final DeferredItem<BlockItem> AQUEDUCT_HYDRATOR = ITEMS.registerItem("aqueduct_hydrator", YTechItems::aqueductHydratorBlockItem);
    public static final DeferredItem<BlockItem> AQUEDUCT_VALVE = ITEMS.registerItem("aqueduct_valve", YTechItems::aqueductValveBlockItem);
    public static final DeferredItem<BlockItem> BRICK_CHIMNEY = ITEMS.registerItem("brick_chimney", (properties) -> descriptionItem(YTechBlocks.BRICK_CHIMNEY, List.of(Component.translatable("text.ytech.hover.chimney", AbstractPrimitiveMachineBlockEntity.TEMP_PER_CHIMNEY).withStyle(DARK_GRAY)), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> BRONZE_ANVIL = ITEMS.registerSimpleBlockItem(YTechBlocks.BRONZE_ANVIL, new Item.Properties().useBlockDescriptionPrefix());
    public static final DeferredItem<BlockItem> CRAFTING_WORKSPACE = ITEMS.registerItem("crafting_workspace", (properties) -> descriptionItem(YTechBlocks.CRAFTING_WORKSPACE, List.of(Component.translatable("text.ytech.hover.crafting_workbench1").withStyle(DARK_GRAY), Component.translatable("text.ytech.hover.crafting_workbench2").withStyle(DARK_GRAY), Component.translatable("text.ytech.hover.crafting_workbench3").withStyle(DARK_GRAY)), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> FIRE_PIT = ITEMS.registerItem("fire_pit", YTechItems::firePitBlockItem);
    public static final DeferredItem<BlockItem> GRASS_BED = ITEMS.registerItem("grass_bed", YTechItems::grassBedBlockItem);
    public static final DeferredItem<BlockItem> MILLSTONE = ITEMS.registerItem("millstone", (properties) -> descriptionItem(YTechBlocks.MILLSTONE, List.of(Component.translatable("text.ytech.hover.millstone").withStyle(DARK_GRAY)), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> POTTERY_WHEEL = ITEMS.registerItem("potters_wheel", (properties) -> descriptionItem(YTechBlocks.POTTERS_WHEEL, List.of(Component.translatable("text.ytech.hover.potters_wheel1").withStyle(DARK_GRAY), Component.translatable("text.ytech.hover.potters_wheel2").withStyle(DARK_GRAY)), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> PRIMITIVE_ALLOY_SMELTER = ITEMS.registerItem("primitive_alloy_smelter", (properties) -> descriptionItem(YTechBlocks.PRIMITIVE_ALLOY_SMELTER, List.of(Component.translatable("text.ytech.hover.primitive_smelter").withStyle(DARK_GRAY)), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> PRIMITIVE_SMELTER = ITEMS.registerItem("primitive_smelter", (properties) -> descriptionItem(YTechBlocks.PRIMITIVE_SMELTER, List.of(Component.translatable("text.ytech.hover.primitive_smelter").withStyle(DARK_GRAY)), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> REINFORCED_BRICKS = ITEMS.registerSimpleBlockItem(YTechBlocks.REINFORCED_BRICKS, new Item.Properties().useBlockDescriptionPrefix());
    public static final DeferredItem<BlockItem> REINFORCED_BRICK_CHIMNEY = ITEMS.registerItem("reinforced_brick_chimney", (properties) -> descriptionItem(YTechBlocks.REINFORCED_BRICK_CHIMNEY, List.of(Component.translatable("text.ytech.hover.chimney", AbstractPrimitiveMachineBlockEntity.TEMP_PER_CHIMNEY).withStyle(DARK_GRAY)), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> TERRACOTTA_BRICKS = ITEMS.registerSimpleBlockItem(YTechBlocks.TERRACOTTA_BRICKS, new Item.Properties().useBlockDescriptionPrefix());
    public static final DeferredItem<BlockItem> TERRACOTTA_BRICK_SLAB = ITEMS.registerSimpleBlockItem(YTechBlocks.TERRACOTTA_BRICK_SLAB, new Item.Properties().useBlockDescriptionPrefix());
    public static final DeferredItem<BlockItem> TERRACOTTA_BRICK_STAIRS = ITEMS.registerSimpleBlockItem(YTechBlocks.TERRACOTTA_BRICK_STAIRS, new Item.Properties().useBlockDescriptionPrefix());
    public static final DeferredItem<BlockItem> THATCH = ITEMS.registerSimpleBlockItem(YTechBlocks.THATCH, new Item.Properties().useBlockDescriptionPrefix());
    public static final DeferredItem<BlockItem> THATCH_SLAB = ITEMS.registerSimpleBlockItem(YTechBlocks.THATCH_SLAB, new Item.Properties().useBlockDescriptionPrefix());
    public static final DeferredItem<BlockItem> THATCH_STAIRS = ITEMS.registerSimpleBlockItem(YTechBlocks.THATCH_STAIRS, new Item.Properties().useBlockDescriptionPrefix());
    public static final DeferredItem<BlockItem> TOOL_RACK = ITEMS.registerSimpleBlockItem(YTechBlocks.TOOL_RACK, new Item.Properties().useBlockDescriptionPrefix());
    public static final DeferredItem<BlockItem> TREE_STUMP = ITEMS.registerSimpleBlockItem(YTechBlocks.TREE_STUMP, new Item.Properties().useBlockDescriptionPrefix());
    public static final DeferredItem<BlockItem> WELL_PULLEY = ITEMS.registerItem("well_pulley", (properties) -> descriptionItem(YTechBlocks.WELL_PULLEY, List.of(Component.translatable("text.ytech.hover.well_pulley1").withStyle(DARK_GRAY), Component.translatable("text.ytech.hover.well_pulley2").withStyle(DARK_GRAY), Component.translatable("text.ytech.hover.well_pulley3").withStyle(DARK_GRAY)), properties.useBlockDescriptionPrefix()));
    public static final DeferredItem<BlockItem> WOODEN_BOX = ITEMS.registerSimpleBlockItem(YTechBlocks.WOODEN_BOX, new Item.Properties().useBlockDescriptionPrefix());

    public static final DeferredItem<Item> CHLORITE_BRACELET = ITEMS.registerItem("chlorite_bracelet", ChloriteBraceletItem::new);
    public static final DeferredItem<Item> LION_MAN = ITEMS.registerItem("lion_man", LionManItem::new);
    public static final DeferredItem<Item> SHELL_BEADS = ITEMS.registerItem("shell_beads", ShellBeadsItem::new);
    public static final DeferredItem<Item> VENUS_OF_HOHLE_FELS = ITEMS.registerItem("venus_of_hohle_fels", VenusOfHohleFelsItem::new);
    public static final DeferredItem<Item> WILD_HORSE = ITEMS.registerItem("wild_horse", WildHorseItem::new);

    public static final DeferredItem<Item> AUROCHS_SPAWN_EGG = ITEMS.registerItem("aurochs_spawn_egg", (properties) -> new SpawnEggItem(YTechEntityTypes.AUROCHS.get(), 0x4688f5, 0xE03C83, properties));
    public static final DeferredItem<Item> DEER_SPAWN_EGG = ITEMS.registerItem("deer_spawn_egg", (properties) -> new SpawnEggItem(YTechEntityTypes.DEER.get(), 0x664825, 0xE09C53, properties));
    public static final DeferredItem<Item> FOWL_SPAWN_EGG = ITEMS.registerItem("fowl_spawn_egg", (properties) -> new SpawnEggItem(YTechEntityTypes.FOWL.get(), 0xc6484b, 0xedde9d, properties));
    public static final DeferredItem<Item> MOUFLON_SPAWN_EGG = ITEMS.registerItem("mouflon_spawn_egg", (properties) -> new SpawnEggItem(YTechEntityTypes.MOUFLON.get(), 0x064aeb, 0xa13bb6, properties));
    public static final DeferredItem<Item> SABER_TOOTH_TIGER_SPAWN_EGG = ITEMS.registerItem("saber_tooth_tiger_spawn_egg", (properties) -> new SpawnEggItem(YTechEntityTypes.SABER_TOOTH_TIGER.get(), 0xa52a80, 0x9194c2, properties));
    public static final DeferredItem<Item> TERROR_BIRD_SPAWN_EGG = ITEMS.registerItem("terror_bird_spawn_egg", (properties) -> new SpawnEggItem(YTechEntityTypes.TERROR_BIRD.get(), 0x759ac0, 0xc19452, properties));
    public static final DeferredItem<Item> WILD_BOAR_SPAWN_EGG = ITEMS.registerItem("wild_boar_spawn_egg", (properties) -> new SpawnEggItem(YTechEntityTypes.WILD_BOAR.get(), 0xf4f7d7, 0x560a59, properties));
    public static final DeferredItem<Item> WOOLLY_MAMMOTH_SPAWN_EGG = ITEMS.registerItem("woolly_mammoth_spawn_egg", (properties) -> new SpawnEggItem(YTechEntityTypes.WOOLLY_MAMMOTH.get(), 0x8a4a71, 0x6cc8ab, properties));
    public static final DeferredItem<Item> WOOLLY_RHINO_SPAWN_EGG = ITEMS.registerItem("woolly_rhino_spawn_egg", (properties) -> new SpawnEggItem(YTechEntityTypes.WOOLLY_RHINO.get(), 0x04b53a, 0x2f7415, properties));

    public static final TypedItem<PartType> CLAY_MOLDS = new PartItem("clay_mold", NameHolder.suffix("clay_mold"), (properties) -> new Item(properties.durability(16)));
    public static final TypedItem<PartType> PATTERNS = new PartItem("pattern", NameHolder.suffix("pattern"), YTechItems::simpleItem);
    public static final TypedItem<PartType> SAND_MOLDS = new PartItem("sand_mold", NameHolder.suffix("sand_mold"), YTechItems::simpleItem);
    public static final TypedItem<PartType> UNFIRED_MOLDS = new PartItem("unfired_mold", NameHolder.both("unfired", "mold"), YTechItems::simpleItem);

    public static final MultiTypedItem<MaterialType, PartType> PARTS = new MaterialPartItem("part", Utils.exclude(MaterialType.ALL_METALS, MaterialType.IRON), Utils.exclude(PartType.ALL_PARTS, PartType.INGOT), NameHolder.suffix("part"), YTechItems::simpleItem);

    public static final TypedItem<MaterialType> ARROWS = new MaterialItem("arrow", NameHolder.suffix("arrow"), MaterialType.ALL_HARD_METALS, MaterialArrowItem::new);
    public static final TypedItem<MaterialType> AXES = new AxeMaterialItem();
    public static final TypedItem<MaterialType> BOLTS = new MaterialItem("bolt", NameHolder.suffix("bolt"), Utils.merge(MaterialType.ALL_METALS, MaterialType.WOODEN), (type, properties) -> burnableSimpleItem(type, 100, properties));
    public static final TypedItem<MaterialType> BOOTS = new BootsMaterialItem();
    public static final TypedItem<MaterialType> CHESTPLATES = new ChestplatesMaterialItem();
    public static final TypedItem<MaterialType> CRUSHED_MATERIALS = new MaterialItem("crushed_material", NameHolder.prefix("crushed"), MaterialType.ALL_ORES, (type, properties) -> simpleItem(properties));
    public static final TypedItem<MaterialType> FILES = new MaterialItem("file", NameHolder.suffix("file"), MaterialType.ALL_METALS, ToolItem::tool);
    public static final TypedItem<MaterialType> HAMMERS = new MaterialItem("hammer", NameHolder.suffix("hammer"), Utils.merge(MaterialType.ALL_METALS, MaterialType.STONE), YTechItems::hammerItem);
    public static final TypedItem<MaterialType> HELMETS = new HelmetMaterialItem();
    public static final TypedItem<MaterialType> HOES = new HoeMaterialItem();
    public static final TypedItem<MaterialType> INGOTS = new IngotMaterialItem();
    public static final TypedItem<MaterialType> KNIVES = new MaterialItem("knife", NameHolder.suffix("knife"), Utils.merge(MaterialType.ALL_METALS, MaterialType.FLINT), ToolItem::knife);
    public static final TypedItem<MaterialType> LEGGINGS = new LeggingsMaterialItem();
    public static final TypedItem<MaterialType> MORTAR_AND_PESTLES = new MaterialItem("mortar_and_pestle", NameHolder.suffix("mortar_and_pestle"), Utils.merge(MaterialType.ALL_METALS, MaterialType.STONE), ToolItem::tool);
    public static final TypedItem<MaterialType> PICKAXES = new PickaxeMaterialItem();
    public static final TypedItem<MaterialType> PLATES = new MaterialItem("plate", NameHolder.suffix("plate"), Utils.merge(MaterialType.ALL_METALS, MaterialType.WOODEN), (type, properties) -> burnableSimpleItem(type, 200, properties));
    public static final TypedItem<MaterialType> RAW_MATERIALS = new RawMaterialItem();
    public static final TypedItem<MaterialType> RODS = new MaterialItem("rod", NameHolder.suffix("rod"), MaterialType.ALL_METALS, (type, properties) -> simpleItem(properties));
    public static final TypedItem<MaterialType> SAWS = new MaterialItem("saw", NameHolder.suffix("saw"), MaterialType.ALL_METALS, YTechItems::sawItem);
    public static final TypedItem<MaterialType> SAW_BLADES = new MaterialItem("saw_blade", NameHolder.suffix("saw_blade"), EnumSet.of(MaterialType.IRON), (type, properties) -> simpleItem(properties));
    public static final TypedItem<MaterialType> SHEARS = new ShearsMaterialItem();
    public static final TypedItem<MaterialType> SHOVELS = new ShovelMaterialItem();
    public static final TypedItem<MaterialType> SPEARS = new MaterialItem("spear", NameHolder.suffix("spear"), Utils.merge(MaterialType.ALL_HARD_METALS, MaterialType.FLINT), (type, properties) -> new SpearItem(SpearType.BY_MATERIAL_TYPE.get(type), properties));
    public static final TypedItem<MaterialType> SWORDS = new SwordMaterialItem();

    public static final TypedItem<MaterialType> AQUEDUCTS = new MaterialItem(YTechBlocks.AQUEDUCTS, YTechItems::aqueductBlockItem);
    public static final TypedItem<MaterialType> DEEPSLATE_ORES = new DeepslateOreMaterialItem();
    public static final TypedItem<MaterialType> DRYING_RACKS = new MaterialItem(YTechBlocks.DRYING_RACKS, YTechItems::dryingRackBlockItem);
    public static final TypedItem<MaterialType> GRAVEL_DEPOSITS = new MaterialItem(YTechBlocks.GRAVEL_DEPOSITS, YTechItems::blockItem);
    public static final TypedItem<MaterialType> NETHER_ORES = new NetherOreMaterialItem();
    public static final TypedItem<MaterialType> RAW_STORAGE_BLOCKS = new RawStorageBlockMaterialItem();
    public static final TypedItem<MaterialType> SAND_DEPOSITS = new MaterialItem(YTechBlocks.SAND_DEPOSITS, YTechItems::blockItem);
    public static final TypedItem<MaterialType> STONE_ORES = new StoneOreMaterialItem();
    public static final TypedItem<MaterialType> STORAGE_BLOCKS = new StorageBlockMaterialItem();
    public static final TypedItem<MaterialType> TANNING_RACKS = new MaterialItem(YTechBlocks.TANNING_RACKS, (block, properties) -> burnableBlockItem(block, 300, properties.useBlockDescriptionPrefix()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static Collection<DeferredHolder<Item, ? extends Item>> getRegisteredItems() {
        return ITEMS.getEntries();
    }

    private static BlockItem blockItem(DeferredBlock<Block> block, Item.Properties properties) {
        return new BlockItem(block.get(), properties.useBlockDescriptionPrefix());
    }

    private static BlockItem burnableBlockItem(DeferredBlock<Block> block, int burnTime, Item.Properties properties) {
        return new BlockItem(block.get(), properties) {
            @Override
            public int getBurnTime(@NotNull ItemStack itemStack, @Nullable RecipeType<?> recipeType, @NotNull FuelValues fuelValues) {
                return burnTime;
            }
        };
    }

    private static Item simpleItem(Item.Properties properties) {
        return new Item(properties);
    }

    private static Item burnableSimpleItem(@NotNull MaterialType material, int burnTime, Item.Properties properties) {
        return new Item(properties) {
            @Override
            public int getBurnTime(@NotNull ItemStack itemStack, @Nullable RecipeType<?> recipeType, @NotNull FuelValues fuelValues) {
                return material == MaterialType.WOODEN ? burnTime : 0;
            }
        };
    }

    private static Item axeItem(MaterialType material, Item.Properties properties) {
        return new AxeItem(YTechToolMaterials.get(material), 6.0f, -3.2f, properties);
    }

    private static Item sawItem(MaterialType material, Item.Properties properties) {
        return new AxeItem(YTechToolMaterials.get(material), 1.0f, -3.0f, properties);
    }

    private static Item pickaxeItem(MaterialType material, Item.Properties properties) {
        return new PickaxeItem(YTechToolMaterials.get(material), 1, -2.8f, properties);
    }

    private static Item hammerItem(MaterialType material, Item.Properties properties) {
        return new PickaxeItem(YTechToolMaterials.get(material), 3.0F, -3.2f, properties);
    }

    private static Item shearsItem(MaterialType materialType, Item.Properties properties) {
        return new ShearsItem(properties.durability(YTechToolMaterials.get(materialType).durability()));
    }

    private static Item shovelItem(MaterialType material, Item.Properties properties) {
        return new ShovelItem(YTechToolMaterials.get(material), 1.5f, -3.0f, properties);
    }

    private static Item helmetItem(MaterialType material, Item.Properties properties) {
        return new ArmorItem(YTechArmorMaterials.get(material), ArmorType.HELMET, properties);
    }

    private static Item chestplateItem(MaterialType material, Item.Properties properties) {
        return new ArmorItem(YTechArmorMaterials.get(material), ArmorType.CHESTPLATE, properties);
    }

    private static Item leggingsItem(MaterialType material, Item.Properties properties) {
        return new ArmorItem(YTechArmorMaterials.get(material), ArmorType.LEGGINGS, properties);
    }

    private static Item bootsItem(MaterialType material, Item.Properties properties) {
        return new ArmorItem(YTechArmorMaterials.get(material), ArmorType.BOOTS, properties);
    }

    private static Item hoeItem(MaterialType material, Item.Properties properties) {
        return new HoeItem(YTechToolMaterials.get(material), 0, -3.0f, properties);
    }

    private static Item swordItem(MaterialType material, Item.Properties properties) {
        return new SwordItem(YTechToolMaterials.get(material), 3, -2.4f, properties);
    }

    private static Item foodItem(int nutrition, float saturation, Item.Properties properties) {
        return new Item(properties.food(new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).build()));
    }

    private static Item descriptionItem(@NotNull List<Component> description, Item.Properties properties) {
        return new Item(properties) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
                tooltipComponents.addAll(description);
            }
        };
    }

    private static BlockItem descriptionItem(DeferredBlock<Block> block, @NotNull List<Component> description, Item.Properties properties) {
        return new BlockItem(block.get(), properties) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
                tooltipComponents.addAll(description);
            }
        };
    }

    public static class TypedItem<E extends Enum<E>> extends AbstractMap<E, DeferredItem<Item>> {
        protected final String group;
        protected final Map<E, DeferredItem<Item>> items = new HashMap<>();

        TypedItem(String group) {
            this.group = group;
        }

        public String getGroup() {
            return group;
        }

        @NotNull
        @Override
        public Set<Entry<E, DeferredItem<Item>>> entrySet() {
            return items.entrySet();
        }
    }

    public static class MultiTypedItem<E extends Enum<E>, F extends Enum<F>> extends AbstractMap<E, Map<F, DeferredItem<Item>>> {
        protected final String group;
        protected final Map<E, Map<F, DeferredItem<Item>>> items = new HashMap<>();

        MultiTypedItem(String group) {
            this.group = group;
        }

        public DeferredItem<Item> get(E type1, F type2) {
            return items.get(type1).get(type2);
        }

        public String getGroup() {
            return group;
        }

        @Override
        public @NotNull Set<Entry<E, Map<F, DeferredItem<Item>>>> entrySet() {
            return items.entrySet();
        }
    }

    public static class PartItem extends TypedItem<PartType> {
        PartItem(String group, NameHolder nameHolder, Function<Item.Properties, Item> itemSupplier) {
            super(group);
            for (PartType partType : PartType.values()) {
                String key = nameHolder.prefix() != null ? nameHolder.prefix() + "_" : "";
                key += partType.key;
                key += nameHolder.suffix() != null ? "_" + nameHolder.suffix() : "";
                items.put(partType, ITEMS.registerItem(key, itemSupplier));
            }
        }
    }

    public static class MaterialPartItem extends MultiTypedItem<MaterialType, PartType> {
        MaterialPartItem(String group, EnumSet<MaterialType> materials, EnumSet<PartType> parts, NameHolder nameHolder, Function<Item.Properties, Item> itemSupplier) {
            super(group);
            for (MaterialType material : materials) {
                for (PartType part : parts) {
                    String key = nameHolder.prefix() != null ? nameHolder.prefix() + "_" : "";
                    key += material.key + "_" + part.key;
                    key += nameHolder.suffix() != null ? "_" + nameHolder.suffix() : "";
                    items.computeIfAbsent(material, (k) -> new HashMap<>()).put(part, ITEMS.registerItem(key, itemSupplier));
                }
            }
        }
    }

    public static class MaterialItem extends TypedItem<MaterialType> {
        public MaterialItem(String group, NameHolder nameHolder, EnumSet<MaterialType> materialTypes, BiFunction<MaterialType, Item.Properties, Item> itemSupplier) {
            super(group);
            materialTypes.forEach((type) -> {
                String key = nameHolder.prefix() != null ? nameHolder.prefix() + "_" : "";

                if (type.key.equals("gold") && nameHolder.prefix() == null) {
                    key += "golden";
                } else {
                    key += type.key;
                }

                key += nameHolder.suffix() != null ? "_" + nameHolder.suffix() : "";
                items.put(type, ITEMS.registerItem(key, (properties) -> itemSupplier.apply(type, properties)));
            });
        }

        public MaterialItem(YTechBlocks.MaterialBlock block, BiFunction<DeferredBlock<Block>, Item.Properties, Item> itemSupplier) {
            this(block, EnumSet.noneOf(MaterialType.class), itemSupplier);
        }

        public MaterialItem(YTechBlocks.MaterialBlock block, EnumSet<MaterialType> exclude, BiFunction<DeferredBlock<Block>, Item.Properties, Item> itemSupplier) {
            super(block.getGroup());
            block.entries().stream().filter((entry) -> !exclude.contains(entry.getKey())).forEach((entry) -> {
                MaterialType type = entry.getKey();
                DeferredBlock<Block> object = entry.getValue();
                items.put(type, ITEMS.registerItem(Utils.getPath(object), (properties) -> itemSupplier.apply(object, properties)));
            });
        }
    }

    private static class AxeMaterialItem extends MaterialItem {
        public AxeMaterialItem() {
            super("axe", NameHolder.suffix("axe"), Utils.exclude(Utils.merge(MaterialType.ALL_METALS, MaterialType.FLINT), MaterialType.GOLD, MaterialType.IRON), YTechItems::axeItem);
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.GOLDEN_AXE)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.IRON_AXE)));
        }
    }

    private static class BootsMaterialItem extends MaterialItem {
        public BootsMaterialItem() {
            super("boots", NameHolder.suffix("boots"), Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::bootsItem);
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.GOLDEN_BOOTS)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.IRON_BOOTS)));
            items.put(MaterialType.LEATHER, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.LEATHER_BOOTS)));
        }
    }

    private static class ChestplatesMaterialItem extends MaterialItem {
        public ChestplatesMaterialItem() {
            super("chestplate", NameHolder.suffix("chestplate"), Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::chestplateItem);
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.GOLDEN_CHESTPLATE)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.IRON_CHESTPLATE)));
            items.put(MaterialType.LEATHER, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.LEATHER_CHESTPLATE)));
        }
    }

    private static class HelmetMaterialItem extends MaterialItem {
        public HelmetMaterialItem() {
            super("helmet", NameHolder.suffix("helmet"), Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::helmetItem);
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.GOLDEN_HELMET)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.IRON_HELMET)));
            items.put(MaterialType.LEATHER, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.LEATHER_HELMET)));
        }
    }

    private static class HoeMaterialItem extends MaterialItem {
        public HoeMaterialItem() {
            super("hoe", NameHolder.suffix("hoe"), Utils.exclude(MaterialType.ALL_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::hoeItem);
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.GOLDEN_HOE)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.IRON_HOE)));
        }
    }

    private static class IngotMaterialItem extends MaterialItem {
        public IngotMaterialItem() {
            super("ingot", NameHolder.suffix("ingot"), Utils.exclude(MaterialType.ALL_METALS, MaterialType.VANILLA_METALS), (type, properties) -> simpleItem(properties));
            items.put(MaterialType.COPPER, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.COPPER_INGOT)));
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.GOLD_INGOT)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.IRON_INGOT)));
        }
    }

    private static class LeggingsMaterialItem extends MaterialItem {
        public LeggingsMaterialItem() {
            super("leggings", NameHolder.suffix("leggings"), Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::leggingsItem);
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.GOLDEN_LEGGINGS)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.IRON_LEGGINGS)));
            items.put(MaterialType.LEATHER, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.LEATHER_LEGGINGS)));
        }
    }

    private static class PickaxeMaterialItem extends MaterialItem {
        public PickaxeMaterialItem() {
            super("pickaxe", NameHolder.suffix("pickaxe"), Utils.exclude(Utils.merge(MaterialType.ALL_METALS, MaterialType.ANTLER), MaterialType.GOLD, MaterialType.IRON), YTechItems::pickaxeItem);
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.GOLDEN_PICKAXE)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.IRON_PICKAXE)));
        }
    }

    private static class RawMaterialItem extends MaterialItem {
        public RawMaterialItem() {
            super("raw_material", NameHolder.prefix("raw"), Utils.exclude(MaterialType.ALL_ORES, MaterialType.VANILLA_METALS), (type, properties) -> simpleItem(properties));
            items.put(MaterialType.COPPER, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.RAW_COPPER)));
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.RAW_GOLD)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.RAW_IRON)));
        }
    }

    private static class ShearsMaterialItem extends MaterialItem {
        public ShearsMaterialItem() {
            super("shears", NameHolder.suffix("shears"), Utils.exclude(MaterialType.ALL_METALS, MaterialType.IRON), YTechItems::shearsItem);
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.SHEARS)));
        }
    }

    private static class ShovelMaterialItem extends MaterialItem {
        public ShovelMaterialItem() {
            super("shovel", NameHolder.suffix("shovel"), Utils.exclude(MaterialType.ALL_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::shovelItem);
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.GOLDEN_SHOVEL)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.IRON_SHOVEL)));
            items.put(MaterialType.WOODEN, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.WOODEN_SHOVEL)));
        }
    }

    private static class SwordMaterialItem extends MaterialItem {
        public SwordMaterialItem() {
            super("sword", NameHolder.suffix("sword"), Utils.exclude(MaterialType.ALL_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::swordItem);
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.GOLDEN_SWORD)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.IRON_SWORD)));
        }
    }

    private static class DeepslateOreMaterialItem extends MaterialItem {
        public DeepslateOreMaterialItem() {
            super(YTechBlocks.DEEPSLATE_ORES, MaterialType.VANILLA_METALS, YTechItems::blockItem);
            items.put(MaterialType.COPPER, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.DEEPSLATE_COPPER_ORE)));
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.DEEPSLATE_GOLD_ORE)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.DEEPSLATE_IRON_ORE)));
        }
    }

    private static class NetherOreMaterialItem extends MaterialItem {
        public NetherOreMaterialItem() {
            super(YTechBlocks.NETHER_ORES, EnumSet.of(MaterialType.GOLD), YTechItems::blockItem);
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.NETHER_GOLD_ORE)));
        }
    }

    private static class RawStorageBlockMaterialItem extends MaterialItem {
        public RawStorageBlockMaterialItem() {
            super(YTechBlocks.RAW_STORAGE_BLOCKS, MaterialType.VANILLA_METALS, YTechItems::blockItem);
            items.put(MaterialType.COPPER, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.RAW_COPPER_BLOCK)));
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.RAW_GOLD_BLOCK)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.RAW_IRON_BLOCK)));
        }
    }

    private static class StoneOreMaterialItem extends MaterialItem {
        public StoneOreMaterialItem() {
            super(YTechBlocks.STONE_ORES, MaterialType.VANILLA_METALS, YTechItems::blockItem);
            items.put(MaterialType.COPPER, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.COPPER_ORE)));
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.GOLD_ORE)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.IRON_ORE)));
        }
    }

    private static class StorageBlockMaterialItem extends MaterialItem {
        public StorageBlockMaterialItem() {
            super(YTechBlocks.STORAGE_BLOCKS, MaterialType.VANILLA_METALS, YTechItems::blockItem);
            items.put(MaterialType.COPPER, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.COPPER_BLOCK)));
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.GOLD_BLOCK)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.IRON_BLOCK)));
        }
    }

    private static BlockItem aqueductBlockItem(DeferredBlock<Block> block, Item.Properties properties) {
        return new BlockItem(block.get(), properties.useBlockDescriptionPrefix()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct1", ((AqueductBlock)block.get()).getCapacity()).withStyle(DARK_GRAY));

                if (CONFIGURATION.shouldRainingFillAqueduct()) {
                    tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct2",
                            CONFIGURATION.getRainingFillAmount(), CONFIGURATION.getRainingFillPerNthTick()).withStyle(DARK_GRAY));
                }
            }
        };
    }

    private static BlockItem aqueductFertilizerBlockItem(Item.Properties properties) {
        return new BlockItem(YTechBlocks.AQUEDUCT_FERTILIZER.get(), properties.useBlockDescriptionPrefix()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_fertilizer1").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_fertilizer2",
                        CONFIGURATION.getHydratorDrainAmount(), CONFIGURATION.getHydratorDrainPerNthTick()).withStyle(DARK_GRAY));
            }
        };
    }

    private static BlockItem aqueductHydratorBlockItem(Item.Properties properties) {
        return new BlockItem(YTechBlocks.AQUEDUCT_HYDRATOR.get(), properties.useBlockDescriptionPrefix()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_hydrator1").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_hydrator2",
                        CONFIGURATION.getHydratorDrainAmount(), CONFIGURATION.getHydratorDrainPerNthTick()).withStyle(DARK_GRAY));
            }
        };
    }

    private static BlockItem aqueductValveBlockItem(Item.Properties properties) {
        return new BlockItem(YTechBlocks.AQUEDUCT_VALVE.get(), properties.useBlockDescriptionPrefix()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_valve1").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_valve2",
                        CONFIGURATION.getValveFillAmount(), CONFIGURATION.getValveFillPerNthTick()).withStyle(DARK_GRAY));
            }
        };
    }

    private static BlockItem firePitBlockItem(Item.Properties properties) {
        return new BlockItem(YTechBlocks.FIRE_PIT.get(), properties.useBlockDescriptionPrefix()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.fire_pit1").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.fire_pit2").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.fire_pit3").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.fire_pit4").withStyle(DARK_GRAY));
            }
        };
    }

    private static BlockItem grassBedBlockItem(Item.Properties properties) {
        return new BlockItem(YTechBlocks.GRASS_BED.get(), properties.useBlockDescriptionPrefix()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.grass_bed").withStyle(DARK_GRAY));
            }
        };
    }

    private static BlockItem dryingRackBlockItem(DeferredBlock<Block> block, Item.Properties properties) {
        return new BlockItem(block.get(), properties.useBlockDescriptionPrefix()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.drying_rack1").withStyle(DARK_GRAY));

                if (CONFIGURATION.noDryingDuringRain()) {
                    tooltipComponents.add(Component.translatable("text.ytech.hover.drying_rack2").withStyle(DARK_GRAY));
                }
            }
        };
    }
}
