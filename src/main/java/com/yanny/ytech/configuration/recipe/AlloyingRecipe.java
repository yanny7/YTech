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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public record AlloyingRecipe(SizedIngredient ingredient1, SizedIngredient ingredient2, int minTemperature, int smeltingTime, ItemStack result) implements Recipe<RecipeInput> {
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

    @NotNull
    @Override
    public RecipeSerializer<AlloyingRecipe> getSerializer() {
        return YTechRecipeSerializers.ALLOYING.get();
    }

    @NotNull
    @Override
    public RecipeType<AlloyingRecipe> getType() {
        return YTechRecipeTypes.ALLOYING.get();
    }

    @NotNull
    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @NotNull
    @Override
    public RecipeBookCategory recipeBookCategory() {
        return YTechRecipeBookCategories.ALLOYING.get();
    }

    public static class Serializer implements RecipeSerializer<AlloyingRecipe> {
        private static final MapCodec<AlloyingRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        SizedIngredient.NESTED_CODEC.fieldOf("ingredient1").forGetter((alloyingRecipe) -> alloyingRecipe.ingredient1),
                        SizedIngredient.NESTED_CODEC.fieldOf("ingredient2").forGetter((alloyingRecipe) -> alloyingRecipe.ingredient2),
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
            SizedIngredient ingredient1 = SizedIngredient.STREAM_CODEC.decode(buffer);
            SizedIngredient ingredient2 = SizedIngredient.STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            int minTemperature = buffer.readInt();
            int smeltingTime = buffer.readInt();
            return new AlloyingRecipe(ingredient1, ingredient2, minTemperature, smeltingTime, result);
        }

        private static void toNetwork(@NotNull RegistryFriendlyByteBuf buffer, @NotNull AlloyingRecipe recipe) {
            SizedIngredient.STREAM_CODEC.encode(buffer, recipe.ingredient1);
            SizedIngredient.STREAM_CODEC.encode(buffer, recipe.ingredient2);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeInt(recipe.minTemperature);
            buffer.writeInt(recipe.smeltingTime);
        }
    }

    public static class Builder implements RecipeBuilder {
        private final SizedIngredient ingredient1;
        private final SizedIngredient ingredient2;
        private final int minTemperature;
        private final int smeltingTime;
        private final Item result;
        private final int count;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        Builder(@NotNull SizedIngredient ingredient1, @NotNull SizedIngredient ingredient2, int minTemperature,
                int smeltingTime, @NotNull Item result, int count) {
            this.ingredient1 = ingredient1;
            this.ingredient2 = ingredient2;
            this.minTemperature = minTemperature;
            this.smeltingTime = smeltingTime;
            this.result = result;
            this.count = count;
        }

        public static Builder alloying(HolderGetter<Item> items, @NotNull TagKey<Item> input1, int count1, @NotNull TagKey<Item> input2, int count2, int minTemperature,
                                       int smeltingTime, @NotNull Item result, int count) {
            return new Builder(SizedIngredient.of(items.getOrThrow(input1), count1), SizedIngredient.of(items.getOrThrow(input2), count2), minTemperature, smeltingTime, result, count);
        }

        public static Builder alloying(HolderGetter<Item> items, @NotNull TagKey<Item> input1, int count1, @NotNull ItemLike input2, int count2, int minTemperature,
                                       int smeltingTime, @NotNull Item result, int count) {
            return new Builder(SizedIngredient.of(items.getOrThrow(input1), count1), SizedIngredient.of(items.getOrThrow(input2), count2), minTemperature, smeltingTime, result, count);
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
                    new AlloyingRecipe(
                            ingredient1,
                            ingredient2,
                            minTemperature,
                            smeltingTime,
                            new ItemStack(result, count)
                    ),
                    builder.build(Utils.modLoc("recipes/alloying/" + recipeId.location().getPath()))
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
