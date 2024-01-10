package com.yanny.ytech.configuration.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yanny.ytech.configuration.Utils;
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

public record DryingRecipe(Ingredient ingredient, int dryingTime, ItemStack result) implements Recipe<Container> {
    public static final Serializer SERIALIZER = new Serializer();
    public static final RecipeType<DryingRecipe> RECIPE_TYPE = new RecipeType<>() {
        @Override
        public String toString() {
            return Utils.modLoc("drying").toString();
        }
    };

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
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @NotNull
    @Override
    public RecipeType<?> getType() {
        return RECIPE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<DryingRecipe> {
        private static final Codec<DryingRecipe> CODEC = RecordCodecBuilder.create((recipe) -> recipe.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter((dryingRecipe) -> dryingRecipe.ingredient),
                Codec.INT.fieldOf("dryingTime").forGetter((dryingRecipe) -> dryingRecipe.dryingTime),
                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter((dryingRecipe) -> dryingRecipe.result)
        ).apply(recipe, DryingRecipe::new));

        @Override
        @NotNull
        public Codec<DryingRecipe> codec() {
            return CODEC;
        }

        @Override
        @NotNull
        public DryingRecipe fromNetwork(@NotNull FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            int dryingTime = buffer.readInt();
            return new DryingRecipe(ingredient, dryingTime, result);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull DryingRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.dryingTime);
        }
    }

    public static class Builder implements RecipeBuilder {
        private final Ingredient ingredient;
        private final int dryingTime;
        private final Item result;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        Builder(@NotNull Ingredient ingredient, int dryingTime, @NotNull Item result) {
            this.ingredient = ingredient;
            this.dryingTime = dryingTime;
            this.result = result;
        }

        public static Builder drying(@NotNull TagKey<Item> input, int dryingTime, @NotNull Item result) {
            return new Builder(Ingredient.of(input), dryingTime, result);
        }

        public static Builder drying(@NotNull ItemLike input, int dryingTime, @NotNull Item result) {
            return new Builder(Ingredient.of(input), dryingTime, result);
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
                    new DryingRecipe(ingredient, dryingTime, new ItemStack(result)),
                    builder.build(recipeId.withPrefix("recipes/drying/"))
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
