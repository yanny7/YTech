package com.yanny.ytech.configuration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.item.CraftUsableDiggerItem;
import com.yanny.ytech.configuration.item.CraftUsableSwordItem;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
            (holder, provider) -> provider.tag(holder.object.itemTag).add(holder.item.get())),
    GRASS_TWINE("grass_twine", "Grass Twine",
            ItemTags.create(Utils.modLoc("grass_twines")),
            SimpleItemType::simpleItem,
            () -> basicTexture(Utils.modItemLoc("grass_twine")),
            SimpleItemType::basicItemModelProvider,
            ((holder, recipeConsumer) -> {
                Holder input = HOLDER.simpleItems().get(SimpleItemType.GRASS_FIBERS);
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, holder.item.get())
                        .define('#', GRASS_FIBERS.itemTag)
                        .pattern("###")
                        .pattern("###")
                        .pattern("###")
                        .unlockedBy(Utils.getHasName(input), RecipeProvider.has(GRASS_FIBERS.itemTag))
                        .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, holder.key));
            }),
            (holder, provider) -> provider.tag(holder.object.itemTag).add(holder.item.get())),
    SHARP_FLINT("sharp_flint", "Sharp Flint",
            ItemTags.create(Utils.modLoc("sharp_flints")),
            () -> new CraftUsableSwordItem(Tiers.WOOD, 0, 0, new Item.Properties()),
            () -> basicTexture(Utils.modItemLoc("sharp_flint")),
            SimpleItemType::basicItemModelProvider,
            IRecipe::noRecipe,
            (holder, provider) -> provider.tag(holder.object.itemTag).add(holder.item.get())),
    FLINT_SAW("flint_saw", "Flint Saw",
            ItemTags.create(Utils.modLoc("flint_saws")),
            () -> new CraftUsableDiggerItem(0, 1, Tiers.WOOD, BlockTags.LOGS, new Item.Properties()),
            () -> basicTexture(Utils.modItemLoc("flint_saw")),
            SimpleItemType::basicItemModelProvider,
            IRecipe::noRecipe,
            (holder, provider) -> provider.tag(holder.object.itemTag).add(holder.item.get()))
    ;

    @NotNull public final String key;
    @NotNull public final String name;
    @NotNull public final TagKey<Item> itemTag;
    @NotNull public final Supplier<Item> itemGetter;
    @NotNull private final HashSet<Integer> tintIndices;
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
        this.tintIndices = new HashSet<>();

        TextureHolder[] holders = textureGetter.get();
        ArrayList<ResourceLocation> resources = new ArrayList<>();

        for (TextureHolder holder : holders) {
            if (holder.tintIndex() >= 0) {
                this.tintIndices.add(holder.tintIndex());
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
    public void registerTag(Holder.@NotNull SimpleItemHolder holder, @NotNull ItemTagsProvider provider) {
        itemTagsGetter.accept(holder, provider);
    }

    @Override
    public @NotNull Set<Integer> getTintIndices() {
        return tintIndices;
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
        return List.of(new TextureHolder(-1, base)).toArray(TextureHolder[]::new);
    }
}
