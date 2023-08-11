package com.yanny.ytech.configuration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.item.CraftUsableDiggerItem;
import com.yanny.ytech.configuration.item.CraftUsableSwordItem;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
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

public enum SimpleItemType implements ISimpleModel<Holder.SimpleItemHolder, ItemModelProvider>, IRecipe<Holder.SimpleItemHolder> {
    GRASS_FIBERS("grass_fibers", "Grass Fibers",
            SimpleItemType::simpleItem,
            () -> basicTexture(IModel.modItemLoc("grass_fibers")),
            SimpleItemType::basicItemModelProvider,
            IRecipe::noRecipe),
    GRASS_TWINE("grass_twine", "Grass Twine",
            SimpleItemType::simpleItem,
            () -> basicTexture(IModel.modItemLoc("grass_twine")),
            SimpleItemType::basicItemModelProvider,
            ((holder, recipeConsumer) -> {
                Item input = HOLDER.simpleItems().get(SimpleItemType.GRASS_FIBERS).item.get();
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, holder.item.get())
                        .define('#', input)
                        .pattern("###")
                        .pattern("###")
                        .pattern("###")
                        .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                        .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, holder.key));
            })),
    SHARP_FLINT("sharp_flint", "Sharp Flint",
            () -> new CraftUsableSwordItem(Tiers.WOOD, 0, 0, new Item.Properties()),
            () -> basicTexture(IModel.modItemLoc("sharp_flint")),
            SimpleItemType::basicItemModelProvider,
            IRecipe::noRecipe),
    FLINT_SAW("flint_saw", "Flint Saw",
            () -> new CraftUsableDiggerItem(0, 1, Tiers.WOOD, BlockTags.LOGS, new Item.Properties()),
            () -> basicTexture(IModel.modItemLoc("flint_saw")),
            SimpleItemType::basicItemModelProvider,
            IRecipe::noRecipe)
    ;

    @NotNull public final String key;
    @NotNull public final String name;
    @NotNull public final Supplier<Item> itemGetter;
    @NotNull private final HashSet<Integer> tintIndices;
    @NotNull private final ResourceLocation[] textures;
    @NotNull private final BiConsumer<Holder.SimpleItemHolder, ItemModelProvider> modelGetter;
    @NotNull private final BiConsumer<Holder.SimpleItemHolder, Consumer<FinishedRecipe>> recipeGetter;

    SimpleItemType(@NotNull String key, @NotNull String name, @NotNull Supplier<Item> itemGetter, @NotNull Supplier<TextureHolder[]> textureGetter,
                   @NotNull BiConsumer<Holder.SimpleItemHolder, ItemModelProvider> modelGetter,
                   @NotNull BiConsumer<Holder.SimpleItemHolder, Consumer<FinishedRecipe>> recipeGetter) {
        this.key = key;
        this.name = name;
        this.itemGetter = itemGetter;
        this.modelGetter = modelGetter;
        this.recipeGetter = recipeGetter;
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
