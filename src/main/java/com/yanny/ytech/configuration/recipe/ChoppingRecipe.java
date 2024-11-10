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

public record ChoppingRecipe(Ingredient ingredient, Ingredient tool, int hitCount, ItemStack result) implements Recipe<Container> {
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
    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

    @NotNull
    @Override
    public ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return result;
    }

    @NotNull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return YTechRecipeSerializers.CHOPPING.get();
    }

    @NotNull
    @Override
    public RecipeType<?> getType() {
        return YTechRecipeTypes.CHOPPING.get();
    }

    public static class Serializer implements RecipeSerializer<ChoppingRecipe> {
        private static final Codec<ChoppingRecipe> CODEC = RecordCodecBuilder.create((recipe) -> recipe.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter((ChoppingRecipe) -> ChoppingRecipe.ingredient),
                Ingredient.CODEC_NONEMPTY.fieldOf("tool").forGetter((ChoppingRecipe) -> ChoppingRecipe.tool),
                Codec.INT.fieldOf("hitCount").forGetter((ChoppingRecipe) -> ChoppingRecipe.hitCount),
                ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter((ChoppingRecipe) -> ChoppingRecipe.result)
        ).apply(recipe, ChoppingRecipe::new));

        @NotNull
        @Override
        public Codec<ChoppingRecipe> codec() {
            return CODEC;
        }

        @NotNull
        @Override
        public ChoppingRecipe fromNetwork(@NotNull FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Ingredient tool = Ingredient.fromNetwork(buffer);
            int hitCount = buffer.readInt();
            ItemStack result = buffer.readItem();
            return new ChoppingRecipe(ingredient, tool, hitCount, result);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull ChoppingRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            recipe.tool.toNetwork(buffer);
            buffer.writeInt(recipe.hitCount);
            buffer.writeItem(recipe.result);
        }
    }

    public static class Builder implements RecipeBuilder {
        private final Ingredient ingredient;
        private final Ingredient tool;
        private final int hitCount;
        private final Item result;
        private final int count;
        private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

        Builder(@NotNull Ingredient ingredient, Ingredient tool, int hitCount, @NotNull Item result, int count) {
            this.ingredient = ingredient;
            this.tool = tool;
            this.hitCount = hitCount;
            this.result = result;
            this.count = count;
        }

        public static Builder chopping(@NotNull TagKey<Item> input, TagKey<Item> tool, int hitCount, @NotNull Item result, int count) {
            return new Builder(Ingredient.of(input), Ingredient.of(tool), hitCount, result, count);
        }

        public static Builder chopping(@NotNull ItemLike input, TagKey<Item> tool, int hitCount, @NotNull Item result, int count) {
            return new Builder(Ingredient.of(input), Ingredient.of(tool), hitCount, result, count);
        }

        @NotNull
        @Override
        public ChoppingRecipe.Builder unlockedBy(@NotNull String criterionName, @NotNull Criterion criterionTrigger) {
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
                    new ChoppingRecipe(ingredient, tool, hitCount, new ItemStack(result, count)),
                    builder.build(recipeId.withPrefix("recipes/chopping/"))
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
