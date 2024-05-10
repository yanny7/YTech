package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.NameHolder;
import com.yanny.ytech.configuration.SpearType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.AbstractPrimitiveMachineBlockEntity;
import com.yanny.ytech.configuration.item.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

import static com.yanny.ytech.YTechMod.CONFIGURATION;
import static net.minecraft.ChatFormatting.DARK_GRAY;

public class YTechItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(YTechMod.MOD_ID);

    public static final DeferredItem<Item> ANTLER = ITEMS.registerSimpleItem("antler");
    public static final DeferredItem<Item> BASKET = ITEMS.register("basket", BasketItem::new);
    public static final DeferredItem<Item> BREAD_DOUGH = ITEMS.registerSimpleItem("bread_dough");
    public static final DeferredItem<Item> BRICK_MOLD = ITEMS.registerSimpleItem("brick_mold", new Item.Properties().durability(256));
    public static final DeferredItem<Item> CLAY_BUCKET = ITEMS.register("clay_bucket", () -> new ClayBucketItem(Fluids.EMPTY, new Item.Properties().stacksTo(8)));
    public static final DeferredItem<Item> COOKED_VENISON = ITEMS.register("cooked_venison", () -> foodItem(7, 0.8f));
    public static final DeferredItem<Item> DRIED_BEEF = ITEMS.register("dried_beef", () -> foodItem(6, 0.7f));
    public static final DeferredItem<Item> DRIED_CHICKEN = ITEMS.register("dried_chicken", () -> foodItem(4, 0.5f));
    public static final DeferredItem<Item> DRIED_COD = ITEMS.register("dried_cod", () -> foodItem(4, 0.5f));
    public static final DeferredItem<Item> DRIED_MUTTON = ITEMS.register("dried_mutton", () -> foodItem(4, 0.5f));
    public static final DeferredItem<Item> DRIED_PORKCHOP = ITEMS.register("dried_porkchop", () -> foodItem(6, 0.7f));
    public static final DeferredItem<Item> DRIED_RABBIT = ITEMS.register("dried_rabbit", () -> foodItem(4, 0.5f));
    public static final DeferredItem<Item> DRIED_SALMON = ITEMS.register("dried_salmon", () -> foodItem(4, 0.5f));
    public static final DeferredItem<Item> DRIED_VENISON = ITEMS.register("dried_venison", () -> foodItem(5, 0.7f));
    public static final DeferredItem<Item> FLOUR = ITEMS.registerSimpleItem("flour");
    public static final DeferredItem<Item> GRASS_FIBERS = ITEMS.register("grass_fibers", () -> descriptionItem(List.of(Component.translatable("text.ytech.hover.grass_fibers").withStyle(ChatFormatting.DARK_GRAY))));
    public static final DeferredItem<Item> GRASS_TWINE = ITEMS.registerSimpleItem("grass_twine");
    public static final DeferredItem<Item> IRON_BLOOM = ITEMS.registerSimpleItem("iron_bloom");
    public static final DeferredItem<Item> LAVA_CLAY_BUCKET = ITEMS.register("lava_clay_bucket", () -> new ClayBucketItem(Fluids.LAVA, new Item.Properties().craftRemainder(YTechItems.CLAY_BUCKET.get()).stacksTo(1)));
    public static final DeferredItem<Item> LEATHER_STRIPS = ITEMS.registerSimpleItem("leather_strips");
    public static final DeferredItem<Item> MAMMOTH_TUSK = ITEMS.register("mammoth_tusk", YTechItems::simpleItem);
    public static final DeferredItem<Item> PEBBLE = ITEMS.register("pebble", PebbleItem::new);
    public static final DeferredItem<Item> RAW_HIDE = ITEMS.registerSimpleItem("raw_hide");
    public static final DeferredItem<Item> RHINO_HORN = ITEMS.register("rhino_horn", YTechItems::simpleItem);
    public static final DeferredItem<Item> SHARP_FLINT = ITEMS.register("sharp_flint", () -> toolItem(Tiers.WOOD));
    public static final DeferredItem<Item> UNFIRED_BRICK = ITEMS.registerSimpleItem("unfired_brick");
    public static final DeferredItem<Item> UNFIRED_CLAY_BUCKET = ITEMS.registerSimpleItem("unfired_clay_bucket");
    public static final DeferredItem<Item> VENISON = ITEMS.register("venison", () -> foodItem(2, 0.3f));
    public static final DeferredItem<Item> WATER_CLAY_BUCKET = ITEMS.register("water_clay_bucket", () -> new ClayBucketItem(Fluids.WATER, new Item.Properties().craftRemainder(YTechItems.CLAY_BUCKET.get()).stacksTo(1)));

    public static final DeferredItem<BlockItem> AQUEDUCT = ITEMS.register("aqueduct", YTechItems::aqueductBlockItem);
    public static final DeferredItem<BlockItem> AQUEDUCT_FERTILIZER = ITEMS.register("aqueduct_fertilizer", YTechItems::aqueductFertilizerBlockItem);
    public static final DeferredItem<BlockItem> AQUEDUCT_HYDRATOR = ITEMS.register("aqueduct_hydrator", YTechItems::aqueductHydratorBlockItem);
    public static final DeferredItem<BlockItem> AQUEDUCT_VALVE = ITEMS.register("aqueduct_valve", YTechItems::aqueductValveBlockItem);
    public static final DeferredItem<BlockItem> BRICK_CHIMNEY = ITEMS.register("brick_chimney", () -> descriptionItem(YTechBlocks.BRICK_CHIMNEY, List.of(Component.translatable("text.ytech.hover.chimney", AbstractPrimitiveMachineBlockEntity.TEMP_PER_CHIMNEY).withStyle(DARK_GRAY))));
    public static final DeferredItem<BlockItem> BRONZE_ANVIL = ITEMS.registerSimpleBlockItem(YTechBlocks.BRONZE_ANVIL);
    public static final DeferredItem<BlockItem> FIRE_PIT = ITEMS.register("fire_pit", YTechItems::firePitBlockItem);
    public static final DeferredItem<BlockItem> GRASS_BED = ITEMS.register("grass_bed", YTechItems::grassBedBlockItem);
    public static final DeferredItem<BlockItem> MILLSTONE = ITEMS.register("millstone", () -> descriptionItem(YTechBlocks.MILLSTONE, List.of(Component.translatable("text.ytech.hover.millstone").withStyle(DARK_GRAY))));
    public static final DeferredItem<BlockItem> PRIMITIVE_ALLOY_SMELTER = ITEMS.register("primitive_alloy_smelter", () -> descriptionItem(YTechBlocks.PRIMITIVE_ALLOY_SMELTER, List.of(Component.translatable("text.ytech.hover.primitive_smelter").withStyle(DARK_GRAY))));
    public static final DeferredItem<BlockItem> PRIMITIVE_SMELTER = ITEMS.register("primitive_smelter", () -> descriptionItem(YTechBlocks.PRIMITIVE_SMELTER, List.of(Component.translatable("text.ytech.hover.primitive_smelter").withStyle(DARK_GRAY))));
    public static final DeferredItem<BlockItem> REINFORCED_BRICKS = ITEMS.registerSimpleBlockItem(YTechBlocks.REINFORCED_BRICKS);
    public static final DeferredItem<BlockItem> REINFORCED_BRICK_CHIMNEY = ITEMS.register("reinforced_brick_chimney", () -> descriptionItem(YTechBlocks.REINFORCED_BRICK_CHIMNEY, List.of(Component.translatable("text.ytech.hover.chimney", AbstractPrimitiveMachineBlockEntity.TEMP_PER_CHIMNEY).withStyle(DARK_GRAY))));
    public static final DeferredItem<BlockItem> TERRACOTTA_BRICKS = ITEMS.registerSimpleBlockItem(YTechBlocks.TERRACOTTA_BRICKS);
    public static final DeferredItem<BlockItem> TERRACOTTA_BRICK_SLAB = ITEMS.registerSimpleBlockItem(YTechBlocks.TERRACOTTA_BRICK_SLAB);
    public static final DeferredItem<BlockItem> TERRACOTTA_BRICK_STAIRS = ITEMS.registerSimpleBlockItem(YTechBlocks.TERRACOTTA_BRICK_STAIRS);
    public static final DeferredItem<BlockItem> THATCH = ITEMS.registerSimpleBlockItem(YTechBlocks.THATCH);
    public static final DeferredItem<BlockItem> THATCH_SLAB = ITEMS.registerSimpleBlockItem(YTechBlocks.THATCH_SLAB);
    public static final DeferredItem<BlockItem> THATCH_STAIRS = ITEMS.registerSimpleBlockItem(YTechBlocks.THATCH_STAIRS);

    public static final DeferredItem<Item> AUROCHS_SPAWN_EGG = ITEMS.register("aurochs_spawn_egg", () -> new DeferredSpawnEggItem(YTechEntityTypes.AUROCHS, 0x4688f5, 0xE03C83, new Item.Properties()));
    public static final DeferredItem<Item> DEER_SPAWN_EGG = ITEMS.register("deer_spawn_egg", () -> new DeferredSpawnEggItem(YTechEntityTypes.DEER, 0x664825, 0xE09C53, new Item.Properties()));
    public static final DeferredItem<Item> FOWL_SPAWN_EGG = ITEMS.register("fowl_spawn_egg", () -> new DeferredSpawnEggItem(YTechEntityTypes.FOWL, 0xc6484b, 0xedde9d, new Item.Properties()));
    public static final DeferredItem<Item> MOUFLON_SPAWN_EGG = ITEMS.register("mouflon_spawn_egg", () -> new DeferredSpawnEggItem(YTechEntityTypes.MOUFLON, 0x064aeb, 0xa13bb6, new Item.Properties()));
    public static final DeferredItem<Item> SABER_TOOTH_TIGER_SPAWN_EGG = ITEMS.register("saber_tooth_tiger_spawn_egg", () -> new DeferredSpawnEggItem(YTechEntityTypes.SABER_TOOTH_TIGER, 0xa52a80, 0x9194c2, new Item.Properties()));
    public static final DeferredItem<Item> WILD_BOAR_SPAWN_EGG = ITEMS.register("wild_boar_spawn_egg", () -> new DeferredSpawnEggItem(YTechEntityTypes.WILD_BOAR, 0xf4f7d7, 0x560a59, new Item.Properties()));
    public static final DeferredItem<Item> WOOLLY_MAMMOTH_SPAWN_EGG = ITEMS.register("woolly_mammoth_spawn_egg", () -> new DeferredSpawnEggItem(YTechEntityTypes.WOOLLY_MAMMOTH, 0x8a4a71, 0x6cc8ab, new Item.Properties()));
    public static final DeferredItem<Item> WOOLLY_RHINO_SPAWN_EGG = ITEMS.register("woolly_rhino_spawn_egg", () -> new DeferredSpawnEggItem(YTechEntityTypes.WOOLLY_RHINO, 0x04b53a, 0x2f7415, new Item.Properties()));

    public static final MaterialItem ARROWS = new MaterialItem("arrow", NameHolder.suffix("arrow"), MaterialType.ALL_HARD_METALS, MaterialArrowItem::new);
    public static final MaterialItem AXES = new AxeMaterialItem();
    public static final MaterialItem BOLTS = new MaterialItem("bolt", NameHolder.suffix("bolt"), Utils.merge(MaterialType.ALL_METALS, MaterialType.WOODEN), (type) -> simpleItem());
    public static final MaterialItem BOOTS = new BootsMaterialItem();
    public static final MaterialItem CHESTPLATES = new ChestplatesMaterialItem();
    public static final MaterialItem CRUSHED_MATERIALS = new MaterialItem("crushed_material", NameHolder.prefix("crushed"), MaterialType.ALL_ORES, (type) -> simpleItem());
    public static final MaterialItem FILES = new MaterialItem("file", NameHolder.suffix("file"), MaterialType.ALL_METALS, (type) -> simpleItem());
    public static final MaterialItem HAMMERS = new MaterialItem("hammer", NameHolder.suffix("hammer"), Utils.merge(MaterialType.ALL_METALS, MaterialType.STONE), (type) -> simpleItem());
    public static final MaterialItem HELMETS = new HelmetMaterialItem();
    public static final MaterialItem HOES = new HoeMaterialItem();
    public static final MaterialItem INGOTS = new IngotMaterialItem();
    public static final MaterialItem KNIVES = new MaterialItem("knife", NameHolder.suffix("knife"), EnumSet.of(MaterialType.FLINT), YTechItems::knifeItem);
    public static final MaterialItem LEGGINGS = new LeggingsMaterialItem();
    public static final MaterialItem MORTAR_AND_PESTLES = new MaterialItem("mortar_and_pestle", NameHolder.suffix("mortar_and_pestle"), Utils.merge(MaterialType.ALL_METALS, MaterialType.STONE), (type) -> toolItem(type.getTier()));
    public static final MaterialItem PICKAXES = new PickaxeMaterialItem();
    public static final MaterialItem PLATES = new MaterialItem("plate", NameHolder.suffix("plate"), Utils.merge(MaterialType.ALL_METALS, MaterialType.WOODEN), (type) -> simpleItem());
    public static final MaterialItem RAW_MATERIALS = new RawMaterialItem();
    public static final MaterialItem RODS = new MaterialItem("rod", NameHolder.suffix("rod"), MaterialType.ALL_METALS, (type) -> simpleItem());
    public static final MaterialItem SAWS = new MaterialItem("saw", NameHolder.suffix("saw"), MaterialType.ALL_METALS, (type) -> simpleItem());
    public static final MaterialItem SAW_BLADES = new MaterialItem("saw_blade", NameHolder.suffix("saw_blade"), EnumSet.of(MaterialType.IRON), (type) -> simpleItem());
    public static final MaterialItem SHOVELS = new ShovelMaterialItem();
    public static final MaterialItem SPEARS = new MaterialItem("spear", NameHolder.suffix("spear"), Utils.merge(MaterialType.ALL_HARD_METALS, MaterialType.FLINT), (type) -> new SpearItem(SpearType.BY_MATERIAL_TYPE.get(type)));
    public static final MaterialItem SWORDS = new SwordMaterialItem();

    public static final MaterialItem DEEPSLATE_ORES = new DeepslateOreMaterialItem();
    public static final MaterialItem DRYING_RACKS = new MaterialItem(YTechBlocks.DRYING_RACKS, YTechItems::dryingRackBlockItem);
    public static final MaterialItem GRAVEL_DEPOSITS = new MaterialItem(YTechBlocks.GRAVEL_DEPOSITS, YTechItems::blockItem);
    public static final MaterialItem NETHER_ORES = new NetherOreMaterialItem();
    public static final MaterialItem RAW_STORAGE_BLOCKS = new RawStorageBlockMaterialItem();
    public static final MaterialItem SAND_DEPOSITS = new MaterialItem(YTechBlocks.SAND_DEPOSITS, YTechItems::blockItem);
    public static final MaterialItem STONE_ORES = new StoneOreMaterialItem();
    public static final MaterialItem STORAGE_BLOCKS = new StorageBlockMaterialItem();
    public static final MaterialItem TANNING_RACKS = new MaterialItem(YTechBlocks.TANNING_RACKS, YTechItems::blockItem);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static Collection<DeferredHolder<Item, ? extends Item>> getRegisteredItems() {
        return ITEMS.getEntries();
    }

    private static Item blockItem(DeferredBlock<Block> block) {
        return new BlockItem(block.get(), new Item.Properties());
    }

    private static Item simpleItem() {
        return new Item(new Item.Properties());
    }

    private static Item toolItem(Tier tier) {
        return new TieredItem(tier, new Item.Properties());
    }

    private static Item axeItem(MaterialType material) {
        return new AxeItem(material.getTier(), new Item.Properties().attributes(AxeItem.createAttributes(material.getTier(), 6.0f, -3.2f)));
    }

    private static Item pickaxeItem(MaterialType material) {
        return new PickaxeItem(material.getTier(), new Item.Properties().attributes(PickaxeItem.createAttributes(material.getTier(), 1, -2.8f)));
    }

    private static Item shovelItem(MaterialType material) {
        return new ShovelItem(material.getTier(), new Item.Properties().attributes(ShovelItem.createAttributes(material.getTier(), 1.5f, -3.0f)));
    }

    private static Item helmetItem(MaterialType material) {
        return new ArmorItem(YTechArmorMaterials.ARMORS.of(material), ArmorItem.Type.HELMET, new Item.Properties());
    }

    private static Item chestplateItem(MaterialType material) {
        return new ArmorItem(YTechArmorMaterials.ARMORS.of(material), ArmorItem.Type.CHESTPLATE, new Item.Properties());
    }

    private static Item leggingsItem(MaterialType material) {
        return new ArmorItem(YTechArmorMaterials.ARMORS.of(material), ArmorItem.Type.LEGGINGS, new Item.Properties());
    }

    private static Item bootsItem(MaterialType material) {
        return new ArmorItem(YTechArmorMaterials.ARMORS.of(material), ArmorItem.Type.BOOTS, new Item.Properties());
    }

    private static Item hoeItem(MaterialType material) {
        return new HoeItem(material.getTier(), new Item.Properties().attributes(HoeItem.createAttributes(material.getTier(), 0, -3.0f)));
    }

    private static Item swordItem(MaterialType material) {
        return new SwordItem(material.getTier(), new Item.Properties().attributes(SwordItem.createAttributes(material.getTier(), 3, -2.4f)));
    }

    private static Item knifeItem(MaterialType material) {
        return new KnifeItem(material.getTier(), new Item.Properties().attributes(KnifeItem.createAttributes(material.getTier(), 1, -1.0f)));
    }

    private static Item foodItem(int nutrition, float saturation) {
        return new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).build()));
    }

    private static Item descriptionItem(@NotNull List<Component> description) {
        return new Item(new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
                tooltipComponents.addAll(description);
            }
        };
    }

    private static BlockItem descriptionItem(DeferredBlock<Block> block, @NotNull List<Component> description) {
        return new BlockItem(block.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
                tooltipComponents.addAll(description);
            }
        };
    }

    public static class MaterialItem {
        protected final String group;
        protected final Map<MaterialType, DeferredItem<Item>> items;

        public MaterialItem(String group, NameHolder nameHolder, EnumSet<MaterialType> materialTypes, Function<MaterialType, Item> itemSupplier) {
            this.group = group;
            items = new HashMap<>();
            materialTypes.forEach((type) -> {
                String key = nameHolder.prefix() != null ? nameHolder.prefix() + "_" : "";

                if (type.key.equals("gold") && nameHolder.prefix() == null) {
                    key += "golden";
                } else {
                    key += type.key;
                }

                key += nameHolder.suffix() != null ? "_" + nameHolder.suffix() : "";
                items.put(type, ITEMS.register(key, () -> itemSupplier.apply(type)));
            });
        }

        public MaterialItem(YTechBlocks.MaterialBlock block, Function<DeferredBlock<Block>, Item> itemSupplier) {
            this(block, EnumSet.noneOf(MaterialType.class), itemSupplier);
        }

        public MaterialItem(YTechBlocks.MaterialBlock block, EnumSet<MaterialType> exclude, Function<DeferredBlock<Block>, Item> itemSupplier) {
            this.group = block.getGroup();
            items = new HashMap<>();
            block.entries().stream().filter((entry) -> !exclude.contains(entry.getKey())).forEach((entry) -> {
                MaterialType type = entry.getKey();
                DeferredBlock<Block> object = entry.getValue();
                items.put(type, ITEMS.register(Utils.getPath(object), () -> itemSupplier.apply(object)));
            });
        }

        public DeferredItem<Item> of(MaterialType material) {
            return Objects.requireNonNull(items.get(material));
        }

        public Collection<DeferredItem<Item>> items() {
            return items.values();
        }

        public Set<MaterialType> materials() {
            return items.keySet();
        }

        public Set<Map.Entry<MaterialType, DeferredItem<Item>>> entries() {
            return items.entrySet();
        }

        public String getGroup() {
            return group;
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
        }
    }

    private static class ChestplatesMaterialItem extends MaterialItem {
        public ChestplatesMaterialItem() {
            super("chestplate", NameHolder.suffix("chestplate"), Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::chestplateItem);
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.GOLDEN_CHESTPLATE)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.IRON_CHESTPLATE)));
        }
    }

    private static class HelmetMaterialItem extends MaterialItem {
        public HelmetMaterialItem() {
            super("helmet", NameHolder.suffix("helmet"), Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::helmetItem);
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.GOLDEN_HELMET)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.IRON_HELMET)));
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
            super("ingot", NameHolder.suffix("ingot"), Utils.exclude(MaterialType.ALL_METALS, MaterialType.VANILLA_METALS), (type) -> simpleItem());
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
            super("raw_material", NameHolder.prefix("raw"), Utils.exclude(MaterialType.ALL_ORES, MaterialType.VANILLA_METALS), (type) -> simpleItem());
            items.put(MaterialType.COPPER, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.RAW_COPPER)));
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.RAW_GOLD)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.RAW_IRON)));
        }
    }

    private static class ShovelMaterialItem extends MaterialItem {
        public ShovelMaterialItem() {
            super("shovel", NameHolder.suffix("shovel"), Utils.exclude(MaterialType.ALL_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::shovelItem);
            items.put(MaterialType.GOLD, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.GOLDEN_SHOVEL)));
            items.put(MaterialType.IRON, DeferredItem.createItem(BuiltInRegistries.ITEM.getKey(Items.IRON_SHOVEL)));
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

    private static BlockItem aqueductBlockItem() {
        return new BlockItem(YTechBlocks.AQUEDUCT.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct1", CONFIGURATION.getBaseFluidStoragePerBlock()).withStyle(DARK_GRAY));

                if (CONFIGURATION.shouldRainingFillAqueduct()) {
                    tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct2",
                            CONFIGURATION.getRainingFillAmount(), CONFIGURATION.getRainingFillPerNthTick()).withStyle(DARK_GRAY));
                }
            }
        };
    }

    private static BlockItem aqueductFertilizerBlockItem() {
        return new BlockItem(YTechBlocks.AQUEDUCT_FERTILIZER.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_fertilizer1").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_fertilizer2",
                        CONFIGURATION.getHydratorDrainAmount(), CONFIGURATION.getHydratorDrainPerNthTick()).withStyle(DARK_GRAY));
            }
        };
    }

    private static BlockItem aqueductHydratorBlockItem() {
        return new BlockItem(YTechBlocks.AQUEDUCT_HYDRATOR.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_hydrator1").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_hydrator2",
                        CONFIGURATION.getHydratorDrainAmount(), CONFIGURATION.getHydratorDrainPerNthTick()).withStyle(DARK_GRAY));
            }
        };
    }

    private static BlockItem aqueductValveBlockItem() {
        return new BlockItem(YTechBlocks.AQUEDUCT_VALVE.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_valve1").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_valve2",
                        CONFIGURATION.getValveFillAmount(), CONFIGURATION.getValveFillPerNthTick()).withStyle(DARK_GRAY));
            }
        };
    }

    private static BlockItem firePitBlockItem() {
        return new BlockItem(YTechBlocks.FIRE_PIT.get(), new Item.Properties()) {
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

    private static BlockItem grassBedBlockItem() {
        return new BlockItem(YTechBlocks.GRASS_BED.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.grass_bed").withStyle(DARK_GRAY));
            }
        };
    }

    private static BlockItem dryingRackBlockItem(DeferredBlock<Block> block) {
        return new BlockItem(block.get(), new Item.Properties()) {
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
