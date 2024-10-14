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

public record SmeltingRecipe(Ingredient ingredient, int inputCount, Ingredient mold, int minTemperature, int smeltingTime, ItemStack result) implements Recipe<Container> {
    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return ingredient.test(container.getItem(0)) && container.getItem(0).getCount() >= inputCount && (mold.isEmpty() || mold.test(container.getItem(1)));
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
        return YTechRecipeSerializers.SMELTING.get();
    }

    @NotNull
    @Override
    public RecipeType<?> getType() {
        return YTechRecipeTypes.SMELTING.get();
    }

    public static class Serializer implements RecipeSerializer<SmeltingRecipe> {
        private static final Codec<SmeltingRecipe> CODEC = RecordCodecBuilder.create((recipe) -> recipe.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter((smeltingRecipe) -> smeltingRecipe.ingredient),
                Codec.INT.fieldOf("inputCount").forGetter((smeltingRecipe) -> smeltingRecipe.inputCount),
                Ingredient.CODEC.fieldOf("mold").forGetter((smeltingRecipe) -> smeltingRecipe.mold),
                Codec.INT.fieldOf("minTemp").forGetter((smeltingRecipe) -> smeltingRecipe.minTemperature),
                Codec.INT.fieldOf("smeltingTime").forGetter((smeltingRecipe) -> smeltingRecipe.smeltingTime),
                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter((smeltingRecipe) -> smeltingRecipe.result)
        ).apply(recipe, SmeltingRecipe::new));

        @Override
        @NotNull
        public SmeltingRecipe fromNetwork(@NotNull FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int inputCount = buffer.readInt();
            Ingredient mold = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            int minTemperature = buffer.readInt();
            int dryingTime = buffer.readInt();
            return new SmeltingRecipe(ingredient, inputCount, mold, minTemperature, dryingTime, result);
        }

        @Override
        @NotNull
        public Codec<SmeltingRecipe> codec() {
            return CODEC;
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull SmeltingRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeInt(recipe.inputCount);
            recipe.mold.toNetwork(buffer);
            buffer.writeItem(recipe.result);
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
