package com.yanny.ytech.configuration;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.item.SharpFlint;
import com.yanny.ytech.registration.Holder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.yanny.ytech.registration.Registration.HOLDER;

public enum SimpleItemType implements IModel<Holder.SimpleItemHolder, ItemModelProvider>, IRecipe<Holder.SimpleItemHolder> {
    GRASS_FIBERS("grass_fibers", "Grass Fibers",
            SimpleItemType::simpleItem,
            SimpleItemType::basicItemModelProvider,
            IRecipe::noRecipe),
    GRASS_TWINE("grass_twine", "Grass Twine",
            SimpleItemType::simpleItem,
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
            SharpFlint::new,
            SimpleItemType::basicItemModelProvider,
            IRecipe::noRecipe),
    ;

    @NotNull public final String key;
    @NotNull public final String name;
    @NotNull public final Supplier<Item> itemGetter;
    @NotNull private final BiConsumer<Holder.SimpleItemHolder, ItemModelProvider> modelGetter;
    @NotNull private final BiConsumer<Holder.SimpleItemHolder, Consumer<FinishedRecipe>> recipeGetter;

    SimpleItemType(@NotNull String key, @NotNull String name, @NotNull Supplier<Item> itemGetter,
                   @NotNull BiConsumer<Holder.SimpleItemHolder, ItemModelProvider> modelGetter,
                   @NotNull BiConsumer<Holder.SimpleItemHolder, Consumer<FinishedRecipe>> recipeGetter) {
        this.key = key;
        this.name = name;
        this.itemGetter = itemGetter;
        this.modelGetter = modelGetter;
        this.recipeGetter = recipeGetter;
    }

    @Override
    public void registerModel(@NotNull Holder.SimpleItemHolder holder, @NotNull ItemModelProvider provider) {
        modelGetter.accept(holder, provider);
    }

    @Override
    public void registerRecipe(@NotNull Holder.SimpleItemHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        recipeGetter.accept(holder, recipeConsumer);
    }

    private static Item simpleItem() {
        return new Item(new Item.Properties());
    }

    private static void basicItemModelProvider(@NotNull Holder.SimpleItemHolder holder, @NotNull ItemModelProvider provider) {
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", provider.modLoc(ModelProvider.ITEM_FOLDER + "/" + holder.key));
    }
}
