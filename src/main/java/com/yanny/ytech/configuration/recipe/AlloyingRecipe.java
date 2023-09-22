package com.yanny.ytech.configuration.recipe;

import com.google.gson.JsonObject;
import com.yanny.ytech.configuration.Utils;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.stream.Stream;

public record AlloyingRecipe(ResourceLocation id, TagStackIngredient ingredient1, TagStackIngredient ingredient2, int minTemperature,
                             int smeltingTime, ItemStack result) implements Recipe<Container> {
    public static final Serializer SERIALIZER = new Serializer();
    public static final RecipeType<AlloyingRecipe> RECIPE_TYPE = new RecipeType<>() {
        @Override
        public String toString() {
            return Utils.modLoc("alloying").toString();
        }
    };

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
    public ResourceLocation getId() {
        return id;
    }

    @NotNull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @NotNull
    @Override
    public RecipeType<?> getType() {
        return RECIPE_TYPE;
    }

    public int getInput1Count() {
        return ingredient1.getItems()[0].getCount();
    }

    public int getInput2Count() {
        return ingredient2.getItems()[0].getCount();
    }

    public boolean matchesPartially(@NotNull ItemStack itemStack1, @NotNull ItemStack itemStack2, boolean ignoreCount) {
        return ingredient1.test(itemStack1, ignoreCount) || ingredient1.test(itemStack2, ignoreCount)
                || ingredient2.test(itemStack1, ignoreCount) || ingredient2.test(itemStack2, ignoreCount);
    }

    public boolean matchesFully(@NotNull ItemStack itemStack1, @NotNull ItemStack itemStack2, boolean ignoreCount) {
        return (ingredient1.test(itemStack1, ignoreCount) && ingredient2.test(itemStack2, ignoreCount))
                || (ingredient1.test(itemStack2, ignoreCount) && ingredient2.test(itemStack1, ignoreCount));
    }

    public boolean matchesIngredient1(@NotNull ItemStack itemStack, boolean ignoreCount) {
        return ingredient1.test(itemStack, ignoreCount);
    }

    public boolean matchesIngredient2(@NotNull ItemStack itemStack, boolean ignoreCount) {
        return ingredient2.test(itemStack, ignoreCount);
    }

    public static class Serializer implements RecipeSerializer<AlloyingRecipe> {
        @NotNull
        @Override
        public AlloyingRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject serializedRecipe) {
            Ingredient ingredient1 = Ingredient.fromJson(serializedRecipe.get("ingredient1"), false);
            Ingredient ingredient2 = Ingredient.fromJson(serializedRecipe.get("ingredient2"), false);
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(serializedRecipe, "result"));
            int minTemperature = GsonHelper.getAsInt(serializedRecipe, "minTemp");
            int smeltingTime = GsonHelper.getAsInt(serializedRecipe, "smeltingTime");
            return new AlloyingRecipe(recipeId, TagStackIngredient.fromIngredient(ingredient1),
                    TagStackIngredient.fromIngredient(ingredient2), minTemperature, smeltingTime, result);
        }

        @Override
        public @Nullable AlloyingRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            Ingredient ingredient1 = Ingredient.fromNetwork(buffer);
            Ingredient ingredient2 = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            int minTemperature = buffer.readInt();
            int dryingTime = buffer.readInt();
            return new AlloyingRecipe(recipeId, TagStackIngredient.fromIngredient(ingredient1),
                    TagStackIngredient.fromIngredient(ingredient2), minTemperature, dryingTime, result);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull AlloyingRecipe recipe) {
            recipe.ingredient1.toNetwork(buffer);
            recipe.ingredient2.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.minTemperature);
            buffer.writeInt(recipe.smeltingTime);
        }
    }

    public record Result(@NotNull ResourceLocation id, @NotNull TagStackIngredient ingredient1, TagStackIngredient ingredient2, int minTemperature,
                         int smeltingTime, @NotNull Item result, int count, @NotNull Advancement.Builder advancement,
                         @NotNull ResourceLocation advancementId) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            json.add("ingredient1", ingredient1.toJson());
            json.add("ingredient2", ingredient2.toJson());

            JsonObject resultItemStack = new JsonObject();
            resultItemStack.addProperty("item", Utils.loc(result).toString());

            if (count > 1) {
                resultItemStack.addProperty("count", count);
            }

            json.add("result", resultItemStack);

            json.addProperty("minTemp", minTemperature);
            json.addProperty("smeltingTime", smeltingTime);
        }

        @NotNull
        @Override
        public ResourceLocation getId() {
            return id;
        }

        @NotNull
        @Override
        public RecipeSerializer<?> getType() {
            return SERIALIZER;
        }

        @NotNull
        @Override
        public JsonObject serializeAdvancement() {
            return advancement.serializeToJson();
        }

        @NotNull
        @Override
        public ResourceLocation getAdvancementId() {
            return advancementId;
        }
    }

    public static class Builder implements RecipeBuilder {
        private final TagStackIngredient ingredient1;
        private final TagStackIngredient ingredient2;
        private final int minTemperature;
        private final int smeltingTime;
        private final Item result;
        private final int count;
        private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

        Builder(@NotNull TagStackIngredient ingredient1, @NotNull TagStackIngredient ingredient2, int minTemperature, int smeltingTime, @NotNull Item result, int count) {
            this.ingredient1 = ingredient1;
            this.ingredient2 = ingredient2;
            this.minTemperature = minTemperature;
            this.smeltingTime = smeltingTime;
            this.result = result;
            this.count = count;
        }

        public static Builder alloying(@NotNull ItemStack input1, @NotNull ItemStack input2, int minTemperature, int smeltingTime,
                                       @NotNull Item result, int count) {
            return new Builder(TagStackIngredient.of(input1), TagStackIngredient.of(input2), minTemperature, smeltingTime, result, count);
        }

        public static Builder alloying(@NotNull TagKey<Item> input1, int count1, @NotNull TagKey<Item> input2, int count2, int minTemperature,
                                       int smeltingTime, @NotNull Item result, int count) {
            return new Builder(TagStackIngredient.fromValues(Stream.of(new TagStackIngredient.TagCountValue(input1, count1))),
                    TagStackIngredient.fromValues(Stream.of(new TagStackIngredient.TagCountValue(input2, count2))), minTemperature, smeltingTime, result, count);
        }

        public static Builder alloying(@NotNull TagKey<Item> input1, int count1, @NotNull ItemLike input2, int count2, int minTemperature,
                                       int smeltingTime, @NotNull Item result, int count) {
            return new Builder(TagStackIngredient.fromValues(Stream.of(new TagStackIngredient.TagCountValue(input1, count1))),
                    TagStackIngredient.fromValues(Stream.of(new Ingredient.ItemValue(new ItemStack(input2, count2)))), minTemperature, smeltingTime, result, count);
        }

        @NotNull
        @Override
        public RecipeBuilder unlockedBy(@NotNull String criterionName, @NotNull CriterionTriggerInstance criterionTrigger) {
            this.advancement.addCriterion(criterionName, criterionTrigger);
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
        public void save(@NotNull Consumer<FinishedRecipe> finishedRecipeConsumer, @NotNull ResourceLocation recipeId) {
            ensureValid(recipeId);
            advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe",
                    RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
            finishedRecipeConsumer.accept(new AlloyingRecipe.Result(recipeId, ingredient1, ingredient2, minTemperature, smeltingTime, result, count, advancement,
                    recipeId.withPrefix("recipes/alloying/")));
        }

        //Makes sure that this recipe is valid and obtainable.
        private void ensureValid(@NotNull ResourceLocation id) {
            if (this.advancement.getCriteria().isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + id);
            }
        }
    }
}
