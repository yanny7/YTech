package com.yanny.ytech.configuration.recipe;

import com.google.gson.JsonObject;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.registration.YTechRecipeSerializers;
import com.yanny.ytech.registration.YTechRecipeTypes;
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

public record HammeringRecipe(ResourceLocation id, Ingredient ingredient, Ingredient tool, int hitCount, ItemStack result) implements Recipe<Container> {
    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return ingredient.test(container.getItem(0));
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
        return YTechRecipeSerializers.HAMMERING.get();
    }

    @NotNull
    @Override
    public RecipeType<?> getType() {
        return YTechRecipeTypes.HAMMERING.get();
    }

    public static class Serializer implements RecipeSerializer<HammeringRecipe> {
        @NotNull
        @Override
        public HammeringRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject serializedRecipe) {
            Ingredient ingredient = Ingredient.fromJson(serializedRecipe.get("ingredient"), false);
            Ingredient tool = Ingredient.fromJson(serializedRecipe.get("tool"), true);
            int hitCount = GsonHelper.getAsInt(serializedRecipe, "hitCount");
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(serializedRecipe, "result"));
            return new HammeringRecipe(recipeId, ingredient, tool, hitCount, result);
        }

        @Override
        public @Nullable HammeringRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Ingredient tool = Ingredient.fromNetwork(buffer);
            int hitCount = buffer.readInt();
            ItemStack result = buffer.readItem();
            return new HammeringRecipe(recipeId, ingredient, tool, hitCount, result);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull HammeringRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            recipe.tool.toNetwork(buffer);
            buffer.writeInt(recipe.hitCount);
            buffer.writeItem(recipe.result);
        }
    }

    public record Result(@NotNull ResourceLocation id, @NotNull Ingredient ingredient, @NotNull Ingredient tool, int hitCount, @NotNull Item result,
                         @NotNull Advancement.Builder advancement, @NotNull ResourceLocation advancementId) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            json.add("ingredient", ingredient.toJson());
            json.add("tool", tool.toJson());
            json.addProperty("hitCount", hitCount);

            JsonObject resultItemStack = new JsonObject();
            resultItemStack.addProperty("item", Utils.loc(result).toString());
            json.add("result", resultItemStack);
        }

        @NotNull
        @Override
        public ResourceLocation getId() {
            return id;
        }

        @NotNull
        @Override
        public RecipeSerializer<?> getType() {
            return YTechRecipeSerializers.HAMMERING.get();
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
        private final Ingredient ingredient;
        private Ingredient tool = Ingredient.EMPTY;
        int hitCount;
        private final Item result;
        private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

        Builder(@NotNull Ingredient ingredient, int hitCount, @NotNull Item result) {
            this.ingredient = ingredient;
            this.hitCount = hitCount;
            this.result = result;
        }

        public static Builder hammering(@NotNull TagKey<Item> input, int hitCount, @NotNull Item result) {
            return new Builder(Ingredient.of(input), hitCount, result);
        }

        public static Builder hammering(@NotNull ItemLike input, int hitCount, @NotNull Item result) {
            return new Builder(Ingredient.of(input), hitCount, result);
        }

        public Builder tool(@NotNull Ingredient tool) {
            this.tool = tool;
            return this;
        }

        @NotNull
        @Override
        public Builder unlockedBy(@NotNull String criterionName, @NotNull CriterionTriggerInstance criterionTrigger) {
            this.advancement.addCriterion(criterionName, criterionTrigger);
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
        public void save(@NotNull Consumer<FinishedRecipe> finishedRecipeConsumer, @NotNull ResourceLocation recipeId) {
            ensureValid(recipeId);
            advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe",
                    RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
            finishedRecipeConsumer.accept(new HammeringRecipe.Result(recipeId, ingredient, tool, hitCount, result, advancement, recipeId.withPrefix("recipes/hammering/")));
        }

        //Makes sure that this recipe is valid and obtainable.
        private void ensureValid(@NotNull ResourceLocation id) {
            if (this.advancement.getCriteria().isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + id);
            }
        }
    }
}
