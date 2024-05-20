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
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public record PotteryRecipe(ResourceLocation id, int count, ItemStack result) implements Recipe<Container> {
    private static final Ingredient clay = Ingredient.of(Items.CLAY_BALL);
    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        ItemStack item = container.getItem(0);
        return clay.test(item) && item.getCount() == count;
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
        return YTechRecipeSerializers.POTTERY.get();
    }

    @NotNull
    @Override
    public RecipeType<?> getType() {
        return YTechRecipeTypes.POTTERY.get();
    }

    public static class Serializer implements RecipeSerializer<PotteryRecipe> {
        @NotNull
        @Override
        public PotteryRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject serializedRecipe) {
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(serializedRecipe, "result"));
            int count = GsonHelper.getAsInt(serializedRecipe, "count");
            return new PotteryRecipe(recipeId, count, result);
        }

        @Override
        public @Nullable PotteryRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            ItemStack result = buffer.readItem();
            int count = buffer.readInt();
            return new PotteryRecipe(recipeId, count, result);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull PotteryRecipe recipe) {
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.count);
        }
    }

    public record Result(@NotNull ResourceLocation id, int count, @NotNull Item result, @NotNull Advancement.Builder advancement,
                         @NotNull ResourceLocation advancementId) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            JsonObject resultItemStack = new JsonObject();
            resultItemStack.addProperty("item", Utils.loc(result).toString());
            json.add("result", resultItemStack);

            json.addProperty("count", count);
        }

        @NotNull
        @Override
        public ResourceLocation getId() {
            return id;
        }

        @NotNull
        @Override
        public RecipeSerializer<?> getType() {
            return YTechRecipeSerializers.POTTERY.get();
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
        private final int count;
        private final Item result;
        private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

        Builder(int count, @NotNull Item result) {
            this.count = count;
            this.result = result;
        }

        public static Builder pottery(int count, @NotNull Item result) {
            return new Builder(count, result);
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
            finishedRecipeConsumer.accept(new PotteryRecipe.Result(recipeId, count, result, advancement, recipeId.withPrefix("recipes/pottery/")));
        }

        //Makes sure that this recipe is valid and obtainable.
        private void ensureValid(@NotNull ResourceLocation id) {
            if (this.advancement.getCriteria().isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + id);
            }
        }
    }
}
