package com.yanny.ytech.configuration;

import com.yanny.ytech.configuration.item.CraftUsableDiggerItem;
import com.yanny.ytech.configuration.item.CraftUsableSwordItem;
import com.yanny.ytech.configuration.recipe.TanningRecipe;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.*;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.yanny.ytech.registration.Registration.HOLDER;

public enum SimpleItemType implements ISimpleModel<Holder.SimpleItemHolder, ItemModelProvider>, IRecipe<Holder.SimpleItemHolder>, IItemTag<Holder.SimpleItemHolder> {
    GRASS_FIBERS("grass_fibers", "Grass Fibers",
            ItemTags.create(Utils.modLoc("grass_fibers")),
            SimpleItemType::simpleItem,
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
    WOODEN_BOLT("wooden_bolt", "Wooden Bolt",
            ItemTags.create(Utils.modLoc("bolts/wooden")),
            SimpleItemType::simpleItem,
            () -> List.of(new TextureHolder(0, MaterialType.OAK_WOOD.color, Utils.modItemLoc("bolt"))).toArray(TextureHolder[]::new),
            SimpleItemType::basicItemModelProvider,
            SimpleItemType::registerBoltRecipe,
            (holder, provider) -> {
                provider.tag(holder.object.itemTag).add(holder.item.get());
                provider.tag(MaterialItemType.BOLT.groupTag).addTag(holder.object.itemTag);
            }),
    RAW_HIDE("raw_hide", "Raw Hide",
            ItemTags.create(Utils.modLoc("raw_hides")),
            SimpleItemType::simpleItem,
            () -> basicTexture(Utils.modItemLoc("raw_hide")),
            SimpleItemType::basicItemModelProvider,
            SimpleItemType::registerRawHideRecipe,
            SimpleItemType::registerSimpleTag),
    SHARP_FLINT("sharp_flint", "Sharp Flint",
            ItemTags.create(Utils.modLoc("sharp_flints")),
            () -> new CraftUsableSwordItem(Tiers.WOOD, 0, 0, new Item.Properties()),
            () -> basicTexture(Utils.modItemLoc("sharp_flint")),
            SimpleItemType::basicItemModelProvider,
            IRecipe::noRecipe,
            SimpleItemType::registerSimpleTag),
    FLINT_SAW("flint_saw", "Flint Saw",
            ItemTags.create(Utils.modLoc("saws/flint")),
            () -> new CraftUsableDiggerItem(0, 1, Tiers.WOOD, BlockTags.LOGS, new Item.Properties()),
            () -> basicTexture(Utils.modItemLoc("flint_saw")),
            SimpleItemType::basicItemModelProvider,
            IRecipe::noRecipe,
            (holder, provider) -> {
                provider.tag(holder.object.itemTag).add(holder.item.get());
                provider.tag(MaterialItemType.SAW.groupTag).addTag(holder.object.itemTag);
            }),
    ;

    @NotNull public final String key;
    @NotNull public final String name;
    @NotNull public final TagKey<Item> itemTag;
    @NotNull public final Supplier<Item> itemGetter;
    @NotNull private final HashMap<Integer, Integer> tintColors;
    @NotNull private final ResourceLocation[] textures;
    @NotNull private final BiConsumer<Holder.SimpleItemHolder, ItemModelProvider> modelGetter;
    @NotNull private final BiConsumer<Holder.SimpleItemHolder, Consumer<FinishedRecipe>> recipeGetter;
    @NotNull private final BiConsumer<Holder.SimpleItemHolder, ItemTagsProvider> itemTagsGetter;

    SimpleItemType(@NotNull String key, @NotNull String name, @NotNull TagKey<Item> itemTag, @NotNull Supplier<Item> itemGetter,
                   @NotNull Supplier<TextureHolder[]> textureGetter, @NotNull BiConsumer<Holder.SimpleItemHolder, ItemModelProvider> modelGetter,
                   @NotNull BiConsumer<Holder.SimpleItemHolder, Consumer<FinishedRecipe>> recipeGetter, @NotNull BiConsumer<Holder.SimpleItemHolder, ItemTagsProvider> itemTagsGetter) {
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
    public void registerRecipe(@NotNull Holder.SimpleItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
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

    private static void basicItemModelProvider(@NotNull Holder.SimpleItemHolder holder, @NotNull ItemModelProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures();
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", textures[0]);
    }

    private static TextureHolder[] basicTexture(ResourceLocation base) {
        return List.of(new TextureHolder(-1, -1, base)).toArray(TextureHolder[]::new);
    }

    private static void registerSimpleTag(@NotNull Holder.SimpleItemHolder holder, @NotNull ItemTagsProvider provider) {
        provider.tag(holder.object.itemTag).add(holder.item.get());
    }

    private static void registerGrassTwineRecipe(@NotNull Holder.SimpleItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        Holder input = HOLDER.simpleItems().get(GRASS_FIBERS);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, holder.item.get())
                .define('#', GRASS_FIBERS.itemTag)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy(Utils.getHasName(input), RecipeProvider.has(GRASS_FIBERS.itemTag))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void registerBoltRecipe(@NotNull Holder.SimpleItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, holder.item.get(), 2)
                .requires(Items.STICK)
                .requires(MaterialItemType.SAW.groupTag)
                .unlockedBy(RecipeProvider.getHasName(Items.STICK), RecipeProvider.has(Items.STICK))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void registerRawHideRecipe(Holder.SimpleItemHolder holder, Consumer<FinishedRecipe> recipeConsumer) {
        TanningRecipe.Builder.tanning(holder.object.itemTag, 5, Items.LEATHER)
                .tool(Ingredient.of(SHARP_FLINT.itemTag))
                .unlockedBy(Utils.getHasName(holder), RecipeProvider.has(RAW_HIDE.itemTag))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }
}
