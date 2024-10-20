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

public record AlloyingRecipe(Ingredient ingredient1, Ingredient ingredient2, int minTemperature, int smeltingTime, ItemStack result) implements Recipe<RecipeInput> {
    @Override
    public boolean matches(@NotNull RecipeInput recipeInput, @NotNull Level level) {
        return (ingredient1.test(recipeInput.getItem(0)) && ingredient2.test(recipeInput.getItem(1))) ||
                (ingredient1.test(recipeInput.getItem(1)) && ingredient2.test(recipeInput.getItem(0)));
    }

    public boolean matchesPartially(@NotNull RecipeInput recipeInput) {
        return (recipeInput.getItem(0).isEmpty() || ingredient1.test(recipeInput.getItem(0))) ||
                (recipeInput.getItem(1).isEmpty() || ingredient2.test(recipeInput.getItem(1))) ||
                (recipeInput.getItem(0).isEmpty() || ingredient2.test(recipeInput.getItem(0))) ||
                (recipeInput.getItem(1).isEmpty() || ingredient1.test(recipeInput.getItem(1)));
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

    public static class Serializer implements RecipeSerializer<AlloyingRecipe> {
        private static final MapCodec<AlloyingRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        Ingredient.CODEC.fieldOf("ingredient1").forGetter((alloyingRecipe) -> alloyingRecipe.ingredient1),
                        Ingredient.CODEC.fieldOf("ingredient2").forGetter((alloyingRecipe) -> alloyingRecipe.ingredient2),
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
            Ingredient ingredient1 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient ingredient2 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            int minTemperature = buffer.readInt();
            int smeltingTime = buffer.readInt();
            return new AlloyingRecipe(ingredient1, ingredient2, minTemperature, smeltingTime, result);
        }

        private static void toNetwork(@NotNull RegistryFriendlyByteBuf buffer, @NotNull AlloyingRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient1);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient2);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeInt(recipe.minTemperature);
            buffer.writeInt(recipe.smeltingTime);
        }
    }

    public static class Builder implements RecipeBuilder {
        private final Ingredient ingredient1;
        private final Ingredient ingredient2;
        private final int minTemperature;
        private final int smeltingTime;
        private final Item result;
        private final int count;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        Builder(@NotNull YTechIngredient ingredient1, @NotNull YTechIngredient ingredient2, int minTemperature,
                int smeltingTime, @NotNull Item result, int count) {
            this.ingredient1 = ingredient1.toVanilla();
            this.ingredient2 = ingredient2.toVanilla();
            this.minTemperature = minTemperature;
            this.smeltingTime = smeltingTime;
            this.result = result;
            this.count = count;
        }

        public static Builder alloying(@NotNull TagKey<Item> input1, int count1, @NotNull TagKey<Item> input2, int count2, int minTemperature,
                                       int smeltingTime, @NotNull Item result, int count) {
            return new Builder(YTechIngredient.of(input1, count1), YTechIngredient.of(input2, count2), minTemperature, smeltingTime, result, count);
        }

        public static Builder alloying(@NotNull TagKey<Item> input1, int count1, @NotNull ItemLike input2, int count2, int minTemperature,
                                       int smeltingTime, @NotNull Item result, int count) {
            return new Builder(YTechIngredient.of(input1, count1), YTechIngredient.of(input2, count2), minTemperature, smeltingTime, result, count);
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
                            ingredient1,
                            ingredient2,
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
