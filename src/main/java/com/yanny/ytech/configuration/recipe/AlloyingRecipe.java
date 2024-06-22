package com.yanny.ytech.configuration.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yanny.ytech.registration.YTechRecipeSerializers;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
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
import java.util.stream.Stream;

public record AlloyingRecipe(IngredientCount ingredient1, IngredientCount ingredient2,
                             int minTemperature, int smeltingTime, ItemStack result) implements Recipe<RecipeInput> {
    @Override
    public boolean matches(@NotNull RecipeInput recipeInput, @NotNull Level level) {
        return matchesFully(recipeInput.getItem(0), recipeInput.getItem(1), false);
    }

    @NotNull
    @Override
    public ItemStack assemble(@NotNull RecipeInput recipeInput, @NotNull HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @NotNull
    @Override
    public ItemStack getResultItem(@NotNull HolderLookup.Provider provider) {
        return result;
    }

    @NotNull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return YTechRecipeSerializers.ALLOYING.get();
    }

    @NotNull
    @Override
    public RecipeType<?> getType() {
        return YTechRecipeTypes.ALLOYING.get();
    }

    public int getInput1Count() {
        return ingredient1.count;
    }

    public int getInput2Count() {
        return ingredient2.count;
    }

    public boolean matchesPartially(@NotNull ItemStack itemStack1, @NotNull ItemStack itemStack2, boolean ignoreCount) {
        return (ingredient1.ingredient.test(itemStack1) && (ignoreCount || itemStack1.getCount() >= ingredient1.count))
                || (ingredient1.ingredient.test(itemStack2) && (ignoreCount || itemStack2.getCount() >= ingredient1.count))
                || (ingredient2.ingredient.test(itemStack1) && (ignoreCount || itemStack1.getCount() >= ingredient2.count))
                || (ingredient2.ingredient.test(itemStack2) && (ignoreCount || itemStack2.getCount() >= ingredient2.count));
    }

    public boolean matchesFully(@NotNull ItemStack itemStack1, @NotNull ItemStack itemStack2, boolean ignoreCount) {
        return (ingredient1.ingredient.test(itemStack1) && (ignoreCount || itemStack1.getCount() >= ingredient1.count) && ingredient2.ingredient.test(itemStack2) && (ignoreCount || itemStack2.getCount() >= ingredient2.count))
                || (ingredient1.ingredient.test(itemStack2) && (ignoreCount || itemStack2.getCount() >= ingredient1.count) && ingredient2.ingredient.test(itemStack1) && (ignoreCount || itemStack1.getCount() >= ingredient2.count));
    }

    public boolean matchesIngredient1(@NotNull ItemStack itemStack, boolean ignoreCount) {
        return ingredient1.ingredient.test(itemStack) && (ignoreCount || itemStack.getCount() >= ingredient1.count);
    }

    public boolean matchesIngredient2(@NotNull ItemStack itemStack, boolean ignoreCount) {
        return ingredient2.ingredient.test(itemStack) && (ignoreCount || itemStack.getCount() >= ingredient2.count);
    }

    public record IngredientCount(Ingredient ingredient, int count) {
        public static final Codec<IngredientCount> CODEC = RecordCodecBuilder.create((ingredient) -> ingredient.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter((ingredientCount) -> ingredientCount.ingredient),
                Codec.INT.fieldOf("count").forGetter((ingredientCount) -> ingredientCount.count)
        ).apply(ingredient, IngredientCount::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, IngredientCount> STREAM_CODEC = StreamCodec.of(
                (buffer, ingredient) -> {
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient.ingredient);
                    buffer.writeInt(ingredient.count);
                },
                (buffer) -> new IngredientCount(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer), buffer.readInt())
        );
    }

    public static class Serializer implements RecipeSerializer<AlloyingRecipe> {
        private static final MapCodec<AlloyingRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        IngredientCount.CODEC.fieldOf("ingredient1").forGetter((alloyingRecipe) -> alloyingRecipe.ingredient1),
                        IngredientCount.CODEC.fieldOf("ingredient2").forGetter((alloyingRecipe) -> alloyingRecipe.ingredient2),
                        Codec.INT.fieldOf("minTemp").forGetter((alloyingRecipe) -> alloyingRecipe.minTemperature),
                        Codec.INT.fieldOf("smeltingTime").forGetter((alloyingRecipe) -> alloyingRecipe.smeltingTime),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter((alloyingRecipe) -> alloyingRecipe.result)
                ).apply(instance, AlloyingRecipe::new)
        );
        private static final StreamCodec<RegistryFriendlyByteBuf, AlloyingRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @NotNull
        @Override
        public MapCodec<AlloyingRecipe> codec() {
            return CODEC;
        }

        @NotNull
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AlloyingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        @NotNull
        private static AlloyingRecipe fromNetwork(@NotNull RegistryFriendlyByteBuf buffer) {
            IngredientCount ingredient1 = IngredientCount.STREAM_CODEC.decode(buffer);
            IngredientCount ingredient2 = IngredientCount.STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            int minTemperature = buffer.readInt();
            int smeltingTime = buffer.readInt();
            return new AlloyingRecipe(ingredient1, ingredient2, minTemperature, smeltingTime, result);
        }

        private static void toNetwork(@NotNull RegistryFriendlyByteBuf buffer, @NotNull AlloyingRecipe recipe) {
            IngredientCount.STREAM_CODEC.encode(buffer, recipe.ingredient1);
            IngredientCount.STREAM_CODEC.encode(buffer, recipe.ingredient2);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeInt(recipe.minTemperature);
            buffer.writeInt(recipe.smeltingTime);
        }
    }

    public static class Builder implements RecipeBuilder {
        private final Ingredient ingredient1;
        private final int count1;
        private final Ingredient ingredient2;
        private final int count2;
        private final int minTemperature;
        private final int smeltingTime;
        private final Item result;
        private final int count;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        Builder(@NotNull Ingredient ingredient1, int count1, @NotNull Ingredient ingredient2, int count2, int minTemperature,
                int smeltingTime, @NotNull Item result, int count) {
            this.ingredient1 = ingredient1;
            this.count1 = count1;
            this.ingredient2 = ingredient2;
            this.count2 = count2;
            this.minTemperature = minTemperature;
            this.smeltingTime = smeltingTime;
            this.result = result;
            this.count = count;
        }

        public static Builder alloying(@NotNull TagKey<Item> input1, int count1, @NotNull TagKey<Item> input2, int count2, int minTemperature,
                                       int smeltingTime, @NotNull Item result, int count) {
            return new Builder(Ingredient.fromValues(Stream.of(new Ingredient.TagValue(input1))), count1,
                    Ingredient.fromValues(Stream.of(new Ingredient.TagValue(input2))), count2, minTemperature, smeltingTime, result, count);
        }

        public static Builder alloying(@NotNull TagKey<Item> input1, int count1, @NotNull ItemLike input2, int count2, int minTemperature,
                                       int smeltingTime, @NotNull Item result, int count) {
            return new Builder(Ingredient.fromValues(Stream.of(new Ingredient.TagValue(input1))), count1,
                    Ingredient.fromValues(Stream.of(new Ingredient.ItemValue(new ItemStack(input2)))), count2, minTemperature, smeltingTime, result, count);
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
        public void save(@NotNull RecipeOutput finishedRecipeConsumer, @NotNull ResourceLocation recipeId) {
            ensureValid(recipeId);
            Advancement.Builder builder = finishedRecipeConsumer.advancement().addCriterion("has_the_recipe",
                    RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(AdvancementRequirements.Strategy.OR);
            this.criteria.forEach(builder::addCriterion);
            finishedRecipeConsumer.accept(
                    recipeId,
                    new AlloyingRecipe(
                            new IngredientCount(ingredient1, count1),
                            new IngredientCount(ingredient2, count2),
                            minTemperature,
                            smeltingTime,
                            new ItemStack(result, count)
                    ),
                    builder.build(recipeId.withPrefix("recipes/alloying/"))
            );
        }

        //Makes sure that this recipe is valid and obtainable.
        private void ensureValid(@NotNull ResourceLocation id) {
            if (this.criteria.isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + id);
            }
        }
    }
}
