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

public record SmeltingRecipe(Ingredient ingredient, int inputCount, Ingredient mold, int minTemperature, int smeltingTime, ItemStack result) implements Recipe<RecipeInput> {
    @Override
    public boolean matches(@NotNull RecipeInput recipeInput, @NotNull Level level) {
        return ingredient.test(recipeInput.getItem(0)) && recipeInput.getItem(0).getCount() >= inputCount && (mold.isEmpty() || mold.test(recipeInput.getItem(1)));
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
        return YTechRecipeSerializers.SMELTING.get();
    }

    @NotNull
    @Override
    public RecipeType<?> getType() {
        return YTechRecipeTypes.SMELTING.get();
    }

    public static class Serializer implements RecipeSerializer<SmeltingRecipe> {
        private static final MapCodec<SmeltingRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter((smeltingRecipe) -> smeltingRecipe.ingredient),
                        Codec.INT.fieldOf("inputCount").forGetter((smeltingRecipe) -> smeltingRecipe.inputCount),
                        Ingredient.CODEC.fieldOf("mold").forGetter((smeltingRecipe) -> smeltingRecipe.mold),
                        Codec.INT.fieldOf("minTemp").forGetter((smeltingRecipe) -> smeltingRecipe.minTemperature),
                        Codec.INT.fieldOf("smeltingTime").forGetter((smeltingRecipe) -> smeltingRecipe.smeltingTime),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter((smeltingRecipe) -> smeltingRecipe.result)
                ).apply(instance, SmeltingRecipe::new)
        );
        private static final StreamCodec<RegistryFriendlyByteBuf, SmeltingRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        @NotNull
        public MapCodec<SmeltingRecipe> codec() {
            return CODEC;
        }

        @NotNull
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SmeltingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        @NotNull
        private static SmeltingRecipe fromNetwork(@NotNull RegistryFriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            int inputCount = buffer.readInt();
            Ingredient mold = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            int minTemperature = buffer.readInt();
            int dryingTime = buffer.readInt();
            return new SmeltingRecipe(ingredient, inputCount, mold, minTemperature, dryingTime, result);
        }

        private static void toNetwork(@NotNull RegistryFriendlyByteBuf buffer, @NotNull SmeltingRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
            buffer.writeInt(recipe.inputCount);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.mold);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeInt(recipe.minTemperature);
            buffer.writeInt(recipe.smeltingTime);
        }
    }

    public static class Builder implements RecipeBuilder {
        private final Ingredient ingredient;
        private final int inputCount;
        private final Ingredient mold;
        private final int minTemperature;
        private final int smeltingTime;
        private final Item result;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        Builder(@NotNull Ingredient ingredient, int inputCount, @NotNull Ingredient mold, int minTemperature, int smeltingTime, @NotNull Item result) {
            this.ingredient = ingredient;
            this.inputCount = inputCount;
            this.mold = mold;
            this.minTemperature = minTemperature;
            this.smeltingTime = smeltingTime;
            this.result = result;
        }

        public static Builder smelting(@NotNull TagKey<Item> input, int inputCount, @NotNull TagKey<Item> mold, int minTemperature, int smeltingTime, @NotNull Item result) {
            return new Builder(Ingredient.of(input), inputCount, Ingredient.of(mold), minTemperature, smeltingTime, result);
        }

        public static Builder smelting(@NotNull TagKey<Item> input, int minTemperature, int smeltingTime, @NotNull Item result) {
            return new Builder(Ingredient.of(input), 1, Ingredient.EMPTY, minTemperature, smeltingTime, result);
        }

        public static Builder smelting(@NotNull ItemLike input, int inputCount, @NotNull TagKey<Item> mold, int minTemperature, int smeltingTime, @NotNull Item result) {
            return new Builder(Ingredient.of(input), inputCount, Ingredient.of(mold), minTemperature, smeltingTime, result);
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
                    new SmeltingRecipe(ingredient, inputCount, mold, minTemperature, smeltingTime, new ItemStack(result)),
                    builder.build(recipeId.withPrefix("recipes/smelting/"))
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
