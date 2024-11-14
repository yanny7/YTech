package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.*;
import com.yanny.ytech.configuration.block_entity.AbstractPrimitiveMachineBlockEntity;
import com.yanny.ytech.configuration.item.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.yanny.ytech.YTechMod.CONFIGURATION;
import static net.minecraft.ChatFormatting.DARK_GRAY;

public class YTechItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, YTechMod.MOD_ID);

    public static final RegistryObject<Item> ANTLER = ITEMS.register("antler", YTechItems::simpleItem);
    public static final RegistryObject<Item> BASKET = ITEMS.register("basket", BasketItem::new);
    public static final RegistryObject<Item> BEESWAX = ITEMS.register("beeswax", YTechItems::simpleItem);
    public static final RegistryObject<Item> BONE_NEEDLE = ITEMS.register("bone_needle", () -> new ToolItem(Tiers.WOOD, false, new Item.Properties().durability(5).setNoRepair()));
    public static final RegistryObject<Item> BREAD_DOUGH = ITEMS.register("bread_dough", YTechItems::simpleItem);
    public static final RegistryObject<Item> BRICK_MOLD = ITEMS.register("brick_mold", () -> new Item(new Item.Properties().durability(256)));
    public static final RegistryObject<Item> CLAY_BUCKET = ITEMS.register("clay_bucket", () -> new ClayBucketItem(() -> Fluids.EMPTY, new Item.Properties().stacksTo(8)));
    public static final RegistryObject<Item> COOKED_VENISON = ITEMS.register("cooked_venison", () -> foodItem(7, 0.8f));
    public static final RegistryObject<Item> DRIED_BEEF = ITEMS.register("dried_beef", () -> foodItem(6, 0.7f));
    public static final RegistryObject<Item> DRIED_CHICKEN = ITEMS.register("dried_chicken", () -> foodItem(4, 0.5f));
    public static final RegistryObject<Item> DRIED_COD = ITEMS.register("dried_cod", () -> foodItem(4, 0.5f));
    public static final RegistryObject<Item> DRIED_MUTTON = ITEMS.register("dried_mutton", () -> foodItem(4, 0.5f));
    public static final RegistryObject<Item> DRIED_PORKCHOP = ITEMS.register("dried_porkchop", () -> foodItem(6, 0.7f));
    public static final RegistryObject<Item> DRIED_RABBIT = ITEMS.register("dried_rabbit", () -> foodItem(4, 0.5f));
    public static final RegistryObject<Item> DRIED_SALMON = ITEMS.register("dried_salmon", () -> foodItem(4, 0.5f));
    public static final RegistryObject<Item> DRIED_VENISON = ITEMS.register("dried_venison", () -> foodItem(5, 0.7f));
    public static final RegistryObject<Item> FLOUR = ITEMS.register("flour", YTechItems::simpleItem);
    public static final RegistryObject<Item> GRASS_FIBERS = ITEMS.register("grass_fibers", () -> burnableDescriptionItem(List.of(Component.translatable("text.ytech.hover.grass_fibers").withStyle(ChatFormatting.DARK_GRAY)), 100));
    public static final RegistryObject<Item> GRASS_TWINE = ITEMS.register("grass_twine", () -> burnableSimpleItem(200));
    public static final RegistryObject<Item> IRON_BLOOM = ITEMS.register("iron_bloom", YTechItems::simpleItem);
    public static final RegistryObject<Item> LAVA_CLAY_BUCKET = ITEMS.register("lava_clay_bucket", LavaClayBucketItem::new);
    public static final RegistryObject<Item> LEATHER_STRIPS = ITEMS.register("leather_strips", YTechItems::simpleItem);
    public static final RegistryObject<Item> MAMMOTH_TUSK = ITEMS.register("mammoth_tusk", YTechItems::simpleItem);
    public static final RegistryObject<Item> PEBBLE = ITEMS.register("pebble", PebbleItem::new);
    public static final RegistryObject<Item> RAW_HIDE = ITEMS.register("raw_hide", YTechItems::simpleItem);
    public static final RegistryObject<Item> RHINO_HORN = ITEMS.register("rhino_horn", YTechItems::simpleItem);
    public static final RegistryObject<Item> SHARP_FLINT = ITEMS.register("sharp_flint", () -> toolCanHurtItem(Tiers.WOOD));
    public static final RegistryObject<Item> UNFIRED_AMPHORA = ITEMS.register("unfired_amphora", YTechItems::simpleItem);
    public static final RegistryObject<Item> UNFIRED_BRICK = ITEMS.register("unfired_brick", YTechItems::simpleItem);
    public static final RegistryObject<Item> UNFIRED_CLAY_BUCKET = ITEMS.register("unfired_clay_bucket", YTechItems::simpleItem);
    public static final RegistryObject<Item> UNFIRED_DECORATED_POT = ITEMS.register("unfired_decoration_pot", YTechItems::simpleItem);
    public static final RegistryObject<Item> UNFIRED_FLOWER_POT = ITEMS.register("unfired_flower_pot", YTechItems::simpleItem);
    public static final RegistryObject<Item> UNLIT_TORCH = ITEMS.register("unlit_torch", UnlitTorchItem::new);
    public static final RegistryObject<Item> VENISON = ITEMS.register("venison", () -> foodItem(2, 0.3f));
    public static final RegistryObject<Item> WATER_CLAY_BUCKET = ITEMS.register("water_clay_bucket", () -> new ClayBucketItem(() -> Fluids.WATER, new Item.Properties().craftRemainder(YTechItems.CLAY_BUCKET.get()).stacksTo(1)));

    public static final RegistryObject<Item> AMPHORA = ITEMS.register("amphora", () -> descriptionItem(YTechBlocks.AMPHORA, List.of(Component.translatable("text.ytech.hover.amphora1").withStyle(DARK_GRAY), Component.translatable("text.ytech.hover.amphora2").withStyle(DARK_GRAY), Component.translatable("text.ytech.hover.amphora3").withStyle(DARK_GRAY))));
    public static final RegistryObject<Item> AQUEDUCT = ITEMS.register("aqueduct", YTechItems::aqueductBlockItem);
    public static final RegistryObject<Item> AQUEDUCT_FERTILIZER = ITEMS.register("aqueduct_fertilizer", YTechItems::aqueductFertilizerBlockItem);
    public static final RegistryObject<Item> AQUEDUCT_HYDRATOR = ITEMS.register("aqueduct_hydrator", YTechItems::aqueductHydratorBlockItem);
    public static final RegistryObject<Item> AQUEDUCT_VALVE = ITEMS.register("aqueduct_valve", YTechItems::aqueductValveBlockItem);
    public static final RegistryObject<Item> BRICK_CHIMNEY = ITEMS.register("brick_chimney", () -> descriptionItem(YTechBlocks.BRICK_CHIMNEY, List.of(Component.translatable("text.ytech.hover.chimney", AbstractPrimitiveMachineBlockEntity.TEMP_PER_CHIMNEY).withStyle(DARK_GRAY))));
    public static final RegistryObject<Item> BRONZE_ANVIL = ITEMS.register("bronze_anvil", () -> blockItem(YTechBlocks.BRONZE_ANVIL));
    public static final RegistryObject<Item> CRAFTING_WORKSPACE = ITEMS.register("crafting_workspace", () -> descriptionItem(YTechBlocks.CRAFTING_WORKSPACE, List.of(Component.translatable("text.ytech.hover.crafting_workbench1").withStyle(DARK_GRAY), Component.translatable("text.ytech.hover.crafting_workbench2").withStyle(DARK_GRAY), Component.translatable("text.ytech.hover.crafting_workbench3").withStyle(DARK_GRAY))));
    public static final RegistryObject<Item> FIRE_PIT = ITEMS.register("fire_pit", YTechItems::firePitBlockItem);
    public static final RegistryObject<Item> GRASS_BED = ITEMS.register("grass_bed", YTechItems::grassBedBlockItem);
    public static final RegistryObject<Item> MILLSTONE = ITEMS.register("millstone", () -> descriptionItem(YTechBlocks.MILLSTONE, List.of(Component.translatable("text.ytech.hover.millstone").withStyle(DARK_GRAY))));
    public static final RegistryObject<Item> POTTERY_WHEEL = ITEMS.register("potters_wheel", () -> descriptionItem(YTechBlocks.POTTERS_WHEEL, List.of(Component.translatable("text.ytech.hover.potters_wheel1").withStyle(DARK_GRAY), Component.translatable("text.ytech.hover.potters_wheel2").withStyle(DARK_GRAY))));
    public static final RegistryObject<Item> PRIMITIVE_ALLOY_SMELTER = ITEMS.register("primitive_alloy_smelter", () -> descriptionItem(YTechBlocks.PRIMITIVE_ALLOY_SMELTER, List.of(Component.translatable("text.ytech.hover.primitive_smelter").withStyle(DARK_GRAY))));
    public static final RegistryObject<Item> PRIMITIVE_SMELTER = ITEMS.register("primitive_smelter", () -> descriptionItem(YTechBlocks.PRIMITIVE_SMELTER, List.of(Component.translatable("text.ytech.hover.primitive_smelter").withStyle(DARK_GRAY))));
    public static final RegistryObject<Item> REINFORCED_BRICKS = ITEMS.register("reinforced_bricks", () -> blockItem(YTechBlocks.REINFORCED_BRICKS));
    public static final RegistryObject<Item> REINFORCED_BRICK_CHIMNEY = ITEMS.register("reinforced_brick_chimney", () -> descriptionItem(YTechBlocks.REINFORCED_BRICK_CHIMNEY, List.of(Component.translatable("text.ytech.hover.chimney", AbstractPrimitiveMachineBlockEntity.TEMP_PER_CHIMNEY).withStyle(DARK_GRAY))));
    public static final RegistryObject<Item> TERRACOTTA_BRICKS = ITEMS.register("terracotta_bricks", () -> blockItem(YTechBlocks.TERRACOTTA_BRICKS));
    public static final RegistryObject<Item> TERRACOTTA_BRICK_SLAB = ITEMS.register("terracotta_brick_slab", () -> blockItem(YTechBlocks.TERRACOTTA_BRICK_SLAB));
    public static final RegistryObject<Item> TERRACOTTA_BRICK_STAIRS = ITEMS.register("terracotta_brick_stairs", () -> blockItem(YTechBlocks.TERRACOTTA_BRICK_STAIRS));
    public static final RegistryObject<Item> THATCH = ITEMS.register("thatch", () -> burnableBlockItem(YTechBlocks.THATCH, 200));
    public static final RegistryObject<Item> THATCH_SLAB = ITEMS.register("thatch_slab", () -> burnableBlockItem(YTechBlocks.THATCH_SLAB, 100));
    public static final RegistryObject<Item> THATCH_STAIRS = ITEMS.register("thatch_stairs", () -> burnableBlockItem(YTechBlocks.THATCH_STAIRS, 200));
    public static final RegistryObject<Item> TOOL_RACK = ITEMS.register("tool_rack", () -> blockItem(YTechBlocks.TOOL_RACK));
    public static final RegistryObject<Item> TREE_STUMP = ITEMS.register("tree_stump", () -> blockItem(YTechBlocks.TREE_STUMP));
    public static final RegistryObject<Item> WOODEN_BOX = ITEMS.register("wooden_box", () -> blockItem(YTechBlocks.WOODEN_BOX));

    public static final RegistryObject<Item> CHLORITE_BRACELET = ITEMS.register("chlorite_bracelet", ChloriteBraceletItem::new);
    public static final RegistryObject<Item> LION_MAN = ITEMS.register("lion_man", LionManItem::new);
    public static final RegistryObject<Item> SHELL_BEADS = ITEMS.register("shell_beads", ShellBeadsItem::new);
    public static final RegistryObject<Item> VENUS_OF_HOHLE_FELS = ITEMS.register("venus_of_hohle_fels", VenusOfHohleFelsItem::new);
    public static final RegistryObject<Item> WILD_HORSE = ITEMS.register("wild_horse", WildHorseItem::new);

    public static final RegistryObject<Item> AUROCHS_SPAWN_EGG = ITEMS.register("aurochs_spawn_egg", () -> new ForgeSpawnEggItem(YTechEntityTypes.AUROCHS, 0x4688f5, 0xE03C83, new Item.Properties()));
    public static final RegistryObject<Item> DEER_SPAWN_EGG = ITEMS.register("deer_spawn_egg", () -> new ForgeSpawnEggItem(YTechEntityTypes.DEER, 0x664825, 0xE09C53, new Item.Properties()));
    public static final RegistryObject<Item> FOWL_SPAWN_EGG = ITEMS.register("fowl_spawn_egg", () -> new ForgeSpawnEggItem(YTechEntityTypes.FOWL, 0xc6484b, 0xedde9d, new Item.Properties()));
    public static final RegistryObject<Item> MOUFLON_SPAWN_EGG = ITEMS.register("mouflon_spawn_egg", () -> new ForgeSpawnEggItem(YTechEntityTypes.MOUFLON, 0x064aeb, 0xa13bb6, new Item.Properties()));
    public static final RegistryObject<Item> SABER_TOOTH_TIGER_SPAWN_EGG = ITEMS.register("saber_tooth_tiger_spawn_egg", () -> new ForgeSpawnEggItem(YTechEntityTypes.SABER_TOOTH_TIGER, 0xa52a80, 0x9194c2, new Item.Properties()));
    public static final RegistryObject<Item> TERROR_BIRD_SPAWN_EGG = ITEMS.register("terror_bird_spawn_egg", () -> new ForgeSpawnEggItem(YTechEntityTypes.TERROR_BIRD, 0x759ac0, 0xc19452, new Item.Properties()));
    public static final RegistryObject<Item> WILD_BOAR_SPAWN_EGG = ITEMS.register("wild_boar_spawn_egg", () -> new ForgeSpawnEggItem(YTechEntityTypes.WILD_BOAR, 0xf4f7d7, 0x560a59, new Item.Properties()));
    public static final RegistryObject<Item> WOOLLY_MAMMOTH_SPAWN_EGG = ITEMS.register("woolly_mammoth_spawn_egg", () -> new ForgeSpawnEggItem(YTechEntityTypes.WOOLLY_MAMMOTH, 0x8a4a71, 0x6cc8ab, new Item.Properties()));
    public static final RegistryObject<Item> WOOLLY_RHINO_SPAWN_EGG = ITEMS.register("woolly_rhino_spawn_egg", () -> new ForgeSpawnEggItem(YTechEntityTypes.WOOLLY_RHINO, 0x04b53a, 0x2f7415, new Item.Properties()));

    public static final TypedItem<PartType> CLAY_MOLDS = new PartItem("clay_mold", NameHolder.suffix("clay_mold"), () -> new Item(new Item.Properties().durability(16)));
    public static final TypedItem<PartType> PATTERNS = new PartItem("pattern", NameHolder.suffix("pattern"), YTechItems::simpleItem);
    public static final TypedItem<PartType> SAND_MOLDS = new PartItem("sand_mold", NameHolder.suffix("sand_mold"), YTechItems::simpleItem);
    public static final TypedItem<PartType> UNFIRED_MOLDS = new PartItem("unfired_mold", NameHolder.both("unfired", "mold"), YTechItems::simpleItem);

    public static final MultiTypedItem<MaterialType, PartType> PARTS = new MaterialPartItem("part", Utils.exclude(MaterialType.ALL_METALS, MaterialType.IRON), Utils.exclude(PartType.ALL_PARTS, PartType.INGOT), NameHolder.suffix("part"), YTechItems::simpleItem);

    public static final TypedItem<MaterialType> ARROWS = new MaterialItem("arrow", NameHolder.suffix("arrow"), MaterialType.ALL_HARD_METALS, MaterialArrowItem::new);
    public static final TypedItem<MaterialType> AXES = new AxeMaterialItem();
    public static final TypedItem<MaterialType> BOLTS = new MaterialItem("bolt", NameHolder.suffix("bolt"), Utils.merge(MaterialType.ALL_METALS, MaterialType.WOODEN), (type) -> burnableSimpleItem(type, 100));
    public static final TypedItem<MaterialType> BOOTS = new BootsMaterialItem();
    public static final TypedItem<MaterialType> CHESTPLATES = new ChestplatesMaterialItem();
    public static final TypedItem<MaterialType> CRUSHED_MATERIALS = new MaterialItem("crushed_material", NameHolder.prefix("crushed"), MaterialType.ALL_ORES, (type) -> simpleItem());
    public static final TypedItem<MaterialType> FILES = new MaterialItem("file", NameHolder.suffix("file"), MaterialType.ALL_METALS, (type) -> toolCanHurtItem(type.getTier()));
    public static final TypedItem<MaterialType> HAMMERS = new MaterialItem("hammer", NameHolder.suffix("hammer"), Utils.merge(MaterialType.ALL_METALS, MaterialType.STONE), (type) -> toolCanHurtItem(type.getTier()));
    public static final TypedItem<MaterialType> HELMETS = new HelmetMaterialItem();
    public static final TypedItem<MaterialType> HOES = new HoeMaterialItem();
    public static final TypedItem<MaterialType> INGOTS = new IngotMaterialItem();
    public static final TypedItem<MaterialType> KNIVES = new MaterialItem("knife", NameHolder.suffix("knife"), Utils.merge(MaterialType.ALL_METALS, MaterialType.FLINT), YTechItems::knifeItem);
    public static final TypedItem<MaterialType> LEGGINGS = new LeggingsMaterialItem();
    public static final TypedItem<MaterialType> MORTAR_AND_PESTLES = new MaterialItem("mortar_and_pestle", NameHolder.suffix("mortar_and_pestle"), Utils.merge(MaterialType.ALL_METALS, MaterialType.STONE), (type) -> toolItem(type.getTier()));
    public static final TypedItem<MaterialType> PICKAXES = new PickaxeMaterialItem();
    public static final TypedItem<MaterialType> PLATES = new MaterialItem("plate", NameHolder.suffix("plate"), Utils.merge(MaterialType.ALL_METALS, MaterialType.WOODEN), (type) -> burnableSimpleItem(type, 200));
    public static final TypedItem<MaterialType> RAW_MATERIALS = new RawMaterialItem();
    public static final TypedItem<MaterialType> RODS = new MaterialItem("rod", NameHolder.suffix("rod"), MaterialType.ALL_METALS, (type) -> simpleItem());
    public static final TypedItem<MaterialType> SAWS = new MaterialItem("saw", NameHolder.suffix("saw"), MaterialType.ALL_METALS, (type) -> toolCanHurtItem(type.getTier()));
    public static final TypedItem<MaterialType> SAW_BLADES = new MaterialItem("saw_blade", NameHolder.suffix("saw_blade"), EnumSet.of(MaterialType.IRON), (type) -> simpleItem());
    public static final TypedItem<MaterialType> SHEARS = new ShearsMaterialItem();
    public static final TypedItem<MaterialType> SHOVELS = new ShovelMaterialItem();
    public static final TypedItem<MaterialType> SPEARS = new MaterialItem("spear", NameHolder.suffix("spear"), Utils.merge(MaterialType.ALL_HARD_METALS, MaterialType.FLINT), (type) -> new SpearItem(SpearType.BY_MATERIAL_TYPE.get(type)));
    public static final TypedItem<MaterialType> SWORDS = new SwordMaterialItem();

    public static final TypedItem<MaterialType> DEEPSLATE_ORES = new DeepslateOreMaterialItem();
    public static final TypedItem<MaterialType> DRYING_RACKS = new MaterialItem(YTechBlocks.DRYING_RACKS, YTechItems::dryingRackBlockItem);
    public static final TypedItem<MaterialType> GRAVEL_DEPOSITS = new MaterialItem(YTechBlocks.GRAVEL_DEPOSITS, YTechItems::blockItem);
    public static final TypedItem<MaterialType> NETHER_ORES = new NetherOreMaterialItem();
    public static final TypedItem<MaterialType> RAW_STORAGE_BLOCKS = new RawStorageBlockMaterialItem();
    public static final TypedItem<MaterialType> SAND_DEPOSITS = new MaterialItem(YTechBlocks.SAND_DEPOSITS, YTechItems::blockItem);
    public static final TypedItem<MaterialType> STONE_ORES = new StoneOreMaterialItem();
    public static final TypedItem<MaterialType> STORAGE_BLOCKS = new StorageBlockMaterialItem();
    public static final TypedItem<MaterialType> TANNING_RACKS = new MaterialItem(YTechBlocks.TANNING_RACKS, block -> burnableBlockItem(block, 300));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static Collection<RegistryObject<Item>> getRegisteredItems() {
        return ITEMS.getEntries();
    }

    private static Item blockItem(RegistryObject<Block> block) {
        return new BlockItem(block.get(), new Item.Properties());
    }

    private static Item burnableBlockItem(RegistryObject<Block> block, int burnTime) {
        return new BlockItem(block.get(), new Item.Properties()) {
            @Override
            public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                return burnTime;
            }
        };
    }

    private static Item simpleItem() {
        return new Item(new Item.Properties());
    }

    private static Item burnableSimpleItem(int burnTime) {
        return new Item(new Item.Properties()) {
            @Override
            public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                return burnTime;
            }
        };
    }

    private static Item burnableSimpleItem(@NotNull MaterialType material, int burnTime) {
        return new Item(new Item.Properties()) {
            @Override
            public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                return material == MaterialType.WOODEN ? burnTime : -1;
            }
        };
    }

    private static Item toolItem(Tier tier) {
        return new ToolItem(tier, false, new Item.Properties());
    }

    private static Item toolCanHurtItem(Tier tier) {
        return new ToolItem(tier, true, new Item.Properties());
    }

    private static Item axeItem(MaterialType material) {
        return new AxeItem(material.getTier(), 6.0f, -3.2f, new Item.Properties());
    }

    private static Item pickaxeItem(MaterialType material) {
        return new PickaxeItem(material.getTier(), 1, -2.8f, new Item.Properties());
    }

    private static Item shearsItem(MaterialType materialType) {
        return new ShearsItem(new Item.Properties().durability(materialType.getTier().getUses()));
    }

    private static Item shovelItem(MaterialType material) {
        return new ShovelItem(material.getTier(), 1.5f, -3.0f, new Item.Properties());
    }

    private static Item helmetItem(MaterialType material) {
        return new ArmorItem(YTechArmorMaterial.get(material), ArmorItem.Type.HELMET, new Item.Properties());
    }

    private static Item chestplateItem(MaterialType material) {
        return new ArmorItem(YTechArmorMaterial.get(material), ArmorItem.Type.CHESTPLATE, new Item.Properties());
    }

    private static Item leggingsItem(MaterialType material) {
        return new ArmorItem(YTechArmorMaterial.get(material), ArmorItem.Type.LEGGINGS, new Item.Properties());
    }

    private static Item bootsItem(MaterialType material) {
        return new ArmorItem(YTechArmorMaterial.get(material), ArmorItem.Type.BOOTS, new Item.Properties());
    }

    private static Item hoeItem(MaterialType material) {
        return new HoeItem(material.getTier(), 0, -3.0f, new Item.Properties());
    }

    private static Item swordItem(MaterialType material) {
        return new SwordItem(material.getTier(), 3, -2.4f, new Item.Properties());
    }

    private static Item knifeItem(MaterialType material) {
        return new SwordItem(material.getTier(), 1, -1.0f, new Item.Properties());
    }

    private static Item foodItem(int nutrition, float saturation) {
        return new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturation).build()));
    }

    private static Item burnableDescriptionItem(@NotNull List<Component> description, int burnTime) {
        return new Item(new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.addAll(description);
            }

            @Override
            public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                return burnTime;
            }
        };
    }

    private static Item descriptionItem(RegistryObject<Block> block, @NotNull List<Component> description) {
        return new BlockItem(block.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.addAll(description);
            }
        };
    }

    public static class TypedItem<E extends Enum<E>> extends AbstractMap<E, RegistryObject<Item>> {
        protected final String group;
        protected final Map<E, RegistryObject<Item>> items = new HashMap<>();

        TypedItem(String group) {
            this.group = group;
        }

        public String getGroup() {
            return group;
        }

        @NotNull
        @Override
        public Set<Entry<E, RegistryObject<Item>>> entrySet() {
            return items.entrySet();
        }
    }

    public static class MultiTypedItem<E extends Enum<E>, F extends Enum<F>> extends AbstractMap<E, Map<F, RegistryObject<Item>>> {
        protected final String group;
        protected final Map<E, Map<F, RegistryObject<Item>>> items = new HashMap<>();

        MultiTypedItem(String group) {
            this.group = group;
        }

        public RegistryObject<Item> get(E type1, F type2) {
            return items.get(type1).get(type2);
        }

        public String getGroup() {
            return group;
        }

        @Override
        public @NotNull Set<Entry<E, Map<F, RegistryObject<Item>>>> entrySet() {
            return items.entrySet();
        }
    }

    public static class PartItem extends TypedItem<PartType> {
        PartItem(String group, NameHolder nameHolder, Supplier<Item> itemSupplier) {
            super(group);
            for (PartType partType : PartType.values()) {
                String key = nameHolder.prefix() != null ? nameHolder.prefix() + "_" : "";
                key += partType.key;
                key += nameHolder.suffix() != null ? "_" + nameHolder.suffix() : "";
                items.put(partType, ITEMS.register(key, itemSupplier));
            }
        }
    }

    public static class MaterialPartItem extends MultiTypedItem<MaterialType, PartType> {
        MaterialPartItem(String group, EnumSet<MaterialType> materials, EnumSet<PartType> parts, NameHolder nameHolder, Supplier<Item> itemSupplier) {
            super(group);
            for (MaterialType material : materials) {
                for (PartType part : parts) {
                    String key = nameHolder.prefix() != null ? nameHolder.prefix() + "_" : "";
                    key += material.key + "_" + part.key;
                    key += nameHolder.suffix() != null ? "_" + nameHolder.suffix() : "";
                    items.computeIfAbsent(material, (k) -> new HashMap<>()).put(part, ITEMS.register(key, itemSupplier));
                }
            }
        }
    }

    public static class MaterialItem extends TypedItem<MaterialType> {
        public MaterialItem(String group, NameHolder nameHolder, EnumSet<MaterialType> materialTypes, Function<MaterialType, Item> itemSupplier) {
            super(group);
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

        public MaterialItem(YTechBlocks.MaterialBlock block, Function<RegistryObject<Block>, Item> itemSupplier) {
            this(block, EnumSet.noneOf(MaterialType.class), itemSupplier);
        }

        public MaterialItem(YTechBlocks.MaterialBlock block, EnumSet<MaterialType> exclude, Function<RegistryObject<Block>, Item> itemSupplier) {
            super(block.getGroup());
            block.entries().stream().filter((entry) -> !exclude.contains(entry.getKey())).forEach((entry) -> {
                MaterialType type = entry.getKey();
                RegistryObject<Block> object = entry.getValue();
                items.put(type, ITEMS.register(Utils.getPath(object), () -> itemSupplier.apply(object)));
            });
        }
    }

    private static class AxeMaterialItem extends MaterialItem {
        public AxeMaterialItem() {
            super("axe", NameHolder.suffix("axe"), Utils.exclude(Utils.merge(MaterialType.ALL_METALS, MaterialType.FLINT), MaterialType.GOLD, MaterialType.IRON), YTechItems::axeItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_AXE), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_AXE), ForgeRegistries.ITEMS));
        }
    }

    private static class BootsMaterialItem extends MaterialItem {
        public BootsMaterialItem() {
            super("boots", NameHolder.suffix("boots"), Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::bootsItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_BOOTS), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_BOOTS), ForgeRegistries.ITEMS));
            items.put(MaterialType.LEATHER, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.LEATHER_BOOTS), ForgeRegistries.ITEMS));
        }
    }

    private static class ChestplatesMaterialItem extends MaterialItem {
        public ChestplatesMaterialItem() {
            super("chestplate", NameHolder.suffix("chestplate"), Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::chestplateItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_CHESTPLATE), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_CHESTPLATE), ForgeRegistries.ITEMS));
            items.put(MaterialType.LEATHER, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.LEATHER_CHESTPLATE), ForgeRegistries.ITEMS));
        }
    }

    private static class HelmetMaterialItem extends MaterialItem {
        public HelmetMaterialItem() {
            super("helmet", NameHolder.suffix("helmet"), Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::helmetItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_HELMET), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_HELMET), ForgeRegistries.ITEMS));
            items.put(MaterialType.LEATHER, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.LEATHER_HELMET), ForgeRegistries.ITEMS));
        }
    }

    private static class HoeMaterialItem extends MaterialItem {
        public HoeMaterialItem() {
            super("hoe", NameHolder.suffix("hoe"), Utils.exclude(MaterialType.ALL_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::hoeItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_HOE), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_HOE), ForgeRegistries.ITEMS));
        }
    }

    private static class IngotMaterialItem extends MaterialItem {
        public IngotMaterialItem() {
            super("ingot", NameHolder.suffix("ingot"), Utils.exclude(MaterialType.ALL_METALS, MaterialType.VANILLA_METALS), (type) -> simpleItem());
            items.put(MaterialType.COPPER, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.COPPER_INGOT), ForgeRegistries.ITEMS));
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLD_INGOT), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_INGOT), ForgeRegistries.ITEMS));
        }
    }

    private static class LeggingsMaterialItem extends MaterialItem {
        public LeggingsMaterialItem() {
            super("leggings", NameHolder.suffix("leggings"), Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::leggingsItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_LEGGINGS), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_LEGGINGS), ForgeRegistries.ITEMS));
            items.put(MaterialType.LEATHER, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.LEATHER_LEGGINGS), ForgeRegistries.ITEMS));
        }
    }

    private static class PickaxeMaterialItem extends MaterialItem {
        public PickaxeMaterialItem() {
            super("pickaxe", NameHolder.suffix("pickaxe"), Utils.exclude(Utils.merge(MaterialType.ALL_METALS, MaterialType.ANTLER), MaterialType.GOLD, MaterialType.IRON), YTechItems::pickaxeItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_PICKAXE), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_PICKAXE), ForgeRegistries.ITEMS));
        }
    }

    private static class RawMaterialItem extends MaterialItem {
        public RawMaterialItem() {
            super("raw_material", NameHolder.prefix("raw"), Utils.exclude(MaterialType.ALL_ORES, MaterialType.VANILLA_METALS), (type) -> simpleItem());
            items.put(MaterialType.COPPER, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.RAW_COPPER), ForgeRegistries.ITEMS));
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.RAW_GOLD), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.RAW_IRON), ForgeRegistries.ITEMS));
        }
    }

    private static class ShearsMaterialItem extends MaterialItem {
        public ShearsMaterialItem() {
            super("shears", NameHolder.suffix("shears"), Utils.exclude(MaterialType.ALL_METALS, MaterialType.IRON), YTechItems::shearsItem);
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.SHEARS), ForgeRegistries.ITEMS));
        }
    }

    private static class ShovelMaterialItem extends MaterialItem {
        public ShovelMaterialItem() {
            super("shovel", NameHolder.suffix("shovel"), Utils.exclude(MaterialType.ALL_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::shovelItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_SHOVEL), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_SHOVEL), ForgeRegistries.ITEMS));
            items.put(MaterialType.WOODEN, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.WOODEN_SHOVEL), ForgeRegistries.ITEMS));
        }
    }

    private static class SwordMaterialItem extends MaterialItem {
        public SwordMaterialItem() {
            super("sword", NameHolder.suffix("sword"), Utils.exclude(MaterialType.ALL_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::swordItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_SWORD), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_SWORD), ForgeRegistries.ITEMS));
        }
    }

    private static class DeepslateOreMaterialItem extends MaterialItem {
        public DeepslateOreMaterialItem() {
            super(YTechBlocks.DEEPSLATE_ORES, MaterialType.VANILLA_METALS, YTechItems::blockItem);
            items.put(MaterialType.COPPER, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.DEEPSLATE_COPPER_ORE), ForgeRegistries.ITEMS));
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.DEEPSLATE_GOLD_ORE), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.DEEPSLATE_IRON_ORE), ForgeRegistries.ITEMS));
        }
    }

    private static class NetherOreMaterialItem extends MaterialItem {
        public NetherOreMaterialItem() {
            super(YTechBlocks.NETHER_ORES, EnumSet.of(MaterialType.GOLD), YTechItems::blockItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.NETHER_GOLD_ORE), ForgeRegistries.ITEMS));
        }
    }

    private static class RawStorageBlockMaterialItem extends MaterialItem {
        public RawStorageBlockMaterialItem() {
            super(YTechBlocks.RAW_STORAGE_BLOCKS, MaterialType.VANILLA_METALS, YTechItems::blockItem);
            items.put(MaterialType.COPPER, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.RAW_COPPER_BLOCK), ForgeRegistries.ITEMS));
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.RAW_GOLD_BLOCK), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.RAW_IRON_BLOCK), ForgeRegistries.ITEMS));
        }
    }

    private static class StoneOreMaterialItem extends MaterialItem {
        public StoneOreMaterialItem() {
            super(YTechBlocks.STONE_ORES, MaterialType.VANILLA_METALS, YTechItems::blockItem);
            items.put(MaterialType.COPPER, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.COPPER_ORE), ForgeRegistries.ITEMS));
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLD_ORE), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_ORE), ForgeRegistries.ITEMS));
        }
    }

    private static class StorageBlockMaterialItem extends MaterialItem {
        public StorageBlockMaterialItem() {
            super(YTechBlocks.STORAGE_BLOCKS, MaterialType.VANILLA_METALS, YTechItems::blockItem);
            items.put(MaterialType.COPPER, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.COPPER_BLOCK), ForgeRegistries.ITEMS));
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLD_BLOCK), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_BLOCK), ForgeRegistries.ITEMS));
        }
    }

    private static Item aqueductBlockItem() {
        return new BlockItem(YTechBlocks.AQUEDUCT.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct1", CONFIGURATION.getBaseFluidStoragePerBlock()).withStyle(DARK_GRAY));

                if (CONFIGURATION.shouldRainingFillAqueduct()) {
                    tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct2",
                            CONFIGURATION.getRainingFillAmount(), CONFIGURATION.getRainingFillPerNthTick()).withStyle(DARK_GRAY));
                }
            }
        };
    }

    private static Item aqueductFertilizerBlockItem() {
        return new BlockItem(YTechBlocks.AQUEDUCT_FERTILIZER.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_fertilizer1").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_fertilizer2",
                        CONFIGURATION.getHydratorDrainAmount(), CONFIGURATION.getHydratorDrainPerNthTick()).withStyle(DARK_GRAY));
            }
        };
    }

    private static Item aqueductHydratorBlockItem() {
        return new BlockItem(YTechBlocks.AQUEDUCT_HYDRATOR.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_hydrator1").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_hydrator2",
                        CONFIGURATION.getHydratorDrainAmount(), CONFIGURATION.getHydratorDrainPerNthTick()).withStyle(DARK_GRAY));
            }
        };
    }

    private static Item aqueductValveBlockItem() {
        return new BlockItem(YTechBlocks.AQUEDUCT_VALVE.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_valve1").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.aqueduct_valve2",
                        CONFIGURATION.getValveFillAmount(), CONFIGURATION.getValveFillPerNthTick()).withStyle(DARK_GRAY));
            }
        };
    }

    private static Item firePitBlockItem() {
        return new BlockItem(YTechBlocks.FIRE_PIT.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.fire_pit1").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.fire_pit2").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.fire_pit3").withStyle(DARK_GRAY));
                tooltipComponents.add(Component.translatable("text.ytech.hover.fire_pit4").withStyle(DARK_GRAY));
            }
        };
    }

    private static Item grassBedBlockItem() {
        return new BlockItem(YTechBlocks.GRASS_BED.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.grass_bed").withStyle(DARK_GRAY));
            }

            @Override
            public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                return 300;
            }
        };
    }

    private static Item dryingRackBlockItem(RegistryObject<Block> block) {
        return new BlockItem(block.get(), new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.drying_rack1").withStyle(DARK_GRAY));

                if (CONFIGURATION.noDryingDuringRain()) {
                    tooltipComponents.add(Component.translatable("text.ytech.hover.drying_rack2").withStyle(DARK_GRAY));
                }
            }

            @Override
            public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                return 300;
            }
        };
    }
}
