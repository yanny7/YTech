package com.yanny.ytech.configuration.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yanny.ytech.registration.YTechRecipeSerializers;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public record AlloyingRecipe(IngredientCount ingredient1, IngredientCount ingredient2,
                             int minTemperature, int smeltingTime, ItemStack result) implements Recipe<Container> {
    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return matchesFully(container.getItem(0), container.getItem(1), false);
    }

    @NotNull
    @Override
    public ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @NotNull
    @Override
    public ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
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
    }

    public static class Serializer implements RecipeSerializer<AlloyingRecipe> {
        private static final Codec<AlloyingRecipe> CODEC = RecordCodecBuilder.create((recipe) -> recipe.group(
                IngredientCount.CODEC.fieldOf("ingredient1").forGetter((alloyingRecipe) -> alloyingRecipe.ingredient1),
                IngredientCount.CODEC.fieldOf("ingredient2").forGetter((alloyingRecipe) -> alloyingRecipe.ingredient2),
                Codec.INT.fieldOf("minTemp").forGetter((alloyingRecipe) -> alloyingRecipe.minTemperature),
                Codec.INT.fieldOf("smeltingTime").forGetter((alloyingRecipe) -> alloyingRecipe.smeltingTime),
                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter((alloyingRecipe) -> alloyingRecipe.result)
        ).apply(recipe, AlloyingRecipe::new));

        @Override
        @NotNull
        public Codec<AlloyingRecipe> codec() {
            return CODEC;
        }

        @Override
        @NotNull
        public AlloyingRecipe fromNetwork(@NotNull FriendlyByteBuf buffer) {
            Ingredient ingredient1 = Ingredient.fromNetwork(buffer);
            Ingredient ingredient2 = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            int minTemperature = buffer.readInt();
            int smeltingTime = buffer.readInt();
            int count1 = buffer.readInt();
            int count2 = buffer.readInt();
            return new AlloyingRecipe(new IngredientCount(ingredient1, count1), new IngredientCount(ingredient2, count2), minTemperature, smeltingTime, result);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull AlloyingRecipe recipe) {
            recipe.ingredient1.ingredient.toNetwork(buffer);
            recipe.ingredient2.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.minTemperature);
            buffer.writeInt(recipe.smeltingTime);
            buffer.writeInt(recipe.ingredient1.count);
            buffer.writeInt(recipe.ingredient1.count);
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
