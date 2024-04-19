package com.yanny.ytech.registration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.SpearType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.AbstractPrimitiveMachineBlockEntity;
import com.yanny.ytech.configuration.item.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

import static com.yanny.ytech.YTechMod.CONFIGURATION;
import static net.minecraft.ChatFormatting.DARK_GRAY;

public class YTechItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, YTechMod.MOD_ID);

    public static final RegistryObject<Item> ANTLER = ITEMS.register("antler", YTechItems::simpleItem);
    public static final RegistryObject<Item> BASKET = ITEMS.register("basket", BasketItem::new);
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
    public static final RegistryObject<Item> GRASS_FIBERS = ITEMS.register("grass_fibers", () -> descriptionItem(List.of(Component.translatable("text.ytech.hover.grass_fibers").withStyle(ChatFormatting.DARK_GRAY))));
    public static final RegistryObject<Item> GRASS_TWINE = ITEMS.register("grass_twine", YTechItems::simpleItem);
    public static final RegistryObject<Item> IRON_BLOOM = ITEMS.register("iron_bloom", YTechItems::simpleItem);
    public static final RegistryObject<Item> LAVA_CLAY_BUCKET = ITEMS.register("lava_clay_bucket", LavaClayBucketItem::new);
    public static final RegistryObject<Item> LEATHER_STRIPS = ITEMS.register("leather_strips", YTechItems::simpleItem);
    public static final RegistryObject<Item> PEBBLE = ITEMS.register("pebble", PebbleItem::new);
    public static final RegistryObject<Item> RAW_HIDE = ITEMS.register("raw_hide", YTechItems::simpleItem);
    public static final RegistryObject<Item> SHARP_FLINT = ITEMS.register("sharp_flint", () -> toolItem(Tiers.WOOD));
    public static final RegistryObject<Item> UNFIRED_BRICK = ITEMS.register("unfired_brick", YTechItems::simpleItem);
    public static final RegistryObject<Item> UNFIRED_CLAY_BUCKET = ITEMS.register("unfired_clay_bucket", YTechItems::simpleItem);
    public static final RegistryObject<Item> VENISON = ITEMS.register("venison", () -> foodItem(2, 0.3f));
    public static final RegistryObject<Item> WATER_CLAY_BUCKET = ITEMS.register("water_clay_bucket", () -> new ClayBucketItem(() -> Fluids.WATER, new Item.Properties().craftRemainder(YTechItems.CLAY_BUCKET.get()).stacksTo(1)));

    public static final RegistryObject<Item> AQUEDUCT = ITEMS.register("aqueduct", YTechItems::aqueductBlockItem);
    public static final RegistryObject<Item> AQUEDUCT_FERTILIZER = ITEMS.register("aqueduct_fertilizer", YTechItems::aqueductFertilizerBlockItem);
    public static final RegistryObject<Item> AQUEDUCT_HYDRATOR = ITEMS.register("aqueduct_hydrator", YTechItems::aqueductHydratorBlockItem);
    public static final RegistryObject<Item> AQUEDUCT_VALVE = ITEMS.register("aqueduct_valve", YTechItems::aqueductValveBlockItem);
    public static final RegistryObject<Item> BRICK_CHIMNEY = ITEMS.register("brick_chimney", () -> descriptionItem(YTechBlocks.BRICK_CHIMNEY, List.of(Component.translatable("text.ytech.hover.chimney", AbstractPrimitiveMachineBlockEntity.TEMP_PER_CHIMNEY).withStyle(DARK_GRAY))));
    public static final RegistryObject<Item> BRONZE_ANVIL = ITEMS.register("bronze_anvil", () -> blockItem(YTechBlocks.BRONZE_ANVIL));
    public static final RegistryObject<Item> FIRE_PIT = ITEMS.register("fire_pit", YTechItems::firePitBlockItem);
    public static final RegistryObject<Item> GRASS_BED = ITEMS.register("grass_bed", YTechItems::grassBedBlockItem);
    public static final RegistryObject<Item> MILLSTONE = ITEMS.register("millstone", () -> descriptionItem(YTechBlocks.MILLSTONE, List.of(Component.translatable("text.ytech.hover.millstone").withStyle(DARK_GRAY))));
    public static final RegistryObject<Item> PRIMITIVE_ALLOY_SMELTER = ITEMS.register("primitive_alloy_smelter", () -> descriptionItem(YTechBlocks.PRIMITIVE_ALLOY_SMELTER, List.of(Component.translatable("text.ytech.hover.primitive_smelter").withStyle(DARK_GRAY))));
    public static final RegistryObject<Item> PRIMITIVE_SMELTER = ITEMS.register("primitive_smelter", () -> descriptionItem(YTechBlocks.PRIMITIVE_SMELTER, List.of(Component.translatable("text.ytech.hover.primitive_smelter").withStyle(DARK_GRAY))));
    public static final RegistryObject<Item> REINFORCED_BRICKS = ITEMS.register("reinforced_bricks", () -> blockItem(YTechBlocks.REINFORCED_BRICKS));
    public static final RegistryObject<Item> REINFORCED_BRICK_CHIMNEY = ITEMS.register("reinforced_brick_chimney", () -> descriptionItem(YTechBlocks.REINFORCED_BRICK_CHIMNEY, List.of(Component.translatable("text.ytech.hover.chimney", AbstractPrimitiveMachineBlockEntity.TEMP_PER_CHIMNEY).withStyle(DARK_GRAY))));
    public static final RegistryObject<Item> TERRACOTTA_BRICKS = ITEMS.register("terracotta_bricks", () -> blockItem(YTechBlocks.TERRACOTTA_BRICKS));
    public static final RegistryObject<Item> TERRACOTTA_BRICK_SLAB = ITEMS.register("terracotta_brick_slab", () -> blockItem(YTechBlocks.TERRACOTTA_BRICK_SLAB));
    public static final RegistryObject<Item> TERRACOTTA_BRICK_STAIRS = ITEMS.register("terracotta_brick_stairs", () -> blockItem(YTechBlocks.TERRACOTTA_BRICK_STAIRS));
    public static final RegistryObject<Item> THATCH = ITEMS.register("thatch", () -> blockItem(YTechBlocks.THATCH));
    public static final RegistryObject<Item> THATCH_SLAB = ITEMS.register("thatch_slab", () -> blockItem(YTechBlocks.THATCH_SLAB));
    public static final RegistryObject<Item> THATCH_STAIRS = ITEMS.register("thatch_stairs", () -> blockItem(YTechBlocks.THATCH_STAIRS));

    public static final MaterialItem ARROWS = new MaterialItem("arrow", GroupLocation.SUFFIX, MaterialType.ALL_HARD_METALS, MaterialArrowItem::new);
    public static final MaterialItem AXES = new AxeMaterialItem();
    public static final MaterialItem BOLTS = new MaterialItem("bolt", GroupLocation.SUFFIX, Utils.merge(MaterialType.ALL_METALS, MaterialType.WOODEN), (type) -> simpleItem());
    public static final MaterialItem BOOTS = new BootsMaterialItem();
    public static final MaterialItem CHESTPLATES = new ChestplatesMaterialItem();
    public static final MaterialItem CRUSHED_MATERIALS = new MaterialItem("crushed_material", "crushed", GroupLocation.PREFIX, MaterialType.ALL_ORES, (type) -> simpleItem());
    public static final MaterialItem FILES = new MaterialItem("file", GroupLocation.SUFFIX, MaterialType.ALL_METALS, (type) -> simpleItem());
    public static final MaterialItem HAMMERS = new MaterialItem("hammer", GroupLocation.SUFFIX, Utils.merge(MaterialType.ALL_METALS, MaterialType.STONE), (type) -> simpleItem());
    public static final MaterialItem HELMETS = new HelmetMaterialItem();
    public static final MaterialItem HOES = new HoeMaterialItem();
    public static final MaterialItem INGOTS = new IngotMaterialItem();
    public static final MaterialItem KNIVES = new MaterialItem("knife", GroupLocation.SUFFIX, EnumSet.of(MaterialType.FLINT), YTechItems::knifeItem);
    public static final MaterialItem LEGGINGS = new LeggingsMaterialItem();
    public static final MaterialItem MORTAR_AND_PESTLES = new MaterialItem("mortar_and_pestle", GroupLocation.SUFFIX, Utils.merge(MaterialType.ALL_METALS, MaterialType.STONE), (type) -> toolItem(type.getTier()));
    public static final MaterialItem PICKAXES = new PickaxeMaterialItem();
    public static final MaterialItem PLATES = new MaterialItem("plate", GroupLocation.SUFFIX, Utils.merge(MaterialType.ALL_METALS, MaterialType.WOODEN), (type) -> simpleItem());
    public static final MaterialItem RAW_MATERIALS = new RawMaterialItem();
    public static final MaterialItem RODS = new MaterialItem("rod", GroupLocation.SUFFIX, MaterialType.ALL_METALS, (type) -> simpleItem());
    public static final MaterialItem SAWS = new MaterialItem("saw", GroupLocation.SUFFIX, MaterialType.ALL_METALS, (type) -> simpleItem());
    public static final MaterialItem SHOVELS = new ShovelMaterialItem();
    public static final MaterialItem SPEARS = new MaterialItem("spear", GroupLocation.SUFFIX, Utils.merge(MaterialType.ALL_HARD_METALS, MaterialType.FLINT), (type) -> new SpearItem(SpearType.BY_MATERIAL_TYPE.get(type)));
    public static final MaterialItem SWORDS = new SwordMaterialItem();

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static Collection<RegistryObject<Item>> getRegisteredItems() {
        return ITEMS.getEntries();
    }

    private static Item blockItem(RegistryObject<Block> block) {
        return new BlockItem(block.get(), new Item.Properties());
    }

    private static Item simpleItem() {
        return new Item(new Item.Properties());
    }

    private static Item toolItem(Tier tier) {
        return new ToolItem(tier, new Item.Properties());
    }

    private static Item axeItem(MaterialType material) {
        return new AxeItem(material.getTier(), 6.0f, -3.2f, new Item.Properties());
    }

    private static Item pickaxeItem(MaterialType material) {
        return new PickaxeItem(material.getTier(), 1, -2.8f, new Item.Properties());
    }

    private static Item shovelItem(MaterialType material) {
        return new ShovelItem(material.getTier(), 1.5f, -3.0f, new Item.Properties());
    }

    private static Item helmetItem(MaterialType material) {
        return new ArmorItem(Objects.requireNonNull(material.armorMaterial), ArmorItem.Type.HELMET, new Item.Properties());
    }

    private static Item chestplateItem(MaterialType material) {
        return new ArmorItem(Objects.requireNonNull(material.armorMaterial), ArmorItem.Type.CHESTPLATE, new Item.Properties());
    }

    private static Item leggingsItem(MaterialType material) {
        return new ArmorItem(Objects.requireNonNull(material.armorMaterial), ArmorItem.Type.LEGGINGS, new Item.Properties());
    }

    private static Item bootsItem(MaterialType material) {
        return new ArmorItem(Objects.requireNonNull(material.armorMaterial), ArmorItem.Type.BOOTS, new Item.Properties());
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

    private static Item descriptionItem(@NotNull List<Component> description) {
        return new Item(new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.addAll(description);
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

    public static class MaterialItem {
        protected final String group;
        protected final GroupLocation groupLocation;
        protected final Map<MaterialType, RegistryObject<Item>> items;

        public MaterialItem(String group, GroupLocation groupLocation, EnumSet<MaterialType> materialTypes, Function<MaterialType, Item> itemSupplier) {
            this(group, group, groupLocation, materialTypes, itemSupplier);
        }

        public MaterialItem(String group, String groupShort, GroupLocation groupLocation, EnumSet<MaterialType> materialTypes, Function<MaterialType, Item> itemSupplier) {
            this.group = group;
            this.groupLocation = groupLocation;
            items = new HashMap<>();
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

                items.put(type, ITEMS.register(key, () -> itemSupplier.apply(type)));
            });
        }

        public RegistryObject<Item> of(MaterialType material) {
            return Objects.requireNonNull(items.get(material));
        }

        public Collection<RegistryObject<Item>> items() {
            return items.values();
        }

        public Set<MaterialType> materials() {
            return items.keySet();
        }

        public Set<Map.Entry<MaterialType, RegistryObject<Item>>> entries() {
            return items.entrySet();
        }

        public String getGroup() {
            return group;
        }

        public GroupLocation getGroupLocation() {
            return groupLocation;
        }
    }

    private static class AxeMaterialItem extends MaterialItem {
        public AxeMaterialItem() {
            super("axe", GroupLocation.SUFFIX, Utils.exclude(Utils.merge(MaterialType.ALL_METALS, MaterialType.FLINT), MaterialType.GOLD, MaterialType.IRON), YTechItems::axeItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_AXE), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_AXE), ForgeRegistries.ITEMS));
        }
    }

    private static class BootsMaterialItem extends MaterialItem {
        public BootsMaterialItem() {
            super("boots", GroupLocation.SUFFIX, Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::bootsItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_BOOTS), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_BOOTS), ForgeRegistries.ITEMS));
        }
    }

    private static class ChestplatesMaterialItem extends MaterialItem {
        public ChestplatesMaterialItem() {
            super("chestplate", GroupLocation.SUFFIX, Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::chestplateItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_CHESTPLATE), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_CHESTPLATE), ForgeRegistries.ITEMS));
        }
    }

    private static class HelmetMaterialItem extends MaterialItem {
        public HelmetMaterialItem() {
            super("helmet", GroupLocation.SUFFIX, Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::helmetItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_HELMET), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_HELMET), ForgeRegistries.ITEMS));
        }
    }

    private static class HoeMaterialItem extends MaterialItem {
        public HoeMaterialItem() {
            super("hoe", GroupLocation.SUFFIX, Utils.exclude(MaterialType.ALL_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::hoeItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_HOE), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_HOE), ForgeRegistries.ITEMS));
        }
    }

    private static class IngotMaterialItem extends MaterialItem {
        public IngotMaterialItem() {
            super("ingot", GroupLocation.SUFFIX, Utils.exclude(MaterialType.ALL_METALS, MaterialType.VANILLA_METALS), (type) -> simpleItem());
            items.put(MaterialType.COPPER, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.COPPER_INGOT), ForgeRegistries.ITEMS));
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLD_INGOT), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_INGOT), ForgeRegistries.ITEMS));
        }
    }

    private static class LeggingsMaterialItem extends MaterialItem {
        public LeggingsMaterialItem() {
            super("leggings", GroupLocation.SUFFIX, Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::leggingsItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_LEGGINGS), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_LEGGINGS), ForgeRegistries.ITEMS));
        }
    }

    private static class PickaxeMaterialItem extends MaterialItem {
        public PickaxeMaterialItem() {
            super("pickaxe", GroupLocation.SUFFIX, Utils.exclude(Utils.merge(MaterialType.ALL_METALS, MaterialType.ANTLER), MaterialType.GOLD, MaterialType.IRON), YTechItems::pickaxeItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_PICKAXE), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_PICKAXE), ForgeRegistries.ITEMS));
        }
    }

    private static class RawMaterialItem extends MaterialItem {
        public RawMaterialItem() {
            super("raw_material", "raw", GroupLocation.PREFIX, Utils.exclude(MaterialType.ALL_ORES, MaterialType.VANILLA_METALS), (type) -> simpleItem());
            items.put(MaterialType.COPPER, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.RAW_COPPER), ForgeRegistries.ITEMS));
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.RAW_GOLD), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.RAW_IRON), ForgeRegistries.ITEMS));
        }
    }

    private static class ShovelMaterialItem extends MaterialItem {
        public ShovelMaterialItem() {
            super("shovel", GroupLocation.SUFFIX, Utils.exclude(MaterialType.ALL_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::shovelItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_SHOVEL), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_SHOVEL), ForgeRegistries.ITEMS));
        }
    }

    private static class SwordMaterialItem extends MaterialItem {
        public SwordMaterialItem() {
            super("sword", GroupLocation.SUFFIX, Utils.exclude(MaterialType.ALL_METALS, MaterialType.GOLD, MaterialType.IRON), YTechItems::swordItem);
            items.put(MaterialType.GOLD, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.GOLDEN_SWORD), ForgeRegistries.ITEMS));
            items.put(MaterialType.IRON, RegistryObject.create(ForgeRegistries.ITEMS.getKey(Items.IRON_SWORD), ForgeRegistries.ITEMS));
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
        };
    }
}
