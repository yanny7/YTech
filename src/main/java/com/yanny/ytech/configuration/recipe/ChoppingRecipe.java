package com.yanny.ytech.configuration.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.registration.YTechRecipeBookCategories;
import com.yanny.ytech.registration.YTechRecipeSerializers;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public record ChoppingRecipe(Ingredient ingredient, Ingredient tool, int hitCount, ItemStack result) implements Recipe<SingleRecipeInput> {
    @Override
    public boolean matches(@NotNull SingleRecipeInput recipeInput, @NotNull Level level) {
        return ingredient.test(recipeInput.getItem(0));
    }

    @NotNull
    @Override
    public ItemStack assemble(@NotNull SingleRecipeInput recipeInput, @NotNull HolderLookup.Provider provider) {
        return result.copy();
    }

    @NotNull
    @Override
    public RecipeSerializer<ChoppingRecipe> getSerializer() {
        return YTechRecipeSerializers.CHOPPING.get();
    }

    @NotNull
    @Override
    public RecipeType<ChoppingRecipe> getType() {
        return YTechRecipeTypes.CHOPPING.get();
    }

    @NotNull
    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @NotNull
    @Override
    public RecipeBookCategory recipeBookCategory() {
        return YTechRecipeBookCategories.CHOPPING.get();
    }

    public static class Serializer implements RecipeSerializer<ChoppingRecipe> {
        private static final MapCodec<ChoppingRecipe> CODEC = RecordCodecBuilder.mapCodec((recipe) -> recipe.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter((ChoppingRecipe) -> ChoppingRecipe.ingredient),
                Ingredient.CODEC.fieldOf("tool").forGetter((ChoppingRecipe) -> ChoppingRecipe.tool),
                Codec.INT.fieldOf("hitCount").forGetter((ChoppingRecipe) -> ChoppingRecipe.hitCount),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter((ChoppingRecipe) -> ChoppingRecipe.result)
        ).apply(recipe, ChoppingRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, ChoppingRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @NotNull
        @Override
        public MapCodec<ChoppingRecipe> codec() {
            return CODEC;
        }

        @NotNull
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ChoppingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        @NotNull
        private static ChoppingRecipe fromNetwork(@NotNull RegistryFriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient tool = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            int hitCount = buffer.readInt();
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            return new ChoppingRecipe(ingredient, tool, hitCount, result);
        }

        private static void toNetwork(@NotNull RegistryFriendlyByteBuf buffer, @NotNull ChoppingRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.tool);
            buffer.writeInt(recipe.hitCount);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        }
    }

    public static class Builder implements RecipeBuilder {
        private final Ingredient ingredient;
        private final Ingredient tool;
        private final int hitCount;
        private final Item result;
        private final int count;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        Builder(@NotNull Ingredient ingredient, Ingredient tool, int hitCount, @NotNull Item result, int count) {
            this.ingredient = ingredient;
            this.tool = tool;
            this.hitCount = hitCount;
            this.result = result;
            this.count = count;
        }

        public static Builder chopping(@NotNull HolderGetter<Item> items, @NotNull TagKey<Item> input, TagKey<Item> tool, int hitCount, @NotNull Item result, int count) {
            return new Builder(Ingredient.of(items.getOrThrow(input)), Ingredient.of(items.getOrThrow(tool)), hitCount, result, count);
        }

        public static Builder chopping(@NotNull HolderGetter<Item> items, @NotNull ItemLike input, TagKey<Item> tool, int hitCount, @NotNull Item result, int count) {
            return new Builder(Ingredient.of(input), Ingredient.of(items.getOrThrow(tool)), hitCount, result, count);
        }

        @NotNull
        @Override
        public ChoppingRecipe.Builder unlockedBy(@NotNull String criterionName, @NotNull Criterion criterionTrigger) {
            this.criteria.put(criterionName, criterionTrigger);
            return this;
        }

        @NotNull
        @Override
        public RecipeBuilder group(@Nullable String groupName) {
            return this;
        }

        @NotNull
        @Override
        public Item getResult() {
            return result;
        }

        @Override
        public void save(@NotNull RecipeOutput finishedRecipeConsumer, @NotNull ResourceKey<Recipe<?>> recipeId) {
            ensureValid(recipeId);
            Advancement.Builder builder = finishedRecipeConsumer.advancement().addCriterion("has_the_recipe",
                    RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(AdvancementRequirements.Strategy.OR);
            this.criteria.forEach(builder::addCriterion);
            finishedRecipeConsumer.accept(
                    recipeId,
                    new ChoppingRecipe(ingredient, tool, hitCount, new ItemStack(result, count)),
                    builder.build(Utils.modLoc("recipes/chopping/" + recipeId.location().getPath()))
            );
        }

        //Makes sure that this recipe is valid and obtainable.
        private void ensureValid(@NotNull ResourceKey<Recipe<?>> id) {
            if (this.criteria.isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + id);
            }
        }
    }
}
