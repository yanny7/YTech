package com.yanny.ytech.configuration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.item.*;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.*;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
    DUST("dust", INameable.suffix("dust"), INameable.suffix("Dust"),
            (material) -> ItemTags.create(Utils.forgeLoc("dusts/" + material.key)),
            Tags.Items.DUSTS,
            MaterialItemType::simpleItem,
            (material) -> basicTexture(Utils.modItemLoc("dust"), material),
            MaterialItemType::basicItemModelProvider,
            IRecipe::noRecipe,
            MaterialItemType::registerMaterialTag,
            EnumSet.noneOf(MaterialType.class)),
    IMPURE_DUST("impure_dust", INameable.both("impure", "dust"), INameable.both("Impure", "Dust"),
            (material) -> ItemTags.create(Utils.forgeLoc("impure_dusts/" + material.key)),
            Tags.Items.DUSTS,
            MaterialItemType::simpleItem,
            (material) -> basicTexture(Utils.modItemLoc("impure_dust"), material),
            MaterialItemType::basicItemModelProvider,
            IRecipe::noRecipe,
            MaterialItemType::registerMaterialTag,
            EnumSet.noneOf(MaterialType.class)),
    PURIFIED_DUST("purified_dust", INameable.both("purified", "dust"), INameable.both("Purified", "Dust"),
            (material) -> ItemTags.create(Utils.forgeLoc("purified_dusts/" + material.key)),
            Tags.Items.DUSTS,
            MaterialItemType::simpleItem,
            (material) -> basicTexture(Utils.modItemLoc("purified_dust"), material),
            MaterialItemType::basicItemModelProvider,
            IRecipe::noRecipe,
            MaterialItemType::registerMaterialTag,
            EnumSet.noneOf(MaterialType.class)),
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
    IMPURE_MATERIAL("impure_material", INameable.prefix("impure"), INameable.prefix("Impure"),
            (material) -> ItemTags.create(Utils.forgeLoc("impure_materials/" + material.key)),
            ItemTags.create(Utils.modLoc("impure_materials")),
            MaterialItemType::simpleItem,
            (material) -> basicTexture(Utils.modItemLoc("impure_material"), material),
            MaterialItemType::basicItemModelProvider,
            IRecipe::noRecipe,
            MaterialItemType::registerMaterialTag,
            EnumSet.noneOf(MaterialType.class)),
    PURIFIED_MATERIAL("purified_material", INameable.prefix("purified"), INameable.prefix("Purified"),
            (material) -> ItemTags.create(Utils.forgeLoc("purified_materials/" + material.key)),
            ItemTags.create(Utils.modLoc("purified_materials")),
            MaterialItemType::simpleItem,
            (material) -> basicTexture(Utils.modItemLoc("purified_material"), material),
            MaterialItemType::basicItemModelProvider,
            IRecipe::noRecipe,
            MaterialItemType::registerMaterialTag,
            EnumSet.noneOf(MaterialType.class)),
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
            (holder) -> new MaterialAxeItem(holder.material.getTier()),
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("axe/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicItemModelProvider,
            MaterialAxeItem::registerRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.exclude(Utils.merge(MaterialType.ALL_METALS, MaterialType.FLINT), MaterialType.GOLD, MaterialType.IRON)),
    PICKAXE("pickaxe", INameable.suffix("pickaxe"), INameable.suffix("Pickaxe"),
            (material) -> ItemTags.create(Utils.modLoc("pickaxes/" + material.key)),
            ItemTags.PICKAXES,
            (holder) -> new MaterialPickaxeItem(holder.material.getTier()),
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("pickaxe/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicItemModelProvider,
            MaterialPickaxeItem::registerRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.exclude(Utils.merge(MaterialType.ALL_METALS, MaterialType.FLINT), MaterialType.GOLD, MaterialType.IRON)),
    SHOVEL("shovel", INameable.suffix("shovel"), INameable.suffix("Shovel"),
            (material) -> ItemTags.create(Utils.modLoc("shovels/" + material.key)),
            ItemTags.SHOVELS,
            (holder) -> new MaterialShovelItem(holder.material.getTier()),
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("shovel/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicItemModelProvider,
            MaterialShovelItem::registerRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.exclude(Utils.merge(MaterialType.ALL_METALS, MaterialType.FLINT), MaterialType.GOLD, MaterialType.IRON)),
    HOE("hoe", INameable.suffix("hoe"), INameable.suffix("Hoe"),
            (material) -> ItemTags.create(Utils.modLoc("hoes/" + material.key)),
            ItemTags.HOES,
            (holder) -> new MaterialHoeItem(holder.material.getTier()),
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("hoe/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicItemModelProvider,
            MaterialHoeItem::registerRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.exclude(Utils.merge(MaterialType.ALL_METALS, MaterialType.FLINT), MaterialType.GOLD, MaterialType.IRON)),
    SWORD("sword", INameable.suffix("sword"), INameable.suffix("Sword"),
            (material) -> ItemTags.create(Utils.modLoc("swords/" + material.key)),
            ItemTags.SWORDS,
            (holder) -> new MaterialSwordItem(holder.material.getTier()),
            (material) -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("sword/" + material.key))).toArray(TextureHolder[]::new),
            MaterialItemType::basicItemModelProvider,
            MaterialSwordItem::registerRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.exclude(Utils.merge(MaterialType.ALL_METALS, MaterialType.FLINT), MaterialType.GOLD, MaterialType.IRON)),
    MORTAR_AND_PESTLE("mortar_and_pestle", INameable.suffix("mortar_and_pestle"), INameable.suffix("Mortar and Pestle"),
            (material) -> ItemTags.create(Utils.modLoc("mortar_and_pestles/" + material.key)),
            ItemTags.create(Utils.modLoc("mortar_and_pestles")),
            (holder) -> new ToolItem(holder.material.getTier(), new Item.Properties()),
            (material) -> basicTexture(Utils.modItemLoc("mortar_and_pestle"), material),
            MaterialItemType::basicItemModelProvider,
            MaterialItemType::registerMortarAndPestleRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.merge(MaterialType.ALL_METALS, MaterialType.STONE)),
    SAW("saw", INameable.suffix("saw"), INameable.suffix("Saw"),
            (material) -> ItemTags.create(Utils.modLoc("saws/" + material.key)),
            ItemTags.create(Utils.modLoc("saws")),
            (holder) -> new ToolItem(holder.material.getTier(), new Item.Properties()),
            (material) -> toolTexture(Utils.modItemLoc("hand_saw_handle"), Utils.modItemLoc("hand_saw_overlay"), material),
            MaterialItemType::toolItemModelProvider,
            MaterialItemType::registerSawRecipe,
            MaterialItemType::registerMaterialTag,
            MaterialType.ALL_METALS),
    HAMMER("hammer", INameable.suffix("hammer"), INameable.suffix("Hammer"),
            (material) -> ItemTags.create(Utils.modLoc("hammers/" + material.key)),
            ItemTags.create(Utils.modLoc("hammers")),
            (holder) -> new ToolItem(holder.material.getTier(), new Item.Properties()),
            (material) -> toolTexture(Utils.modItemLoc("hammer_handle"), Utils.modItemLoc("hammer_overlay"), material),
            MaterialItemType::toolItemModelProvider,
            MaterialItemType::registerHammerRecipe,
            MaterialItemType::registerMaterialTag,
            Utils.merge(MaterialType.ALL_METALS, MaterialType.STONE)),
    FILE("file", INameable.suffix("file"), INameable.suffix("File"),
            (material) -> ItemTags.create(Utils.modLoc("files/" + material.key)),
            ItemTags.create(Utils.modLoc("files")),
            (holder) -> new ToolItem(holder.material.getTier(), new Item.Properties()),
            (material) -> toolTexture(Utils.modItemLoc("file_handle"), Utils.modItemLoc("file_overlay"), material),
            MaterialItemType::toolItemModelProvider,
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
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, holder.item.get(), 1)
                .requires(MaterialBlockType.STORAGE_BLOCK.itemTag.get(holder.material))
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(MaterialBlockType.STORAGE_BLOCK.itemTag.get(holder.material)))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    public static void registerRawMaterialRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, holder.item.get(), 1)
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

    public static void registerMortarAndPestleRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        switch (holder.material) {
            case STONE -> ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, holder.item.get())
                    .define('I', Items.STICK)
                    .define('#', Tags.Items.STONE)
                    .pattern(" I ")
                    .pattern("# #")
                    .pattern(" # ")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(Tags.Items.STONE))
                    .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, holder.key));
            default -> ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, holder.item.get())
                    .define('I', INGOT.itemTag.get(holder.material))
                    .define('#', PLATE.itemTag.get(holder.material))
                    .pattern(" I ")
                    .pattern("# #")
                    .pattern(" # ")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(INGOT.itemTag.get(holder.material)))
                    .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, holder.key));
        }
    }

    public static void registerSawRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, holder.item.get())
                .define('S', Items.STICK)
                .define('#', PLATE.itemTag.get(holder.material))
                .pattern("S##")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(PLATE.itemTag.get(holder.material)))
                .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, holder.key));
    }

    public static void registerHammerRecipe(@NotNull Holder.ItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        switch (holder.material) {
            case STONE -> ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, holder.item.get())
                    .define('S', Items.STICK)
                    .define('T', SimpleItemType.LEATHER_STRIPS.itemTag)
                    .define('#', Items.STONE)
                    .pattern(" #T")
                    .pattern(" S#")
                    .pattern("S  ")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(SimpleItemType.LEATHER_STRIPS.itemTag))
                    .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, holder.key));
            default -> ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, holder.item.get())
                    .define('S', Items.STICK)
                    .define('#', MaterialBlockType.STORAGE_BLOCK.itemTag.get(holder.material))
                    .pattern(" # ")
                    .pattern(" S#")
                    .pattern("S  ")
                    .unlockedBy(Utils.getHasName(), RecipeProvider.has(MaterialBlockType.STORAGE_BLOCK.itemTag.get(holder.material)))
                    .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, holder.key));
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

    private static void toolItemModelProvider(@NotNull Holder.ItemHolder holder, @NotNull ItemModelProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures(holder.material);
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", textures[0]);
        builder.texture("layer1", textures[1]);
    }

    @NotNull
    private static TextureHolder[] basicTexture(@NotNull ResourceLocation overlay, MaterialType material) {
        return List.of(new TextureHolder(0, material.color, overlay)).toArray(TextureHolder[]::new);
    }

    @NotNull
    private static TextureHolder[] toolTexture(@NotNull ResourceLocation base, @NotNull ResourceLocation overlay, MaterialType material) {
        return List.of(new TextureHolder(-1, -1, base), new TextureHolder(1, material.color, overlay)).toArray(TextureHolder[]::new);
    }

    private static void registerMaterialTag(@NotNull Holder.ItemHolder holder, @NotNull ItemTagsProvider provider) {
        provider.tag(holder.object.itemTag.get(holder.material)).add(holder.item.get());
        provider.tag(holder.object.groupTag).addTag(holder.object.itemTag.get(holder.material));
    }
}
