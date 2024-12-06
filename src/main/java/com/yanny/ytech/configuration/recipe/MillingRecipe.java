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

public record MillingRecipe(Ingredient ingredient, ItemStack result, float bonusChance) implements Recipe<RecipeInput> {
    @Override
    public boolean matches(@NotNull RecipeInput recipeInput, @NotNull Level level) {
        return ingredient.test(recipeInput.getItem(0));
    }

    @NotNull
    @Override
    public ItemStack assemble(@NotNull RecipeInput recipeInput, @NotNull HolderLookup.Provider provider) {
        return result.copy();
    }

    @NotNull
    @Override
    public RecipeSerializer<MillingRecipe> getSerializer() {
        return YTechRecipeSerializers.MILLING.get();
    }

    @NotNull
    @Override
    public RecipeType<MillingRecipe> getType() {
        return YTechRecipeTypes.MILLING.get();
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
        return YTechRecipeBookCategories.MILLING.get();
    }

    public static class Serializer implements RecipeSerializer<MillingRecipe> {
        private static final MapCodec<MillingRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        Ingredient.CODEC.fieldOf("ingredient").forGetter((millingRecipe) -> millingRecipe.ingredient),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter((millingRecipe) -> millingRecipe.result),
                        Codec.FLOAT.fieldOf("bonusChance").forGetter((smeltingRecipe) -> smeltingRecipe.bonusChance)
                ).apply(instance, MillingRecipe::new)
        );
        private static final StreamCodec<RegistryFriendlyByteBuf, MillingRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        @NotNull
        public MapCodec<MillingRecipe> codec() {
            return CODEC;
        }

        @NotNull
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MillingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        @NotNull
        private static MillingRecipe fromNetwork(@NotNull RegistryFriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            float bonusChance = buffer.readFloat();
            return new MillingRecipe(ingredient, result, bonusChance);
        }

        private static void toNetwork(@NotNull RegistryFriendlyByteBuf buffer, @NotNull MillingRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeFloat(recipe.bonusChance);
        }
    }

    public static class Builder implements RecipeBuilder {
        private final Ingredient ingredient;
        private final Item result;
        private final int count;
        private float bonusChance = 0.0f;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        Builder(@NotNull Ingredient ingredient, @NotNull Item result, int count) {
            this.ingredient = ingredient;
            this.result = result;
            this.count = count;
        }

        public static Builder milling(@NotNull HolderGetter<Item> items, @NotNull TagKey<Item> input, @NotNull Item result, int count) {
            return new Builder(Ingredient.of(items.getOrThrow(input)), result, count);
        }

        public static Builder milling(@NotNull ItemLike input, @NotNull Item result, int count) {
            return new Builder(Ingredient.of(input), result, count);
        }

        public RecipeBuilder bonusChance(float bonusChance) {
            this.bonusChance = bonusChance;
            return this;
        }

        @NotNull
        @Override
        public RecipeBuilder unlockedBy(@NotNull String criterionName, @NotNull Criterion criterionTrigger) {
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
                    new MillingRecipe(ingredient, new ItemStack(result, count), bonusChance),
                    builder.build(Utils.modLoc("recipes/milling/" + recipeId.location().getPath()))
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
