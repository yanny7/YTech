package com.yanny.ytech.configuration;

import com.yanny.ytech.configuration.item.*;
import com.yanny.ytech.configuration.recipe.*;
import com.yanny.ytech.registration.Holder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.yanny.ytech.configuration.MaterialItemType.*;
import static com.yanny.ytech.registration.Registration.HOLDER;
import static com.yanny.ytech.registration.Registration.item;

public enum SimpleItemType implements ISimpleModel<Holder.SimpleItemHolder, ItemModelProvider>, IRecipe<Holder.SimpleItemHolder>, IItemTag<Holder.SimpleItemHolder> {
    GRASS_FIBERS("grass_fibers", "Grass Fibers",
            ItemTags.create(Utils.modLoc("grass_fibers")),
            SimpleItemType::grassFibersItem,
            () -> basicTexture(Utils.modItemLoc("grass_fibers")),
            SimpleItemType::basicItemModelProvider,
            IRecipe::noRecipe,
            SimpleItemType::registerSimpleTag),
    GRASS_TWINE("grass_twine", "Grass Twine",
            ItemTags.create(Utils.modLoc("grass_twines")),
            SimpleItemType::simpleItem,
            () -> basicTexture(Utils.modItemLoc("grass_twine")),
            SimpleItemType::basicItemModelProvider,
            SimpleItemType::registerGrassTwineRecipe,
            SimpleItemType::registerSimpleTag),
    BRICK_MOLD("brick_mold", "Brick Mold",
            ItemTags.create(Utils.modLoc("brick_molds")),
            () -> new Item(new Item.Properties().durability(256)),
            () -> basicTexture(Utils.modItemLoc("brick_mold")),
            SimpleItemType::basicItemModelProvider,
            SimpleItemType::registerBrickMoldRecipe,
            SimpleItemType::registerSimpleTag),
    UNFIRED_BRICK("unfired_brick", "Unfired Brick",
            ItemTags.create(Utils.modLoc("unfired_bricks")),
            SimpleItemType::simpleItem,
            () -> basicTexture(Utils.modItemLoc("unfired_brick")),
            SimpleItemType::basicItemModelProvider,
            SimpleItemType::registerUnfiredBrickRecipe,
            SimpleItemType::registerSimpleTag),
    WOODEN_PLATE("wooden_plate", "Wooden Plate",
            ItemTags.create(Utils.modLoc("plates/wooden")),
            SimpleItemType::simpleItem,
            () -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("plate/wooden"))).toArray(TextureHolder[]::new),
            SimpleItemType::basicItemModelProvider,
            SimpleItemType::registerWoodenPlateRecipe,
            (holder, provider) -> {
                provider.tag(holder.object.itemTag).add(holder.item.get());
                provider.tag(PLATE.groupTag).addTag(holder.object.itemTag);
            }),
    WOODEN_BOLT("wooden_bolt", "Wooden Bolt",
            ItemTags.create(Utils.modLoc("bolts/wooden")),
            SimpleItemType::simpleItem,
            () -> List.of(new TextureHolder(-1, -1, Utils.modItemLoc("bolt/wooden"))).toArray(TextureHolder[]::new),
            SimpleItemType::basicItemModelProvider,
            SimpleItemType::registerWoodenBoltRecipe,
            (holder, provider) -> {
                provider.tag(holder.object.itemTag).add(holder.item.get());
                provider.tag(BOLT.groupTag).addTag(holder.object.itemTag);
            }),
    RAW_HIDE("raw_hide", "Raw Hide",
            ItemTags.create(Utils.modLoc("raw_hides")),
            SimpleItemType::simpleItem,
            () -> basicTexture(Utils.modItemLoc("raw_hide")),
            SimpleItemType::basicItemModelProvider,
            SimpleItemType::registerRawHideRecipe,
            SimpleItemType::registerSimpleTag),
    LEATHER_STRIPS("leather_strips", "Leather strips",
            ItemTags.create(Utils.modLoc("leather_strips")),
            SimpleItemType::simpleItem,
            () -> basicTexture(Utils.modItemLoc("leather_strips")),
            SimpleItemType::basicItemModelProvider,
            SimpleItemType::registerLeatherStripsRecipe,
            SimpleItemType::registerSimpleTag),
    IRON_BLOOM("iron_bloom", "Iron Bloom",
            ItemTags.create(Utils.modLoc("iron_blooms")),
            SimpleItemType::simpleItem,
            () -> basicTexture(Utils.modItemLoc("iron_bloom")),
            SimpleItemType::basicItemModelProvider,
            SimpleItemType::registerIronBloomRecipe,
            SimpleItemType::registerSimpleTag),
    BASKET("basket", "Basket",
            ItemTags.create(Utils.modLoc("baskets")),
            BasketItem::new,
            SimpleItemType::basketTexture,
            SimpleItemType::basketItemModelProvider,
            SimpleItemType::registerBasketRecipe,
            SimpleItemType::registerSimpleTag),
    DRIED_BEEF("dried_beef", "Dried Beef",
            ItemTags.create(Utils.modLoc("dried_beefs")),
            () -> simpleFood(6, 0.7f),
            () -> basicTexture(Utils.modItemLoc("dried_beef")),
            SimpleItemType::basicItemModelProvider,
            (holder, recipeConsumer) -> registerDryingRecipe(holder, recipeConsumer, Items.BEEF),
            SimpleItemType::registerSimpleTag),
    DRIED_CHICKEN("dried_chicken", "Dried Chicken",
            ItemTags.create(Utils.modLoc("dried_chickens")),
            () -> simpleFood(4, 0.5f),
            () -> basicTexture(Utils.modItemLoc("dried_chicken")),
            SimpleItemType::basicItemModelProvider,
            (holder, recipeConsumer) -> registerDryingRecipe(holder, recipeConsumer, Items.CHICKEN),
            SimpleItemType::registerSimpleTag),
    DRIED_COD("dried_cod", "Dried Cod",
            ItemTags.create(Utils.modLoc("dried_cods")),
            () -> simpleFood(4, 0.5f),
            () -> basicTexture(Utils.modItemLoc("dried_cod")),
            SimpleItemType::basicItemModelProvider,
            (holder, recipeConsumer) -> registerDryingRecipe(holder, recipeConsumer, Items.COD),
            SimpleItemType::registerSimpleTag),
    DRIED_MUTTON("dried_mutton", "Dried Mutton",
            ItemTags.create(Utils.modLoc("dried_muttons")),
            () -> simpleFood(4, 0.5f),
            () -> basicTexture(Utils.modItemLoc("dried_mutton")),
            SimpleItemType::basicItemModelProvider,
            (holder, recipeConsumer) -> registerDryingRecipe(holder, recipeConsumer, Items.MUTTON),
            SimpleItemType::registerSimpleTag),
    DRIED_PORKCHOP("dried_porkchop", "Dried Porkchop",
            ItemTags.create(Utils.modLoc("dried_porkchops")),
            () -> simpleFood(6, 0.7f),
            () -> basicTexture(Utils.modItemLoc("dried_porkchop")),
            SimpleItemType::basicItemModelProvider,
            (holder, recipeConsumer) -> registerDryingRecipe(holder, recipeConsumer, Items.PORKCHOP),
            SimpleItemType::registerSimpleTag),
    DRIED_RABBIT("dried_rabbit", "Dried Rabbit",
            ItemTags.create(Utils.modLoc("dried_rabbits")),
            () -> simpleFood(4, 0.5f),
            () -> basicTexture(Utils.modItemLoc("dried_rabbit")),
            SimpleItemType::basicItemModelProvider,
            (holder, recipeConsumer) -> registerDryingRecipe(holder, recipeConsumer, Items.RABBIT),
            SimpleItemType::registerSimpleTag),
    DRIED_SALMON("dried_salmon", "Dried Salmon",
            ItemTags.create(Utils.modLoc("dried_salmons")),
            () -> simpleFood(4, 0.5f),
            () -> basicTexture(Utils.modItemLoc("dried_salmon")),
            SimpleItemType::basicItemModelProvider,
            (holder, recipeConsumer) -> registerDryingRecipe(holder, recipeConsumer, Items.SALMON),
            SimpleItemType::registerSimpleTag),
    DRIED_VENISON("dried_venison", "Dried Venison",
            ItemTags.create(Utils.modLoc("dried_venison")),
            () -> simpleFood(5, 0.7f),
            () -> basicTexture(Utils.modItemLoc("dried_venison")),
            SimpleItemType::basicItemModelProvider,
            SimpleItemType::registerDriedVenisonRecipe,
            SimpleItemType::registerSimpleTag),
    COOKED_VENISON("cooked_venison", "Cooked Venison",
            ItemTags.create(Utils.modLoc("cooked_venison")),
            () -> simpleFood(7, 0.8f),
            () -> basicTexture(Utils.modItemLoc("cooked_venison")),
            SimpleItemType::basicItemModelProvider,
            SimpleItemType::registerCookedVenisonRecipe,
            SimpleItemType::registerSimpleTag),
    VENISON("venison", "Venison",
            ItemTags.create(Utils.modLoc("venison")),
            () -> simpleFood(2, 0.3f),
            () -> basicTexture(Utils.modItemLoc("venison")),
            SimpleItemType::basicItemModelProvider,
            IRecipe::noRecipe, // drops from Deer
            SimpleItemType::registerSimpleTag),
    ANTLER("antler", "Antler",
            ItemTags.create(Utils.modLoc("antlers")),
            SimpleItemType::simpleItem,
            () -> basicTexture(Utils.modItemLoc("antler")),
            SimpleItemType::basicItemModelProvider,
            IRecipe::noRecipe, // drops from Deer
            SimpleItemType::registerSimpleTag),
    SHARP_FLINT("sharp_flint", "Sharp Flint",
            ItemTags.create(Utils.modLoc("sharp_flints")),
            () -> new ToolItem(Tiers.WOOD, new Item.Properties()),
            () -> basicTexture(Utils.modItemLoc("sharp_flint")),
            SimpleItemType::basicItemModelProvider,
            IRecipe::noRecipe, // hit flint on stone
            SimpleItemType::registerSimpleTag),
    FLINT_KNIFE("flint_knife", "Flint Knife",
            ItemTags.create(Utils.modLoc("knives/flint")),
            () -> new SwordItem(Tiers.WOOD, 1, -1.0f, new Item.Properties()),
            () -> basicTexture(Utils.modItemLoc("knife/flint")),
            SimpleItemType::basicItemModelProvider,
            SimpleItemType::registerFlintKnifeRecipe,
            SimpleItemType::registerSimpleTag),
    SPEAR("spear", "Spear",
            ItemTags.create(Utils.modLoc("spears")),
            () -> new SpearItem(new Item.Properties().durability(20)),
            () -> basicTexture(Utils.modItemLoc("spear")),
            SimpleItemType::spearItemModelProvider,
            SimpleItemType::registerSpearRecipe,
            SimpleItemType::registerSimpleTag),
    FLOUR("flour", "Flour",
            ItemTags.create(Utils.modLoc("flour")),
            SimpleItemType::simpleItem,
            () -> basicTexture(Utils.modItemLoc("flour")),
            SimpleItemType::basicItemModelProvider,
            SimpleItemType::registerFlourRecipe,
            SimpleItemType::registerSimpleTag),
    BREAD_DOUGH("bread_dough", "Bread Dough",
            ItemTags.create(Utils.modLoc("bread_dough")),
            SimpleItemType::simpleItem,
            () -> basicTexture(Utils.modItemLoc("bread_dough")),
            SimpleItemType::basicItemModelProvider,
            SimpleItemType::registerBreadDoughRecipe,
            SimpleItemType::registerSimpleTag),
    UNFIRED_CLAY_BUCKET("unfired_clay_bucket", "Unfired Clay Bucket",
            ItemTags.create(Utils.modLoc("unfired_clay_bucket")),
            SimpleItemType::simpleItem,
            () -> basicTexture(Utils.modItemLoc("unfired_clay_bucket")),
            SimpleItemType::basicItemModelProvider,
            SimpleItemType::registerUnfiredClayBucketRecipe,
            SimpleItemType::registerSimpleTag),
    CLAY_BUCKET("clay_bucket", "Clay Bucket",
            ItemTags.create(Utils.modLoc("clay_bucket")),
            () -> new ClayBucketItem(() -> Fluids.EMPTY, new Item.Properties().stacksTo(8)),
            () -> basicTexture(Utils.modItemLoc("clay_bucket")),
            SimpleItemType::basicItemModelProvider,
            SimpleItemType::registerClayBucketRecipe,
            SimpleItemType::registerSimpleTag),
    WATER_CLAY_BUCKET("water_clay_bucket", "Water Clay Bucket",
            ItemTags.create(Utils.modLoc("water_bucket")),
            () -> new ClayBucketItem(() -> Fluids.WATER, new Item.Properties().craftRemainder(item(CLAY_BUCKET)).stacksTo(1)),
            () -> clayBucketTexture(Utils.modItemLoc("bucket_overlay"), 0x0C4DF5),
            SimpleItemType::clayBucketItemModelProvider,
            IRecipe::noRecipe, // take water
            (holder, provider) -> {
                provider.tag(holder.object.itemTag).add(holder.item.get());
                provider.tag(holder.object.itemTag).add(Items.WATER_BUCKET);
            }),
    LAVA_CLAY_BUCKET("lava_clay_bucket", "Lava Clay Bucket",
            ItemTags.create(Utils.modLoc("lava_bucket")),
            () -> new ClayBucketItem(() -> Fluids.LAVA, new Item.Properties().craftRemainder(item(CLAY_BUCKET)).stacksTo(1)),
            () -> clayBucketTexture(Utils.modItemLoc("bucket_overlay"), 0xF54D0C),
            SimpleItemType::clayBucketItemModelProvider,
            IRecipe::noRecipe, // take lava
            (holder, provider) -> {
                provider.tag(holder.object.itemTag).add(holder.item.get());
                provider.tag(holder.object.itemTag).add(Items.LAVA_BUCKET);
            }),
    ;

    @NotNull public final String key;
    @NotNull public final String name;
    @NotNull public final TagKey<Item> itemTag;
    @NotNull public final Supplier<Item> itemGetter;
    @NotNull private final HashMap<Integer, Integer> tintColors;
    @NotNull private final ResourceLocation[] textures;
    @NotNull private final BiConsumer<Holder.SimpleItemHolder, ItemModelProvider> modelGetter;
    @NotNull private final BiConsumer<Holder.SimpleItemHolder, RecipeOutput> recipeGetter;
    @NotNull private final BiConsumer<Holder.SimpleItemHolder, ItemTagsProvider> itemTagsGetter;

    SimpleItemType(@NotNull String key, @NotNull String name, @NotNull TagKey<Item> itemTag, @NotNull Supplier<Item> itemGetter,
                   @NotNull Supplier<TextureHolder[]> textureGetter, @NotNull BiConsumer<Holder.SimpleItemHolder, ItemModelProvider> modelGetter,
                   @NotNull BiConsumer<Holder.SimpleItemHolder, RecipeOutput> recipeGetter, @NotNull BiConsumer<Holder.SimpleItemHolder, ItemTagsProvider> itemTagsGetter) {
        this.key = key;
        this.name = name;
        this.itemTag = itemTag;
        this.itemGetter = itemGetter;
        this.modelGetter = modelGetter;
        this.recipeGetter = recipeGetter;
        this.itemTagsGetter = itemTagsGetter;
        this.tintColors = new HashMap<>();

        TextureHolder[] holders = textureGetter.get();
        ArrayList<ResourceLocation> resources = new ArrayList<>();

        for (TextureHolder holder : holders) {
            if (holder.tintIndex() >= 0) {
                this.tintColors.put(holder.tintIndex(), holder.color());
            }
            resources.add(holder.texture());
        }

        this.textures = resources.toArray(ResourceLocation[]::new);
    }

    @Override
    public void registerModel(@NotNull Holder.SimpleItemHolder holder, @NotNull ItemModelProvider provider) {
        modelGetter.accept(holder, provider);
    }

    @Override
    public void registerRecipe(@NotNull Holder.SimpleItemHolder holder, @NotNull RecipeOutput recipeConsumer) {
        recipeGetter.accept(holder, recipeConsumer);
    }

    @Override
    public void registerTag(@NotNull Holder.SimpleItemHolder holder, @NotNull ItemTagsProvider provider) {
        itemTagsGetter.accept(holder, provider);
    }

    @Override
    public @NotNull Map<Integer, Integer> getTintColors() {
        return tintColors;
    }

    @Override
    public @NotNull ResourceLocation[] getTextures() {
        return textures;
    }

    private static Item simpleItem() {
        return new Item(new Item.Properties());
    }

    private static Item grassFibersItem() {
        return new Item(new Item.Properties()) {
            @Override
            public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
                super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
                tooltipComponents.add(Component.translatable("text.ytech.hover.grass_fibers")
                        .withStyle(ChatFormatting.DARK_GRAY)
                        .append(CommonComponents.SPACE)
                        .append(item(SHARP_FLINT).asItem().getDefaultInstance().getDisplayName())
                );
            }
        };
    }

    private static Item simpleFood(int nutrition, float saturation) {
        return new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturation).build()));
    }

    private static void basicItemModelProvider(@NotNull Holder.SimpleItemHolder holder, @NotNull ItemModelProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures();
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", textures[0]);
    }

    private static void basketItemModelProvider(@NotNull Holder.SimpleItemHolder holder, @NotNull ItemModelProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures();
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        ModelFile model = provider.getBuilder(holder.key + "_filled")
                .parent(builder)
                .texture("layer0", textures[1]);

        builder.override().predicate(BasketItem.FILLED_PREDICATE, 0.0001f).model(model).end();
        builder.texture("layer0", textures[0]);
    }

    private static void spearItemModelProvider(@NotNull Holder.SimpleItemHolder holder, @NotNull ItemModelProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures();

        provider.getBuilder(holder.key)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", textures[0]);

        ItemModelBuilder throwing = provider.getBuilder(holder.key + "_throwing")
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .guiLight(BlockModel.GuiLight.FRONT)
                .texture("particle", textures[0])
                .transforms()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(0, 90, 180).translation(8, -17, 9).end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(0, 90, 180).translation(8, 17, -7).end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, -90, 25).translation(-3, 17, 1).end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 90, -25).translation(13, 17, 1).end()
                .transform(ItemDisplayContext.GUI).rotation(15, -25, -5).translation(2, 3, 0).scale(0.65F).end()
                .transform(ItemDisplayContext.FIXED).rotation(0, 180, 0).translation(-2, 4, -5).scale(0.5F).end()
                .transform(ItemDisplayContext.GROUND).translation(4, 4, 2).scale(0.25F).end()
                .end();

        provider.getBuilder(holder.key + "_in_hand")
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .guiLight(BlockModel.GuiLight.FRONT)
                .texture("particle", textures[0])
                .transforms()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(0, 60, 0).translation(11, 17, -2).end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(0, 60, 0).translation(3, 17, 12).end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, -90, 25).translation(-3, 17, 1).end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 90, -25).translation(13, 17, 1).end()
                .transform(ItemDisplayContext.GUI).rotation(15, -25, -5).translation(2, 3, 0).scale(0.65F).end()
                .transform(ItemDisplayContext.FIXED).rotation(0, 180, 0).translation(-2, 4, -5).scale(0.5F).end()
                .transform(ItemDisplayContext.GROUND).translation(4, 4, 2).scale(0.25F).end()
                .end()
                .override().predicate(SpearItem.THROWING_PREDICATE, 1).model(throwing).end();
    }

    private static void clayBucketItemModelProvider(@NotNull Holder.SimpleItemHolder holder, @NotNull ItemModelProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures();
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", textures[0]);
        builder.texture("layer1", textures[1]);
    }

    @NotNull
    private static TextureHolder[] clayBucketTexture(@NotNull ResourceLocation overlay, int color) {
        return List.of(new TextureHolder(-1, -1, Utils.modItemLoc("clay_bucket")),
                new TextureHolder(1, color, overlay)).toArray(TextureHolder[]::new);
    }

    @NotNull
    private static TextureHolder[] basketTexture() {
        return List.of(new TextureHolder(-1, -1, Utils.modItemLoc("basket")),
                new TextureHolder(-1, -1, Utils.modItemLoc("basket_filled"))).toArray(TextureHolder[]::new);
    }

    private static TextureHolder[] basicTexture(ResourceLocation base) {
        return List.of(new TextureHolder(-1, -1, base)).toArray(TextureHolder[]::new);
    }

    private static void registerSimpleTag(@NotNull Holder.SimpleItemHolder holder, @NotNull ItemTagsProvider provider) {
        provider.tag(holder.object.itemTag).add(holder.item.get());
    }

    private static void registerGrassTwineRecipe(@NotNull Holder.SimpleItemHolder holder, @NotNull RecipeOutput recipeConsumer) {
        Holder input = HOLDER.simpleItems().get(GRASS_FIBERS);
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, holder.item.get())
                .define('#', GRASS_FIBERS.itemTag)
                .pattern("##")
                .pattern("##")
                .unlockedBy(Utils.getHasItem(input), RecipeProvider.has(GRASS_FIBERS.itemTag))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void registerBrickMoldRecipe(@NotNull Holder.SimpleItemHolder holder, @NotNull RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, holder.item.get())
                .define('#', WOODEN_PLATE.itemTag)
                .define('I', WOODEN_BOLT.itemTag)
                .pattern("I#I")
                .pattern("###")
                .pattern("I#I")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(WOODEN_PLATE.itemTag))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void registerUnfiredBrickRecipe(@NotNull Holder.SimpleItemHolder holder, @NotNull RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, holder.item.get(), 8)
                .define('#', BRICK_MOLD.itemTag)
                .define('B', Items.CLAY_BALL)
                .pattern("BBB")
                .pattern("B#B")
                .pattern("BBB")
                .unlockedBy(RecipeProvider.getHasName(Items.CLAY_BALL), RecipeProvider.has(Items.CLAY_BALL))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void registerWoodenPlateRecipe(@NotNull Holder.SimpleItemHolder holder, @NotNull RecipeOutput recipeConsumer) {
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, holder.item.get())
                .requires(ItemTags.WOODEN_SLABS)
                .requires(AXE.groupTag)
                .group(holder.key)
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(ItemTags.WOODEN_SLABS))
                .save(recipeConsumer, Utils.modLoc(holder.key + "_using_axe"));
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, holder.item.get(), 2)
                .requires(ItemTags.WOODEN_SLABS)
                .requires(SAW.groupTag)
                .group(holder.key)
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(ItemTags.WOODEN_SLABS))
                .save(recipeConsumer, Utils.modLoc(holder.key + "_using_saw"));
    }

    private static void registerWoodenBoltRecipe(@NotNull Holder.SimpleItemHolder holder, @NotNull RecipeOutput recipeConsumer) {
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, holder.item.get())
                .requires(Items.STICK)
                .requires(AXE.groupTag)
                .group(holder.key)
                .unlockedBy(RecipeProvider.getHasName(Items.STICK), RecipeProvider.has(Items.STICK))
                .save(recipeConsumer, Utils.modLoc(holder.key + "_using_axe"));
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, holder.item.get(), 2)
                .requires(Items.STICK)
                .requires(SAW.groupTag)
                .group(holder.key)
                .unlockedBy(RecipeProvider.getHasName(Items.STICK), RecipeProvider.has(Items.STICK))
                .save(recipeConsumer, Utils.modLoc(holder.key + "_using_saw"));
    }

    private static void registerRawHideRecipe(Holder.SimpleItemHolder holder, RecipeOutput recipeConsumer) {
        TanningRecipe.Builder.tanning(holder.object.itemTag, 5, Items.LEATHER)
                .tool(Ingredient.of(SHARP_FLINT.itemTag))
                .unlockedBy(Utils.getHasItem(holder), RecipeProvider.has(RAW_HIDE.itemTag))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void registerLeatherStripsRecipe(Holder.SimpleItemHolder holder, RecipeOutput recipeConsumer) {
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, holder.item.get(), 4)
                .requires(Items.LEATHER)
                .requires(SHARP_FLINT.itemTag)
                .unlockedBy(RecipeProvider.getHasName(Items.LEATHER), RecipeProvider.has(Items.LEATHER))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void registerIronBloomRecipe(Holder.SimpleItemHolder holder, RecipeOutput recipeConsumer) {
        AlloyingRecipe.Builder.alloying(CRUSHED_MATERIAL.itemTag.get(MaterialType.IRON), 1, Items.CHARCOAL, 1, 1250, 200, holder.item.get(), 1)
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(CRUSHED_MATERIAL.itemTag.get(MaterialType.IRON)))
                .save(recipeConsumer, Utils.modLoc(Utils.loc(holder.item.get()).getPath()));
    }

    private static void registerBasketRecipe(Holder.SimpleItemHolder holder, RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, holder.item.get())
                .define('#', GRASS_TWINE.itemTag)
                .pattern(" # ")
                .pattern("###")
                .pattern("###")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(GRASS_TWINE.itemTag))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void registerDriedVenisonRecipe(Holder.SimpleItemHolder holder, RecipeOutput recipeConsumer) {
        registerDryingRecipe(holder, recipeConsumer, item(VENISON));
    }

    private static void registerCookedVenisonRecipe(Holder.SimpleItemHolder holder, RecipeOutput recipeConsumer) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(item(VENISON)), RecipeCategory.FOOD, holder.item.get(), 0.35f, 200)
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(VENISON.itemTag))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void registerFlintKnifeRecipe(Holder.SimpleItemHolder holder, RecipeOutput recipeConsumer) {
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.COMBAT, holder.item.get())
                .requires(Items.STICK)
                .requires(Items.FLINT)
                .requires(LEATHER_STRIPS.itemTag)
                .unlockedBy(RecipeProvider.getHasName(Items.FLINT), RecipeProvider.has(Items.FLINT))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void registerSpearRecipe(Holder.SimpleItemHolder holder, RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, holder.item.get())
                .define('T', LEATHER_STRIPS.itemTag)
                .define('S', Items.FLINT)
                .define('#', Items.STICK)
                .pattern(" TS")
                .pattern(" #T")
                .pattern("#  ")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(LEATHER_STRIPS.itemTag))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void registerDryingRecipe(Holder.SimpleItemHolder holder, RecipeOutput recipeConsumer, Item rawMeat) {
        DryingRecipe.Builder.drying(rawMeat, 20 * 60, holder.item.get())
                .unlockedBy(RecipeProvider.getHasName(rawMeat), RecipeProvider.has(rawMeat))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void registerFlourRecipe(Holder.SimpleItemHolder holder, RecipeOutput recipeConsumer) {
        MillingRecipe.Builder.milling(Tags.Items.CROPS_WHEAT, holder.item.get())
                .unlockedBy(RecipeProvider.getHasName(Items.WHEAT), RecipeProvider.has(Tags.Items.CROPS_WHEAT))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void registerUnfiredClayBucketRecipe(@NotNull Holder.SimpleItemHolder holder, @NotNull RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, holder.item.get())
                .define('#', Items.CLAY_BALL)
                .pattern("# #")
                .pattern("# #")
                .pattern(" # ")
                .unlockedBy(RecipeProvider.getHasName(Items.CLAY_BALL), RecipeProvider.has(Items.CLAY_BALL))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void registerClayBucketRecipe(@NotNull Holder.SimpleItemHolder holder, @NotNull RecipeOutput recipeConsumer) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(item(UNFIRED_CLAY_BUCKET)), RecipeCategory.MISC, holder.item.get(), 0.35f, 200)
                .unlockedBy(RecipeProvider.getHasName(Items.CLAY_BALL), RecipeProvider.has(Items.CLAY_BALL))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void registerBreadDoughRecipe(@NotNull Holder.SimpleItemHolder holder, @NotNull RecipeOutput recipeConsumer) {
        RemainingShapelessRecipe.Builder.shapeless(RecipeCategory.MISC, holder.item.get())
                .requires(FLOUR.itemTag)
                .requires(FLOUR.itemTag)
                .requires(FLOUR.itemTag)
                .requires(WATER_CLAY_BUCKET.itemTag)
                .unlockedBy(RecipeProvider.getHasName(Items.CLAY_BALL), RecipeProvider.has(Items.CLAY_BALL))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }
}
