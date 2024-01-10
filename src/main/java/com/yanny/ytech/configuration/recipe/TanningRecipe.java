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

public record TanningRecipe(Ingredient ingredient, Ingredient tool, int hitCount, ItemStack result) implements Recipe<Container> {
    public static final Serializer SERIALIZER = new Serializer();
    public static final RecipeType<TanningRecipe> RECIPE_TYPE = new RecipeType<>() {
        @Override
        public String toString() {
            return Utils.modLoc("tanning").toString();
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

    public static class Serializer implements RecipeSerializer<TanningRecipe> {
        private static final Codec<TanningRecipe> CODEC = RecordCodecBuilder.create((recipe) -> recipe.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter((tanningRecipe) -> tanningRecipe.ingredient),
                Ingredient.CODEC_NONEMPTY.fieldOf("tool").forGetter((tanningRecipe) -> tanningRecipe.tool),
                Codec.INT.fieldOf("hitCount").forGetter((tanningRecipe) -> tanningRecipe.hitCount),
                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter((tanningRecipe) -> tanningRecipe.result)
        ).apply(recipe, TanningRecipe::new));

        @Override
        @NotNull
        public Codec<TanningRecipe> codec() {
            return CODEC;
        }

        @Override
        @NotNull
        public TanningRecipe fromNetwork(@NotNull FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Ingredient tool = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            int hitCount = buffer.readInt();
            return new TanningRecipe(ingredient, tool, hitCount, result);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull TanningRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            recipe.tool.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.hitCount);
        }
    }

    public static class Builder implements RecipeBuilder {
        private final Ingredient ingredient;
        private Ingredient tool = Ingredient.EMPTY;
        private final int hitCount;
        private final Item result;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        Builder(@NotNull Ingredient ingredient, int hitCount, @NotNull Item result) {
            this.ingredient = ingredient;
            this.hitCount = hitCount;
            this.result = result;
        }

        public static Builder tanning(@NotNull TagKey<Item> input, int hitCount, @NotNull Item result) {
            return new Builder(Ingredient.of(input), hitCount, result);
        }

        public static Builder tanning(@NotNull ItemLike input, int hitCount, @NotNull Item result) {
            return new Builder(Ingredient.of(input), hitCount, result);
        }

        public Builder tool(@NotNull Ingredient tool) {
            this.tool = tool;
            return this;
        }

        @NotNull
        @Override
        public Builder unlockedBy(@NotNull String criterionName, @NotNull Criterion criterionTrigger) {
            this.criteria.put(criterionName, criterionTrigger);
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
        public void save(@NotNull RecipeOutput finishedRecipeConsumer, @NotNull ResourceLocation recipeId) {
            ensureValid(recipeId);
            Advancement.Builder builder = finishedRecipeConsumer.advancement().addCriterion("has_the_recipe",
                    RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(AdvancementRequirements.Strategy.OR);
            this.criteria.forEach(builder::addCriterion);
            finishedRecipeConsumer.accept(
                    recipeId,
                    new TanningRecipe(ingredient, tool, hitCount, new ItemStack(result)),
                    builder.build(recipeId.withPrefix("recipes/tanning/"))
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
