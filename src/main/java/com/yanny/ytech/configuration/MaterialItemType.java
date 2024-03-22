package com.yanny.ytech.configuration;

import com.yanny.ytech.configuration.item.*;
import com.yanny.ytech.configuration.recipe.HammeringRecipe;
import com.yanny.ytech.configuration.recipe.MillingRecipe;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.*;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum MaterialItemType implements INameable, IMaterialModel<Holder.ItemHolder, ItemModelProvider>, IRecipe<Holder.ItemHolder>,
        IItemTag<Holder.ItemHolder> {
    INGOT("ingot", INameable.suffix("ingot"), INameable.suffix("Ingot"),
            (material) -> ItemTags.create(Utils.forgeLoc("ingots/" + material.key)),
            Tags.Items.INGOTS,
            MaterialItemType::simpleItem,
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("ingot/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicItemModelProvider,
            MaterialItemType::registerIngotRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.exclude(MaterialType.ALL_METALS, MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON)),
    RAW_MATERIAL("raw_material", INameable.prefix("raw"), INameable.prefix("Raw"),
            (material) -> ItemTags.create(Utils.forgeLoc("raw_materials/" + material.key)),
            Tags.Items.RAW_MATERIALS,
            MaterialItemType::simpleItem,
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("raw_material/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicItemModelProvider,
            MaterialItemType::registerRawMaterialRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.exclude(MaterialType.ALL_ORES, MaterialType.COPPER, MaterialType.GOLD, MaterialType.IRON)),
    CRUSHED_MATERIAL("crushed_material", INameable.prefix("crushed"), INameable.prefix("Crushed"),
            (material) -> ItemTags.create(Utils.forgeLoc("crushed_materials/" + material.key)),
            ItemTags.create(Utils.modLoc("crushed_materials")),
            MaterialItemType::simpleItem,
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("crushed_material/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicItemModelProvider,
            MaterialItemType::registerCrushedRawMaterialRecipe,
            MaterialItemType::registerMaterialTag,
            MaterialType.ALL_ORES),
    PLATE("plate", INameable.suffix("plate"), INameable.suffix("Plate"),
            (material) -> ItemTags.create(Utils.modLoc("plates/" + material.key)),
            ItemTags.create(Utils.modLoc("plates")),
            MaterialItemType::simpleItem,
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("plate/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicItemModelProvider,
            MaterialItemType::registerPlateRecipe,
            MaterialItemType::registerMaterialTag,
            MaterialType.ALL_METALS),
    ROD("rod", INameable.suffix("rod"), INameable.suffix("Rod"),
            (material) -> ItemTags.create(Utils.modLoc("rods/" + material.key)),
            ItemTags.create(Utils.modLoc("rods")),
            MaterialItemType::simpleItem,
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("rod/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicItemModelProvider,
            MaterialItemType::registerRodRecipe,
            MaterialItemType::registerMaterialTag,
            MaterialType.ALL_METALS),
    BOLT("bolt", INameable.suffix("bolt"), INameable.suffix("Bolt"),
            (material) -> ItemTags.create(Utils.modLoc("bolts/" + material.key)),
            ItemTags.create(Utils.modLoc("bolts")),
            MaterialItemType::simpleItem,
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("bolt/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicItemModelProvider,
            MaterialItemType::registerBoltRecipe,
            MaterialItemType::registerMaterialTag,
            MaterialType.ALL_METALS),

    AXE("axe", INameable.suffix("axe"), INameable.suffix("Axe"),
            (material) -> ItemTags.create(Utils.modLoc("axes/" + material.key)),
            ItemTags.AXES,
            MaterialAxeItem::new,
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("axe/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicHandheldModelProvider,
            MaterialAxeItem::registerRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.exclude(Utils.merge(MaterialType.ALL_METALS, MaterialType.FLINT), MaterialType.GOLD, MaterialType.IRON)),
    PICKAXE("pickaxe", INameable.suffix("pickaxe"), INameable.suffix("Pickaxe"),
            (material) -> ItemTags.create(Utils.modLoc("pickaxes/" + material.key)),
            ItemTags.PICKAXES,
            MaterialPickaxeItem::new,
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("pickaxe/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicHandheldModelProvider,
            MaterialPickaxeItem::registerRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.exclude(Utils.merge(MaterialType.ALL_METALS, MaterialType.ANTLER), MaterialType.GOLD, MaterialType.IRON)),
    SHOVEL("shovel", INameable.suffix("shovel"), INameable.suffix("Shovel"),
            (material) -> ItemTags.create(Utils.modLoc("shovels/" + material.key)),
            ItemTags.SHOVELS,
            MaterialShovelItem::new,
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("shovel/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicHandheldModelProvider,
            MaterialShovelItem::registerRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.exclude(MaterialType.ALL_METALS, MaterialType.GOLD, MaterialType.IRON)),
    HOE("hoe", INameable.suffix("hoe"), INameable.suffix("Hoe"),
            (material) -> ItemTags.create(Utils.modLoc("hoes/" + material.key)),
            ItemTags.HOES,
            MaterialHoeItem::new,
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("hoe/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicHandheldModelProvider,
            MaterialHoeItem::registerRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.exclude(MaterialType.ALL_METALS, MaterialType.GOLD, MaterialType.IRON)),
    SWORD("sword", INameable.suffix("sword"), INameable.suffix("Sword"),
            (material) -> ItemTags.create(Utils.modLoc("swords/" + material.key)),
            ItemTags.SWORDS,
            MaterialSwordItem::new,
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("sword/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicHandheldModelProvider,
            MaterialSwordItem::registerRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.exclude(MaterialType.ALL_METALS, MaterialType.GOLD, MaterialType.IRON)),
    HELMET("helmet", INameable.suffix("helmet"), INameable.suffix("Helmet"),
            (material) -> ItemTags.create(Utils.modLoc("helmets/" + material.key)),
            Tags.Items.ARMORS_HELMETS,
            (holder) -> new ArmorItem(Objects.requireNonNull(holder.material.armorMaterial), ArmorItem.Type.HELMET, new Item.Properties()),
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc(material.key + "_helmet"))).toArray(TextureHolder[]::new),
            MaterialItemType::basicArmorModelProvider,
            MaterialItemType::registerHelmetRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.IRON)),
    CHESTPLATE("chestplate", INameable.suffix("chestplate"), INameable.suffix("Chestplate"),
            (material) -> ItemTags.create(Utils.modLoc("chestplates/" + material.key)),
            Tags.Items.ARMORS_CHESTPLATES,
            (holder) -> new ArmorItem(Objects.requireNonNull(holder.material.armorMaterial), ArmorItem.Type.CHESTPLATE, new Item.Properties()),
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc(material.key + "_chestplate"))).toArray(TextureHolder[]::new),
            MaterialItemType::basicArmorModelProvider,
            MaterialItemType::registerChestplateRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.IRON)),
    LEGGINGS("leggings", INameable.suffix("leggings"), INameable.suffix("Leggings"),
            (material) -> ItemTags.create(Utils.modLoc("leggings/" + material.key)),
            Tags.Items.ARMORS_LEGGINGS,
            (holder) -> new ArmorItem(Objects.requireNonNull(holder.material.armorMaterial), ArmorItem.Type.LEGGINGS, new Item.Properties()),
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc(material.key + "_leggings"))).toArray(TextureHolder[]::new),
            MaterialItemType::basicArmorModelProvider,
            MaterialItemType::registerLeggingsRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.IRON)),
    BOOTS("boots", INameable.suffix("boots"), INameable.suffix("Boots"),
            (material) -> ItemTags.create(Utils.modLoc("boots/" + material.key)),
            Tags.Items.ARMORS_BOOTS,
            (holder) -> new ArmorItem(Objects.requireNonNull(holder.material.armorMaterial), ArmorItem.Type.BOOTS, new Item.Properties()),
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc(material.key + "_boots"))).toArray(TextureHolder[]::new),
            MaterialItemType::basicArmorModelProvider,
            MaterialItemType::registerBootsRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.exclude(MaterialType.ALL_HARD_METALS, MaterialType.IRON)),
    ARROW("arrow", INameable.suffix("arrow"), INameable.suffix("Arrow"),
            (material) -> ItemTags.create(Utils.modLoc("arrows/" + material.key)),
            ItemTags.ARROWS,
            MaterialArrowItem::new,
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("arrow/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicItemModelProvider,
            MaterialArrowItem::registerRecipe,
            MaterialItemType::registerMaterialTag,
            MaterialType.ALL_HARD_METALS),
    MORTAR_AND_PESTLE("mortar_and_pestle", INameable.suffix("mortar_and_pestle"), INameable.suffix("Mortar and Pestle"),
            (material) -> ItemTags.create(Utils.modLoc("mortar_and_pestles/" + material.key)),
            ItemTags.create(Utils.modLoc("mortar_and_pestles")),
            (holder) -> new ToolItem(holder.material.getTier(), new Item.Properties()),
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("mortar_and_pestle/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicItemModelProvider,
            MaterialItemType::registerMortarAndPestleRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.merge(MaterialType.ALL_METALS, MaterialType.STONE)),
    SAW("saw", INameable.suffix("saw"), INameable.suffix("Saw"),
            (material) -> ItemTags.create(Utils.modLoc("saws/" + material.key)),
            ItemTags.create(Utils.modLoc("saws")),
            (holder) -> new ToolItem(holder.material.getTier(), new Item.Properties()),
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("saw/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicItemModelProvider,
            MaterialItemType::registerSawRecipe,
            MaterialItemType::registerMaterialTag,
            MaterialType.ALL_METALS),
    HAMMER("hammer", INameable.suffix("hammer"), INameable.suffix("Hammer"),
            (material) -> ItemTags.create(Utils.modLoc("hammers/" + material.key)),
            ItemTags.create(Utils.modLoc("hammers")),
            (holder) -> new ToolItem(holder.material.getTier(), new Item.Properties()),
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("hammer/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicHandheldModelProvider,
            MaterialItemType::registerHammerRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.merge(MaterialType.ALL_METALS, MaterialType.STONE)),
    FILE("file", INameable.suffix("file"), INameable.suffix("File"),
            (material) -> ItemTags.create(Utils.modLoc("files/" + material.key)),
            ItemTags.create(Utils.modLoc("files")),
            (holder) -> new ToolItem(holder.material.getTier(), new Item.Properties()),
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("file/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicItemModelProvider,
            MaterialItemType::registerFileRecipe,
            MaterialItemType::registerMaterialTag,
            MaterialType.ALL_METALS),
    ;

    // inject vanilla item tags
    static {
        INGOT.itemTag.put(MaterialType.COPPER, Tags.Items.INGOTS_COPPER);
        INGOT.itemTag.put(MaterialType.GOLD, Tags.Items.INGOTS_GOLD);
        INGOT.itemTag.put(MaterialType.IRON, Tags.Items.INGOTS_IRON);

        RAW_MATERIAL.itemTag.put(MaterialType.COPPER, Tags.Items.RAW_MATERIALS_COPPER);
        RAW_MATERIAL.itemTag.put(MaterialType.GOLD, Tags.Items.RAW_MATERIALS_GOLD);
        RAW_MATERIAL.itemTag.put(MaterialType.IRON, Tags.Items.RAW_MATERIALS_IRON);
    }

    @NotNull public final String id;
    @NotNull private final NameHolder key;
    @NotNull private final NameHolder name;
    @NotNull public final Map<MaterialType, TagKey<Item>> itemTag;
    @NotNull public final TagKey<Item> groupTag;
    @NotNull private final Function<Holder.ItemHolder, Item> itemGetter;
    @NotNull private final Map<MaterialType, Map<Integer, Integer>> tintColors;
    @NotNull private final Map<MaterialType, ResourceLocation[]> textures;
    @NotNull private final BiConsumer<Holder.ItemHolder, ItemModelProvider> modelGetter;
    @NotNull private final BiConsumer<Holder.ItemHolder, Consumer<FinishedRecipe>> recipeGetter;
    @NotNull private final BiConsumer<Holder.ItemHolder, ItemTagsProvider> itemTagsGetter;
    @NotNull public final EnumSet<MaterialType> materials;

    MaterialItemType(@NotNull String id, @NotNull NameHolder key, @NotNull NameHolder name, @NotNull Function<MaterialType, TagKey<Item>> itemTag, @NotNull TagKey<Item> groupTag,
                     @NotNull Function<Holder.ItemHolder, Item> itemGetter, @NotNull Function<MaterialType, TextureHolder[]> textureGetter,
                     @NotNull BiConsumer<Holder.ItemHolder, ItemModelProvider> modelGetter, @NotNull BiConsumer<Holder.ItemHolder, Consumer<FinishedRecipe>> recipeGetter,
                     @NotNull BiConsumer<Holder.ItemHolder, ItemTagsProvider> itemTagsGetter, @NotNull EnumSet<MaterialType> materials) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.itemTag = materials.stream().map((material) -> Pair.of(material, itemTag.apply(material))).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        this.groupTag = groupTag;
        this.itemGetter = itemGetter;
        this.recipeGetter = recipeGetter;
        this.itemTagsGetter = itemTagsGetter;
        this.tintColors = new HashMap<>();
        this.textures = new HashMap<>();
        this.modelGetter = modelGetter;
        this.materials = materials;

        for (MaterialType material : materials) {
            TextureHolder[] holders = textureGetter.apply(material);
            ArrayList<ResourceLocation> resources = new ArrayList<>();
            Map<Integer, Integer> tintMap = new HashMap<>();

            for (TextureHolder holder : holders) {
                if (holder.tintIndex() >= 0) {
                    tintMap.put(holder.tintIndex(), holder.color());
                }

                resources.add(holder.texture());
            }

            this.tintColors.put(material, tintMap);
            this.textures.computeIfAbsent(material, (m) -> resources.toArray(ResourceLocation[]::new));
        }
    }

    @NotNull
    @Override
    public NameHolder getKeyHolder() {
        return key;
    }

    @NotNull
    @Override
    public NameHolder getNameHolder() {
        return name;
    }

    @NotNull
    @Override
    public Map<Integer, Integer> getTintColors(@NotNull MaterialType material) {
        return tintColors.get(material);
    }

    @NotNull
    @Override
    public ResourceLocation[] getTextures(@NotNull MaterialType material) {
        return textures.get(material);
    }

    @Override
    public void registerModel(@NotNull Holder.ItemHolder holder, @NotNull ItemModelProvider provider) {
        modelGetter.accept(holder, provider);
    }

    @Override
    public void registerRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        recipeGetter.accept(holder, recipeConsumer);
    }

    @Override
    public void registerTag(@NotNull Holder.ItemHolder holder, @NotNull ItemTagsProvider provider) {
        itemTagsGetter.accept(holder, provider);
    }

    public Item getItem(@NotNull Holder.ItemHolder holder) {
        return itemGetter.apply(holder);
    }

    private static Item simpleItem(@NotNull Holder.ItemHolder holder) {
        return new Item(new Item.Properties());
    }

    public static void registerIngotRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, holder.item.get(), 9)
                .requires(MaterialBlockType.STORAGE_BLOCK.itemTag.get(holder.material))
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(MaterialBlockType.STORAGE_BLOCK.itemTag.get(holder.material)))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    public static void registerRawMaterialRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, holder.item.get(), 9)
                .requires(MaterialBlockType.RAW_STORAGE_BLOCK.itemTag.get(holder.material))
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(MaterialBlockType.RAW_STORAGE_BLOCK.itemTag.get(holder.material)))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    public static void registerCrushedRawMaterialRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, holder.item.get(), 1)
                .requires(RAW_MATERIAL.itemTag.get(holder.material))
                .requires(MORTAR_AND_PESTLE.groupTag)
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(RAW_MATERIAL.itemTag.get(holder.material)))
                .save(recipeConsumer, Utils.modLoc(holder.key));
        MillingRecipe.Builder.milling(RAW_MATERIAL.itemTag.get(holder.material), holder.item.get())
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(RAW_MATERIAL.itemTag.get(holder.material)))
                .save(recipeConsumer, Utils.modLoc(holder.key + "_from_milling"));
    }

    public static void registerPlateRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, holder.item.get())
                .define('#', INGOT.itemTag.get(holder.material))
                .define('H', HAMMER.groupTag)
                .pattern("#")
                .pattern("#")
                .pattern("H")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(INGOT.itemTag.get(holder.material)))
                .save(recipeConsumer, Utils.modLoc(holder.key));
        HammeringRecipe.Builder.hammering(INGOT.itemTag.get(holder.material), holder.item.get())
                .tool(Ingredient.of(HAMMER.groupTag))
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(INGOT.itemTag.get(holder.material)))
                .save(recipeConsumer, Utils.modLoc(holder.key + "_from_hammering"));
    }

    public static void registerRodRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, holder.item.get())
                .define('#', INGOT.itemTag.get(holder.material))
                .define('F', FILE.groupTag)
                .pattern("#")
                .pattern("F")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(INGOT.itemTag.get(holder.material)))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    public static void registerBoltRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, holder.item.get(), 2)
                .define('#', ROD.itemTag.get(holder.material))
                .define('S', SAW.groupTag)
                .pattern("# ")
                .pattern(" S")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(ROD.itemTag.get(holder.material)))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    public static void registerHelmetRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, holder.item.get(), 1)
                .define('#', PLATE.itemTag.get(holder.material))
                .pattern("###")
                .pattern("# #")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(PLATE.itemTag.get(holder.material)))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    public static void registerChestplateRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, holder.item.get(), 1)
                .define('#', PLATE.itemTag.get(holder.material))
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(PLATE.itemTag.get(holder.material)))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    public static void registerLeggingsRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, holder.item.get(), 1)
                .define('#', PLATE.itemTag.get(holder.material))
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(PLATE.itemTag.get(holder.material)))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    public static void registerBootsRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, holder.item.get(), 1)
                .define('#', PLATE.itemTag.get(holder.material))
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(PLATE.itemTag.get(holder.material)))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    public static void registerMortarAndPestleRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        if (holder.material == MaterialType.STONE) {
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, holder.item.get())
                    .define('I', Items.STICK)
                    .define('#', Tags.Items.STONE)
                    .pattern(" I ")
                    .pattern("# #")
                    .pattern(" # ")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(Tags.Items.STONE))
                    .save(recipeConsumer, Utils.modLoc(holder.key));
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, holder.item.get())
                    .define('I', INGOT.itemTag.get(holder.material))
                    .define('#', Tags.Items.STONE)
                    .pattern(" I ")
                    .pattern("# #")
                    .pattern(" # ")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(INGOT.itemTag.get(holder.material)))
                    .save(recipeConsumer, Utils.modLoc(holder.key));
        }
    }

    public static void registerSawRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, holder.item.get())
                .define('S', Items.STICK)
                .define('#', PLATE.itemTag.get(holder.material))
                .pattern("S##")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(PLATE.itemTag.get(holder.material)))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    public static void registerHammerRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        if (holder.material == MaterialType.STONE) {
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, holder.item.get())
                    .define('S', Items.STICK)
                    .define('T', SimpleItemType.LEATHER_STRIPS.itemTag)
                    .define('#', Items.STONE)
                    .pattern(" #T")
                    .pattern(" S#")
                    .pattern("S  ")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(SimpleItemType.LEATHER_STRIPS.itemTag))
                    .save(recipeConsumer, Utils.modLoc(holder.key));
        } else {
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, holder.item.get())
                    .define('S', Items.STICK)
                    .define('#', MaterialBlockType.STORAGE_BLOCK.itemTag.get(holder.material))
                    .pattern(" # ")
                    .pattern(" S#")
                    .pattern("S  ")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(MaterialBlockType.STORAGE_BLOCK.itemTag.get(holder.material)))
                    .save(recipeConsumer, Utils.modLoc(holder.key));
        }
    }

    public static void registerFileRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, holder.item.get())
                .define('#', PLATE.itemTag.get(holder.material))
                .define('S', Items.STICK)
                .pattern("#")
                .pattern("S")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(PLATE.itemTag.get(holder.material)))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void basicItemModelProvider(@NotNull Holder.ItemHolder holder, @NotNull ItemModelProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures(holder.material);
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", textures[0]);
    }

    private static void basicHandheldModelProvider(@NotNull Holder.ItemHolder holder, @NotNull ItemModelProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures(holder.material);
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/handheld"));
        builder.texture("layer0", textures[0]);
    }

    private static void basicArmorModelProvider(@NotNull Holder.ItemHolder holder, @NotNull ItemModelProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures(holder.material);
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", textures[0]);
    }

    private static void registerMaterialTag(@NotNull Holder.ItemHolder holder, @NotNull ItemTagsProvider provider) {
        provider.tag(holder.object.itemTag.get(holder.material)).add(holder.item.get());
        provider.tag(holder.object.groupTag).addTag(holder.object.itemTag.get(holder.material));
    }
}
