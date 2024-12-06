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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public record TanningRecipe(Ingredient ingredient, Ingredient tool, int hitCount, ItemStack result) implements Recipe<SingleRecipeInput> {
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
    public RecipeSerializer<TanningRecipe> getSerializer() {
        return YTechRecipeSerializers.TANNING.get();
    }

    @NotNull
    @Override
    public RecipeType<TanningRecipe> getType() {
        return YTechRecipeTypes.TANNING.get();
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
        return YTechRecipeBookCategories.TANNING.get();
    }

    public static class Serializer implements RecipeSerializer<TanningRecipe> {
        private static final MapCodec<TanningRecipe> CODEC = RecordCodecBuilder.mapCodec((recipe) ->
                recipe.group(
                        Ingredient.CODEC.fieldOf("ingredient").forGetter((tanningRecipe) -> tanningRecipe.ingredient),
                        Ingredient.CODEC.fieldOf("tool").forGetter((tanningRecipe) -> tanningRecipe.tool),
                        Codec.INT.fieldOf("hitCount").forGetter((tanningRecipe) -> tanningRecipe.hitCount),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter((tanningRecipe) -> tanningRecipe.result)
                ).apply(recipe, TanningRecipe::new)
        );
        private static final StreamCodec<RegistryFriendlyByteBuf, TanningRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        @NotNull
        public MapCodec<TanningRecipe> codec() {
            return CODEC;
        }

        @NotNull
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TanningRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        @NotNull
        private static TanningRecipe fromNetwork(@NotNull RegistryFriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient tool = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            int hitCount = buffer.readInt();
            return new TanningRecipe(ingredient, tool, hitCount, result);
        }

        private static void toNetwork(@NotNull RegistryFriendlyByteBuf buffer, @NotNull TanningRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.tool);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeInt(recipe.hitCount);
        }
    }

    public static class Builder implements RecipeBuilder {
        private final Ingredient ingredient;
        private final Ingredient tool;
        private final int hitCount;
        private final Item result;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        Builder(@NotNull Ingredient ingredient, Ingredient tool, int hitCount, @NotNull Item result) {
            this.ingredient = ingredient;
            this.tool = tool;
            this.hitCount = hitCount;
            this.result = result;
        }

        public static Builder tanning(@NotNull HolderGetter<Item> items, @NotNull TagKey<Item> input, @NotNull TagKey<Item> tool, int hitCount, @NotNull Item result) {
            return new Builder(Ingredient.of(items.getOrThrow(input)), Ingredient.of(items.getOrThrow(tool)), hitCount, result);
        }

        @NotNull
        @Override
        public Builder unlockedBy(@NotNull String criterionName, @NotNull Criterion criterionTrigger) {
            this.criteria.put(criterionName, criterionTrigger);
            return this;
        }

        @NotNull
        @Override
        public Builder group(@Nullable String groupName) {
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
                    new TanningRecipe(ingredient, tool, hitCount, new ItemStack(result)),
                    builder.build(Utils.modLoc("recipes/tanning/" + recipeId.location().getPath()))
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
